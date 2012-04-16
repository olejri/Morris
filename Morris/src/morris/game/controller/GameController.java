package morris.game.controller;

import java.util.ArrayList;
import java.util.Timer;

import morris.game.Network;
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
	}


	/*
	 *  createNewGame() method - starts a new game that other can join
	 */
	public void createNewGame() {
		GameController.getInstance().setMorrisGame(new Game());
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
				
		if (morrisGame.getState() instanceof PlacementState) {
			for (int i = 0; i < morrisGame.getPlayer1().getPieces().size(); i++) {
				Piece piece = morrisGame.getPlayer1().getPieces().get(i);
				if (piece.getPosition() < 0) {
					piece.setPosition(p.getId());
					morrisGame.playerPlacedPiece(morrisGame.getPlayer1(),piece, p.getId());
					morrisGame.getBoard().reserveModelPoint(p.getId(), piece);  
					break;
				}
			}
		}
		else if(morrisGame.getState() instanceof SelectState){
			if(morrisGame.selectable(morrisGame.getPlayer1(), p.getId())){
				morrisGame.updatePieceImages(morrisGame.getPlayer1(), p.getId());
				morrisGame.setState(new MoveState());
			}
		}
		else if(morrisGame.getState() instanceof MoveState){
			morrisGame.updatePieceImages(morrisGame.getPlayer1(), p.getId());
			if(morrisGame.getPlayer1().getSelectedPiece().getPosition()==p.getId()){
				morrisGame.setState(new SelectState());
			}
			else{
				morrisGame.move(morrisGame.getPlayer1().getSelectedPiece(), p.getId(), morrisGame.getPlayer1()); // SISTE PARAMETER ER SPILLER ID
				if(morrisGame.getState() instanceof RemovalState){
					morrisGame.updatePieceImages(morrisGame.getPlayer1(), p.getId());
				}
				else{
					morrisGame.setState(new SelectState());
				}

			}

		}
		else if(morrisGame.getState() instanceof RemovalState){
			//getPlayer1() just for testing, must be changed to getPlayer2()
			morrisGame.removePiece(p, morrisGame.getPlayer1());
			morrisGame.setState(new SelectState());
			morrisGame.updatePieceImages(morrisGame.getPlayer1(), p.getId());

		}
		else if(morrisGame.getState() instanceof FlyingState){
			if(morrisGame.getPlayer1().getSelectedPiece().getPosition()==p.getId()){
				morrisGame.setState(new SelectState());
			}else{
				morrisGame.move(morrisGame.getPlayer1().getSelectedPiece(), p.getId(), morrisGame.getPlayer1()); // SISTE PARAMETER ER SPILLER ID
				morrisGame.setState(new SelectState());

			}
		}
	}
	//Player1 is used for testing, must change with getPlayingPlayer()
	public static ArrayList<ModelPoint> getHighlightsList() {
		ArrayList<ModelPoint> highlights; 
		if(morrisGame.getPlayer1().getSelectedPiece() != null){					
			highlights = morrisGame.getHighlightList(morrisGame.getPlayer1().getSelectedPiece().getPosition(), morrisGame.getPlayer1());  
		} else {
			highlights = morrisGame.getHighlightList(-1, morrisGame.getPlayer1());  
		}
		return highlights;
	}

}
