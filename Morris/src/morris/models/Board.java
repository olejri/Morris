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
		assignSlotIdentifiers();
	}
	
	public Slot[][] getSlots(){
		return slots;
	}
		
	// Burde kanskje ogsŒ ta inn en brikke som skal stŒ der?
	public void reserveSlot(int id){
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(slots[i][j].getId() == id){
					slots[i][j].setTaken(true);
					break;
				}
			}
		}
	}
	
	public void unreserveSlot(int id){
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(slots[i][j].getId() == id){
					slots[i][j].setTaken(false);
					break;
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
	
	private void assignSlotIdentifiers(){
		if(slots != null){
			// First row
			slots[0][0].setId(0);
			slots[0][3].setId(1);
			slots[0][6].setId(2);

			// Second row
			slots[1][1].setId(6);
			slots[1][3].setId(7);
			slots[1][5].setId(8);			

			// Third row
			slots[2][2].setId(12);
			slots[2][3].setId(13);
			slots[2][4].setId(14);
			
			// Fourth row
			slots[3][0].setId(18);
			slots[3][1].setId(19);
			slots[3][2].setId(20);
			slots[3][4].setId(21);
			slots[3][5].setId(22);
			slots[3][6].setId(23);
			
			// Fifth row
			slots[4][2].setId(15);
			slots[4][3].setId(16);
			slots[4][4].setId(17);
			
			// Sixth row
			slots[5][1].setId(9);
			slots[5][3].setId(10);
			slots[5][5].setId(11);
			
			// Seventh row
			slots[6][0].setId(3);
			slots[6][3].setId(4);
			slots[6][6].setId(5);
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
}
