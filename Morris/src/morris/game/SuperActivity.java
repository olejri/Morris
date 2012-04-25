package morris.game;

import morris.help.Constant;

import com.skiller.api.listeners.SKBaseListener;
import com.skiller.api.operations.SKApplication;
import com.skiller.api.responses.SKBaseResponse;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;

public class SuperActivity extends Activity {
	public SKApplication skMorris;
	public Network network;
	public Display display;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		display = getWindowManager().getDefaultDisplay();
		network = Network.getInstance();
		network.setCanvasContext(getApplicationContext());
		// Init soundManager

		initSkiller();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (SoundManager.getInstance().getSound()) {
			Log.i("sound","onStart() [Super] : Sound is true");
			((Button) findViewById(R.id.soundButton)).setBackgroundResource(R.drawable.sound_on);
		} else {
			Log.i("sound","onStart() [Super] : Sound is true");
			((Button) findViewById(R.id.soundButton)).setBackgroundResource(R.drawable.sound_off);
		}
		
	}

	public void initSkiller() {
		skMorris = new SKApplication(Constant.app_id, Constant.app_key,
				Constant.app_secret, "1", 0);
		network.setSkApplication(skMorris);
		skMorris.login(this, display.getWidth(), display.getHeight(), null,
				null, new SKBaseListener() {
					@Override
					public void onResponse(SKBaseResponse st) {
					}
				});
	}

	/**
	 * Called when user is pressing sound button. Turn sound on/off
	 * 
	 * @param v
	 */
	public void clickSound(View v) {
		if (!SoundManager.getInstance().getSound()) {
			SoundManager.getInstance().startBackgroundSound();
			((Button) findViewById(R.id.soundButton))
					.setBackgroundResource(R.drawable.sound_on);
		} else {
			SoundManager.getInstance().stopBackgroundSound();
			((Button) findViewById(R.id.soundButton))
					.setBackgroundResource(R.drawable.sound_off);
		}
	}

}
