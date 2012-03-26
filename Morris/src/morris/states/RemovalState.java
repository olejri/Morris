package morris.states;

import java.util.ArrayList;

import morris.help.Constant;
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

	@Override
	public void updatePieceImages(Player player,int positionId) {
		for (Piece p : player.getPieces()) {
			if (p.inMorris() == false){
				p.updatePieceResource(Constant.REMOVABLE);
			}
		}
		
	}


}

