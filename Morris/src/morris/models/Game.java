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
		unreserveBoardSlot(p.getPosition());
		reserveBoardSlot(to, player);
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
	
	// MÅ HUSKE Å SETTE p.inMorris = true for brikken som ble flytta
	// Vurder å skifte navn på metoden til checkMorris eller noe
	public boolean achievedMorris(int id){
		int row = board.getSlotByID(id).getX();
		int column = board.getSlotByID(id).getY();
		int occupant =  board.getSlotOccupant(row, column);
		if(checkRow(row, occupant) && checkColumn(column, occupant)){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean checkRow(int row, int occupant) {
		int counter = 0;
		Slot[][] slots = board.getSlots();
		for(int i=0; i<slots.length; i++){
			if(slots[row][i].getOccupant() == occupant){
				counter++;
			}
		}
		if(counter == 3){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean checkColumn(int column, int occupant) {
		int counter = 0;
		Slot[][] slots = board.getSlots();
		for(int i=0; i<slots.length; i++){
			if(slots[i][column].getOccupant() == occupant){
				counter++;
			}
		}
		if(counter == 3){
			return true;
		} else {
			return false;
		}
	}
	
	// Her kan man ta inn pointID og et Player-objekt.
	public ArrayList<Slot> getHighlightList(int id, Player player) {
		//getBoard().getSlotByID(11).setTaken(true);
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
	
	public void reserveBoardSlot(int id, int player){
		board.reserveSlot(id, player);
	}
	
	public void unreserveBoardSlot(int id){
		board.unreserveSlot(id);
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
		if(achievedMorris(piece.getPosition())){
			System.out.println("MORRIS OMG!");
		}
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
