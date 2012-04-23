package morris.interfaces;

import morris.models.Piece;
import morris.models.Player;

public interface GameListener {
	
	public void playerMoved(int pieceFromPosition, int pieceToPosition);
	public void playerPlacedPiece(Player player, Piece piece);
	public void playerRemovedPiece(int pieceRemoved,int pieceMovedFromPosition, int pieceMovedToPosition);
	public void playerChangeTurn(Player p);
}
