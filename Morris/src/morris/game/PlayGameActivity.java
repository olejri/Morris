package morris.game;

import morris.gui.PieceAdapter;
import morris.help.Constant;
import morris.models.Game;
import morris.models.Piece;
import morris.models.Player;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

public class PlayGameActivity extends Activity {
	PieceAdapter pieceAdapter1;
	PieceAdapter pieceAdapter2;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.play_game_layout);
		
		GameHandler.setMorrisGame(new Game());
		GameHandler.getMorrisGame().initPlayers();

		
		setUpScoreBoard();
		setScoreBoardNames();
	

	}
	/**
	 * Init score board
	 */
	private void setUpScoreBoard(){
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
	private void updateScoreBoard(){
		pieceAdapter1.notifyDataSetChanged();
		pieceAdapter2.notifyDataSetChanged();
	}
	
	
	
}
