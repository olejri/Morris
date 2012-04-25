package morris.interfaces;

import morris.models.Piece;
import morris.models.Player;

public interface GameListener {
	
	public void playerMoved(int pieceFromPosition, int pieceToPosition, boolean won, boolean hotseat);
	public void playerPlacedPiece(Player player, Piece piece, boolean won, boolean hotseat);
	public void playerRemovedPiece(int pieceRemoved,int pieceMovedFromPosition, int pieceMovedToPosition, boolean won, boolean hotseat);
	public void playerChangeTurn(Player p);
	public void playerWon(int player);
	public void update();
}
