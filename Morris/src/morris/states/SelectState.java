package morris.states;

import java.util.ArrayList;

import morris.interfaces.State;
import morris.models.Board;
import morris.models.Player;
import morris.models.Slot;

public class SelectState implements State {

	
	/*
	 * TEMPORARY JUST FOR TESTING. METHOD MUST BE REWRITTEN! OMG!
	 * @see morris.interfaces.State#getHighlightList(morris.models.Board, int, morris.models.Player)
	 */
	@Override
	public ArrayList<Slot> getHighlightList(Board board, int id, Player currentPlayer) {
		// Highlights the selectable/movable pieces belonging to the current player
		Slot[][] slots = board.getSlots();
		ArrayList<Slot> highlights = new ArrayList<Slot>();
		for(int i=0; i<slots.length; i++){
			for(int j=0; j<slots.length; j++){
				if(slots[i][j].isEnabled() && !slots[i][j].isTaken()) highlights.add(slots[i][j]);
			}
		}
		return highlights;
	}

}
