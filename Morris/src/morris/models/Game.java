package morris.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.states.PlacementState;

public class Game {

	private List<StateListener> stateListeners = new CopyOnWriteArrayList<StateListener>();
	private State state;
	private Board board = new Board();
	
	// Satt til placementstate midlertidig. Logisk Œ starte der uansett.
	public Game(){
		setState(new PlacementState());
	}
	
	public ArrayList<Slot> getHighlightList() {
		return this.state.getHighlightList(board.getSlots(), new Player("Kjell Barry", 9)); // aktuell spiller benyttes
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
     * Eksempel pï¿½ ï¿½ kjï¿½re en metode
     * 
    private void firePieceMoved(Piece piece) {
    	for(GameListener l : gamelisteners) {
    		l.pieceMoved(piece);
    	}    	
    }
    */
	
}
