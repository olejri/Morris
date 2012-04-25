package morris.interfaces;

import java.util.ArrayList;

import morris.models.Board;
import morris.models.ModelPoint;
import morris.models.Player;

public interface State {
	
	public ArrayList<ModelPoint> getHighlightList(Board board, int id, Player currentPlayer, boolean hotseat);
	public void updatePieceImages(Player player,int positionId);
	
}
