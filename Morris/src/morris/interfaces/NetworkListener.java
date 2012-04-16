package morris.interfaces;

public interface NetworkListener {
	public void networkPlayerMoved();
	public void networkPlayerPlacedPiece();
	public void networkPlayerWon();
	public void networkPlayerRemovedPiece();
}
