package morris.game;

import morris.help.Constant;
import morris.models.StartGame;
import morris.models.Game;
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
import android.view.Window;

public class MorrisActivity extends Activity {


	SKApplication skMorris;
	private int screen_width; 
	private int screen_height; 


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		//GameHandler.getInstance().setMenuContext(ma);

		Display display = getWindowManager().getDefaultDisplay();
		screen_width = display.getWidth();
		screen_height = display.getHeight();

		skMorris = new SKApplication(Constant.app_id, Constant.app_key, Constant.app_secret, "1", 0);
		GameHandler.getInstance().setSkApplication(skMorris);
		skMorris.login(this, screen_width, screen_height, null, null, new SKBaseListener(){
			public void onResponse(SKBaseResponse st){

			}
		});	
		
		
	}

	public void onClick(View view) {
		Intent i = new Intent();
		if (view.getId() == R.id.menu_button_creategame) {
			GameHandler.getInstance().clearGame();
			GameHandler.getInstance().createNewGame();
			//i.setClass(this, PlayGameActivity.class);
			//startActivity(i);
		} else if (view.getId() == R.id.menu_button_joingame) {
			skMorris.getUIManager().showTurnbasedGamesLobbyScreen(this, new SKOnTurnbasedGameChosenListener(){
				public void onResponse(SKTurnbasedGameChosenResponse st){
					skMorris.getGameManager().getTurnBasedTools().joinGame(st.getGameId(), new StartGame());
				}
			});
		} else if (view.getId() == R.id.menu_button_help) {
			i.setClass(this, HelpActivity.class);
			startActivity(i);
		}

	}
	
	@Override
	public void onDestroy(){
		skMorris.logout(new SKBaseListener(){
			@Override
			public void onResponse(SKBaseResponse st){
				
			}
		});
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}