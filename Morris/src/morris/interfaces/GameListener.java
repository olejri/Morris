package morris.interfaces;

import morris.models.Piece;
import morris.models.Player;

public interface GameListener {
	
	public void playerMoved(int pieceFromPosition, int pieceToPosition,int morris);
	public void playerPlacedPiece(Player player, Piece piece,int morris);
	public void playerRemovedPiece(int piecePosition);
	public void playerChangeTurn(Player p);
}
