package morris.interfaces;

import java.util.ArrayList;

import morris.models.Board;
import morris.models.Player;
import morris.models.Slot;

public interface State {
	
	// Takes in the slot matrix and the the slot id where a potential selected piece is located.
	// An ID equal to -1 indicates that no piece is selected.
	public ArrayList<Slot> getHighlightList(Board board, int id, Player currentPlayer);
}
