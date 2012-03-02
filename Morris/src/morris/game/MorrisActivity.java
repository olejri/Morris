package morris.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MorrisActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		

		// SKJER A?
		System.out.println("HAHAHAHAHAH GIT ROX");
	}

	public void onClick(View view) {
		Intent i = new Intent();
		if (view.getId() == R.id.menu_button_creategame) {
			i.setClass(this, PlayGameActivity.class);
		} else if (view.getId() == R.id.menu_button_joinegame) {
			i.setClass(this, PlayGameActivity.class);
		} else if (view.getId() == R.id.menu_button_help) {
			i.setClass(this, HelpActivity.class);
		}
		startActivity(i);
	}

}