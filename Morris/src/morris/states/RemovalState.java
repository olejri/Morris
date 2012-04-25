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
	public ArrayList<ModelPoint> getHighlightList(Board board, int id, Player opponent, boolean hotseat) {
		ArrayList<ModelPoint> highlights = new ArrayList<ModelPoint>();
		ArrayList<Piece> pieces = opponent.getPieces();
		for(Piece p : pieces){
			if(!p.inMorris()){
				if(board.getPoint(p.getPosition()) != null){
				highlights.add(board.getPoint(p.getPosition()));
				}
			}
		}
		return highlights;
	}

	
	@Override
	public void updatePieceImages(Player opponent,int positionId) {
		for (Piece p : opponent.getPieces()) {
			if (!p.inMorris()){
				p.updatePieceResource(Constant.REMOVABLE);
			}
			//TODO fix for opponent
			else {
				p.updatePieceResource(Constant.NORMAL);
			}
		}
	}
}

