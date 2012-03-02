package morris.models;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import morris.interfaces.StateListener;

public class Game {

	private List<StateListener> gamelisteners = new CopyOnWriteArrayList<StateListener>();
	
	public Game(){
		
	}
	
	/**
	 * Add listener
	 * @param listener
	 */
    public void addListener(StateListener listener) {
    	gamelisteners.add(listener);
    }
    /**
     * remove listener
     * @param listener
     */
    public void removeListener(StateListener listener){
    	gamelisteners.remove(listener);
    }
    
    /**
     * Eksempel på å kjøre en metode
     * 
    private void firePieceMoved(Piece piece) {
    	for(GameListener l : gamelisteners) {
    		l.pieceMoved(piece);
    	}    	
    }
    */
	
}
