package morris.game;

import morris.help.Constant;

import com.skiller.api.listeners.SKBaseListener;
import com.skiller.api.operations.SKApplication;
import com.skiller.api.responses.SKBaseResponse;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;

public class SuperActivity extends Activity {
	public SKApplication skMorris;
	public Network network;
	public Display display;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		display = getWindowManager().getDefaultDisplay();
		network = Network.getInstance();
		network.setCanvasContext(getApplicationContext());
		initSkiller();
	}
	
	public void initSkiller(){
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
	
}
