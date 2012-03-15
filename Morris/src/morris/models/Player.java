package morris.models;

import java.util.ArrayList;

import android.util.Log;

import morris.game.GameHandler;
import morris.help.Constant;

public class Player {
	public String name;
	public String color;
	public ArrayList<Piece>pieces;
	
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
	public String getName(){
		return name;
	}
	/**
	 * Init player pieces
	 * 
	 */
	public void initPieces(){
		int pieces_number = 9;
		if(GameHandler.getMorrisGame().getMorrisGameType().equals(Constant.TWELVE_MENS_MORRIS)){
			pieces_number = 12;
		}
		for(int i=0;i<pieces_number;i++){
			pieces.add(new Piece());
		}
	}

}
