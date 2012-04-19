package morris.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

import morris.game.Network;
import morris.gui.Point;
import morris.help.Constant;
import morris.interfaces.GameListener;
import morris.interfaces.NetworkListener;
import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.states.FlyingState;
import morris.states.MoveState;
import morris.states.PlacementState;
import morris.states.RemovalState;
import morris.states.SelectState;

public class Game implements NetworkListener {

	private List<StateListener> stateListeners = new CopyOnWriteArrayList<StateListener>();
	private List<GameListener> gameListeners = new CopyOnWriteArrayList<GameListener>();
	public boolean yourTurn = true;
	private State state;
	private Board board;
	private State previousState;
	private Handler h;


	public String gameType;

	int pieceCounter = 0;

	public Player player1;
	public Player player2;
	public Player currentPlayer;

	// Satt til placementstate midlertidig. Logisk � starte der uansett.
	public Game(){
		setState(new PlacementState());
		previousState = new PlacementState();
		board = new Board();
		gameType = Constant.NINE_MENS_MORRIS;
		
		Network.getInstance().addListener(this);
	}

	public void initPlayers(){
		player1 = new Player(Constant.WHITE,"Emil");
		player2 = new Player(Constant.BLACK,"Steinar");
		currentPlayer = player1;
	}

	public ArrayList<Piece> getSelectablePieces(Player player){
		ArrayList<Piece> pieces = player.getPieces();
		ArrayList<Piece> selectable = new ArrayList<Piece>();
		for(Piece p : pieces){
			if(p.isSelectable()) selectable.add(p);
		}
		return selectable;
	}


	private boolean isValidMove(Piece piece, int to){
		if(state instanceof PlacementState || state instanceof FlyingState){
			if(!board.getPoint(to).isTaken()){
				return true;
			}
		} else if(state instanceof MoveState){
			if(!board.getPoint(to).isTaken() && board.getPoint(piece.getPosition()).isNeighbour(to)){
				return true;
			}
		}
		else if(state instanceof RemovalState){
			for (Piece p : getOpponent().getPieces()){
				if(p == piece && !p.inMorris()){
					return true;
				}
			}

		}
		return false;
	}


	/*
	 * Input parameters are selected piece and destination point ID
	 * ID for the point the piece was moved from is available via p.getPosition().
	 * The player integer can be 1 or 2, depending on whose turn it is.
	 */
	public void move(Piece p, int to, Player player){ 
		if(isValidMove(p, to)){
			unreserveBoardModelPoint(p.getPosition());
			reserveBoardModelPoint(to, p);
			p.setPosition(to);

			// Checks for Morris at the point the piece is placed at.		
			if(checkMorris(p,player)){ // endret fra achievedMorris(to)
				Log.i("LOGHELP", player.name + " got morris");
				setState(new RemovalState());
				updateMorrisStates(player);
				System.out.println("Removable pieces:");
				for(Piece piece : getPieces(1)){
					if(!piece.inMorris() && piece.getPosition() >= 0){
						System.out.println("Piece at position "+piece.getPosition()+" is removable!");
					}
				}
			} 
			else{
				changePlayer();
				
			}
			
		}

	}

	public State getState(){
		return state;
	}
	public boolean isYourTurn(){
		return yourTurn;
	}
	public State getPreviousState() {
		return previousState;
	}

	public void setPreviousState(State previousState) {
		this.previousState = previousState;
	}
	public void setYourTurn(boolean yourTurn){
		this.yourTurn = yourTurn;
	}

	public Player getPlayer1(){
		return player1;
	}
	public Player getPlayer2(){
		return player2;
	}

	public String getMorrisGameType(){
		return gameType;
	}

	private void setMorrisInDomain(ArrayList<Integer> domain, Player player){
		for(Integer id : domain){
			board.getPoint(id).getPiece().setMorris(true);		
		}
	}
	/*
	 * Checks if the newly placed piece caused a Morris state.
	 */
	private boolean checkMorris(Piece piece, Player player){
		ArrayList<Integer> hDomain = board.getHorizontalDomain(piece.getPosition());
		int horizontal = 0;
		for(Integer i : hDomain){
			if(board.getPoint(i) != null){
				if(board.getPoint(i).getPiece() != null){
					if(piece.getColor() == board.getPoint(i).getPiece().getColor()) horizontal++;
				}
			}
		}
		if(horizontal==3){
			setMorrisInDomain(hDomain, player);
		}
		ArrayList<Integer> vDomain = board.getVerticalDomain(piece.getPosition());

		int vertical = 0;
		for(Integer i : vDomain){
			if(board.getPoint(i) != null){
				if(board.getPoint(i).getPiece() != null){
					if(piece.getColor() == board.getPoint(i).getPiece().getColor()) vertical++;
				}
			}
		}
		if(vertical==3){
			setMorrisInDomain(vDomain, player);
		}
		if(vertical==3 || horizontal==3){
			return true;
		} else {
			return false;
		}
	}

	public boolean removePiece(Point p, Player player) {
		ArrayList<Piece> pieces = player.getPieces();
		Piece ps = null;
		for (Piece piece : pieces){
			if(piece.getPosition() == p.getId()){
				ps = piece;


			}
		}
		if(isValidMove(ps, 0)){
			unreserveBoardModelPoint(p.getId());
			player.removePiece(ps);
			changePlayer();
			return true;
		}
		return false;



	}

	/*
	 * Updates the board, setting correct state on all pieces.
	 * TODO Update Morris states for opponent's pieces
	 */
	private void updateMorrisStates(Player player){
		ArrayList<Piece> pieces = player.getPieces(); // endret fra getPieces(1)
		int horizontal = 0;
		int vertical = 0;

		for(Piece p : pieces){
			if(p.getPosition() >= 0){
				p.setMorris(false);
				ArrayList<Integer> hDomain = board.getHorizontalDomain(p.getPosition());
				horizontal = 0;
				for(Integer i : hDomain){
					if(board.getPoint(i).getPiece() != null){
						if(p.getColor() == board.getPoint(i).getPiece().getColor()) horizontal++;
					}
				}
				if(horizontal==3){
					setMorrisInDomain(hDomain, player);
				}
				ArrayList<Integer> vDomain = board.getVerticalDomain(p.getPosition());
				vertical = 0;
				for(Integer i : vDomain){
					if(board.getPoint(i).getPiece() != null){
						if(p.getColor() == board.getPoint(i).getPiece().getColor()) vertical++;
					}
				}
				if(vertical==3){
					setMorrisInDomain(vDomain, player);
				}
			}
		}
	}

	/*
	 * Help method only used to get the player's pieces when checking for Morris state (rows and columns).
	 */
	private ArrayList<Piece> getPieces(int player){
		if(player == 1){
			return getPlayer1().getPieces();
		} else {
			return getPlayer2().getPieces();
		}
	}

	// Her kan man ta inn pointID og et Player-objekt.
	public ArrayList<ModelPoint> getHighlightList(int id, Player player) {
		return this.state.getHighlightList(board, id, player); // aktuell spiller benyttes
	}


	/**
	 * Update pieces resource images
	 * @param player
	 * @param positionId
	 */
	public void updatePieceImages(Player player,int positionId){
		this.state.updatePieceImages(player, positionId);
	}

	public void setState(State state){
		this.state = state;
	}

	public Board getBoard(){
		return board;
	}

	public void reserveBoardModelPoint(int id, Piece piece){
		board.reserveModelPoint(id, piece);
	}

	public void unreserveBoardModelPoint(int id){
		board.unReserveModelPoint(id);
	}

	/**
	 * Add listener
	 * @param listener
	 */

    public void addGameListener(GameListener listener) {
    	gameListeners.add(listener);
    }
    /**
     * remove listener
     * @param listener
     */
    public void removeListener(StateListener listener){
    	stateListeners.remove(listener);
    }
    
    /**
     * FIRE LISTENERS
     */
     
    private void firePiecePlaced(Player player,Piece piece) {
    	for(GameListener l : gameListeners){
    		l.playerPlacedPiece(player, piece);
    	}
    }
    
    private void firePieceMoved(int pieceFromPosition,int pieceToPosition) {
    	for(GameListener l : gameListeners){
    		l.playerMoved(pieceFromPosition, pieceToPosition);
    	}
    }

	/*
	 * TODO
	 * Remove pieceCounter and implement logic for initial state change in GameController.
	 */
    /*
     * TODO
     * Remove pieceCounter and implement logic for initial state change in GameController.
     */

	public void playerPlacedPiece(Player player,Piece piece, int position) {
		Log.i("skiller","playerPlacedPieceMethod");
		Log.i("skiller","Is place taken?: "+ board.getPoint(position).isTaken());
		if(isValidMove(piece, position)){
			Log.i("skiller","Move is valid");
			piece.setPosition(position);
			reserveBoardModelPoint(position, piece); 
			if(checkMorris(piece, player)){
				System.out.println("Morris achieved. Removal State should be set!");
				updateMorrisStates(player);
				setState(new RemovalState());

			}
			else{
				changePlayer();
			}
			Log.i("skiller","Time to fire piece placed");
			firePiecePlaced(player, piece);
			pieceCounter++;
			if(pieceCounter == 18){
				previousState = new SelectState();
				Log.i("LOGHELP", "testing started");
				if(state instanceof PlacementState){
					setState(new SelectState());
				}
				
			}

		}
	}


	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void changePlayer(){
		if (currentPlayer == player1){
			setCurrentPlayer(player2);
		}
		else {
			setCurrentPlayer(player1);
		}
	}

	public Player getOpponent(){
		if (currentPlayer == getPlayer1()){
			return player2;
		}
		else {
			return player1;
		}
	}

	public boolean selectable(Player player,int positionId){
		for(Piece p : player.getPieces()){
			if(p.getPosition()==positionId){
				if(p.isSelectable()) return true;
			}
		}
		return false;
	}

	/**
	 * Methods from network
	 */
	
	@Override
	public void networkPlayerMoved(int pieceID, int toPosition) {
		
		
	}

	@Override
	public void networkPlayerPlacedPiece(final int pieceID, final int toPosition) {
		Log.i("skiller", "place piece in game from network");
		Log.i("skiller", "what");

		for(Piece p : getOpponent().getPieces()){
			Log.i("skiller", "Piece position: "+p.getPosition());
			if(p.getPosition()<0){
				Log.i("skiller", "fire playerPlacedPiece(Player,Piece,Position");
				playerPlacedPiece(getOpponent(),p, toPosition);
				break;
			}
		}
		
	}

	@Override
	public void networkPlayerWon() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void networkPlayerRemovedPiece() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void networkSetPlayerNames() {
		// TODO Auto-generated method stub
		
	}


}
