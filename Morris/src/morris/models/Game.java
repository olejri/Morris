package morris.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import morris.help.Constant;
import morris.interfaces.GameListener;
import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.states.MoveState;
import morris.states.PlacementState;
import morris.states.RemovalState;
import morris.states.SelectState;

public class Game {

	private List<StateListener> stateListeners = new CopyOnWriteArrayList<StateListener>();
	private List<GameListener> gameListeners = new CopyOnWriteArrayList<GameListener>();
	public boolean yourTurn = true;
	private State state;
	private Board board;
	public String gameType;
	
	int pieceCounter = 0;
	
	public Player player1;
	public Player player2;
	
	// Satt til placementstate midlertidig. Logisk å starte der uansett.
	public Game(){
		setState(new PlacementState());
		board = new Board();
		gameType = Constant.NINE_MENS_MORRIS;
	}
	
	public void initPlayers(){
		player1 = new Player(Constant.WHITE,"Emil");
		player2 = new Player(Constant.BLACK,"Steinar");
	}
	
	public ArrayList<Piece> getSelectablePieces(Player player){
		ArrayList<Piece> pieces = player.getPieces();
		ArrayList<Piece> selectable = new ArrayList<Piece>();
		for(Piece p : pieces){
			if(p.isSelectable()) selectable.add(p);
		}
		return selectable;
	}
	
	/*
	 * Input parameters are selected piece and destination point ID
	 * ID for the point the piece was moved from is available via p.getPosition().
	 * The player integer can be 1 or 2, depending on whose turn it is.
	 */
	public void move(Piece p, int to, int player){ 
		unreserveBoardModelPoint(p.getPosition());
		reserveBoardModelPoint(to, p);
		p.setPosition(to);
		
		// Checks for Morris at the point the piece is placed at.		
		if(checkMorris(p)){ // endret fra achievedMorris(to)
			System.out.println("Movement Morris achieved. Removal State should be set!");
			//updateMorrisStates(player); // Her skal mostander benyttes for å oppdatere hans brikker
		}
	}
	
	public State getState(){
		return state;
	}
	public boolean isYourTurn(){
		return yourTurn;
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
	
	/*
	 * This method is intended to use when the player enters removalstate, and updates inMorris for all opponent pieces
	 * from pointID to pointID + occupant integer
	 */
	public void updateMorrisStates(int occupant){
		ArrayList<Piece> pieces = getPieces(occupant);

		// Reset inMorris for all pieces
		for(Piece p : pieces){
			p.setMorris(false);
		}

		ArrayList<ModelPoint> points = board.getPoints();

	}
	
	private void setMorrisInDomain(ArrayList<Integer> domain){
		for(Integer id : domain){
			board.getPoint(id).getPiece().setMorris(true);
		}
	}
	/*
	 * Checks if the newly placed piece caused a Morris state.
	 */
	private boolean checkMorris(Piece piece){
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
			setMorrisInDomain(hDomain);
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
			setMorrisInDomain(vDomain);
		}
		if(vertical==3 || horizontal==3){
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Updates the board, setting correct state on all pieces.
	 * TODO Make certain that both pieces (belonging to player and modelpoint) is updated.
	 */
	private void updateMorrisStates(Player player){
		ArrayList<Piece> pieces = getPieces(1);
		int horizontal = 0;
		int vertical = 0;
		for(Piece p : pieces){
			p.setMorris(false); // Usikker på om denne vil funke.
			ArrayList<Integer> hDomain = board.getHorizontalDomain(p.getPosition());
			horizontal = 0;
			for(Integer i : hDomain){
				if(board.getPoint(i) != null){
					if(p.getColor() == board.getPoint(i).getPiece().getColor()) horizontal++;
				}
			}
			if(horizontal==3){
				setMorrisInDomain(hDomain);
			}
			ArrayList<Integer> vDomain = board.getVerticalDomain(p.getPosition());
			vertical = 0;
			for(Integer i : vDomain){
				if(board.getPoint(i) != null){
					if(p.getColor() == board.getPoint(i).getPiece().getColor()) vertical++;
				}
			}
			if(vertical==3){
				setMorrisInDomain(vDomain);
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

    /*
     * TODO
     * Remove pieceCounter and implement logic for initial state change in GameController.
     */
	public void playerPlacedPiece(Player player,Piece piece) {
		if(piece.getPosition() > 0){	
			
			// Denne bør ikke trigge dersom det er et feilaktig trykk
			reserveBoardModelPoint(piece.getPosition(), piece);
			
			if(checkMorris(piece)){
				System.out.println("Morris achieved. Removal State should be set!");
			}
			
			firePiecePlaced(player, piece);
			pieceCounter++;
			if(pieceCounter == 4){
				setState(new SelectState());
			}
		}
	}
	public boolean selectable(Player player,int positionId){
		for(Piece p : player.getPieces()){
			if(p.getPosition()==positionId){
				if(p.isSelectable())return true;
			}
		}
		return false;
	}
   
	
}
