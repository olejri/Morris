package morris.states;

import java.util.ArrayList;

import morris.game.controller.GameController;
import morris.help.Constant;
import morris.interfaces.State;
import morris.models.Board;
import morris.models.ModelPoint;
import morris.models.Piece;
import morris.models.Player;

public class PlacementState implements State{

	/*
	 * Returns an ArrayList of the Slot objects that are to be highlighted.
	 * @see morris.interfaces.State#highlightPossibilities(morris.models.Slot[][], morris.models.Player)
	 */
	@Override
	public ArrayList<ModelPoint> getHighlightList(Board board, int id, Player currentPlayer, boolean hotseat) {
		ArrayList<ModelPoint> points = board.getPoints();
		ArrayList<ModelPoint> highlights = new ArrayList<ModelPoint>();
		GameController.getInstance();
		if(hotseat){
			for(ModelPoint mp : points){
				if(!mp.isTaken())highlights.add(mp);
			}
		} else {
			if(currentPlayer == GameController.getGame().getPlayer1()){
				for(ModelPoint mp : points){
					if(!mp.isTaken())highlights.add(mp);
				}
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
