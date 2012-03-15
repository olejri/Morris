package morris.models;

import java.io.Console;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import morris.help.Constant;
import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.states.MoveState;
import morris.states.RemovalState;

public class Game {

	private List<StateListener> stateListeners = new CopyOnWriteArrayList<StateListener>();
	private State state;
	public String gameType = "nine_mens_morris";
	
	public Player player1;
	public Player player2;
	
	public Game(){
		setState(new MoveState());
		//gameType = Constant.NINE_MENS_MORRIS;
		
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
	
	public void listSelectablePieces() {
		if(state != null){
			this.state.listSelectablePieces();
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
     * FIRE LISTENERS
     */
    
    
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
