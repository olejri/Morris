package morris.models;

import java.util.ArrayList;

public class ModelPoint {

	private ArrayList<Integer> neighbours = new ArrayList<Integer>();
	private MorrisDomain morrisdomain;
	private Piece piece = null;
	private boolean taken;
	private int id;	
	
	public ModelPoint(int id){
		morrisdomain = new MorrisDomain(id);
		assignNeighbours(id);
		taken = false;
		this.id = id;
	}
	
	public MorrisDomain getMorrisDomain(){
		return morrisdomain;
	}
	
	public ArrayList<Integer> getNeighbours(){
		return neighbours;
	}
	
	public boolean isTaken(){
		return taken;
	}
	
	public boolean isNeighbour(int target){
		for(Integer i : getNeighbours()){
			if(target == i) return true;
		}
		return false;
	}
	
	public Piece getPiece(){
		return piece;
	}
	
	public int getId(){
		return id;
	}
	
	public void reserve(Piece piece){
		if(this.piece == null){
			this.piece = piece;
			taken = true;
		}
	}
	
	public void unReserve(){
		if(piece != null){
			piece = null;
			taken = false;
		}
	}
	
	public void setPiece(Piece piece){
		this.piece = piece;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setNeighbours(int[] neighbours){
		this.neighbours.clear();
		for(int i=0; i<neighbours.length; i++){
			this.neighbours.add(neighbours[i]);
		}
	}
	
	private void assignNeighbours(int id){
		switch(id){
		case 0: setNeighbours(new int[] {1,18});break;
		case 1: setNeighbours(new int[] {0,2,7});break;
		case 2: setNeighbours(new int[] {1,23});break;
		
		case 3: setNeighbours(new int[] {18,4});break;
		case 4: setNeighbours(new int[] {3,10,5});break;
		case 5: setNeighbours(new int[] {4,23});break;
		
		case 6: setNeighbours(new int[] {19,7});break;
		case 7: setNeighbours(new int[] {1,6,8,13});break;
		case 8: setNeighbours(new int[] {7,22});break;
		
		case 9: setNeighbours(new int[] {19,10});break;
		case 10: setNeighbours(new int[] {9,16,4,11});break;
		case 11: setNeighbours(new int[] {10,22});break;
		
		case 12: setNeighbours(new int[] {13,20});break;
		case 13: setNeighbours(new int[] {12,7,14});break;
		case 14: setNeighbours(new int[] {13,21});break;
		
		case 15: setNeighbours(new int[] {20,16});break;
		case 16: setNeighbours(new int[] {15,10,17});break;
		case 17: setNeighbours(new int[] {16,21});break;
		
		case 18: setNeighbours(new int[] {0,3,19});break;
		case 19: setNeighbours(new int[] {18,6,20,9});break;
		case 20: setNeighbours(new int[] {19,12,15});break;
		
		case 21: setNeighbours(new int[] {14,22,17});break;
		case 22: setNeighbours(new int[] {21,8,23,11});break;
		case 23: setNeighbours(new int[] {22,2,5});break;
		}		
	}
}
