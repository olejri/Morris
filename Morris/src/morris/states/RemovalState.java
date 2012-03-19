package morris.states;

import java.util.ArrayList;

import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.models.Player;
import morris.models.Slot;

public class RemovalState implements StateListener, State {

	@Override
	public ArrayList<Slot> getHighlightList(Slot[][] slots, Player currentPlayer) {
		// TODO Auto-generated method stub
		// Highlights all pieces that are not causing Morris state.
		return null;
	}

}