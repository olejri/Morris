package morris.game;

import morris.gui.PieceAdapter;
import morris.help.Constant;
import morris.interfaces.GameListener;
import morris.models.Game;
import morris.models.Piece;
import morris.models.Player;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayGameActivity extends Activity implements GameListener {
	public PieceAdapter pieceAdapter1;
	public PieceAdapter pieceAdapter2;
	
	Handler h;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.play_game_layout);
		
		h = new Handler();
		
		GameHandler.setMorrisGame(new Game());
		GameHandler.getMorrisGame().initPlayers();

		
		setUpScoreBoard();
		setScoreBoardNames();
		
		GameHandler.getInstance().getMorrisGame().getPlayer1().getPieces().get(1).setPosition(1);
		
		updateScoreBoard();
	

	}
	/**
	 * Init score board
	 */
	public void setUpScoreBoard(){
		//Set up player 1 pieces
		GridView gridview_black = (GridView) findViewById(R.id.gridview_player1);
		pieceAdapter1 = new PieceAdapter(this,Constant.WHITE);
		gridview_black.setAdapter(pieceAdapter1);
		//Set up player 2 pieces
		GridView gridview_white = (GridView) findViewById(R.id.gridview_player2);
		pieceAdapter2 = new PieceAdapter(this,Constant.BLACK);
		gridview_white.setAdapter(pieceAdapter2);	
	}
	private void setScoreBoardNames(){
		TextView player1 = (TextView)findViewById(R.id.player1_name);
		TextView player2 = (TextView)findViewById(R.id.player2_name);
		player1.setText(GameHandler.getMorrisGame().getPlayer1().getName());
		player2.setText(GameHandler.getMorrisGame().getPlayer2().getName());
	}
	
	/**
	 * When a player have placed a piece. Use this method to update score board
	 */
	public void updateScoreBoard(){
		
		h.post(new Runnable() {
			@Override
			public void run() {
				System.out.println("Tris to update adapters");
				pieceAdapter1.notifyDataSetChanged();
				pieceAdapter2.notifyDataSetChanged();
				LinearLayout l = (LinearLayout)findViewById(R.id.score_board_id);
				l.refreshDrawableState();
			}
		});

	}
	@Override
	public void playerMoved(Player player, Piece piece) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void playerPlacedPiece(Player player, Piece piece) {
		System.out.println("Player placed: GameAct");
		updateScoreBoard();
		
	}
	
	
	
}
