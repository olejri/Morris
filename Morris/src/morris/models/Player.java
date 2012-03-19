package morris.models;

import java.util.ArrayList;

public class Player {
	
	private String name;
	private ArrayList<Piece> pieces;

	public Player(String name, int numberOfPieces){
		this.name = name;
		for(int i=0; i<numberOfPieces; i++){
			pieces.add(new Piece());
		}
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void removePiece(int row, int column){
		// TODO
		// Logikken som finner ut hvilken brikke som skal fjernes (og hvilken koordinat den har) trenger ikke n¿dvendigvis ligge her.
	}
	
}