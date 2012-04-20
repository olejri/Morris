package morris.states;

import java.util.ArrayList;

import morris.game.controller.GameController;
import morris.help.Constant;
import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.models.Board;
import morris.models.ModelPoint;
import morris.models.Piece;
import morris.models.Player;

public class PlacementState implements StateListener, State{

	/*
	 * Returns an ArrayList of the Slot objects that are to be highlighted.
	 * @see morris.interfaces.State#highlightPossibilities(morris.models.Slot[][], morris.models.Player)
	 */
	@Override
	public ArrayList<ModelPoint> getHighlightList(Board board, int id, Player currentPlayer) {
		ArrayList<ModelPoint> points = board.getPoints();
		ArrayList<ModelPoint> highlights = new ArrayList<ModelPoint>();
		if(currentPlayer == GameController.getInstance().getGame().getPlayer1()){
			for(ModelPoint mp : points){
				if(!mp.isTaken())highlights.add(mp);
			}
		}
		return highlights;
	}

	@Override
	public void updatePieceImages(Player player,int positionId){
		for (Piece p : player.getPieces()) {
			p.updatePieceResource(Constant.NORMAL);
		}


	}

}
