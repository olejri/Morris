package morris.game;

import morris.game.controller.GameController;
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
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {

	private SKApplication skMorris;
	private int screen_width;
	private int screen_height;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		GameController.getInstance().setMenuContext(this);
		Display display = getWindowManager().getDefaultDisplay();
		setButtonFonts();

		skMorris = new SKApplication(Constant.app_id, Constant.app_key,
				Constant.app_secret, "1", 0);
		GameController.getInstance().setSkApplication(skMorris);
		skMorris.login(this, display.getWidth(), display.getHeight(), null,
				null, new SKBaseListener() {
			public void onResponse(SKBaseResponse st) {


			}
		});	


	}

	private void setButtonFonts() {
		Typeface button_font = Typeface.createFromAsset(getAssets(),
		"fonts/text-font.otf");
		((Button) ((Activity) this).findViewById(R.id.menu_button_creategame))
		.setTypeface(button_font);
		((Button) ((Activity) this).findViewById(R.id.menu_button_joingame))
		.setTypeface(button_font);
		((Button) ((Activity) this).findViewById(R.id.menu_button_help))
		.setTypeface(button_font);
		((Button) ((Activity) this).findViewById(R.id.menu_button_show_board))
		.setTypeface(button_font);
		((TextView) ((Activity) this).findViewById(R.id.toolbar_title))
		.setTypeface(button_font);

	}

	public void onClick(View view) {
		Intent i = new Intent();
		if (view.getId() == R.id.menu_button_creategame) {
			GameController.getInstance().clearGame();
			GameController.getInstance().createNewGame();
			// i.setClass(this, PlayGameActivity.class);
			// startActivity(i);
		} else if (view.getId() == R.id.menu_button_joingame) {
			skMorris.getUIManager().showTurnbasedGamesLobbyScreen(this,
					new SKOnTurnbasedGameChosenListener() {
				public void onResponse(SKTurnbasedGameChosenResponse st) {
					skMorris.getGameManager().getTurnBasedTools()
					.joinGame(st.getGameId(), new StartGame());
				}
			});
		} else if (view.getId() == R.id.menu_button_help) {
			i.setClass(this, HelpActivity.class);
			startActivity(i);
		} else if (view.getId() == R.id.menu_button_show_board) {
			startActivity(new Intent(this, PlayGameActivity.class));
		}

	}

	@Override
	public void onDestroy() {
		skMorris.logout(new SKBaseListener() {
			@Override
			public void onResponse(SKBaseResponse st) {
			}
		});
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}