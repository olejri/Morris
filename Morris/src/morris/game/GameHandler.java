package morris.game;

import android.util.Log;
import morris.models.Game;

public class GameHandler {
	
	private static Game morrisGame = null;
	
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
		setMorrisGame(new Game());
	}
	
	// clearGame() method - clears the game attributes
	public void clearGame(){
		
	}

}
