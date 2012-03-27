package morris.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import morris.help.Constant;
import morris.interfaces.GameListener;
import morris.interfaces.State;
import morris.interfaces.StateListener;
import morris.states.MoveState;
import morris.states.PlacementState;
import morris.states.RemovalState;
import morris.states.SelectState;

public class Game {

	private List<StateListener> stateListeners = new CopyOnWriteArrayList<StateListener>();
	private List<GameListener> gameListeners = new CopyOnWriteArrayList<GameListener>();
	public boolean yourTurn = true;
	private State state;
	private Board board;
	public String gameType;
	
	int pieceCounter = 0;
	
	public Player player1;
	public Player player2;
	
	// Satt til placementstate midlertidig. Logisk � starte der uansett.
	public Game(){
		setState(new PlacementState());
		board = new Board();
		gameType = Constant.NINE_MENS_MORRIS;
	}
	
	public void initPlayers(){
		player1 = new Player(Constant.WHITE,"Emil");
		player2 = new Player(Constant.BLACK,"Steinar");
	}
	
	public ArrayList<Piece> getSelectablePieces(Player player){
		ArrayList<Piece> pieces = player.getPieces();
		ArrayList<Piece> selectable = new ArrayList<Piece>();
		for(Piece p : pieces){
			if(p.isSelectable()) selectable.add(p);
		}
		return selectable;
	}
	
	/*
	 * Input parameters are selected piece and destination point ID
	 * ID for the point the piece was moved from is available via p.getPosition().
	 * The player integer can be 1 or 2, depending on whose turn it is.
	 */
	public void move(Piece p, int to, int player){ 
		unreserveBoardSlot(p.getPosition());
		reserveBoardSlot(to, player);
		p.setPosition(to);
		
		// Checks for Morris at the point the piece is placed at.
		if(achievedMorris(to)){
			System.out.println("Placement Morris achieved. Removal State should be set!");
			updateMorrisStates(player); // Her skal mostander benyttes for � oppdatere hans brikker
			
			//setState(new RemovalState());
			// M� trigge metode som setter removable image resource
			//updatePieceImages(getPlayer1(), p.getPosition());
		}
	}
	
	public State getState(){
		return state;
	}
	public boolean isYourTurn(){
		return yourTurn;
	}
	public void setYourTurn(boolean yourTurn){
		this.yourTurn = yourTurn;
	}
	
	public Player getPlayer1(){
		return player1;
	}
	public Player getPlayer2(){
		return player2;
	}
	
	public String getMorrisGameType(){
		return gameType;
	}
	
	/*
	 * This method is intended to use when the player enters removalstate, and updates inMorris for all opponent pieces
	 * from pointID to pointID + occupant integer
	 */
	public void updateMorrisStates(int occupant){
		ArrayList<Piece> pieces = getPieces(occupant);

		// Reset inMorris for all pieces
		for(Piece p : pieces){
			p.setMorris(false);
		}

		// Checks all rows and columns and sets Morris state for the pieces in Morris.
		Slot[][] slots = board.getSlots();
		for(int i=0; i<slots.length; i++){
			for(int j=0; j<slots.length; j++){
				checkRow(i,j,occupant);
				checkColumn(j,i,occupant);
			}
		}
	}
	
	// Vurder � skifte navn p� metoden til checkMorris eller noe
	// Metoden er avhengig av at occupant settes til slots n�r flytt gj�res (fra BoardView per n�)
	
	public boolean achievedMorris(int id){
		int row = board.getSlotByID(id).getX();
		int column = board.getSlotByID(id).getY();
		int occupant =  board.getSlotOccupant(row, column);
		if(checkRow(row, column, occupant) || checkColumn(column, row, occupant)){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean checkRow(int row, int x, int occupant) {
		int counter = 0;
		Slot[][] slots = board.getSlots();
		ArrayList<Piece> pieces = getPieces(occupant);
		
		// Check for all rows other than row 3
		if(row != 3){
			for(int i=0; i<slots.length; i++){
				if(slots[row][i].getOccupant() == occupant){
					counter++;
				}
			}	
		} else {
			// Separate check for left part of row 3
			if(x<4){
				for(int i=0; i<3; i++){
					if(slots[row][i].getOccupant() == occupant) counter++;
				}
				
				if(counter == 3){
					for(int i=0; i<3; i++){
						for(int j=0; j<pieces.size(); j++){
							if(slots[row][i].isEnabled()){
								if(pieces.get(j).getPosition() == slots[row][i].getId()){
									pieces.get(j).setMorris(true);
								}
							}
						}
					}
					return true;
				} else {
					return false;
				}
			} else{
				// Separate check for right part of row 3
				for(int i=4; i<7; i++){
					if(slots[row][i].getOccupant() == occupant) counter++;
				}			
				if(counter == 3){
					for(int i=4; i<7; i++){
						for(int j=0; j<pieces.size(); j++){
							if(slots[row][i].isEnabled()){
								if(pieces.get(j).getPosition() == slots[row][i].getId()) {
									pieces.get(j).setMorris(true);
								}
							}
						}
					}
					return true;
				} else {
					return false;
				}
			}
		}
		// Morris in row != 3
		if(counter == 3){
			for(int i=0; i<slots.length; i++){
				for(int j=0; j<pieces.size(); j++){
					if(slots[row][i].isEnabled()){
						if(pieces.get(j).getPosition() == slots[row][i].getId()){
							pieces.get(j).setMorris(true);
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Help method only used to get the player's pieces when checking for Morris state (rows and columns).
	 */
	private ArrayList<Piece> getPieces(int player){
		if(player == 1){
			return getPlayer1().getPieces();
		} else {
			return getPlayer2().getPieces();
		}
	}
	
	private boolean checkColumn(int column, int y, int occupant) {
		int counter = 0;
		Slot[][] slots = board.getSlots();
		ArrayList<Piece> pieces = getPieces(occupant);
		
		// Check for all columns other than column 3
		if(column != 3){
			for(int i=0; i<slots.length; i++){
				if(slots[i][column].getOccupant() == occupant){
					counter++;		
				}
			}
		} else {
			// Separate check for upper part of column 3
			if(y<4){
				for(int i=0; i<3; i++){
					if(slots[i][column].getOccupant() == occupant) counter++;
				}
				if(counter == 3){
					for(int i=0; i<3; i++){
						for(int j=0; j<pieces.size(); j++){
							if(slots[i][column].isEnabled()){
								if(pieces.get(j).getPosition() == slots[i][column].getId()) pieces.get(j).setMorris(true);
							}
						}
					}
					return true;
				} else {
					return false;
				}
				
			// Separate check for lower part of column 3
			} else{
				
				for(int i=4; i<7; i++){
					if(slots[i][column].getOccupant() == occupant) counter++;
				}	
				if(counter == 3){
					for(int i=4; i<7; i++){
						for(int j=0; j<pieces.size(); j++){
							if(slots[i][column].isEnabled()){
								if(pieces.get(j).getPosition() == slots[i][column].getId()) pieces.get(j).setMorris(true);
							}
						}
					}
					return true;
				} else {
					return false;
				}
			}
		}
		// Morris in column != 3
		if(counter == 3){
			for(int i=0; i<slots.length; i++){
				for(int j=0; j<pieces.size(); j++){
					if(slots[i][column].isEnabled()){
						if(pieces.get(j).getPosition() == slots[i][column].getId()){
							pieces.get(j).setMorris(true);
							if(pieces.get(j).inMorris()) System.out.println("CHECKCOLUMN - Piece with ID "+pieces.get(j).getPosition()+" in column "+column+" is in Morris.");
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	// Her kan man ta inn pointID og et Player-objekt.
	public ArrayList<Slot> getHighlightList(int id, Player player) {
		//getBoard().getSlotByID(11).setTaken(true);
		return this.state.getHighlightList(board, id, player); // aktuell spiller benyttes
	}
	/**
	 * Update pieces resource images
	 * @param player
	 * @param positionId
	 */
	public void updatePieceImages(Player player,int positionId){
		this.state.updatePieceImages(player, positionId);
	}

	public void setState(State state){
		this.state = state;
	}
	
	public Board getBoard(){
		return board;
	}
	
	public void reserveBoardSlot(int id, int player){
		board.reserveSlot(id, player);
	}
	
	public void unreserveBoardSlot(int id){
		board.unreserveSlot(id);
	}
	
	/**
	 * Add listener
	 * @param listener
	 */
    public void addGameListener(GameListener listener) {
    	gameListeners.add(listener);
    }
    /**
     * remove listener
     * @param listener
     */
    public void removeListener(StateListener listener){
    	stateListeners.remove(listener);
    }
    
    /**
     * FIRE LISTENERS
     */
     
    private void firePiecePlaced(Player player,Piece piece) {
    	for(GameListener l : gameListeners){
    		l.playerPlacedPiece(player, piece);
    	}
    }

    /*
     * TODO
     * Remove pieceCounter and implement logic for initial state change in GameController.
     */
	public void playerPlacedPiece(Player player,Piece piece) {
		if(piece.getPosition() > 0){
			// TEMP
			if(player == getPlayer1()){
				reserveBoardSlot(piece.getPosition(), 1);
			} else {
				reserveBoardSlot(piece.getPosition(), 2);
			}
			
			if(achievedMorris(piece.getPosition())){
				System.out.println("Placement Morris achieved. Removal State should be set!");
				//setState(new RemovalState());
			}
	
			firePiecePlaced(player, piece);
			pieceCounter++;
			if(pieceCounter == 4){
				setState(new SelectState());
			}
		}
	}
	public boolean selectable(Player player,int positionId){
		for(Piece p : player.getPieces()){
			if(p.getPosition()==positionId){
				if(p.isSelectable())return true;
			}
		}
		return false;
	}
   
	
}
