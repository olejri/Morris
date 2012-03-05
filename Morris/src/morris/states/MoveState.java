package morris.states;

import morris.interfaces.State;
import morris.interfaces.StateListener;

public class MoveState implements StateListener, State {

	@Override
	public void highlightPossibilities() {
		// TODO 
		System.out.println("All possibilities highlighted in MoveState.");
	}
}
