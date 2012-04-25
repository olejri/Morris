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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class PlayGameActivity extends SuperActivity implements GameListener {
	public PieceAdapter pieceAdapter1;
	public PieceAdapter pieceAdapter2;
	public GridView gridview_black;
	public GridView gridview_white;
	public TextView player1;
	public TextView player2;
	private Animation textFadingAnimation;
	private boolean hotseat;

	Handler h;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.play_game_layout);
		setBoardHeight();
		Bundle extras = getIntent().getExtras();
		hotseat = extras.getBoolean("Hotseat");
		h = new Handler();
		setButtonFonts();
		
		player1 = (TextView) findViewById(R.id.player1_name);
		player2 = (TextView) findViewById(R.id.player2_name);

		if (GameController.getMorrisGame() == null) {
			GameController.setMorrisGame(new Game(hotseat));
			GameController.getMorrisGame().initPlayers();
			if(network.isGameOwner()){
				GameController.getMorrisGame().setCurrentPlayer(GameController.getMorrisGame().player2);
			}
		}
		GameController.getInstance();
		GameController.getMorrisGame().addGameListener(this);

		SKUser owner = network.getOwner();
		SKUser guest = network.getGuest();

		Network.getInstance().setCanvasContext(this);
		Network.getInstance().setCanvasContextON(true);

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
					network.showToastOnCanvas("Game started");
				}
			} else {
				Log.i("skiller", "Vil ikke starte");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		GameController.getMorrisGame().addGameListener(network);
		BoardView b = (BoardView) findViewById(R.id.board_view_id);
		GameController.getMorrisGame().addGameListener(b);
		
		startTextFading(GameController.getMorrisGame().getCurrentPlayer());

	}

	/**
	 * onWindowChange we dedicate 70 % of the screen to the BoardView and 20 for
	 * scoreview
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		Display display = getWindowManager().getDefaultDisplay();
		int screenHeight = display.getHeight();

		RelativeLayout header = (RelativeLayout) findViewById(R.id.header);
		int headerheight = header.getHeight();

		Log.i("height", "Header height: " + headerheight);

		int usableArea = screenHeight - (headerheight);
		float viewHeight = usableArea * 0.60f;
		float scoreHeight = usableArea * 0.30f;

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    //Handle the back button
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
	        //Ask the user if they want to quit
	        new AlertDialog.Builder(this)
	        .setIcon(R.drawable.icon_networkgame)
	        .setTitle("Quit Game")
	        .setMessage("Do you want to quit the game?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {

	            	try {
						Network.getInstance().sendInformation("",SKTurnBasedTools.GAME_EVENT_QUIT_GAME, null);
						GameController.setMorrisGame(null);
						PlayGameActivity.this.finish();
					} catch (Exception e) {
						finish();
					}
					PlayGameActivity.this.finish();
	            }

	        })
	        .setNegativeButton("No", null)
	        .show();

	        return true;
	    }
	    else {
	        return super.onKeyDown(keyCode, event);
	    }

	}
	
	public void startTextFading(Player p){
		Log.i("animation","StartTextFading(): " + p.getName());
		textFadingAnimation = AnimationUtils.loadAnimation(this, R.anim.textfading);
		textFadingAnimation.setRepeatCount(Animation.INFINITE);
		if(p==GameController.getMorrisGame().getPlayer1()){
			player2.clearAnimation();
			player1.setAnimation(textFadingAnimation);
			textFadingAnimation.start();
		}else{
			player1.clearAnimation();
			player2.setAnimation(textFadingAnimation);
			textFadingAnimation.start();
		}
	}


	@Override
	public void playerMoved(int pieceFromPosition, int pieceToPosition,boolean won, boolean hotseat) {

	}

	@Override
	public void playerRemovedPiece(int piecePosition,int movedFromPosition, int movedToPosition,boolean won, boolean hotseat) {
		
	}

	@Override
	public void playerChangeTurn(Player p) {
		startTextFading(p);
		
	}

	@Override
	public void playerPlacedPiece(Player player, Piece piece,boolean won, boolean hotseat) {
		// TODO Auto-generated method stub
		updateScoreBoard();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerWon(int player) {
		if(player==1){
			showWinToast("Emil");
		}else{
			showWinToast("Steinar");
		}
		
	}
	
	private void showWinToast(final String player){
		h.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "Player: " + player + " WON!", Toast.LENGTH_LONG).show();
			}
		});
	}

}
