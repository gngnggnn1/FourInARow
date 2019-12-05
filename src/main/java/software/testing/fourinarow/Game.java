package software.testing.fourinarow;

import java.util.Stack;

/**
 * Represents the grid that holds the pieces that have been
 * placed into each column.
 */
public class Game {

    /** The maximum number of rows in the game grid */
    public static int MAXIMUM_ROWS = 6;

    /** The maximum number of columns in the game grid */
    public static int MAXIMUM_COLUMNS = 7;

    /** The game grid that holds information about the status of each cell */
    private CellStatus[][] gameGrid;
    
    /** Record who take turn by turn%2 */
    private int turn = 0;
    
    /** Record last turn */
    private Stack<Integer> toUndo = new Stack<>();
    
    /** Record last undo */
    private Stack<Integer> toRedo = new Stack<>();
    
    /**
     * Creates a new instance, initialising the board with the
     * Maximum values for columns and rows.
     */
    public Game() {
        gameGrid = new CellStatus[MAXIMUM_COLUMNS][MAXIMUM_ROWS];
    }

    /**
     * Returns the number of cells in a column that have the status
     * GridCellStatus.EMPTY.
     *
     * @param column The column to check. This is 0 indexed, so 0 represents column 1.
     * @return A count of the number of empty cells.
     * @throws FourInARowException if the column value is outside the range
     * 0 to MAXIMUM_COLUMNS.
     */
    public int numberOfEmptyCellsInColumn(int column) throws FourInARowException {

        int emptyCount = 0;

        for(int row = 0; row < MAXIMUM_ROWS; row++) {
            if(getCellStatus(column, row) == CellStatus.EMPTY) {
                emptyCount++;
            }
        }

        return emptyCount;
    }

    /**
     * Gets the GridCellStatus for a cell at the given grid position.
     * @param row The number of the row, with 0 representing the first row.
     * @param column The number of the column, with 0 representing the first column.
     * @return The status value for the grid at the specified position.
     * @throws FourInARowException if the row or column number is out of bounds for the
     * number of rows and columns.
     */
    public CellStatus getCellStatus(int column, int row)
           throws FourInARowException {

        if(row < 0) {
            throw new FourInARowException("Row cannot be negative");
        }

        if(row > MAXIMUM_ROWS - 1) {
            throw new FourInARowException("Row cannot be greater than the number of rows");
        }

        if(column < 0) {
            throw new FourInARowException("Column cannot be negative");
        }

        if(column > MAXIMUM_COLUMNS - 1) {
            throw new FourInARowException("Column cannot be greater than the number of columns");
        }

        return gameGrid[column][row];
    }

    /**
     * Resets the board so that every position contains the value
     * GridCellStatus.EMPTY.
     *//*
    public void reset() {
        for(int column = 0; column < MAXIMUM_COLUMNS; column++) {
            for(int row = 0; row < MAXIMUM_ROWS; row++) {
                gameGrid[column][row] = CellStatus.EMPTY;
            }
        }

    }*/
    
    public Player getActivePlayer() {
    	if(turn % 2 == 0) {
    		return Player.ONE;
    	}
    	else {
    		return Player.TWO;
    	}
    }
    
    public Player getLastActivePlayer() {
    	if(turn % 2 != 0) {
    		return Player.ONE;
    	}
    	else {
    		return Player.TWO;
    	}
    }
    
    public boolean takeTurn(int column) throws FourInARowException {
    	int i = 0;
    	/*while(getCellStatus(column, i) != CellStatus.EMPTY) {
    		if(i < MAXIMUM_ROWS - 1) i++;
    		else return false;
    	}*/
    	
    	if(numberOfEmptyCellsInColumn(column) != 0) i = MAXIMUM_ROWS - numberOfEmptyCellsInColumn(column);
    	else return false;
    	
    	if(turn % 2 == 0) {
    		gameGrid[column][i] = CellStatus.PLAYER_ONE;
    		turn++;
    		toUndo.push(column);
    		return true;
    	}
    	if(turn % 2 != 0) {
    		gameGrid[column][i] = CellStatus.PLAYER_TWO;
    		turn++;
    		toUndo.push(column);
    		return true;
    	}
    	return false;
    }

	public void resetGame() {
    	for(int i = 0; i < MAXIMUM_COLUMNS; i++) {
    		for(int j = 0; j < MAXIMUM_ROWS; j++) {
    			gameGrid[i][j] = CellStatus.EMPTY;
    		}
    	}
    }
    
    public boolean undo() throws FourInARowException {
    	if(toUndo.empty()) return false;
    	int undoColumn = toUndo.pop();
    	
    	for(int i = 0; i < MAXIMUM_ROWS; i++) {
    		if(getCellStatus(undoColumn, i) == CellStatus.EMPTY) {
    			gameGrid[undoColumn][i - 1] = CellStatus.EMPTY;
    			toRedo.push(undoColumn);
    			turn--;
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public boolean redo() throws FourInARowException {
    	if(toRedo.empty()) return false;
    	int redoColumn = toRedo.pop();
    	//CellStatus cs = statusToRedo.pop();
    	
    	/*for(int i = 0; i < MAXIMUM_ROWS; i++) {
    		if(getCellStatus(redoColumn, i) == CellStatus.EMPTY) {
    			gameGrid[redoColumn][i] = cs;
    			toUndo.push(redoColumn);
    			turn++;
    			return true;
    		} 
    	}*/
    	
    	takeTurn(redoColumn);
    	return false;
    } 
    
    public boolean horizonWin(int i, int j) throws FourInARowException {
    	int left = i;
    	int right = i;
    	int count = 0;
    	
    	if(getCellStatus(i, j) != CellStatus.EMPTY) {
    		while(left >= 0 && getCellStatus(left, j) == getCellStatus(i, j)) {
        		count++;
        		left--;
        	}
        	
        	while(right < MAXIMUM_ROWS - 1 && getCellStatus(right, j) == getCellStatus(i, j)) {
        		count++;
        		right++;
        	}
    	}
    	if(count > 4) {
    		return true;
    	}
    	return false;
    }
	
    public boolean verticalWin(int i, int j) throws FourInARowException{
    	int up = j;
    	int down = j;
    	int count = 0;
    	
    	if(getCellStatus(i, j) != CellStatus.EMPTY) {
    		while(down >= 0 && getCellStatus(i, down) == getCellStatus(i, j)) {
    			count++;
    			down--;
    		}
    		while(up < MAXIMUM_COLUMNS - 1 && getCellStatus(i, up) == getCellStatus(i, j)) {
        		count++;
        		up++;
        	}
    	}
    	
    	if(count > 4) {
    		return true;
    	}
    	return false;
    }
    
    public boolean diagonalWin1(int i, int j) throws FourInARowException {
    	int up = j;
    	int right = i;
    	int down = j;
    	int left = i;
    	int count = 0;
    	
    	if(getCellStatus(i, j) != CellStatus.EMPTY) {
    		while(left >= 0 && down >= 0 && getCellStatus(left, down) == getCellStatus(i, j)) {
    			count++;
    			left--;
    			down--;
    		}
    		while(right < MAXIMUM_ROWS - 1 && up < MAXIMUM_COLUMNS && getCellStatus(right, up) == getCellStatus(i, j)) {
    			count++;
    			right++;
    			up++;
    		}
    	}
    	if(count > 4) {
    		return true;
    	}
    	return false;
    }
    
    public boolean diagonalWin2(int i, int j) throws FourInARowException {
    	int up = j;
    	int right = i;
    	int down = j;
    	int left = i;
    	int count = 0;
    	
    	if(getCellStatus(i, j) != CellStatus.EMPTY) {
    		while(left >= 0 && up < MAXIMUM_COLUMNS && getCellStatus(left, up) == getCellStatus(i, j)) {
    			count++;
    			left--;
    			up++;
    		}
    		while(right < MAXIMUM_ROWS - 1 && down >= 0 && getCellStatus(right, down) == getCellStatus(i, j)) {
    			count++;
    			right++;
    			down--;
    		}
    	}
    	if(count > 4) {
    		return true;
    	}
    	return false;
    }
    
    public boolean hasWon() throws FourInARowException {
    	if(toUndo.empty()) return false;
    	int i = toUndo.peek();
    	int j = 0;
    	
    	j = MAXIMUM_ROWS - numberOfEmptyCellsInColumn(i);
    	
    	if(horizonWin(i, j - 1)) return true;
    	if(verticalWin(i, j - 1)) return true;
    	if(diagonalWin1(i, j - 1)) return true;
    	if(diagonalWin2(i, j - 1)) return true;
    	
    	return false;
    }
    
}
