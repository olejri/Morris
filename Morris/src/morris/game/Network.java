package morris.game;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.util.Log;
import android.widget.Toast;

import com.skiller.api.items.SKUser;
import com.skiller.api.listeners.SKBaseListener;
import com.skiller.api.listeners.SKOnFeeChosenListener;
import com.skiller.api.listeners.SKOnGameMoveListener;
import com.skiller.api.operations.SKApplication;
import com.skiller.api.operations.SKTurnBasedTools;
import com.skiller.api.responses.SKBaseResponse;
import com.skiller.api.responses.SKFeeChosenResponse;
import com.skiller.api.responses.SKGameMoveResponse;

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
		Log.i("skiller", "fireNetworkPlayerMoved ID: " + pieceID + " TO: "
				+ toPosition);
		Log.i("movement","fireNetworkPlayerMoved() [Network]");
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
	private void fireNetworkPlayerPlacedPiece(int toPosition) {
		Log.i("skiller", "fireNetworkPlayerPlaced TO: " + toPosition);
		for (NetworkListener l : networkListeners) {
			l.networkPlayerPlacedPiece(toPosition);
		}
	}
	/**
	 * Fire component player removed piece
	 * @param piecePosition
	 */
	private void fireNetworkPlayerDeletedPiece(int piecePosition, int pieceMovedFrom, int pieceMovedTo){
		Log.i("removed","fireNetworkPlayerDeleted [Network]");
		for(NetworkListener l : networkListeners){
			l.networkPlayerRemovedPiece(piecePosition, pieceMovedFrom,pieceMovedTo);
		}
	}
	
	private void fireNetworkPlayerLost(){
		for(NetworkListener l : networkListeners){
			l.networkPlayerLost();
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

		//Starter play game
		 Network.getInstance().getMenuContext().startActivity(new Intent(Network.getInstance().getMenuContext(), PlayGameActivity.class));
	}

	/*
	 * sendInformation() method - Standard game move, sending payload with as a
	 * String with the x,y coordinates/id's
	 */
	public void sendInformation(final String payload, final int event, final String chat) {
		Log.i("skiller", "Checking turn: " + turn);
		if (turn == 1) {
			
				Log.i("skiller", "Network. Send message: " + payload);
				skMorris.getGameManager().getTurnBasedTools().makeGameMove(game_id, event, payload, chat,new SKOnGameMoveListener() {
					@Override
					public void onResponse(SKGameMoveResponse st) {
						if (st.getStatusCode() == 0) {// status OK

							// 1. received data:
							String chat = st.getChatLine();
							int game_state = st.getGameState();
							String game_id = st.getGameId();
							// 2. game logic
							String Opponentpayload = st.getPayload();
							Network.getInstance().handleOpponentMove(game_state, game_id,Opponentpayload);
						}else{
							Log.i("turn","Sending message again: Message" + payload);
							//sendInformation(payload, event, chat);
						}
						
					}
				});
		}else{
			turn = 1;
		}
	}
	
	public void sendWin(){
		
	}

	public void startGame(boolean owner) {
		Network.getInstance().setGameStarted(true);
		if (isGameOwner()) {
			// sending the information
			Network.getInstance().sendInformation(null,
					SKTurnBasedTools.GAME_EVENT_READY_TO_PLAY, null);
		}
		if(!owner){
		Intent intent = new Intent(Network.getInstance().getMenuContext(),
				PlayGameActivity.class);
		Network.getInstance().getMenuContext().startActivity(intent);
		}
	}

	/*
	 * handleOpponentMove() method - handles the opponent move according to
	 * received game_state. invokes the suitable communication method for every
	 * game_state.
	 */
	public void handleOpponentMove(int game_state, String game_id,
			String Opponentpayload) {
		Network.getInstance().setGameStarted(true);
		Log.i("skiller", "handleOpponmentMove(): " + Opponentpayload);

		switch (game_state) {
		
		case SKTurnBasedTools.GAME_STATE_WON:
			Network.getInstance().setServerEndGameresponse(true);
			break;

		case SKTurnBasedTools.GAME_STATE_LOST:
			Network.getInstance().setServerEndGameresponse(true);
			handleMessage(Opponentpayload);
			fireNetworkPlayerLost();
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
		default :
			Log.i("skiller", "After switch turns: " + turn);
			
			handleMessage(Opponentpayload);
			break;
		}


		
	}

	/**
	 * Decode message and performe
	 * 
	 * @param message
	 */
	private void handleMessage(String message) {
		Log.i("skiller", "handleMessage: " + message);
		if (message != null && !message.equals("")) {
			String[] parts = message.split(Constant.SPLIT);
			if ((parts[0]).equals(Constant.MESSAGE_PIECE_PLACED)) {
				int toPosition = Integer.parseInt(parts[1]);
				Network.getInstance().switchTurns();

				fireNetworkPlayerPlacedPiece(toPosition);
				// DO SOMETHING
			} else if ((parts[0]).equals(Constant.MESSAGE_PIECE_MOVED)) {
				Log.i("skiller", "decode message: PIECE_MOVED");
				Log.i("movement","handleMoveMessage() [Network]");
				Network.getInstance().switchTurns();
				int pieceID = Integer.parseInt(parts[1]);
				int toPosition = Integer.parseInt(parts[2]);
				
				fireNetworkPlayerMoved(pieceID, toPosition);

				// DO SOMETHING
			} else if ((parts[0]).equals(Constant.MESSAGE_PIECE_DELETED)) {
				Log.i("turn","handleMessage() [Network] :before: turn = " + turn);
				//Network.getInstance().switchTurns();
				Log.i("turn","handleMessage() [Network] :after:  turn = " + turn);
				int piecePosition = Integer.parseInt(parts[1]);
				int pieceMovedFrom = Integer.parseInt(parts[2]);
				int pieceMovedTo = Integer.parseInt(parts[3]);
				Log.i("removed","handleMoveMessage() PIECE_DELETED [Network]");
				fireNetworkPlayerDeletedPiece(piecePosition,pieceMovedFrom,pieceMovedTo);
			} else {
				//Network.getInstance().switchTurns();
				Log.i("handleMessage [Network]", "switchTurns trigged");
			}
			if(message.contains(Constant.MESSAGE_WON)){
				Network.getInstance().sendInformation("", SKTurnBasedTools.GAME_EVENT_CLAIM_LOSE, null);
			}
		}else{
			Log.i("turn", "HandleMessage() message==null || message.equals('')");
		}
	}

	/*
	 * SwitchTurns() method - switches the turns between the users
	 */
	public void switchTurns() {
		if (turn == 1) {
			Network.getInstance().setTurn(2);
		} else {
			Network.getInstance().setTurn(1);
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
	public void playerPlacedPiece(Player player, Piece piece, boolean won, boolean hotseat) {
		if(!hotseat){
			// Sending place message
			if(GameController.getMorrisGame().getCurrentPlayer()==GameController.getMorrisGame().getPlayer1()){
				String placeMessage = Constant.MESSAGE_PIECE_PLACED + Constant.SPLIT + piece.getPosition();
				send(placeMessage, won);
				Log.i("placement", "playerPlacedPiece [Network]" );
			}
		}
	}

	@Override
	public void playerMoved(int pieceFromPosition, int pieceToPosition, boolean won, boolean hotseat) {
		if(!hotseat){
			// Sending move message
			if(GameController.getMorrisGame().getCurrentPlayer()==GameController.getMorrisGame().getPlayer1()){
				Log.i("movement","playerMoved() [Network]");
				String movedMessage = Constant.MESSAGE_PIECE_MOVED + Constant.SPLIT + pieceFromPosition + Constant.SPLIT + pieceToPosition;
				send(movedMessage, won);
			}
		}
	}

	@Override
	public void playerRemovedPiece(int piecePosition,int pieceMovedFromPosition, int pieceMovedToPosition, boolean won, boolean hotseat) {
		if(!hotseat){
			// Sending remove message
			if(GameController.getMorrisGame().getCurrentPlayer()==GameController.getMorrisGame().getPlayer1()){
				Log.i("removed","playerRemovedPiece() Create Message[Network]");
				String removeMessage = Constant.MESSAGE_PIECE_DELETED + Constant.SPLIT + piecePosition + Constant.SPLIT + pieceMovedFromPosition + Constant.SPLIT + pieceMovedToPosition;
				send(removeMessage, won);
			}
		}
	}
	
	private void send(String payload,boolean won){
		if(won){
			payload+=Constant.SPLIT+Constant.MESSAGE_WON;
			Network.getInstance().sendInformation(payload, SKTurnBasedTools.GAME_EVENT_CLAIM_WIN, null);
		}else{
			Network.getInstance().sendInformation(payload, SKTurnBasedTools.GAME_EVENT_MAKING_MOVE, null);
		}
	}

	@Override
	public void playerChangeTurn(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerWon(int player) {
		Log.i("win", "playerWon [Network]");
		if(player==1){
			Log.i("win", "sending GAME_EVENT_CLAIM_WIN [Network]");
			skMorris.getGameManager().getTurnBasedTools().endPractice(SKTurnBasedTools.GAME_EVENT_CLAIM_WIN, new SKBaseListener() {
				@Override
				public void onResponse(SKBaseResponse st) {
					if(st.getStatusCode()==0)Log.i("win", "GAME_EVENT_CLAIM_WIN sent successfully [Network]");
				}
			});
		}
		
	}
}
