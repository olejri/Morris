package morris.models;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import morris.interfaces.GameListener;


public class Game {

	private List<GameListener> gamelisteners = new CopyOnWriteArrayList<GameListener>();
	
	public Game(){
		
	}
	
	/**
	 * Add listener
	 * @param listener
	 */
    public void addListener(GameListener listener) {
    	gamelisteners.add(listener);
    }
    /**
     * remove listener
     * @param listener
     */
    public void removeListener(GameListener listener) {
    	gamelisteners.remove(listener);
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
