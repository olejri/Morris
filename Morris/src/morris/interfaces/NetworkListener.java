package morris.interfaces;

public interface NetworkListener {
	public void networkPlayerMoved(int fromPosition, int toPosition);
	public void networkPlayerPlacedPiece(int piecePlacedPosition);
	public void networkPlayerWon();
	public void networkPlayerRemovedPiece(int piecePosition, int pieceMovedFromPosition, int pieceMovedToPosition);
	public void networkSetPlayerNames();
}
