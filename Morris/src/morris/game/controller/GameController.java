package morris.game.controller;

import java.util.ArrayList;
import java.util.Timer;

import morris.gui.Point;
import morris.models.ModelPoint;
import morris.models.Piece;
import android.app.ProgressDialog;
import android.content.Context;
import morris.models.Game;
import morris.states.FlyingState;
import morris.states.MoveState;
import morris.states.PlacementState;
import morris.states.RemovalState;
import morris.states.SelectState;

import com.skiller.api.items.SKUser;
import com.skiller.api.operations.SKApplication;

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
	
	static boolean hotseat = true;

	public static GameController getInstance() {
		if (instance == null) {
			instance = new GameController();
		}
		return instance;
	}

	public static Game getGame(){
		GameController.getInstance();
		return GameController.getMorrisGame();
	}


	private GameController() {
	}


	/*
	 *  createNewGame() method - starts a new game that other can join
	 */
	public void createNewGame() {
		GameController.getInstance();
		GameController.setMorrisGame(new Game());
		GameController.getInstance();
		GameController.getMorrisGame().initPlayers();
		//chooseFeeDialog();
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
		if(morrisGame.getCurrentPlayer()==morrisGame.getPlayer1()){		
			if (morrisGame.getState() instanceof PlacementState) {
				for (int i = 0; i < morrisGame.getCurrentPlayer().getPieces().size(); i++) {
					Piece piece = morrisGame.getCurrentPlayer().getPieces().get(i);
					if (piece.getPosition() < 0) {
						morrisGame.playerPlacedPiece(morrisGame.getCurrentPlayer(),piece, p.getId());
						// NYTT
						if(morrisGame.checkMorris(piece, morrisGame.getCurrentPlayer())){
							morrisGame.setState(new RemovalState());
						} else {
							morrisGame.changePlayer(true);
						}
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
					if(morrisGame.getCurrentPlayer().getPieces().size() == 3){
						morrisGame.setState(new FlyingState());
					} else {
						morrisGame.setState(new MoveState());
					}
				}
			}
			else if(morrisGame.getState() instanceof MoveState){
				morrisGame.setPreviousState(new MoveState());
				morrisGame.updatePieceImages(morrisGame.getCurrentPlayer(), p.getId());
				if(morrisGame.getCurrentPlayer().getSelectedPiece().getPosition()==p.getId()){
					morrisGame.setState(new SelectState());
				}
				else{
					// LAGT TIL IF-SETNING RUNDT MOVE
					// SISTE PARAMETER ER SPILLER ID
					if(morrisGame.move(morrisGame.getCurrentPlayer().getSelectedPiece(), p.getId(), morrisGame.getCurrentPlayer())){
						if(morrisGame.checkMorris(morrisGame.getCurrentPlayer().getSelectedPiece(), morrisGame.getCurrentPlayer())){
							morrisGame.setState(new RemovalState());
							morrisGame.updatePieceImages(morrisGame.getOpponent(), -1); // -1 fordi den er uvensentlig
						} else {
							morrisGame.changePlayer(hotseat); 
						}
					}
					if(morrisGame.getState() instanceof RemovalState){
						morrisGame.updatePieceImages(morrisGame.getOpponent(), p.getId());
					}
					else{
						morrisGame.setState(new SelectState());
						morrisGame.updatePieceImages(morrisGame.getCurrentPlayer(), p.getId());
						
					}
	
				}
	
			}
			else if(morrisGame.getState() instanceof RemovalState){
				//getPlayer1() just for testing, must be changed to getPlayer2()
				morrisGame.updateMorrisStates(morrisGame.getOpponent());
				if(morrisGame.removePiece(p, morrisGame.getOpponent())){
					//morrisGame.setState(morrisGame.getPreviousState());
					if(morrisGame.getPreviousState() instanceof PlacementState){
						morrisGame.setState(morrisGame.getPreviousState());
					} else {
						morrisGame.setState(new SelectState());
					}
					// ENDRET TIL OPPONENT 12:34
					morrisGame.updatePieceImages(morrisGame.getOpponent(), p.getId());
					morrisGame.changePlayer(hotseat);	
				}
				
				//morrisGame.updatePieceImages(morrisGame.getOpponent(), p.getId());
	
			}
			else if(morrisGame.getState() instanceof FlyingState){
				if(morrisGame.getCurrentPlayer().getSelectedPiece().getPosition()==p.getId()){
					morrisGame.setState(new SelectState());
				}else{
					// SISTE PARAMETER ER SPILLER ID
					if(morrisGame.move(morrisGame.getCurrentPlayer().getSelectedPiece(), p.getId(), morrisGame.getCurrentPlayer())){
						if(morrisGame.checkMorris(morrisGame.getCurrentPlayer().getSelectedPiece(), morrisGame.getCurrentPlayer())){
							morrisGame.setState(new RemovalState());
						} else {
							morrisGame.changePlayer(hotseat);
							morrisGame.setState(new SelectState());
						}
					}
					morrisGame.updatePieceImages(morrisGame.getOpponent(), p.getId());
					morrisGame.updatePieceImages(morrisGame.getCurrentPlayer(), p.getId());
				}
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
