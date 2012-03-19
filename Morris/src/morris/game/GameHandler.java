package morris.game;

import java.util.Timer;

import morris.models.StartGame;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import morris.models.Game;

import com.skiller.api.items.SKUser;
import com.skiller.api.listeners.SKOnFeeChosenListener;
import com.skiller.api.listeners.SKOnGetFeeOptionsListener;
import com.skiller.api.operations.SKApplication;
import com.skiller.api.responses.SKFeeChosenResponse;
import com.skiller.api.responses.SKGetFeeOptionsResponse;

public class GameHandler {

	private static GameHandler instance = null;
	private SKApplication skMorris;
	private Context menuContext;
	private ProgressDialog progressDialog;
	private boolean waiting_for_opponnent = false;
	private Timer timer;

	private boolean gameOwner;

	private String game_id;
	private SKUser owner;
	private SKUser guest;
	private int pot;
	private static Game morrisGame = null;

	public static GameHandler getInstance() {
		if (instance == null) {
			instance = new GameHandler();
		}
		return instance;
	}

	private GameHandler() {
	}

	/**
	 * Set MorrisGame
	 * 
	 * @param morrisGame
	 */
	public static void setMorrisGame(Game morrisGame) {
		GameHandler.morrisGame = morrisGame;
	}

	/**
	 * Return MorrisGame
	 * 
	 * @return
	 */
	public static Game getMorrisGame() {
		return morrisGame;
	}

	// createNewGame() method - starts a new game that other can join
	public void createNewGame() {
		// startGameWithChosenFee(2);
		// getMinMAXValuesForChooseFeeDialog();
		chooseFeeDialog();
		setMorrisGame(new Game());
	}

	// clearGame() method - clears the game attributes
	public void clearGame() {

	}

	// chooseFeeDialog() method - opens a dialog where u state the fee for the
	// new game
	private void chooseFeeDialog() {
		skMorris.getUIManager().showChooseFeeScreen(menuContext,
				new SKOnFeeChosenListener() {
					@Override
					public void onResponse(SKFeeChosenResponse st) {
						startGameWithChosenFee(st.getFee());
					}
				});
	}

	private void getMinMAXValuesForChooseFeeDialog() {
		String lols = skMorris.getGameManager().getTurnBasedTools().toString();
		System.out.println("WUT" + lols);
		skMorris.getGameManager().getTurnBasedTools()
				.getFeeOptions(new SKOnGetFeeOptionsListener() {

					private int minFee = 0;
					private int maxFee = 0;
					private String title = "Choose fee";
					private String text;

					@Override
					public void onResponse(SKGetFeeOptionsResponse arg0) {
						if (arg0.getStatusCode() == 0) {// valid response
							minFee = arg0.getMinFee();
							maxFee = arg0.getMaxFee();
							text = "Game fee (" + minFee + " - " + maxFee
									+ "): ";
							showChooseFeeDialog(title, text);
						} else {// error
							text = "Game fee (" + minFee + " - " + maxFee
									+ "): ";
							showChooseFeeDialog(title, text);
						}
					}

					private void showChooseFeeDialog(final String title,
							final String text) {
						AlertDialog.Builder alert = new AlertDialog.Builder(
								menuContext);
						alert.setTitle(title);
						alert.setMessage(text);
						// Set an EditText view to get user input
						final EditText input = new EditText(menuContext);

						alert.setView(input);
						alert.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int whichButton) {

										String value = input.getText().toString();
										int fee = 0;
										try {
											fee = Integer.parseInt(value);
										} catch (NumberFormatException e) {
											showFeeErrorToast();
											return;
										}

										if ((fee < minFee) || (fee > maxFee)) {
											showFeeErrorToast();
											return;
										}

										// invokes the CreateNewGame() method of
										// the TurnBasedGames object.
										// this method creates a new game that
										// is represented in the lobby and
										// opened for other users to join
										startGameWithChosenFee(fee);

										return;
									}

									private void showFeeErrorToast() {
										Toast.makeText(menuContext,"Invalid fee value!",Toast.LENGTH_SHORT).show();
										showChooseFeeDialog(title, text);
									}
								});

						alert.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int which) {
										return;
									}
								});

						alert.show();
					}

				});
	}

	private void startGameWithChosenFee(int fee) {
		System.out.println("startGameWithChosenFee() started");
	//	skMorris.getGameManager().getTurnBasedTools().createNewGame(fee, null, null, new StartGame());
		/*
		 * GameHandler.getInstance().clearGame();
		 * GameHandler.getInstance().setWaiting_for_opponent(true); Context lol
		 * = GameHandler.getInstance().getMenuContext();
		 * System.out.println(""+fee); System.out.println(""+lol);
		 */
		Intent intent = new Intent(GameHandler.getInstance().getMenuContext(),
				PlayGameActivity.class);
		GameHandler.getInstance().getMenuContext().startActivity(intent);
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String gameId) {
		game_id = gameId;
	}

	public SKUser getOwner() {
		return owner;
	}

	public void setOwner(SKUser owner) {
		this.owner = owner;
	}

	public SKUser getGuest() {
		return guest;
	}

	public void setGuest(SKUser guest) {
		this.guest = guest;
	}

	public int getPot() {
		return pot;
	}

	public void setPot(int pot) {
		this.pot = pot;
	}

	public boolean isGameOwner() {
		return gameOwner;
	}

	public void setGameOwner(boolean gameCreator) {
		this.gameOwner = gameCreator;
	}

	public boolean isWaiting_for_opponnent() {
		return waiting_for_opponnent;
	}

	public void setWaiting_for_opponent(boolean waitingForOpponnent) {
		waiting_for_opponnent = waitingForOpponnent;
	}

	public Context getMenuContext() {
		return menuContext;
	}

	public void setMenuContext(Context context) {
		this.menuContext = context;
	}

	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	public SKApplication getSkApplication() {
		return skMorris;
	}

	public void setSkApplication(SKApplication skMorris) {
		this.skMorris = skMorris;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;

	}

}
