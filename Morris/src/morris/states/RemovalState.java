package morris.states;

import morris.interfaces.State;
import morris.interfaces.StateListener;

public class RemovalState implements StateListener, State {

	@Override
	public void listSelectablePieces() {
		// TODO
		// Lists all pieces that are flagged as removable.
		System.out.println("All selectable pieces listed from RemovalState.");
		
	}

}
