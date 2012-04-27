package morris.game.controller;

import java.util.ArrayList;
import java.util.Timer;

import morris.gui.Point;
import morris.help.Constant;
import morris.models.ModelPoint;
import morris.models.Piece;
import morris.models.Player;
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
	private static Game morrisGame = null;
	private static Point justPressedPoint = new Point(100, 0, 0);
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


	private GameController() {}


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
		Player player = morrisGame.getPlayer1();
		if(morrisGame.isHotseat())player = morrisGame.getCurrentPlayer();
		if(morrisGame.getCurrentPlayer()==player){	
			if (morrisGame.getState() instanceof PlacementState) {
				if(p != justPressedPoint){
					if (morrisGame.isHotseat())justPressedPoint = p;
					
					for (int i = 0; i < morrisGame.getCurrentPlayer().getPieces().size(); i++) {
						Piece piece = morrisGame.getCurrentPlayer().getPieces().get(i);
						if (piece.getPosition() < 0) {
							morrisGame.playerPlacedPiece(morrisGame.getCurrentPlayer(),piece, p.getId());
							// NYTT 26.04.2012
							if(piece != null){
								if(morrisGame.checkMorris(piece, morrisGame.getCurrentPlayer()) && morrisGame.opponentHasRemovablePieces(morrisGame.getCurrentPlayer())){
									morrisGame.setState(new RemovalState());
								} else {
									morrisGame.changePlayer(true);
								}
							}
							break;
						}
					}
					if(morrisGame.getState() instanceof RemovalState){
						morrisGame.updatePieceImages(morrisGame.getOpponent(morrisGame.getCurrentPlayer()), p.getId()); // ENDRET FRA getOpponent()
					}
					morrisGame.updatePieceImages(morrisGame.getOpponent(morrisGame.getCurrentPlayer()), p.getId());
				}
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
							if(morrisGame.opponentHasRemovablePieces(morrisGame.getCurrentPlayer())) {
								morrisGame.setState(new RemovalState());
								morrisGame.updatePieceImages(morrisGame.getOpponent(morrisGame.getCurrentPlayer()), -1); // -1 fordi den er uvensentlig
							} else {
								morrisGame.changePlayer(hotseat);
							}
						} else {
							morrisGame.changePlayer(hotseat); 
						}
					}
					// Kan fjernes, og kun ha en sjekk p� om den fortsatt er i MoveState, og deretter sette select.
					if(morrisGame.getState() instanceof RemovalState){
						morrisGame.updatePieceImages(morrisGame.getOpponent(morrisGame.getCurrentPlayer()), p.getId());
					}
					else{
						morrisGame.setState(new SelectState());
						morrisGame.updatePieceImages(morrisGame.getCurrentPlayer(), p.getId());	
					}

				}

			}
			else if(morrisGame.getState() instanceof RemovalState){
				//getPlayer1() just for testing, must be changed to getPlayer2()
				morrisGame.updateMorrisStates(morrisGame.getOpponent(morrisGame.getCurrentPlayer()));
				if(morrisGame.removePiece(p, morrisGame.getOpponent(morrisGame.getCurrentPlayer()))){
					//morrisGame.setState(morrisGame.getPreviousState());
					if(morrisGame.getPreviousState() instanceof PlacementState){
						morrisGame.setState(morrisGame.getPreviousState());
						morrisGame.updatePieceImages(morrisGame.getCurrentPlayer(), -1); // LAGT TIL 10:59 - 26.04.2012
					} else {
						morrisGame.setState(new SelectState());
						morrisGame.updatePieceImages(morrisGame.getCurrentPlayer(), -1); // LAGT TIL 10:59 - 26.04.2012
					}
					// ENDRET TIL OPPONENT 12:34
					morrisGame.updatePieceImages(morrisGame.getOpponent(morrisGame.getCurrentPlayer()), p.getId());
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
							morrisGame.updatePieceImages(morrisGame.getOpponent(morrisGame.getCurrentPlayer()), -1);
							morrisGame.updatePieceImages(morrisGame.getCurrentPlayer(), -1);
						} else {
							morrisGame.setState(new SelectState());
							morrisGame.updatePieceImages(morrisGame.getCurrentPlayer(), -1); 
							morrisGame.changePlayer(hotseat);
							// -1 som siste parameter for � indikere at ingen brikke er selected (26.04 09:46) 
						}
					}



				}
			}
		}

	}
	//Player1 is used for testing, must change with getPlayingPlayer()
	public static ArrayList<ModelPoint> getHighlightsList() {
		ArrayList<ModelPoint> highlights = new ArrayList<ModelPoint>();
		if(morrisGame != null){
			if(morrisGame.getCurrentPlayer() != null){
				if(morrisGame.getCurrentPlayer().getSelectedPiece() != null){					
					highlights = morrisGame.getHighlightList(morrisGame.getCurrentPlayer().getSelectedPiece().getPosition(), morrisGame.getCurrentPlayer());  
				} else {
					highlights = morrisGame.getHighlightList(-1, morrisGame.getCurrentPlayer());  
				}
			}
		}
		return highlights;
	}

}
