package sudoku;

public class SudokuSolver {
	// 1. erstes oder nächstes freies Feld (0) wählen
	// 2. Beschränkung Zeile/Spalte/Box: alle möglichen Zahlen auswählen
	private Cells lastCell;

	public void solveSudoku(Cells[][] sudokuCells) {
		while (getNextEmptyCell(sudokuCells) != null) {
			Cells currentCell = getNextEmptyCell(sudokuCells);
			// Letzte Zelle speichern (außer bei erster freier Zelle)
			if (lastCell != null) {
				currentCell.setLastCell(lastCell);
				lastCell.setNextCell(currentCell);
			}
			// Alle möglichen Werte für aktuelle Zelle finden
			if (currentCell.posVal.isEmpty()) {
				findPosVals(currentCell, sudokuCells);
			}
			// Wenn Werte vorhanden sind, wähle einen Zufälligen aus
			System.out.println("posVal is Empty: " + currentCell.posVal.isEmpty());
			if (currentCell.posVal.isEmpty() == false) {
				currentCell.pickValueFromList();
				lastCell = currentCell;
			} else if (currentCell.posVal.isEmpty() == true) {
				System.out.println(
						"stepped back at Cell: " + (char) (currentCell.getY() + 65) + (currentCell.getX() + 1));
				stepBack(currentCell.getLastCell());
			}
		}
	}

	public void stepBack(Cells currentCell) {
		currentCell.excVal.add(currentCell.getValue());
		currentCell.setValue(0);
		currentCell.getNextCell().excVal.clear();
		System.out.println("stepped back to Cell: " + (char) (currentCell.getY() + 65) + (currentCell.getX() + 1));
	}

	public void findPosVals(Cells currentCell, Cells[][] sudokuCells) {
		for (int i = 1; i <= 9; i++) {
			if (valIsAllowed(sudokuCells, currentCell.getX(), currentCell.getY(), i, currentCell)) {
				currentCell.posVal.add(i);
				System.out.println("added " + i + " to List of posVal");
			}
		}
	}

	public Cells getNextEmptyCell(Cells[][] sudokuCells) {
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				if (sudokuCells[i][j].getValue() == 0) {
					// System.out.println("Nächste leere Zelle: " + (char) (i +
					// 65) + (j + 1));
					return sudokuCells[i][j];
				}
			}
		}
		// Keine leere Zelle mehr übrig
		return null;
	}

	public int[] getCurrentRow(Cells[][] sudokuCells, int y) {
		int[] row = new int[9];
		// i := Spalten
		for (int i = 0; i <= 8; i++) {
			// Zeilen 0 - 8 aus Sudoku-Array in row speichern
			row[i] = sudokuCells[y][i].getValue();
		}
		return row;
	}

	public int[] getCurrentCol(Cells[][] sudokuCells, int x) {
		int[] col = new int[9];
		// i := Zeilen
		for (int i = 0; i <= 8; i++) {
			// Zeilen 0 - 8 aus Sudoku-Array in row speichern
			col[i] = sudokuCells[i][x].getValue();
		}
		return col;
	}

	public int[] getCurrentBox(Cells[][] sudokuCells, int x, int y) {
		int temp = 0;
		int[] box = new int[9];
		int startZ = (y / 3) * 3;
		int startS = (x / 3) * 3;
		for (int i = startZ; i <= (startZ + 2); i++) {
			// n := Spalten in Zeile m in Box
			for (int j = startS; j <= (startS + 2); j++) {
				// temp Index für Reihen-Array box
				box[temp] = sudokuCells[i][j].getValue();
				// temp inkrementieren
				temp++;
			}
		}
		return box;
	}
	
	public Boolean valIsAllowed(Cells[][] sudokuCells, int x, int y, int value, Cells currentCell) {
		SudokuChecker check = new SudokuChecker();
		int[] excVals;
		Boolean bln;
		bln = check.checkNum(getCurrentRow(sudokuCells, y), value);
		bln = bln && check.checkNum(getCurrentCol(sudokuCells, x), value);
		bln = bln && check.checkNum(getCurrentBox(sudokuCells, x, y), value);
		excVals = currentCell.listToArray(currentCell.excVal);
		bln = bln && check.checkNum(excVals, value);
		return bln;
	}
}
