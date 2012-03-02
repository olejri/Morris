package morris.game;

import com.skiller.api.listeners.SKBaseListener;
import com.skiller.api.listeners.SKOnTurnbasedGameChosenListener;
import com.skiller.api.operations.SKApplication;
import com.skiller.api.responses.SKBaseResponse;
import com.skiller.api.responses.SKTurnbasedGameChosenResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MorrisActivity extends Activity {
	
	
	public MorrisActivity ma = this;
	
	SKApplication skMorris;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		//GameHandler.getInstance().setMenuContext(ma);
		
		Display display = ma.getWindowManager().getDefaultDisplay();
		
		int screen_width = display.getWidth();
		int screen_height = display.getHeight();
		String deviceId = null;
		String user_agent = null;
		
		//Skiller id's for permission to use the Skiller Tools
		String app_key = "465abbaa5ebd41498903a2a42576f639";
		String app_secret = "d7a8155968d9437781f82f10df7c5188";
		String app_id = "934190831255";
		
		skMorris = new SKApplication(app_id, app_key, app_secret, "1", 0);
		
		skMorris.login(ma, screen_width, screen_height, deviceId, user_agent, new SKBaseListener(){
			public void onResponse(SKBaseResponse st){
				
			}
		});
		
		
		Button openNewGameButton = (Button) findViewById(R.id.menu_button_creategame);
		Button joinGameButton = (Button) findViewById(R.id.menu_button_joingame);
		Button helpGameButton = (Button) findViewById(R.id.menu_button_help);
		
		
		openNewGameButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				GameHandler.getInstance().clearGame();
				GameHandler.getInstance().createNewGame();
				Intent intent=new Intent(MorrisActivity.this, PlayGameActivity.class);
				startActivity(intent);
			}
		});
		
		joinGameButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				GameHandler.getInstance().clearGame();
				skMorris.getUIManager().showTurnbasedGamesLobbyScreen(ma, new SKOnTurnbasedGameChosenListener(){
					public void onResponse(SKTurnbasedGameChosenResponse arg0){
						//skMorris.getGameManager().getTurnBasedTools().joinGame(arg0.getGameId(), new StartGameListener());
					}
				});
			}
		});
		
		helpGameButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent=new Intent(MorrisActivity.this, HelpActivity.class);
				startActivity(intent);	
			}
		});
		
	}

}