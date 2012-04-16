package morris.help;

import morris.game.R;

public class Constant {
	
	//Skiller id's for permission to use the Skiller Tools
	public static final String app_key = "465abbaa5ebd41498903a2a42576f639";
	public static final String app_secret = "d7a8155968d9437781f82f10df7c5188";
	public static final String app_id = "934190831255";
	
	
	public static int boardHeight ;
	public static int boardWidth;
	public static int scoreBoardHeight;
	
	public static int pieceSize;
	/**
	 * Screen height
	 */
	/**
	 * Game Types
	 */
	public static final String TWELVE_MENS_MORRIS = "twelve_mens_morris";
	public static final String NINE_MENS_MORRIS = "nine_mens_morris";
	/**
	 * Game colors
	 */
	public static final String BLACK = "black";
	public static final String WHITE = "white";
	
	/**
	 * Selected states on pieces
	 */
	public static final int NORMAL = 3;
	public static final int SELECTED = 1;
	public static final int SELECTABLE = 2;
	public static final int REMOVABLE = 4;
	
	/**
	 * Log
	 */
	public static final String STATE_DEBUG = "state_debug";
	
	/**
	 * Message protocol messages
	 */
	public static final String SPLIT =";";
	public static final String MESSAGE_PIECE_PLACED = "m_p_p";
	public static final String MESSAGE_PIECE_MOVED = "m_p_m";
	public static final String MESSAGE_PIECE_DELETED ="m_p_d";
	
	
	

}
