package morris.states;

import java.util.ArrayList;

import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.models.Board;
import morris.models.Player;
import morris.models.Slot;

public class MoveState implements StateListener, State {

	
	// Should be renamed to getPointHighlights, and there should also be a method for Piece highlighting.
	@Override
	public ArrayList<Slot> getHighlightList(Board board, int id, Player currentPlayer) {
		ArrayList<Integer> IDs = getPossibleMoves(board, id);
		ArrayList<Slot> highlights = new ArrayList<Slot>();
		for(int i=0; i<IDs.size(); i++){
			highlights.add(board.getSlotByID(IDs.get(i)));
		}
		return highlights;		
	}
	
	private ArrayList<Integer> getPossibleMoves(Board board, int id){
		//Slot[][] slots = board.getSlots();
		Slot selected = board.getSlotByID(id);
		ArrayList<Integer> freeSlots = new ArrayList<Integer>();
		for(int i=0; i<selected.getDomain().size(); i++){
			if(!board.getSlotByID(selected.getDomain().get(i)).isTaken()){
				freeSlots.add(selected.getDomain().get(i));
			}
		}
		return freeSlots;	
	}
}
