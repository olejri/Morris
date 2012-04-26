package morris.game;

import com.skiller.api.items.SKImage;
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
	private boolean hotseat = false;
	private PopUp vsPopUp;
	SKImage images;

	Handler h;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.play_game_layout);
		setBoardHeight();

		if (getIntent().getExtras() != null){
		hotseat = getIntent().getBooleanExtra("Hotseat",false);
		}
		h = new Handler();
		setButtonFonts();
		
		player1 = (TextView) findViewById(R.id.player1_name);
		player2 = (TextView) findViewById(R.id.player2_name);

		
		
		
		if (GameController.getMorrisGame() == null) {
			Log.i("game", "GameController.getMorrisGame() == null");

			GameController.setMorrisGame(new Game(hotseat));
			GameController.getMorrisGame().initPlayers();
		}
		
		setScoreBoardNames("Player1", "Player2");
		
		GameController.getInstance();
		GameController.getMorrisGame().addGameListener(this);

		SKUser owner = network.getOwner();
		SKUser guest = network.getGuest();


		Network.getInstance().setCanvasContext(this);
		Network.getInstance().setCanvasContextON(true);

		setUpScoreBoard();
		
		

		try {
			if (network.isWaiting_for_opponnent()) {
				network.showToastOnCanvas("Still waiting on opponent");
				GameController.getMorrisGame().setCurrentPlayer(GameController.getMorrisGame().getPlayer2());
				if(!hotseat)setScoreBoardNames("You","Waiting for opponent..");
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
					GameController.getMorrisGame().setCurrentPlayer(GameController.getMorrisGame().getPlayer2());
				}
			} else {
				//vsPopUp = new PopUp(this, owner, guest, Network.getInstance().getImageArray(), "playing for "+ Network.getInstance().getPot() + " coins!");
				//vsPopUp.show();
				setScoreBoardNames(network.getGuest().getUserName(),network.getOwner().getUserName());
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

	private void setScoreBoardNames(final String name1,final String name2) {
		h.post(new Runnable() {
			@Override
			public void run() {
				Log.i("names", "settingScoreBoardNames() [PlayGameActivity]");
				player1.setText(name1);
				player2.setText(name2);
			}
		});
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
						//Network.getInstance().sendInformation("",SKTurnBasedTools.GAME_EVENT_QUIT_GAME, null);
	            		setScoreBoardNames("Player1", "Player2");
	            		//Remove gamelistener
	            		GameController.getMorrisGame().removeListener(network);
	            		
						GameController.setMorrisGame(null);
						//Remove listeners
						//GameController.getMorrisGame().removeListener(network);
						
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
	public void playerMoved(int pieceFromPosition, int pieceToPosition, boolean hotseat,boolean send) {
		SoundManager.getInstance().playSoundEffect(Constant.SOUND_MOVE);
	}

	@Override
	public void playerRemovedPiece(int piecePosition,int movedFromPosition, int movedToPosition, boolean hotseat) {
		SoundManager.getInstance().playSoundEffect(Constant.SOUND_REMOVE);
	}

	@Override
	public void playerChangeTurn(Player p) {
		startTextFading(p);
		
	}

	@Override
	public void playerPlacedPiece(Player player, Piece piece, boolean hotseat,boolean send) {
		// TODO Auto-generated method stub
		updateScoreBoard();
		SoundManager.getInstance().playSoundEffect(Constant.SOUND_DROP);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerLost(int player,boolean hotseat) {
		Log.i("lost","playerLost [PlayGameActivity]");
		String userName1 ="";
		String userName2 = "";
		if(!hotseat){
			if(network.isGameOwner()){
				userName1 = network.getOwner().getUserName();
				userName2 = network.getGuest().getUserName();
			}else{
				userName1 = network.getGuest().getUserName();
				userName2 = network.getOwner().getUserName();
			}
		}
		if(player==1){
			setScoreBoardNames(userName1+" Wins", userName2+ " Lose");
		}else{
			setScoreBoardNames(userName1+" Lose", userName2+ " Wins");
		}
	}
	
	private void showToast(final String message){
		h.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void gameStarted() {
		Log.i("names", "gameStarted() [PlayGameActivity]");
		if(network.isGameOwner())showToast(network.getGuest().getUserName() + " joined your game");
		else showToast("You have joined " + network.getOwner().getUserName());
		setScoreBoardNames(network.getOwner().getUserName(),network.getGuest().getUserName());
	}

}
