package sudoku;

public class SudokuSolver {
	// 1. erstes oder nächstes freies Feld (0) wählen
	// 2. Beschränkung Zeile/Spalte/Box: alle möglichen Zahlen auswählen
	SudokuChecker check = new SudokuChecker();

	public void solveSudoku(Cells[][] sudokuCells, Cells currentCell) {
		findNextVal(sudokuCells, currentCell);
	}

	public void findNextVal(Cells[][] sudokuCells, Cells currentCell) {
		int stepps = 0;
		while (!check.sudokuIsSolved(sudokuCells)) {
			if (!check.valIsAllowed(sudokuCells, currentCell, currentCell.getValue()) || currentCell.getValue() == 0) {
				check.findPosVals(sudokuCells, currentCell);
				// System.out.println("cell: " + (char) (currentCell.getY() +
				// 65) + (currentCell.getX() + 1));
				if (currentCell.posVal.isEmpty()) {
					// Stepping back -> backtracking
					currentCell.setValue(0);
					if (!currentCell.excVal.isEmpty()) {
						currentCell.excVal.clear();
						// System.out.println("clr excVal");
					}
					currentCell.getLastCell().remValFromPosVals(currentCell.getLastCell().getValue());
					currentCell = currentCell.getLastCell();
					// System.out.println("step back");
				} else if (!currentCell.posVal.isEmpty()) {
					// fill value
					currentCell.pickValueFromList();
					currentCell = currentCell.getNextCell();
					// System.out.println("step next");
				}
			} else {
				currentCell.setValue(0);
			}
			stepps++;
		}
		System.out.println("step: " + stepps);
	}

	public void bckTrckError(Cells[][] sudokuCells, Cells currentCell) {
		// currentCell.excVal.add(currentCell.getValue());
		// currentCell.posVal.remove((int) currentCell.getValue());
		// currentCell.pickValueFromList();
		stepForward(sudokuCells, currentCell);
		;
	}

	public void stepBack(Cells[][] sudokuCells, Cells currentCell) {
		bckTrckError(sudokuCells, currentCell.getLastCell());
	}

	public void stepForward(Cells[][] sudokuCells, Cells currentCell) {
		findNextVal(sudokuCells, currentCell.getNextCell());
	}
}
