package morris.states;

import java.util.ArrayList;

import morris.help.Constant;
import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.models.Board;
import morris.models.ModelPoint;
import morris.models.Piece;
import morris.models.Player;

public class MoveState implements StateListener, State {

	@Override
	public ArrayList<ModelPoint> getHighlightList(Board board, int id, Player currentPlayer) {
		ArrayList<Integer> IDs = getPossibleMoves(board, id);
		ArrayList<ModelPoint> highlights = new ArrayList<ModelPoint>();
		for (int i = 0; i < IDs.size(); i++) {
			highlights.add(board.getPoint(IDs.get(i)));
		}
		return highlights;
	}
	
	private ArrayList<Integer> getPossibleMoves(Board board, int id) {
		ModelPoint selected = board.getPoint(id);
		ArrayList<Integer> freeSlots = new ArrayList<Integer>();
		ArrayList<Integer> temp = selected.getNeighbours();
		for (int i = 0; i < temp.size(); i++) {
			if (!board.getPoint(temp.get(i)).isTaken()) {
				freeSlots.add(temp.get(i));
			}
		}
		return freeSlots;
	}

	@Override
	public void updatePieceImages(Player player, int positionId) {
		for (Piece p : player.getPieces()) {
			if (p.getImageState() != Constant.SELECTED) {
				p.updatePieceResource(Constant.NORMAL);
			}
		}

	}

}
