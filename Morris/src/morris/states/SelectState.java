package morris.states;

import java.util.ArrayList;
import morris.help.Constant;
import morris.interfaces.State;
import morris.models.Board;
import morris.models.ModelPoint;
import morris.models.Piece;
import morris.models.Player;


public class SelectState implements State {	
	
	/*
	 * Returns an ArrayList of the current players piece positions that are to be highlighted.
	 * @see morris.interfaces.State#getHighlightList(morris.models.Board, int, morris.models.Player)
	 */
	@Override
	public ArrayList<ModelPoint> getHighlightList(Board board, int id, Player currentPlayer) {
		ArrayList<ModelPoint> highlights = new ArrayList<ModelPoint>();
		ArrayList<Piece> pieces = currentPlayer.getPieces();
		for(Piece p : pieces){
			if(board.getPoint(p.getPosition()) != null){
				ArrayList<Integer> neighbours = board.getPoint(p.getPosition()).getNeighbours();
				boolean selectable = false;
				for(Integer i : neighbours){
					if(!board.getPoint(i).isTaken()) selectable = true;
					//updateSelectablePieces(p, board, i);
					if(!board.getPoint(i).isTaken() && !highlights.contains(board.getPoint(p.getPosition()))){
						highlights.add(board.getPoint(p.getPosition()));
					}
				}
				if(selectable){
					p.setSelectable(true);
				} else {
					p.setSelectable(false);
				}
			}
		}
		return highlights;
	}


	@Override
	public void updatePieceImages(Player player,int positionId) {
		for(Piece p : player.getPieces()){
			if(p.getPosition()==positionId){
				if(p.getImageState()==Constant.SELECTABLE){
					p.updatePieceResource(Constant.SELECTED);
					player.setSelectedPiece(p);
				}
			}else{
				p.updatePieceResource(Constant.NORMAL);
			}
		}
	}
}
