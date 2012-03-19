package morris.interfaces;

import java.util.ArrayList;

import morris.models.Player;
import morris.models.Slot;

public interface State {

	public ArrayList<Slot> getHighlightList(Slot[][] slots, Player currentPlayer);
	
	
	//public String setOnScreenHint();
	
}
