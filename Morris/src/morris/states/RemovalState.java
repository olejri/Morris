package morris.states;

import java.util.ArrayList;

import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.models.Board;
import morris.models.Piece;
import morris.models.Player;
import morris.models.Slot;

public class RemovalState implements StateListener, State {

	/*
	 * Highlights all pieces that are not in Morris state. Needs to take in opponent Player object.
	 * @see morris.interfaces.State#getHighlightList(morris.models.Board, int, morris.models.Player)
	 */
	@Override
	public ArrayList<Slot> getHighlightList(Board board, int id, Player currentPlayer) {
		ArrayList<Slot> highlights = new ArrayList<Slot>();
		ArrayList<Piece> pieces = currentPlayer.getPieces();
		for(Piece p : pieces){
			if(!p.inMorris()){
				highlights.add(board.getSlotByID(p.getPosition()));
			}
		}
		return highlights;
	}
	
	
	// TEMP
	// M� FINNE ALLE BRIKKER SOM IKKE ST�R I MORRIS. B�R HA EN LIVE SJEKK FOR HVA SOM ST�R I MORRIS, 
	// FORDI EN BRIKKE KAN DANNE MORRIS VERTIKALT OG HORISONTALT SAMTIDIG

}

