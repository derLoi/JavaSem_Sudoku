package sudoku;

public class SudokuSolver {
	// 1. erstes oder nächstes freies Feld (0) wählen
	// 2. Beschränkung Zeile/Spalte/Box: alle möglichen Zahlen auswählen
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
