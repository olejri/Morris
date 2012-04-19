package morris.models;

import java.util.ArrayList;

import android.R;
import android.util.Log;

import morris.game.controller.GameController;
import morris.help.Constant;

public class Player {
	public String name;
	public String color;
	public ArrayList<Piece>pieces;
	public Piece selectedPiece = null;
	
	public Player(String color, String name){
		this.name = name;
		this.color = color;
		pieces = new ArrayList<Piece>();
		
		initPieces();
	}
	
	public ArrayList<Piece>getPieces(){
		return pieces;
		
	}
	public String getColor(){
		return color;
	}
	
	public boolean hasSelectablePieces(){
		for(Piece p : pieces){
			if(p.isSelectable()) return true;
		}
		return false;
	}
	
	public void setSelectedPiece(Piece selectedPiece){
		this.selectedPiece = selectedPiece;
	}
	
	public Piece getSelectedPiece(){
		return selectedPiece;
	}
	
	public void removePiece(Piece piece) {
		/*Piece pRemove = null;
		for (Piece p : pieces){
			if(piece.getPosition()== p.getPosition()){
				pRemove = p;
			}
			
		}
		*/
		pieces.remove(piece);
		
	}

	/**
	 * Init player pieces
	 * 
	 */
	public void initPieces(){
		int pieces_number = 9;
		if(GameController.getInstance().getMorrisGame().getMorrisGameType().equals(Constant.TWELVE_MENS_MORRIS)){
			pieces_number = 12;
		}
		for(int i=0;i<pieces_number;i++){
			pieces.add(new Piece(color));
		}
	}

	public Player(String name, int numberOfPieces){
		this.name = name;
		for(int i=0; i<numberOfPieces; i++){
			pieces.add(new Piece(color));
		}
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
}
