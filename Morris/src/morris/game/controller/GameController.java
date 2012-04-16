package morris.game.controller;

import java.util.ArrayList;
import java.util.Timer;

import morris.game.PlayGameActivity;
import morris.gui.Point;
import morris.help.Constant;
import morris.models.GameMove;
import morris.models.ModelPoint;
import morris.models.Piece;
import morris.models.StartGame;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import morris.models.Game;
import morris.states.FlyingState;
import morris.states.MoveState;
import morris.states.PlacementState;
import morris.states.RemovalState;
import morris.states.SelectState;

import com.skiller.api.items.SKUser;
import com.skiller.api.listeners.SKOnFeeChosenListener;
import com.skiller.api.listeners.SKOnGetFeeOptionsListener;
import com.skiller.api.operations.SKApplication;
import com.skiller.api.operations.SKTurnBasedTools;
import com.skiller.api.responses.SKFeeChosenResponse;
import com.skiller.api.responses.SKGetFeeOptionsResponse;

public class GameController {

	private static GameController instance = null;


	private Context menuContext;
	private Context canvasContext;

	private ProgressDialog progressDialog;
	private Timer timer;

	private boolean serverEndGameresponse=false;
	private boolean gameStarted=false;
	private boolean waiting_for_opponnent = false;
	private boolean canvasContextON=false;
	private boolean gameOwner;
	private boolean printed = false;

	private static Game morrisGame = null;

	private SKApplication skMorris;
	private SKUser owner;
	private SKUser guest;
	private String game_id;
	private int pot;

	private int turn;
	private int side;

	public static GameController getInstance() {
		if (instance == null) {
			instance = new GameController();
		}
		return instance;
	}

	public static Game getGame(){
		return GameController.getInstance().getMorrisGame();
	}


	private GameController() {
		clearGame();
	}


	/*
	 *  createNewGame() method - starts a new game that other can join
	 */
	public void createNewGame() {
		chooseFeeDialog();
	}

	/*
	 *  clearGame() method - clears the game attributes
	 */
	public void clearGame() {
		serverEndGameresponse=false;
		turn = 1;
		printed = false;
		gameStarted=false;
	}

	/*
	 * chooseFeeDialog() method - opens a dialog where u choose the fee for the new game
	 */
	private void chooseFeeDialog() {
		skMorris.getUIManager().showChooseFeeScreen(menuContext,
				new SKOnFeeChosenListener() {
			@Override
			public void onResponse(SKFeeChosenResponse st) {
				startGameWithChosenFee(st.getFee());
			}
		});
	}

	/*
	 * 	getMinMAXValuesForChooseFeeDialog() method - create a custom fee dialog when creating
	 * a new game. *Currently not used*
	 */
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

	/*
	 * startGameWithChosenFee() method - creating a game with a chosen fee, the game will
	 * now wait for an opponent to join before anything happens.
	 */
	private void startGameWithChosenFee(int fee) {
		System.out.println("startGameWithChosenFee() started");
		skMorris.getGameManager().getTurnBasedTools().createNewGame(fee, null, null, new StartGame());
		GameController.getInstance().clearGame();
		GameController.getInstance().setWaiting_for_opponent(true);


		Intent intent = new Intent(GameController.getInstance().getMenuContext(), PlayGameActivity.class);
		GameController.getInstance().getMenuContext().startActivity(intent);
	}

	/*
	 * sendInformation() method - Standard game move, sending payload with as a String with the x,y coordinates/id's
	 */
	public void sendInformation(String payload, int event, String chat){
		skMorris.getGameManager().getTurnBasedTools().makeGameMove(game_id, event, payload, chat, new GameMove());
	}	


	/*
	 * handleMyMove() method - handles my move according to recieved x and y data.
	 * Invotes the suitable communication method when needed
	 */
	public void handleMyMove(int x, int y){

	}

	/*
	 *  handleOpponentMove() method - handles the opponent move according to received game_state.
	 *  invokes the suitable communication method for every game_state.
	 */	
	public void handleOpponentMove(int game_state, String game_id, String Opponentpayload){
		GameController.getInstance().setGameStarted(true);

		switch(game_state){
		case SKTurnBasedTools.GAME_STATE_WON:
			GameController.getInstance().setServerEndGameresponse(true);
			break;

		case SKTurnBasedTools.GAME_STATE_LOST:
			GameController.getInstance().setServerEndGameresponse(true);
			break;

		case SKTurnBasedTools.GAME_STATE_TIED:
			GameController.getInstance().setServerEndGameresponse(true);
			break;

		case SKTurnBasedTools.GAME_STATE_ARE_YOU_HERE:
			skMorris.getGameManager().getTurnBasedTools().makeGameMove(game_id, SKTurnBasedTools.GAME_EVENT_STILL_HERE, "", null, new GameMove());

		default:

			String strx = Opponentpayload.substring(0, 1);
			String stry = Opponentpayload.substring(1, 2);

			int x = Integer.parseInt(strx);
			int y = Integer.parseInt(stry);

			GameController.getInstance().makeMove(x, y);

			GameController.getInstance().switchTurns();
			//NOW CHECK IF SOMEONE IS WINNING, NEEDS MOAR LOGIC

			break;
		}
	}


	/*
	 * makeMove() method - gets the coordinates and updates the data structure
	 * *MANGLER LOGIKK*
	 */
	public boolean makeMove(int x, int y){
		return true;
	}


	/*
	 *  SwitchTurns() method - switches the turns between the users
	 */
	public void switchTurns(){
		if(turn == 1){
			this.setTurn(2);
		}
		else{
			this.setTurn(1);
		}
	}

	/*
	 * showToastOnCanvas() method - shows a specified toast message on the canvas
	 */
	public void showToastOnCanvas(final String string){
		Runnable toastAction = new Runnable(){

			@Override
			public void run() {
				Toast.makeText(GameController.getInstance().getCanvasContext(), string, Toast.LENGTH_SHORT).show();

			}

		};
		((Activity)(GameController.getInstance().getCanvasContext())).runOnUiThread(toastAction );
	}


	public void setTurn(int turn){
		this.turn = turn;
	}

	public int getSide(){
		return side;
	}

	public void setSide(int side){
		this.side = side;
	}

	public boolean isServerEndGameresponse(){
		return serverEndGameresponse;
	}

	public void setServerEndGameresponse(boolean serverEndGameresponse){
		this.serverEndGameresponse = serverEndGameresponse;
	}

	public boolean isGameStarted() 
	{
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) 
	{
		this.gameStarted = gameStarted;
	}

	public boolean isPrinted(){
		return printed;
	}

	public void setPrinted(boolean printed) {
		this.printed = printed;
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

	public Context getCanvasContext() {
		return canvasContext;
	}

	public void setCanvasContext(Context canvasContext) {
		this.canvasContext = canvasContext;
	}

	public boolean isCanvasContextON() {
		return canvasContextON;
	}

	public void setCanvasContextON(boolean canvasContextON) {
		this.canvasContextON = canvasContextON;
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
	/**
	 * Set MorrisGame
	 * 
	 * @param morrisGame
	 */
	public static void setMorrisGame(Game morrisGame) {
		GameController.morrisGame = morrisGame;
	}

	/**
	 * Return MorrisGame
	 * 
	 * @return
	 */
	public static Game getMorrisGame() {
		return morrisGame;
	}

	public static void handlePlayerAction(Point p) {
		if (morrisGame.getState() instanceof PlacementState) {
			for (int i = 0; i < morrisGame.getCurrentPlayer().getPieces().size(); i++) {
				Piece piece = morrisGame.getCurrentPlayer().getPieces().get(i);
				if (piece.getPosition() < 0) {
					morrisGame.playerPlacedPiece(morrisGame.getCurrentPlayer(),piece, p.getId());
					break;
				}
			}
			if(morrisGame.getState() instanceof RemovalState){
				morrisGame.updatePieceImages(morrisGame.getOpponent(), p.getId());
			}
			morrisGame.updatePieceImages(morrisGame.getOpponent(), p.getId());
		}
		else if(morrisGame.getState() instanceof SelectState){
			if(morrisGame.selectable(morrisGame.getCurrentPlayer(), p.getId())){
				morrisGame.updatePieceImages(morrisGame.getCurrentPlayer(), p.getId());
				morrisGame.setState(new MoveState());
			}
		}
		else if(morrisGame.getState() instanceof MoveState){
			morrisGame.setPreviousState(new MoveState());
			morrisGame.updatePieceImages(morrisGame.getCurrentPlayer(), p.getId());
			if(morrisGame.getCurrentPlayer().getSelectedPiece().getPosition()==p.getId()){
				morrisGame.setState(new SelectState());
			}
			else{
				morrisGame.move(morrisGame.getCurrentPlayer().getSelectedPiece(), p.getId(), morrisGame.getCurrentPlayer()); // SISTE PARAMETER ER SPILLER ID
				if(morrisGame.getState() instanceof RemovalState){
					morrisGame.updatePieceImages(morrisGame.getOpponent(), p.getId());
				}
				else{
					morrisGame.setState(new SelectState());
				}

			}

		}
		else if(morrisGame.getState() instanceof RemovalState){
			//getPlayer1() just for testing, must be changed to getPlayer2()
			if(morrisGame.removePiece(p, morrisGame.getOpponent())){
				morrisGame.setState(morrisGame.getPreviousState());
				morrisGame.updatePieceImages(morrisGame.getCurrentPlayer(), p.getId());
				
			}
			
			//morrisGame.updatePieceImages(morrisGame.getOpponent(), p.getId());

		}
		else if(morrisGame.getState() instanceof FlyingState){
			if(morrisGame.getCurrentPlayer().getSelectedPiece().getPosition()==p.getId()){
				morrisGame.setState(new SelectState());
			}else{
				morrisGame.move(morrisGame.getCurrentPlayer().getSelectedPiece(), p.getId(), morrisGame.getCurrentPlayer()); // SISTE PARAMETER ER SPILLER ID
				morrisGame.setState(new SelectState());

			}
		}
	}
	//Player1 is used for testing, must change with getPlayingPlayer()
	public static ArrayList<ModelPoint> getHighlightsList() {
		ArrayList<ModelPoint> highlights; 
		if(morrisGame.getCurrentPlayer().getSelectedPiece() != null){					
			highlights = morrisGame.getHighlightList(morrisGame.getCurrentPlayer().getSelectedPiece().getPosition(), morrisGame.getCurrentPlayer());  
		} else {
			highlights = morrisGame.getHighlightList(-1, morrisGame.getCurrentPlayer());  
		}
		return highlights;
	}

}
