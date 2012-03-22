package morris.models;

public class Piece {

	private boolean selectable;
	private boolean movable;
	private int currentRow;
	private int currentColumn;
	private boolean morris = false;
	
	private int atPosition = -1;
	
	public Piece(){
		
	}
	
	public void placeAtCoordinate(int row, int column){
		currentRow = row;
		currentColumn = column;
	}
	
	public int getPosition(){
		return atPosition;
	}
	
	public boolean inMorris(){
		return morris;
	}
	
	public void setMorris(boolean bool){
		morris = bool;
	}
	
	public int getCurrentRow(){
		return currentRow;
	}
	
	public void setPosition(int atPosition){
		this.atPosition = atPosition;
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

