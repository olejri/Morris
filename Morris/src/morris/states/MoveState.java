package morris.states;

import morris.interfaces.State;
import morris.interfaces.StateListener;

public class MoveState implements StateListener, State {

	@Override
	public void listSelectablePieces() {
		// TODO Auto-generated method stub
		// Lists all pieces that are flagged as movable.
		System.out.println("All selectable pieces listed from MoveState.");
	}

}
