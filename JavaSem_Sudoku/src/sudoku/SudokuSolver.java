package sudoku;

public class SudokuSolver {
	// 1. erstes oder n�chstes freies Feld (0) w�hlen
	// 2. Beschr�nkung Zeile/Spalte/Box: alle m�glichen Zahlen ausw�hlen
	SudokuChecker check = new SudokuChecker();
	
	public void solveSudoku(Cells[][] sudokuCells, Cells currentCell) {
		findNextVal(sudokuCells, currentCell);
	}
	
	public void findNextVal(Cells[][] sudokuCells, Cells currentCell){
		check.findPosVals(sudokuCells, currentCell);
		if (currentCell.getValue() == 0 || !check.valIsAllowed(sudokuCells, currentCell, currentCell.getValue())){
			if (!currentCell.posVal.isEmpty()){
				currentCell.pickValueFromList();
			}else{
				stepBack(sudokuCells, currentCell);
			}
		}
	}
	
	public void bckTrckError(Cells[][] sudokuCells, Cells currentCell){
//		currentCell.excVal.add(currentCell.getValue());
//		currentCell.posVal.remove((int) currentCell.getValue());
//		currentCell.pickValueFromList();
		stepForward(sudokuCells, currentCell);;
	}

	public void stepBack(Cells[][] sudokuCells, Cells currentCell) {
		bckTrckError(sudokuCells, currentCell.getLastCell());
	}

	public void stepForward(Cells[][] sudokuCells, Cells currentCell) {
		findNextVal(sudokuCells, currentCell.getNextCell());
	}
}
