package morris.models;

public class Board {
	
	private Slot[][] slots;
	private int rows = 7;
	private int columns = 7;
	
	public Board(){
		System.out.println("Executing board constructor");
		slots = new Slot[rows][columns];
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				slots[i][j] = new Slot();
				if(isValidBoardCoordinate(i, j)){
					slots[i][j].setEnabled(true);
				}
			}
		}
	}
	
	public void test(){
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(isValidBoardCoordinate(i, j)){
					System.out.println("["+i+"]["+j+"]");
				}
			}
		}	
	}
	
	private boolean isValidBoardCoordinate(int row, int column){
		if(row == 0){
			if(column == 0 || column == 3 || column == 6) return true;
		} else if(row == 1){
			if(column == 1 || column == 3 || column == 5) return true;
		} else if(row == 2){
			if(column == 2 || column == 3 || column == 4) return true;
		} else if(row == 3){
			if(column == 0 || column == 1 || column == 2 || column == 4 || column == 5 || column == 6) return true;
		} else if(row == 4){
			if(column == 2 || column == 3 || column == 4) return true;
		} else if(row == 5){
			if(column == 1 || column == 3 || column == 5) return true;
		} else if(row == 6){
			if(column == 0 || column == 3 || column == 6) return true;
		} 
		return false;		
	}
}
