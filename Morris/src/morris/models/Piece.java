package morris.models;

public class Piece {

	private boolean selectable;
	private boolean movable;
	private int currentRow;
	private int currentColumn;
	
	public Piece(){
		
	}
	
	public void placeAtCoordinate(int row, int column){
		currentRow = row;
		currentColumn = column;
	}
	
	public int getCurrentRow(){
		return currentRow;
	}
	
	public int getCurrentColumn(){
		return currentColumn;
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public boolean isMovable() {
		return movable;
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
	}
	
	
}

