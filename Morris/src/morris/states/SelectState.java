package morris.states;

import java.util.ArrayList;

import android.R;
import android.util.Log;

import morris.game.GameHandler;
import morris.help.Constant;
import morris.interfaces.State;
import morris.models.Board;
import morris.models.Piece;
import morris.models.Player;
import morris.models.Slot;


public class SelectState implements State {	
	
	/*
	 * Returns an ArrayList of the current players piece positions that are to be highlighted.
	 * @see morris.interfaces.State#getHighlightList(morris.models.Board, int, morris.models.Player)
	 */
	@Override
	public ArrayList<Slot> getHighlightList(Board board, int id, Player currentPlayer) {
		ArrayList<Slot> highlights = new ArrayList<Slot>();
		ArrayList<Piece> pieces = currentPlayer.getPieces();
		for(Piece p : pieces){
			ArrayList<Integer> domain = board.getSlotByID(p.getPosition()).getDomain();
			//System.out.println("ID:"+p.getPosition()+" Domain size: "+domain.size());
			boolean selectable = false;
			for(Integer i : domain){
				if(!board.getSlotByID(i).isTaken()) selectable = true;
				//updateSelectablePieces(p, board, i);
				if(!board.getSlotByID(i).isTaken() && !highlights.contains(board.getSlotByID(p.getPosition()))) highlights.add(board.getSlotByID(p.getPosition()));
			}
			if(selectable){
				p.setSelectable(true);
			} else {
				p.setSelectable(false);
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
