package morris.game;

import morris.game.controller.GameController;
import morris.help.Constant;
import morris.models.StartGame;
import morris.models.Game;

import com.skiller.api.listeners.SKBaseListener;
import com.skiller.api.listeners.SKOnTurnbasedGameChosenListener;
import com.skiller.api.operations.SKApplication;
import com.skiller.api.operations.SKUIManager;
import com.skiller.api.responses.SKBaseResponse;
import com.skiller.api.responses.SKTurnbasedGameChosenResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class MainActivity extends SuperActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		network.setMenuContext(this);
		setButtonFonts();

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
		((Button) ((Activity) this).findViewById(R.id.menu_button_achievements))
				.setTypeface(button_font);
		((TextView) ((Activity) this).findViewById(R.id.toolbar_title))
				.setTypeface(button_font);

	}

	public void onClick(View view) {
		Intent i = new Intent();
		if (view.getId() == R.id.menu_button_creategame) {
			// GameController.getInstance().createNewGame();
			network.chooseFeeDialog();
		} else if (view.getId() == R.id.menu_button_joingame) {
			skMorris.getUIManager().showTurnbasedGamesLobbyScreen(this,
					new SKOnTurnbasedGameChosenListener() {
						@Override
						public void onResponse(SKTurnbasedGameChosenResponse st) {
							skMorris.getGameManager().getTurnBasedTools()
									.joinGame(st.getGameId(), new StartGame());
						}
					});
		} else if (view.getId() == R.id.menu_button_help) {
			i.setClass(this, HelpActivity.class);
			startActivity(i);
		} else if (view.getId() == R.id.menu_button_show_board) {
			i = new Intent(this, PlayGameActivity.class);
			i.putExtra("Hotseat", true);
			startActivity(i);
		} else if (view.getId() == R.id.menu_button_achievements) {
			skMorris.getUIManager().showScreen(this,
					SKUIManager.ACHIEVEMENTS_SOCIAL);
		}

	}

	@Override
	public void onBackPressed(){
		super.onBackPressed();
		skMorris.logout(new SKBaseListener() {
			@Override
			public void onResponse(SKBaseResponse st) {
			}
		});
	}

}