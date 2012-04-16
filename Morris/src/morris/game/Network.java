package morris.game;

import java.util.Timer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.skiller.api.items.SKUser;
import com.skiller.api.listeners.SKOnFeeChosenListener;
import com.skiller.api.operations.SKApplication;
import com.skiller.api.operations.SKTurnBasedTools;
import com.skiller.api.responses.SKFeeChosenResponse;

import morris.game.controller.GameController;
import morris.interfaces.GameListener;
import morris.interfaces.NetworkListener;
import morris.models.GameMove;
import morris.models.Piece;
import morris.models.Player;
import morris.models.StartGame;

public class Network implements GameListener{
	
	private static Network instance = null;
	
	// Get / Set variables
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
		skMorris.getGameManager().getTurnBasedTools().makeGameMove(game_id, event, payload, chat, new NetworkListener());
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
	 * handleMyMove() method - handles my move according to recieved x and y data.
	 * Invotes the suitable communication method when needed
	 */
	public void handleMyMove(int x, int y){
		Network.getInstance().switchTurns();
		String payload = Integer.toString(x)+Integer.toString(y);
		int event1 = SKTurnBasedTools.GAME_EVENT_MAKING_MOVE;
		String chat = null;
		
		Network.getInstance().sendInformation(payload, event1, chat);
	}
	
	//game logic's methods	
	//makeMove method - gets the coordinates and updates the data structure if needed 
	public boolean makeMove(int x, int y){
		//oppdater spillebrette med det nye trekket, evnt brikke som er fjernet.
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


	@Override
	public void playerMoved(Player player, Piece piece) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void playerPlacedPiece(Player player, Piece piece) {
		// TODO Auto-generated method stub
		
	}
}
