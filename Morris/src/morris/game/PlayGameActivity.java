package morris.game;

import morris.gui.PieceAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.GridView;

public class PlayGameActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.play_game_layout);
		
		setUpScoreBoard();

	}
	
	private void setUpScoreBoard(){
		//Set up player 1 pieces
		GridView gridview_black = (GridView) findViewById(R.id.gridview_player1);
		PieceAdapter adapter_black = new PieceAdapter(this,false,9);
		gridview_black.setAdapter(adapter_black);
		//Set up player 2 pieces
		GridView gridview_white = (GridView) findViewById(R.id.gridview_player2);
		PieceAdapter adapter_white = new PieceAdapter(this,true,9);
		gridview_white.setAdapter(adapter_white);
	}
	
	
	
}
