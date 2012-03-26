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
	
	// Satt til placementstate midlertidig. Logisk Œ starte der uansett.
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
	 * Input parameters are Point IDs
	 */
	public void move(Piece p, int to){
		// ENDRER atPosition for piece, og setTaken for de aktuelle slots. 
		board.unreserveSlot(p.getPosition());
		board.reserveSlot(to);
		p.setPosition(to);
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
	
	// Her kan man ta inn pointID og et Player-objekt.
	public ArrayList<Slot> getHighlightList(int id, Player player) {
		//getBoard().getSlotByID(11).setTaken(true);
		return this.state.getHighlightList(board, id, player); // aktuell spiller benyttes
	}
	/**
	 * Update pieces resourse images
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
	
	public void reserveBoardSlot(int id){
		board.reserveSlot(id);
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

	public void playerPlacedPiece(Player player,Piece piece) {
		//Check morris etc then:
		System.out.println("Player placed: GameAct");
		firePiecePlaced(player, piece);
		pieceCounter++;
		if(pieceCounter == 4){
			setState(new SelectState());
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
