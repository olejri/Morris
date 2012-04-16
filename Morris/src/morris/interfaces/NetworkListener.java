package morris.interfaces;

public interface NetworkListener {
	public void networkPlayerMoved(int pieceID, int toPosition);
	public void networkPlayerPlacedPiece(int pieceID, int toPosition);
	public void networkPlayerWon();
	public void networkPlayerRemovedPiece();
}
