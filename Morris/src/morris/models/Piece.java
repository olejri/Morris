package morris.models;

import android.R;
import morris.help.Constant;

public class Piece {

	private boolean selectable;
	private boolean movable;
	private int currentRow;
	private int currentColumn;
	private boolean inMorris = false;
	private int atPosition = -1;
	private String color;
	private int imageResource = -1;
	public int imageState = -1;
	
	public Piece(String color){
		this.color = color;
		initResource();
	}
	
	public void initResource(){
		imageState = Constant.NORMAL;
		if(color.equals(Constant.WHITE)){
			imageResource = morris.game.R.drawable.piece_white;
		}else{
			imageResource = morris.game.R.drawable.piece_black;
		}
	}
	/**
	 * Update piece resource
	 * @param imageState
	 */
	public void updatePieceResource(int imageState){
		if(color.equals(Constant.WHITE)){
			if(imageState == Constant.SELECTED){
				imageResource = morris.game.R.drawable.piece_white_selected;
			}else if(imageState==Constant.REMOVABLE){
				imageResource = morris.game.R.drawable.piece_white_remove;
			}else if(imageState==Constant.NORMAL){
				imageResource = morris.game.R.drawable.piece_white;
			}
		}else{
			if(imageState==Constant.SELECTED){
				imageResource = morris.game.R.drawable.piece_black_selected;
			}else if(imageState==Constant.REMOVABLE){
				imageResource = morris.game.R.drawable.piece_black_remove;
			}else if(imageState==Constant.NORMAL){
				imageResource = morris.game.R.drawable.piece_black;
			}
		}
	}
	
	public int getResource(){
		return imageResource;
	}
	
	public int getImageState(){
		return imageState;
	}
	
	public void placeAtCoordinate(int row, int column){
		currentRow = row;
		currentColumn = column;
	}
	
	public int getPosition(){
		return atPosition;
	}
	
	public boolean inMorris(){
		return inMorris;
	}
	
	public void setMorris(boolean bool){
		inMorris = bool;
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

