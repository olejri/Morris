package morris.states;

import java.util.ArrayList;

import morris.game.controller.GameController;
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
	public ArrayList<ModelPoint> getHighlightList(Board board, int id, Player currentPlayer, boolean hotseat) {
		ArrayList<ModelPoint> highlights = new ArrayList<ModelPoint>();
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		
		if(hotseat){
			pieces = currentPlayer.getPieces();
		} else {
			if(currentPlayer==GameController.getGame().getPlayer1()){
				pieces = GameController.getGame().getPlayer1().getPieces();
			}
		}

		for(Piece p : pieces){
			if(pieces.size() == 3) {
				p.setSelectable(true);
			} else if(pieces.size() < 3){
				p.setSelectable(false);
			} else {
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
		}
	
		return highlights;
	}

	// PLAYER ER ALLTID PLAYER1
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
		// OPPDATERER MOTSTANDERS BRIKKER. FUNKER FOR ONLINE SPILLING
		// IF !HOTSEAT
		if(GameController.getGame().getCurrentPlayer() != GameController.getGame().getPlayer2()){
			for(Piece p : GameController.getGame().getPlayer2().getPieces()){
				p.updatePieceResource(Constant.NORMAL);
			}
		}
	}
}
