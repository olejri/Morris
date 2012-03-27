package morris.states;

import java.util.ArrayList;

import morris.help.Constant;
import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.models.Board;
import morris.models.ModelPoint;
import morris.models.Piece;
import morris.models.Player;

public class RemovalState implements StateListener, State {

	/*
	 * Highlights all pieces that are not in Morris state. Needs to take in opponent Player object.
	 * @see morris.interfaces.State#getHighlightList(morris.models.Board, int, morris.models.Player)
	 */
	@Override
	public ArrayList<ModelPoint> getHighlightList(Board board, int id, Player currentPlayer) {
		/*ArrayList<Slot> highlights = new ArrayList<Slot>();
		ArrayList<Piece> pieces = currentPlayer.getPieces();
		for(Piece p : pieces){
			if(!p.inMorris()){
				highlights.add(board.getSlotByID(p.getPosition()));
			}
		}
		return highlights;*/
		return null;
	}

	@Override
	public void updatePieceImages(Player player,int positionId) {
		// TODO Auto-generated method stub
	}
}

