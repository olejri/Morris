package morris.models;

import java.util.ArrayList;

public class Board {
	
	private ArrayList<ModelPoint> points = new ArrayList<ModelPoint>();

	public Board(){
		for(int id=0; id<24; id++){
			points.add(new ModelPoint(id));
		}
		System.out.println("ModelPoint neighbours:");
		for(ModelPoint mp : points){
			System.out.println("ID: "+mp.getId()+" Neighbours: "+mp.getNeighbours());
		}
	}
	
	public ArrayList<ModelPoint> getPoints(){
		return points;
	}
	
	public void reserveModelPoint(int id, Piece piece){
		getPoint(id).reserve(piece);
	}
	
	public void unReserveModelPoint(int id){
		getPoint(id).unReserve();
	}
	
	public ModelPoint getPoint(int id){
		for(ModelPoint mp : points){
			if(mp.getId() == id) return mp;
		}
		return null;
	}
	
	public ArrayList<Integer> getHorizontalDomain(int id){
		return getPoint(id).getMorrisDomain().getHorizontalDomain();
	}
	
	public ArrayList<Integer> getVerticalDomain(int id){
		return getPoint(id).getMorrisDomain().getVerticalDomain();
	}
}
