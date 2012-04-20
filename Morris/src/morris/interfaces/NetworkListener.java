package morris.interfaces;

public interface NetworkListener {
	public void networkPlayerMoved(int fromPosition, int toPosition, int morris);
	public void networkPlayerPlacedPiece(int toPosition);
	public void networkPlayerWon();
	public void networkPlayerRemovedPiece(int piecePosition);
	public void networkSetPlayerNames();
}
