package morris.game;

import com.skiller.api.items.SKUser;
import com.skiller.api.operations.SKApplication;
import com.skiller.api.operations.SKTurnBasedTools;

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
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class PlayGameActivity extends SuperActivity implements GameListener {
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

		SKUser owner = network.getOwner();
		SKUser guest = network.getGuest();
		GameController.getInstance().getMorrisGame().initPlayers();

		setUpScoreBoard();
		setScoreBoardNames();

		try {
			if (network.isWaiting_for_opponnent()) {
				network.showToastOnCanvas("Still waiting on opponent");
				Log.i("skiller", "Venter på en kar");
			}
			if (network.isGameOwner()) {
				if (network.isGameStarted()) {
					network.clearGame();
					network.setGameStarted(false);
					network.setWaiting_for_opponent(false);
					network.setSide(1);
					network.setSide(2);
					network.showToastOnCanvas("Game starting?");
					Log.i("skiller", "ER DU HER ???");
					network.sendInformation("Dette funker",
							SKTurnBasedTools.GAME_EVENT_MAKING_MOVE, null);
				}
				Log.i("skiller", "ER DU HER ???");
				network.sendInformation("Dette funker",
						SKTurnBasedTools.GAME_EVENT_MAKING_MOVE, null);
			} else {
				Log.i("skiller", "Vil ikke starte");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		GameController.getMorrisGame().addGameListener(network);
		
	}

	/**
	 * onWindowChange we dedicate 70 % of the screen to the BoardView and 20 for
	 * scoreview
	 */
	public void onWindowFocusChanged(boolean hasFocus) {
		Display display = getWindowManager().getDefaultDisplay();
		int screenHeight = display.getHeight();

		RelativeLayout header = (RelativeLayout) findViewById(R.id.header);
		int headerheight = header.getHeight();

		Log.i("height", "Header height: " + headerheight);

		int usableArea = screenHeight - (headerheight);
		float viewHeight = (float) usableArea * 0.60f;
		float scoreHeight = (float) usableArea * 0.30f;

		Constant.boardHeight = (int) viewHeight;
		Constant.scoreBoardHeight = (int) scoreHeight;

		BoardView cardView = (BoardView) findViewById(R.id.board_view_id);
		LinearLayout scoreView = (LinearLayout) findViewById(R.id.score_board_id);
		RelativeLayout.LayoutParams scoreParams = (RelativeLayout.LayoutParams) scoreView
				.getLayoutParams();
		RelativeLayout.LayoutParams params = (LayoutParams) cardView
				.getLayoutParams();

		scoreView.setLayoutParams(scoreParams);
		cardView.setLayoutParams(params);

	}

	/**
	 * Set boardView height to 70 % of screen
	 */
	private void setBoardHeight() {

	}

	private void setButtonFonts() {

		Typeface button_font = Typeface.createFromAsset(getAssets(),
				"fonts/text-font.otf");
		((TextView) ((Activity) this).findViewById(R.id.toolbar_title))
				.setTypeface(button_font);

	}

	/**
	 * Init score board
	 */
	private void setUpScoreBoard() {
		// Set up player 1 pieces
		gridview_black = (GridView) findViewById(R.id.gridview_player1);
		pieceAdapter1 = new PieceAdapter(getApplicationContext(),
				Constant.WHITE);
		gridview_black.setAdapter(pieceAdapter1);
		// Set up player 2 pieces

		gridview_white = (GridView) findViewById(R.id.gridview_player2);

		pieceAdapter2 = new PieceAdapter(getApplicationContext(),
				Constant.BLACK);
		gridview_white.setAdapter(pieceAdapter2);

	}

	private void setScoreBoardNames() {
		TextView player1 = (TextView) findViewById(R.id.player1_name);
		TextView player2 = (TextView) findViewById(R.id.player2_name);
		player1.setText(GameController.getInstance().getMorrisGame()
				.getPlayer1().getName());
		player2.setText(GameController.getInstance().getMorrisGame()
				.getPlayer2().getName());
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
	public void playerPlacedPiece(Player player, Piece piece) {
		updateScoreBoard();

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			try{
				Network.getInstance().sendInformation(null, SKTurnBasedTools.GAME_EVENT_QUIT_GAME, null);
				((Activity) Network.getInstance().getCanvasContext()).finish();
			} catch (Exception e){
			}
		return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void playerMoved(int pieceFromPosition, int pieceToPosition) {
		// TODO Auto-generated method stub
		
	}

}
