package morris.models;

public class Board {
			
	private Slot[][] slots;
	private int rows = 7;
	private int columns = 7;

	public Board(){
		slots = new Slot[rows][columns];
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				slots[i][j] = new Slot(i,j);
				if(isValidBoardCoordinate(i, j)){
					slots[i][j].setEnabled(true);
				}
			}
		}
		assignSlotIdentifiers();
		assignSlotDomains();
	}
	
	public Slot getSlotByID(int id){
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(slots[i][j].getId() == id) return slots[i][j];
			}
		}
		return null;
	}
	
	public int getSlotOccupant(int x, int y){
		return slots[x][y].getOccupant();
	}
	
	public Slot[][] getSlots(){
		return slots;
	}
		
	// Burde kanskje ogsŒ ta inn en brikke som skal stŒ der?
	public void reserveSlot(int id, int player){
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(slots[i][j].getId() == id){
					slots[i][j].setTaken(true, player);
					break;
				}
			}
		}
	}
	
	public void unreserveSlot(int id){
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(slots[i][j].getId() == id){
					slots[i][j].setTaken(false, 0);
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
	
	/*
	 * Assigns logical neighbours to all slots.
	 */
	private void assignSlotDomains(){
		if(slots != null){
			// First row (0-2)
			slots[0][0].setDomain(new int[] {1,18});
			slots[0][3].setDomain(new int[] {0,2,7});
			slots[0][6].setDomain(new int[] {1,23});

			// Second row (6-8)
			slots[1][1].setDomain(new int[] {19,7});
			slots[1][3].setDomain(new int[] {1,6,8,13});
			slots[1][5].setDomain(new int[] {7,22});			

			// Third row (12-14)
			slots[2][2].setDomain(new int[] {13,20});
			slots[2][3].setDomain(new int[] {12,7,14});
			slots[2][4].setDomain(new int[] {13,21});
			
			// Fourth row (18-23)
			slots[3][0].setDomain(new int[] {0,3,19});
			slots[3][1].setDomain(new int[] {18,6,20,9});
			slots[3][2].setDomain(new int[] {19,12,15});
			slots[3][4].setDomain(new int[] {14,22,17});
			slots[3][5].setDomain(new int[] {21,8,23,11});
			slots[3][6].setDomain(new int[] {22,2,5});
			
			// Fifth row (15-17)
			slots[4][2].setDomain(new int[] {20,16});
			slots[4][3].setDomain(new int[] {15,10,17});
			slots[4][4].setDomain(new int[] {16,21});
			
			// Sixth row (9-11)
			slots[5][1].setDomain(new int[] {19,10});
			slots[5][3].setDomain(new int[] {9,16,4,11});
			slots[5][5].setDomain(new int[] {10,22});
			
			// Seventh row (3-5)
			slots[6][0].setDomain(new int[] {18,4});
			slots[6][3].setDomain(new int[] {3,10,5});
			slots[6][6].setDomain(new int[] {4,23});
		}
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
}
