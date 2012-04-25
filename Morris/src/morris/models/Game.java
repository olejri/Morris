package morris.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.skiller.api.operations.SKTurnBasedTools;

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
	private int lastMoveFromPosition;
	private int lastMoveToPosition;
	private boolean hotseat = false;


	public String gameType;

	int pieceCounter = 0;

	public Player player1;
	public Player player2;
	public Player currentPlayer;

	// Satt til placementstate midlertidig. Logisk � starte der uansett.
	public Game(boolean hotseat){
		this.hotseat = hotseat;
		setState(new PlacementState());
		previousState = new PlacementState();
		board = new Board();
		gameType = Constant.NINE_MENS_MORRIS;
		h = new Handler();
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
	
	public boolean opponentHasRemovablePieces(){
		for(Piece p : getOpponent().getPieces()){
			if(!p.inMorris()) return true;
		}
		return false;
	}


	private boolean isValidMove(Piece piece, int to){
		if(state instanceof PlacementState || state instanceof FlyingState){
			if(!board.getPoint(to).isTaken()){
				return true;
			}
		} else if(state instanceof MoveState || state instanceof SelectState){
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
	public void checkGameOwer(Player player){
		Log.i("win", "checkGameOver [Game] for player:"+player.getName() + " pieces size: " + player.getPieces().size());
		if(player.getPieces().size() < 9 || player.hasSelectablePieces()){
			Log.i("win", "Game is over : " + player.getName() + " lost [Game]");
			if(getCurrentPlayer()==player1)firePlayerWon(1);
			else firePlayerWon(2);
		}
	}
	*/
	public boolean playerWon(Player player){
		Log.i("win", "checkGameOver [Game] for player:"+player.getName() + " pieces size: " + player.getPieces().size());
		if(player.getPieces().size() < 9 || player.hasSelectablePieces()){
			Log.i("win", "Game is over : " + player.getName() + " lost [Game]");
			firePlayerWon(1);
			return true;
		}else return false;
	}


	/*
	 * Input parameters are selected piece and destination point ID
	 * ID for the point the piece was moved from is available via p.getPosition().
	 * The player integer can be 1 or 2, depending on whose turn it is.
	 */
	public boolean move(Piece p, int to, Player player){
		int from = p.getPosition();
		if(isValidMove(p, to)){
			unreserveBoardModelPoint(p.getPosition());
			reserveBoardModelPoint(to, p);
			p.setPosition(to);
			if(p.inMorris()){
				updateMorrisStates(player);
			}
			if(checkMorris(p, player)){
				//Sets last move
				lastMoveFromPosition = from;
				lastMoveToPosition = to;
			}else{
				firePieceMoved(from, to,playerWon(player));
			}
			//Check if player won
			//checkGameOwer(getOpponent());
			
			return true;
			
		} else {
			return false;
		}
	}

	public State getState(){
		return state;
	}
	public boolean isYourTurn(){
		return true;
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
	public boolean checkMorris(Piece piece, Player player){
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
			updateMorrisStates(player);
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
			firePieceRemoved(p.getId(),lastMoveFromPosition,lastMoveToPosition,playerWon(player));
			//checkGameOwer(getOpponent());
			return true;
		}
		return false;
	}

	/*
	 * Updates the board, setting correct state on all pieces.
	 * TODO Update Morris states for opponent's pieces
	 */
	public void updateMorrisStates(Player player){
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
     * FIRE LISTENER METHODS
     */
     
    private void firePiecePlaced(Player player,Piece piece,boolean won) {
    	//changePlayer(true);
    	for(GameListener l : gameListeners){
    		l.playerPlacedPiece(player, piece,won, hotseat);
    	}
    }
    
    private void firePieceMoved(int pieceFromPosition,int pieceToPosition,boolean won) {
    	Log.i("movement","firePieceMoved() [Game]");
    	for(GameListener l : gameListeners){
    		l.playerMoved(pieceFromPosition, pieceToPosition,won, hotseat);
    	}
    }
    
    private void firePieceRemoved(int piecePosition,int movedFromPosition,int movedToPosition,boolean won){
    	for(GameListener l : gameListeners){
    		l.playerRemovedPiece(piecePosition,movedFromPosition,movedToPosition,won, hotseat);
    	}
    }
    
    private void firePlayerChangeTurn(Player p){
    	for(GameListener l : gameListeners){
    		l.playerChangeTurn(p);
    	}
    }
    
    private void fireUpdate(){
    	for(GameListener l : gameListeners){
    		l.update();
    	}
    }
    
    private void firePlayerWon(int player){
    	for(GameListener l : gameListeners){
    		l.playerWon(player);
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
				Log.i("skiller","placePlacedPiece: morris [Game]");
				lastMoveFromPosition = -1;
				lastMoveToPosition = position;
				
			}else{
				Log.i("skiller","placePlacedPiece: not morris firePiecePlaced() [Game]");
				firePiecePlaced(player, piece,playerWon(player));	
			}
			//Check for win
			//checkGameOwer(getOpponent());
			Log.i("skiller","Time to fire piece placed");
			
			updatePieceCounter();

		}
	}
	
	private void updatePieceCounter(){
		pieceCounter++;
		if(pieceCounter == 8){
			previousState = new SelectState();
			Log.i("LOGHELP", "testing started");
			if(state instanceof PlacementState){
				setState(new SelectState());
			}
			
		}
	}


	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void changePlayer(boolean hotseat){
		boolean playerOne = currentPlayer == player1;
		if(playerOne)Log.i("currentPlayer", "ChangePlayer: Player1");
		else Log.i("currentPlayer", "ChangePlayer: Player2");
		
		
		if(hotseat){
			if (currentPlayer == player1){
				setCurrentPlayer(player2);
			}
			else {
				setCurrentPlayer(player1);
			}
		}
		Log.i("currentPlayer", "--------");
		playerOne = currentPlayer == player1;
		if(playerOne)Log.i("currentPlayer", "ChangePlayer: Player1");
		else Log.i("currentPlayer", "ChangePlayer: Player2");
		Log.i("currentPlayer", "****************");
		
		firePlayerChangeTurn(getCurrentPlayer());
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
	public void networkPlayerMoved(int fromPostion, int toPosition) {
		Log.i("movement","networkPlayerMoved() [Game]");
		
			changePlayer(true);
		/*
		if(morris==Constant.MESSAGE_MORRIS){
			Network.getInstance().sendInformation("YOU HAVE MORRIS. STILL YOUR TURN", SKTurnBasedTools.GAME_EVENT_MAKING_MOVE, null);
		}*/
		
		Piece pieceMoved = board.getPoint(fromPostion).getPiece();
		unreserveBoardModelPoint(fromPostion);
		reserveBoardModelPoint(toPosition, pieceMoved);
		pieceMoved.setPosition(toPosition);
		
		firePieceMoved(fromPostion, toPosition,false);
		
	}

	@Override
	public void networkPlayerPlacedPiece(int toPosition) {
		
			changePlayer(true);
		
		for(Piece piece : player2.getPieces()){
			if(piece.getPosition()==-1){
				piece.setPosition(toPosition);
				reserveBoardModelPoint(toPosition, piece);
				updatePieceCounter();
				firePiecePlaced(player2, piece,false);
				break;
			}
		}
		
		
	}

	@Override
	public void networkPlayerRemovedPiece(final int piecePosition,final int pieceMovedFromPosition,final int pieceMovedToPosition) {
		
		
		
		Log.i("sleep", "before sleeping [game]");;
				//Fire playerChangeTurn to update board
			h.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//Move first
						Piece pieceMoved = null;
						Log.i("values", "pieceMovedFromPosition: "+ pieceMovedFromPosition+" [game]");;
						if(pieceMovedFromPosition!=-1){
							pieceMoved = board.getPoint(pieceMovedFromPosition).getPiece();
							unreserveBoardModelPoint(pieceMovedFromPosition);
						}else{
							pieceMoved = getFirstAvailablePiece();
						}
						reserveBoardModelPoint(pieceMovedToPosition, pieceMoved);
						if(pieceMoved!=null)pieceMoved.setPosition(pieceMovedToPosition);
						fireUpdate();
					}
				}, 10);
				
				
				h.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						changePlayer(true);
						Piece pieceRemoved = board.getPoint(piecePosition).getPiece();
						unreserveBoardModelPoint(piecePosition);
						currentPlayer.removePiece(pieceRemoved);
						fireUpdate();
						
					}
				}, 1500);
				
				
		

		
	}
	
	private Piece getFirstAvailablePiece(){
		Piece available = null;
		for(Piece piece : player2.getPieces()){
			if(piece.getPosition()==-1){
				available = piece;
				break;
			}
		}
		return available;
	}

	@Override
	public void networkSetPlayerNames() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void networkPlayerLost() {
		Log.i("win", "networkPlayerLost [Game]");
		firePlayerWon(2);
		
	}


}
