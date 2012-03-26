package morris.models;

import java.util.ArrayList;

public class Slot {
	
	/*
	 * This class is mapped with the class Point. Objects of both classes have the same identificator for ease of use.
	 * If a Slot object has an ID equal -1, its ID has not been assigned.
	 */
	
	private boolean taken;
	private boolean enabled;
	private int x;
	private int y;
	private int id = -1;
	private int occupant = 0;
	private ArrayList<Integer> domain = new ArrayList<Integer>();
	
	public Slot(int x, int y){
		this.x = x;
		this.y = y;
		enabled = false;
		taken = false;
	}
	
	public void setOccupant(int occupant){
		this.occupant = occupant;
	}
	
	public int getOccupant(){
		return occupant;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
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
	
	public void setTaken(boolean bool, int occupant){
		this.taken = bool;
		this.occupant = occupant;
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
