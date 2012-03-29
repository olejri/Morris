package morris.game;

import com.skiller.api.items.SKUser;

import morris.game.controller.GameController;
import morris.gui.BoardView;
import morris.gui.PieceAdapter;
import morris.help.Constant;
import morris.interfaces.GameListener;
import morris.models.Game;
import morris.models.Piece;
import morris.models.Player;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class PlayGameActivity extends Activity implements GameListener {
	public PieceAdapter pieceAdapter1;
	public PieceAdapter pieceAdapter2;
	public GridView gridview_black;
	public GridView gridview_white;

	Handler h;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.play_game_layout);

		setBoardHeight();
		
		h = new Handler();
		setButtonFonts();

		GameController.setMorrisGame(new Game());
		GameController.getMorrisGame().initPlayers();		
		GameController.getInstance().getMorrisGame().addGameListener(this);

		SKUser owner = GameController.getInstance().getOwner();
		SKUser guest = GameController.getInstance().getGuest();
		GameController.getInstance().getMorrisGame().initPlayers();
		
		GameController.getInstance().setCanvasContext(this);

		setUpScoreBoard();
		setScoreBoardNames();
		// updateScoreBoard();
		
		try{
			if(GameController.getInstance().isWaiting_for_opponnent()){
				GameController.getInstance().showToastOnCanvas("Still waiting on opponent");
			}
			if(GameController.getInstance().isGameOwner()){
				if(GameController.getInstance().isGameStarted()){
					GameController.getInstance().clearGame();
					GameController.getInstance().setGameStarted(false);
					GameController.getInstance().setWaiting_for_opponent(false);
					GameController.getInstance().setSide(1);
					GameController.getInstance().setSide(2);
					GameController.getInstance().showToastOnCanvas("Game starting?");
				}
			}
			else{
				
			}
		}
		catch(Exception e){
			
		}
	}
	/**
	 * onWindowChange we dedicate 70 % of the screen to the BoardView and 20 for scoreview
	 */
	public void onWindowFocusChanged(boolean hasFocus) {
		Display display = getWindowManager().getDefaultDisplay();
		int screenHeight = display.getHeight();
		
		RelativeLayout header = (RelativeLayout)findViewById(R.id.header);
		int headerheight = header.getHeight();
		
		Log.i("height", "Header height: " + headerheight);
		
		int usableArea = screenHeight - (headerheight);
		float viewHeight = (float) usableArea * 0.70f;
		float scoreHeight = (float)usableArea * 0.20f;
		

		Constant.boardHeight = (int)viewHeight;
		Constant.scoreBoardHeight = (int)scoreHeight;
		
		
		BoardView cardView = (BoardView) findViewById(R.id.board_view_id);
		LinearLayout scoreView = (LinearLayout)findViewById(R.id.score_board_id);
		RelativeLayout.LayoutParams scoreParams = (RelativeLayout.LayoutParams) scoreView.getLayoutParams();
		RelativeLayout.LayoutParams params = (LayoutParams) cardView.getLayoutParams();
		
		scoreView.setLayoutParams(scoreParams);
		cardView.setLayoutParams(params);

	}
	/**
	 * Set boardView height to 70 % of screen
	 */
	private void setBoardHeight(){

	}
	
	private void setButtonFonts(){
		Typeface button_font = Typeface.createFromAsset(getAssets(), "fonts/text-font.otf");
		((TextView)((Activity)this).findViewById(R.id.toolbar_title)).setTypeface(button_font);
	}

	/**
	 * Init score board
	 */
	private void setUpScoreBoard() {
		// Set up player 1 pieces
		gridview_black = (GridView) findViewById(R.id.gridview_player1);
		pieceAdapter1 = new PieceAdapter(getApplicationContext(),Constant.WHITE);
		gridview_black.setAdapter(pieceAdapter1);
		// Set up player 2 pieces
		
		gridview_white = (GridView) findViewById(R.id.gridview_player2);

		
		pieceAdapter2 = new PieceAdapter(getApplicationContext(), Constant.BLACK);
		gridview_white.setAdapter(pieceAdapter2);
		
		
	}

	private void setScoreBoardNames() {
		TextView player1 = (TextView) findViewById(R.id.player1_name);
		TextView player2 = (TextView) findViewById(R.id.player2_name);
		player1.setText(GameController.getInstance().getMorrisGame().getPlayer1().getName());
		player2.setText(GameController.getInstance().getMorrisGame().getPlayer2().getName());
	}

	/**
	 * When a player have placed a piece. Use this method to update score board
	 */
	public void updateScoreBoard() {
		pieceAdapter1.notifyDataSetChanged();
		pieceAdapter2.notifyDataSetChanged();
		gridview_white.refreshDrawableState();
		gridview_black.refreshDrawableState();
	}

	@Override
	public void playerMoved(Player player, Piece piece) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playerPlacedPiece(Player player, Piece piece) {
		updateScoreBoard();

	}

}
