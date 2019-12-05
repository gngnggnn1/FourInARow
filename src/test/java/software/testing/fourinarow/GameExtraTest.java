package software.testing.fourinarow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GameExtraTest {
	Game game = new Game();
	
	@Test
	public void testGetCellStatus() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		assertEquals(CellStatus.PLAYER_ONE, game.getCellStatus(0, 0));
	}
	
	@Test
	public void testGetCellStatusNegativeRow() {
		FourInARowException exception = assertThrows(FourInARowException.class, () -> {
			game.resetGame();
			game.getCellStatus(0, -1);
		});
	}
	
	@Test
	public void testGetCellStatusGreatRow() {
		FourInARowException exception = assertThrows(FourInARowException.class, () -> {
			game.resetGame();
			game.getCellStatus(0, 6);
		});
	}
	
	@Test
	public void testGetCellStatusNegativeColumn() {
		FourInARowException exception = assertThrows(FourInARowException.class, () -> {
			game.resetGame();
			game.getCellStatus(-1, 0);
		});
	}
	
	@Test
	public void testGetCellStatusGreatColunm() {
		FourInARowException exception = assertThrows(FourInARowException.class, () -> {
			game.resetGame();
			game.getCellStatus(7, 0);
		});
	}
	
	@Test
	public void testResetGame() throws FourInARowException {
		game.resetGame();
		boolean check = true;
		for(int i = 0; i < Game.MAXIMUM_COLUMNS; i++) {
    		for(int j = 0; j < Game.MAXIMUM_ROWS; j++) {
    			if(game.getCellStatus(i, j) != CellStatus.EMPTY) {
    				check = false;
    			}
    		}
    	}
		assertTrue(check);
	}
	
	@Test
	public void testGetActivePlayer2() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		assertEquals(Player.TWO, game.getActivePlayer());
	}
	
	@Test
	public void testGetActivePlayer1() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		assertEquals(Player.ONE, game.getActivePlayer());
	}
	
	@Test
	public void testGetLastActivePlayer1() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		assertEquals(Player.ONE, game.getLastActivePlayer());
	}
	
	@Test
	public void testGetLastActivePlayer2() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		assertEquals(Player.TWO, game.getLastActivePlayer());
	}
	
	@Test
	public void testTakeTurn() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		assertEquals(CellStatus.PLAYER_ONE, game.getCellStatus(0, 0));
		game.takeTurn(1);
		assertEquals(CellStatus.PLAYER_TWO, game.getCellStatus(1, 0)); 
	}
	
	@Test
	public void testTakeTurnMax() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		assertFalse(game.takeTurn(0));
	}
	
	@Test
	public void testUndo() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		game.undo();
		assertEquals(CellStatus.EMPTY, game.getCellStatus(0, 2));
		game.undo();
		assertEquals(CellStatus.EMPTY, game.getCellStatus(0, 1));
	}
	
	@Test
	public void testUndoWithoutTurn() throws FourInARowException {
		game.resetGame();
		assertFalse(game.undo());
	}
	
	@Test
	public void testRedo() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		game.undo();
		game.redo();
		assertEquals(CellStatus.PLAYER_ONE, game.getCellStatus(0, 2));
	}
	
	@Test
	public void testRedoWithoutUndo() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		assertFalse(game.redo());
	}
	
	@Test
	public void testHorizonWin() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(1);
		game.takeTurn(0);
		game.takeTurn(2);
		game.takeTurn(0);
		game.takeTurn(3);
		assertTrue(game.horizonWin(1, 0));
	}
	
	@Test
	public void testHorizonWinActualNotWon() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		assertFalse(game.horizonWin(0, 1));
	}
	
	@Test
	public void testVerticalWin() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);//
		game.takeTurn(0);
		game.takeTurn(0);//1
		game.takeTurn(1);
		game.takeTurn(0);//w2
		game.takeTurn(1);
		game.takeTurn(0);//3
		game.takeTurn(1);
		game.takeTurn(0);
		assertTrue(game.verticalWin(0, 2));
	}
	
	@Test
	public void testVerticalWinActualNotWon() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		assertFalse(game.verticalWin(0, 1));
	}
	
	@Test
	public void testDiagonalWin1() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);//
		game.takeTurn(1);
		game.takeTurn(1);//
		game.takeTurn(2);
		game.takeTurn(0);//
		game.takeTurn(2);
		game.takeTurn(2);//
		game.takeTurn(3);
		game.takeTurn(3);//
		game.takeTurn(3);
		game.takeTurn(3);
		assertTrue(game.diagonalWin1(2, 2));
	}
	
	@Test
	public void testDiagonalWin1ActualNotWon() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		assertFalse(game.diagonalWin1(0, 1));
	} 
	
	@Test
	public void testDiagonalWin2() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(1);
		game.takeTurn(0);
		game.takeTurn(1);
		game.takeTurn(1);
		game.takeTurn(2);
		game.takeTurn(2);
		game.takeTurn(1);
		game.takeTurn(3);
		assertTrue(game.diagonalWin2(3, 0));
	}
	
	@Test
	public void testDiagonalWin2ActualNotWon() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		assertFalse(game.diagonalWin2(0, 1));
	} 
	
	@Test
	public void testHasWonWithoutTakeTurn() throws FourInARowException {
		game.resetGame();
		game.hasWon();
	}
	
	@Test
	public void testHasWonHorizon() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(1);
		game.takeTurn(0);
		game.takeTurn(2);
		game.takeTurn(0);
		game.takeTurn(3);
		assertTrue(game.hasWon());
	}
	
	@Test
	public void testHasVerticalWin() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);//
		game.takeTurn(0);
		game.takeTurn(0);//1
		game.takeTurn(1);
		game.takeTurn(0);//w2
		game.takeTurn(1);
		game.takeTurn(0);//3
		game.takeTurn(1);
		game.takeTurn(0);
		assertTrue(game.hasWon());
	}
	
	@Test
	public void testHasDiagonalWin1() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);//
		game.takeTurn(1);
		game.takeTurn(1);//
		game.takeTurn(2);
		game.takeTurn(0);//
		game.takeTurn(2);
		game.takeTurn(2);//
		game.takeTurn(3);
		game.takeTurn(3);//
		game.takeTurn(3);
		game.takeTurn(3);
		assertTrue(game.hasWon());
	}
	
	@Test
	public void testHasDiagonalWin2() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(1);
		game.takeTurn(0);
		game.takeTurn(1);
		game.takeTurn(1);
		game.takeTurn(2);
		game.takeTurn(2);
		game.takeTurn(1);
		game.takeTurn(3);
		assertTrue(game.hasWon());
	}
	
	@Test
	public void testHasNotWon() throws FourInARowException {
		game.resetGame();
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(0);
		game.takeTurn(1);
		game.takeTurn(0);
		game.takeTurn(1);
		assertFalse(game.hasWon());
	}
}
