package morris.models;

import java.util.ArrayList;

public class Slot {
	
	/*
	 * This class is mapped with the class Point. Objects of both classes have the same identificator for ease of use.
	 * If a Slot object has an ID equal -1, its ID has not been assigned.
	 */
	
	private boolean taken;
	private boolean enabled;
	private int id = -1;
	private ArrayList<Integer> domain = new ArrayList<Integer>();
	private Piece piece;
	
	public Slot(){
		piece = null;
		enabled = false;
		taken = false;
	}
	
	public void setPiece(Piece piece){
		this.piece = piece;
	}
	
	public Piece getPiece(){
		return piece;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void setEnabled(boolean bool){
		this.enabled = bool;
	}
	
	public boolean isTaken(){
		return taken;
	}
	
	public void setTaken(boolean bool){
		this.taken = bool;
	}
	
	public void setDomain(int[] domain){
		this.domain.clear();
		for(int i=0; i<domain.length; i++){
			this.domain.add(domain[i]);
		}
	}
	
	public ArrayList<Integer> getDomain(){
		return domain;
	}
}
