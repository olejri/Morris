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

public class Game {

	private List<StateListener> stateListeners = new CopyOnWriteArrayList<StateListener>();
	private List<GameListener> gameListeners = new CopyOnWriteArrayList<GameListener>();
	private State state;
	private Board board;
	public String gameType;
	
	public Player player1;
	public Player player2;
	
	// Satt til placementstate midlertidig. Logisk Œ starte der uansett.
	public Game(){
		setState(new MoveState());
		board = new Board();
		gameType = Constant.NINE_MENS_MORRIS;
		
	}
	
	public void initPlayers(){
		player1 = new Player(Constant.WHITE,"Emil");
		player2 = new Player(Constant.BLACK,"Steinar");
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
	
	public ArrayList<Slot> getHighlightList() {
		//getBoard().getSlotByID(11).setTaken(true);
		return this.state.getHighlightList(board, 10, new Player("Black", "Kjell Barry")); // aktuell spiller benyttes
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
    public void addListener(StateListener listener) {
    	stateListeners.add(listener);
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
   
	
}
