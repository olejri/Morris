package morris.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class PlayGameActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.play_game_layout);
		
	}
	
}
