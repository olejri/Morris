package morris.game;

import morris.models.Game;

public class GameHandler {
	
	private static GameHandler instance = null;
	public static Game morrisGame = null;
	
	// GameHandler is a Singleton.
	// the access to it is only through the getInstance() method
	public static GameHandler getInstance(){
		if(instance == null){
			instance = new GameHandler();
		}
		return instance;
	}
	/**
	 * Set MorrisGame
	 * @param morrisGame
	 */
	public static void setMorrisGame(Game morrisGame){
		GameHandler.morrisGame = morrisGame;
	}
	/**
	 * Return MorrisGame
	 * @return
	 */
	public static Game getMorrisGame(){
		return morrisGame;
	}
	
	
	// createNewGame() method - starts a new game that other can join
	public void createNewGame(){
		
	}
	
	// clearGame() method - clears the game attributes
	public void clearGame(){
		
	}

}
