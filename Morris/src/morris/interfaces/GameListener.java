package morris.interfaces;

import morris.models.Piece;
import morris.models.Player;

public interface GameListener {
	
	public void playerMoved(Player player, Piece piece);
	public void playerPlacedPiece(Player player, Piece piece);

}
