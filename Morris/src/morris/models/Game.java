package morris.models;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.states.MoveState;

public class Game {

	private List<StateListener> stateListeners = new CopyOnWriteArrayList<StateListener>();
	private State state;
	
	public Game(){
		setState(new MoveState());
	}
	
	public void listSelectablePieces() {
		if(state != null){
			this.state.highlightPossibilities();
		}
	}
	
	public void setState(State state){
		this.state = state;
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
     * Eksempel p� � kj�re en metode
     * 
    private void firePieceMoved(Piece piece) {
    	for(GameListener l : gamelisteners) {
    		l.pieceMoved(piece);
    	}    	
    }
    */
	
}
