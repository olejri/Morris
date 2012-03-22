package morris.states;

import java.util.ArrayList;

import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.models.Board;
import morris.models.Player;
import morris.models.Slot;

public class FlyingState implements State, StateListener{

	// Copied and pasted from PlacementState
	@Override
	public ArrayList<Slot> getHighlightList(Board board, int id, Player currentPlayer) {
		Slot[][] slots = board.getSlots();
		ArrayList<Slot> highlights = new ArrayList<Slot>();
		for(int i=0; i<slots.length; i++){
			for(int j=0; j<slots.length; j++){
				if(slots[i][j].isEnabled() && !slots[i][j].isTaken()) highlights.add(slots[i][j]);
			}
		}
		return highlights;
	}

	@Override
	public void updatePieceImages() {
		// TODO Auto-generated method stub
		
	}
}
