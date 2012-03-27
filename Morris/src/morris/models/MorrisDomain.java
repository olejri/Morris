package morris.models;

import java.util.ArrayList;

public class MorrisDomain {
	
	private ArrayList<Integer> horizontalDomain = new ArrayList<Integer>();
	private ArrayList<Integer> verticalDomain = new ArrayList<Integer>();
	
	public MorrisDomain(int id){
		setDomains(id);
	}
	
	private void setDomains(int id){
		switch(id){
		case 0: setHorizontalDomain(new int[] {0,1,2});
				setVerticalDomain(new int[] {0,18,3});
				break;
		case 1: setHorizontalDomain(new int[] {0,1,2});
				setVerticalDomain(new int[] {1,7,13});
				break;
		case 2: setHorizontalDomain(new int[] {0,1,2});
				setVerticalDomain(new int[] {2,23,5});
				break;
		case 3: setHorizontalDomain(new int[] {3,4,5});
				setVerticalDomain(new int[] {0,18,3});
				break;
		case 4: setHorizontalDomain(new int[] {3,4,5});
				setVerticalDomain(new int[] {16,10,4});
				break;
		case 5: setHorizontalDomain(new int[] {3,4,5});
				setVerticalDomain(new int[] {2,23,5});
				break;
		case 6: setHorizontalDomain(new int[] {6,7,8});
				setVerticalDomain(new int[] {6,19,9});
				break;
		case 7: setHorizontalDomain(new int[] {6,7,8});
				setVerticalDomain(new int[] {1,17,13});
				break;
		case 8: setHorizontalDomain(new int[] {6,7,8});
				setVerticalDomain(new int[] {8,22,11});
				break;
		case 9: setHorizontalDomain(new int[] {9,10,11});
				setVerticalDomain(new int[] {6,19,9});
				break;
		case 10: setHorizontalDomain(new int[] {9,10,11});
				setVerticalDomain(new int[] {16,10,4});
				break;
		case 11: setHorizontalDomain(new int[] {9,10,11});
				setVerticalDomain(new int[] {8,22,11});
				break;
		case 12: setHorizontalDomain(new int[] {12,13,14});
				setVerticalDomain(new int[] {12,20,15});
				break;
		case 13: setHorizontalDomain(new int[] {12,13,14});
				setVerticalDomain(new int[] {1,7,13});
				break;
		case 14: setHorizontalDomain(new int[] {12,13,14});
				setVerticalDomain(new int[] {14,21,17});
				break;
		case 15: setHorizontalDomain(new int[] {15,16,17});
				setVerticalDomain(new int[] {12,20,15});
				break;
		case 16: setHorizontalDomain(new int[] {15,16,17});
				setVerticalDomain(new int[] {16,10,4});
				break;
		case 17: setHorizontalDomain(new int[] {15,16,17});
				setVerticalDomain(new int[] {14,21,17});
				break;
		case 18: setHorizontalDomain(new int[] {18,19,20});
				setVerticalDomain(new int[] {0,18,3});
				break;
		case 19: setHorizontalDomain(new int[] {18,19,20});
				setVerticalDomain(new int[] {6,19,9});
				break;
		case 20: setHorizontalDomain(new int[] {18,19,20});
				setVerticalDomain(new int[] {12,20,15});
				break;
		case 21: setHorizontalDomain(new int[] {21,22,23});
				setVerticalDomain(new int[] {14,21,17});
				break;
		case 22: setHorizontalDomain(new int[] {21,22,23});
				setVerticalDomain(new int[] {8,22,11});
				break;
		case 23: setHorizontalDomain(new int[] {21,22,23});
				setVerticalDomain(new int[] {2,23,5});
				break;
		}
	}
	
	public ArrayList<Integer> getHorizontalDomain(){
		return horizontalDomain;
	}
	
	public void setHorizontalDomain(int[] horizontalDomain){
		for(int i=0; i<horizontalDomain.length; i++){
			this.horizontalDomain.add(horizontalDomain[i]);
		}
	}
	
	public ArrayList<Integer> getVerticalDomain(){
		return verticalDomain;
	}
	
	public void setVerticalDomain(int[] verticalDomain){
		for(int i=0; i<verticalDomain.length; i++){
			this.verticalDomain.add(verticalDomain[i]);
		}	}
}
