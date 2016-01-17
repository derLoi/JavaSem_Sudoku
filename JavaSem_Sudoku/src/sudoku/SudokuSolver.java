package sudoku;

public class SudokuSolver {
	// 1. erstes oder n�chstes freies Feld (0) w�hlen
	// 2. Beschr�nkung Zeile/Spalte/Box: alle m�glichen Zahlen ausw�hlen
	SudokuChecker check = new SudokuChecker();

	public void solveSudoku(Cells[][] sudokuCells, Cells currentCell) {
		int stepps;
		stepps = 0;
		while (!check.sudokuIsSolved(sudokuCells)) {
			if (!check.valIsAllowed(sudokuCells, currentCell, currentCell.getValue()) || currentCell.getValue() == 0) {
				check.findPosVals(sudokuCells, currentCell);
				if (currentCell.posVal.isEmpty()) {
					// Stepping back -> backtracking
					currentCell.setValue(0);
					if (!currentCell.excVal.isEmpty()) {
						currentCell.excVal.clear();
					}
					currentCell.getLastCell().addValueToExcVals(currentCell.getLastCell().getValue());
					currentCell = currentCell.getLastCell();
				} else if (!currentCell.posVal.isEmpty()) {
					// fill value
					currentCell.pickValueFromList();
					currentCell = currentCell.getNextCell();
				}
			} else {
				currentCell.setValue(0);
			}
			stepps++;
		}
		System.out.println("Solved: it took me " + stepps + " iterations to generate this sudoku for you!");
	}
}
