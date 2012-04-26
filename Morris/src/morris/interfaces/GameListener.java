package morris.interfaces;

import morris.models.Piece;
import morris.models.Player;

public interface GameListener {
	
	public void playerMoved(int pieceFromPosition, int pieceToPosition, boolean hotseat, boolean send);
	public void playerPlacedPiece(Player player, Piece piece, boolean hotseat, boolean send);
	public void playerRemovedPiece(int pieceRemoved,int pieceMovedFromPosition, int pieceMovedToPosition, boolean hotseat);
	public void playerChangeTurn(Player p);
	public void playerLost(int player,boolean hotseat);
	public void gameStarted();
	public void update();
}
