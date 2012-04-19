package morris.game;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.skiller.api.items.SKUser;
import com.skiller.api.listeners.SKOnFeeChosenListener;
import com.skiller.api.operations.SKApplication;
import com.skiller.api.operations.SKTurnBasedTools;
import com.skiller.api.responses.SKFeeChosenResponse;

import morris.game.controller.GameController;
import morris.help.Constant;
import morris.interfaces.GameListener;
import morris.interfaces.NetworkListener;
import morris.models.GameMove;
import morris.models.Piece;
import morris.models.Player;
import morris.models.StartGame;

public class Network implements GameListener {

	List<NetworkListener> networkListeners = new CopyOnWriteArrayList<NetworkListener>();

	private static Network instance = null;

	// Get / Set variables
	private Context menuContext;
	private Context canvasContext;
	private ProgressDialog progressDialog;
	private Timer timer;
	private boolean serverEndGameresponse = false;
	private boolean gameStarted = false;
	private boolean waiting_for_opponnent = false;
	private boolean canvasContextON = false;
	private boolean gameOwner;
	private boolean printed = false;

	// Skiller variables
	private SKApplication skMorris;
	private SKUser owner;
	private SKUser guest;
	private String game_id;
	private int pot;

	private int turn;
	private int side;

	/*
	 * Singleton
	 */
	public static Network getInstance() {
		if (instance == null) {
			instance = new Network();
		}
		return instance;
	}

	public void addListener(NetworkListener listener) {
		networkListeners.add(listener);
	}

	public void removeListener(NetworkListener listener) {
		networkListeners.remove(listener);
	}

	/**
	 * Fire component player moved
	 * 
	 * @param playerID
	 * @param toPosition
	 */
	private void fireNetworkPlayerMoved(int pieceID, int toPosition) {
		Log.i("skiller", "fireNetworkPlayerMoved ID: " + pieceID + " TO: " + toPosition );
		for (NetworkListener l : networkListeners) {
			l.networkPlayerMoved(pieceID, toPosition);
		}
	}

	/**
	 * Fire component player placed piece
	 * 
	 * @param pieceID
	 * @param toPosition
	 */
	private void fireNetworkPlayerPlacedPiece(int pieceID, int toPosition) {
		Log.i("skiller", "fireNetworkPlayerPlaced TO: " + toPosition);
		for (NetworkListener l : networkListeners) {
			l.networkPlayerPlacedPiece(pieceID, toPosition);
		}
	}

	public void clearGame() {
		serverEndGameresponse = false;
		turn = 1;
		printed = false;
		gameStarted = false;
	}

	public void chooseFeeDialog() {
		skMorris.getUIManager().showChooseFeeScreen(menuContext,
				new SKOnFeeChosenListener() {
					@Override
					public void onResponse(SKFeeChosenResponse st) {
						startGameWithChosenFee(st.getFee());
					}
				});
	}

	/*
	 * startGameWithChosenFee() method - creating a game with a chosen fee, the
	 * game will now wait for an opponent to join before anything happens.
	 */
	private void startGameWithChosenFee(int fee) {
		System.out.println("startGameWithChosenFee() started");
		skMorris.getGameManager().getTurnBasedTools()
				.createNewGame(fee, null, null, new StartGame());
		Network.getInstance().clearGame();
		Network.getInstance().setWaiting_for_opponent(true);

		// Intent intent = new Intent(Network.getInstance().getMenuContext(),
		// PlayGameActivity.class);
		// Network.getInstance().getMenuContext().startActivity(intent);
	}

	/*
	 * sendInformation() method - Standard game move, sending payload with as a
	 * String with the x,y coordinates/id's
	 */
	public void sendInformation(String payload, int event, String chat) {
		Log.i("skiller","Checking turn: " + turn);
		if(turn==1){
			Log.i("skiller", "Network. Send message: " + payload);
		skMorris.getGameManager().getTurnBasedTools().makeGameMove(game_id, event, payload, chat, new GameMove());
		}
	}

	public void startGame() {
		Network.getInstance().setGameStarted(true);
		if (isGameOwner()) {
			// sending the information
			Network.getInstance().sendInformation(null,
					SKTurnBasedTools.GAME_EVENT_READY_TO_PLAY, null);
		}
		Intent intent = new Intent(Network.getInstance().getMenuContext(),
				PlayGameActivity.class);
		Network.getInstance().getMenuContext().startActivity(intent);
	}

	/*
	 * handleOpponentMove() method - handles the opponent move according to
	 * received game_state. invokes the suitable communication method for every
	 * game_state.
	 */
	public void handleOpponentMove(int game_state, String game_id,
			String Opponentpayload) {
		Network.getInstance().setGameStarted(true);
		Log.i("skiller", "Payload recieved: " + Opponentpayload);
		handleMessage(Opponentpayload);
		switch (game_state) {
		case SKTurnBasedTools.GAME_STATE_WON:
			Network.getInstance().setServerEndGameresponse(true);
			break;

		case SKTurnBasedTools.GAME_STATE_LOST:
			Network.getInstance().setServerEndGameresponse(true);
			break;

		case SKTurnBasedTools.GAME_STATE_TIED:
			Network.getInstance().setServerEndGameresponse(true);
			break;

		case SKTurnBasedTools.GAME_STATE_ARE_YOU_HERE:
			skMorris.getGameManager()
					.getTurnBasedTools()
					.makeGameMove(game_id,
							SKTurnBasedTools.GAME_EVENT_STILL_HERE, "", null,
							new GameMove());

		default:

			String strx = Opponentpayload.substring(0, 1);
			String stry = Opponentpayload.substring(1, 2);

			int x = Integer.parseInt(strx);
			int y = Integer.parseInt(stry);

			Network.getInstance().makeMove(x, y);

			Network.getInstance().switchTurns();
			// NOW CHECK IF SOMEONE IS WINNING, NEEDS MOAR LOGIC
			
			
			break;
		}
	}

	/**
	 * Decode message and performe
	 * 
	 * @param message
	 */
	private void handleMessage(String message) {
		Log.i("skiller", "decode message: " + message);
		String[] parts = message.split(Constant.SPLIT);
		if ((parts[0]).equals(Constant.MESSAGE_PIECE_PLACED)) {
			Log.i("skiller", "decode message: PIECE_PLACED");
			int toPosition = Integer.parseInt(parts[1]);
			fireNetworkPlayerPlacedPiece(0, toPosition);
			// DO SOMETHING
		} else if ((parts[0]).equals(Constant.MESSAGE_PIECE_MOVED)) {
			Log.i("skiller", "decode message: PIECE_MOVED");
			int pieceID = Integer.parseInt(parts[1]);
			int toPosition = Integer.parseInt(parts[2]);
			fireNetworkPlayerMoved(pieceID, toPosition);
			// DO SOMETHING
		} else if ((parts[0]).equals(Constant.MESSAGE_PIECE_DELETED)) {
			int pieceID = Integer.parseInt(parts[1]);
		}
	}

	/*
	 * handleMyMove() method - handles my move according to recieved x and y
	 * data. Invotes the suitable communication method when needed
	 */
	public void handleMyMove(int x, int y) {
		Network.getInstance().switchTurns();
		String payload = Integer.toString(x) + Integer.toString(y);
		int event1 = SKTurnBasedTools.GAME_EVENT_MAKING_MOVE;
		String chat = null;

		Network.getInstance().sendInformation(payload, event1, chat);
	}

	// game logic's methods
	// makeMove method - gets the coordinates and updates the data structure if
	// needed
	public boolean makeMove(int x, int y) {
		// oppdater spillebrette med det nye trekket, evnt brikke som er
		// fjernet.
		return true;
	}

	/*
	 * SwitchTurns() method - switches the turns between the users
	 */
	public void switchTurns() {
		if (turn == 1) {
			this.setTurn(2);
		} else {
			this.setTurn(1);
		}
	}

	/*
	 * showToastOnCanvas() method - shows a specified toast message on the
	 * canvas
	 */
	public void showToastOnCanvas(final String string) {
		Runnable toastAction = new Runnable() {

			@Override
			public void run() {
				Toast.makeText(Network.getInstance().getCanvasContext(),
						string, Toast.LENGTH_SHORT).show();

			}

		};
		((Activity) (Network.getInstance().getCanvasContext()))
				.runOnUiThread(toastAction);
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	public boolean isServerEndGameresponse() {
		return serverEndGameresponse;
	}

	public void setServerEndGameresponse(boolean serverEndGameresponse) {
		this.serverEndGameresponse = serverEndGameresponse;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public boolean isPrinted() {
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

	@Override
	public void playerPlacedPiece(Player player, Piece piece) {
		// Sending place message
		String movedMessage = Constant.MESSAGE_PIECE_PLACED + Constant.SPLIT
				+ piece.getPosition();
		Network.getInstance().sendInformation(movedMessage,
				SKTurnBasedTools.GAME_EVENT_MAKING_MOVE, null);
	}

	@Override
	public void playerMoved(int pieceFromPosition, int pieceToPosition) {
		// Sending move message
		String movedMessage = Constant.MESSAGE_PIECE_MOVED + Constant.SPLIT
				+ pieceFromPosition + Constant.SPLIT + pieceToPosition;
		Network.getInstance().sendInformation(movedMessage,
				SKTurnBasedTools.GAME_EVENT_MAKING_MOVE, null);

	}
}
