package morris.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class HelpActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help_layout);
	}

}
