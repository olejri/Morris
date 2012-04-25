package morris.states;

import java.util.ArrayList;

import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.models.Board;
import morris.models.ModelPoint;
import morris.models.Player;

public class FlyingState implements State, StateListener{

	// Copied and pasted from PlacementState
	@Override
	public ArrayList<ModelPoint> getHighlightList(Board board, int id, Player currentPlayer, boolean hotseat) {
		ArrayList<ModelPoint> points = board.getPoints();
		ArrayList<ModelPoint> highlights = new ArrayList<ModelPoint>();
		for(ModelPoint mp : points){
			if(!mp.isTaken())highlights.add(mp);
		}
		return highlights;
	}

	@Override
	public void updatePieceImages(Player player,int positionId) {
		// TODO Auto-generated method stub	
	}
}
