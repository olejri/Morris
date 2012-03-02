package morris.game;

public class GameHandler {
	
	private static GameHandler instance = null;
	
	
	// GameHandler is a Singleton.
	// the access to it is only through the getInstance() method
	public static GameHandler getInstance(){
		if(instance == null){
			instance = new GameHandler();
		}
		return instance;
	}
	
	// Singleton's private constructor
	private GameHandler(){
		
	}
	
	
	// createNewGame() method - starts a new game that other can join
	public void createNewGame(){
		
	}
	
	// clearGame() method - clears the game attributes
	public void clearGame(){
		
	}

}
