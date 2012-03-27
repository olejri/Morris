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
	public ArrayList<ModelPoint> getHighlightList(Board board, int id, Player currentPlayer) {
		/*Slot[][] slots = board.getSlots();
		ArrayList<Slot> highlights = new ArrayList<Slot>();
		for(int i=0; i<slots.length; i++){
			for(int j=0; j<slots.length; j++){
				if(slots[i][j].isEnabled() && !slots[i][j].isTaken()) highlights.add(slots[i][j]);
			}
		}
		return highlights;*/
		return null;
	}

	@Override
	public void updatePieceImages(Player player,int positionId) {
		// TODO Auto-generated method stub
		
	}


}
