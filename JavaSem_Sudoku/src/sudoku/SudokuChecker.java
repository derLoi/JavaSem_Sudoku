package sudoku;

public class SudokuChecker {

	/*
	 * @author: LS
	 * 
	 * @version: 11/12/2015
	 * 
	 * @changelog: LS - Überprüfung für 3x3-Boxen hinzugefügt
	 * 
	 * @param: 2-dim Integer Array, dass den aktuellen Spielstand enthält
	 * 
	 * @return: Boolean zeigt an, ob Sudoku gültig ist
	 * 
	 * @desc: Überprüft, ob das Sudoku gültig ist
	 */
	public void findPosVals(Cells[][] sudokuCells, Cells currentCell) {
		if (!currentCell.posVal.isEmpty()) currentCell.posVal.clear();
		for (int i = 1; i <= 9; i++) {
			if (valIsAllowed(sudokuCells, currentCell, i)) {
				currentCell.posVal.add(i);
				// System.out.println("added " + i + " to List of posVal");
			}
		}
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

	public Boolean valIsAllowed(Cells[][] sudokuCells, Cells currentCell, int value) {
		// excVals = currentCell.listToArray(currentCell.excVal);
		return checkNum(getCurrentRow(sudokuCells, currentCell.getY()), value)
				&& checkNum(getCurrentCol(sudokuCells, currentCell.getX()), value)
				&& checkNum(getCurrentBox(sudokuCells, currentCell.getX(), currentCell.getY()), value) 
				&& checkNum(currentCell.listToArray(currentCell.excVal), value);
	}

	public boolean checkNum(int[] row, int num) {
		// Hilfsvariable
		boolean bln = true;
		// i := Index für aktuell verglichene Zahl
		for (int i = 0; i <= (row.length - 1); i++) {
			if (row[i] == num) {
				bln = false;
			}
		}
		// Rückgabewert: true (keine Fehler), false (mind. 1 Fehler)
		return bln;
	}
	
	public boolean checkNum(int[] row) {
		// Hilfsvariable
		boolean bln = true;
		// i := Index für aktuell verglichene Zahl
		for (int i = 0; i < row.length; i++) {
			for (int j = 1; j <= 9; j++){
				if (row[i] == j || row[i] == 0) {
					bln = false;
				}
			}
		}
		// Rückgabewert: true (keine Fehler), false (mind. 1 Fehler)
		return bln;
	}
	
	public boolean sudokuIsSolved(Cells[][] sudokuCells){
		boolean bln = true;
		for (int i = 0; i <= 8; i++){
			bln = bln && checkNum(getCurrentRow(sudokuCells, i));
			bln = bln && checkNum(getCurrentCol(sudokuCells, i));
		}
		for (int i = 0; i <= 6; i += 3){
			for (int j = 0; j <= 6; j += 3){
				bln = bln && checkNum(getCurrentBox(sudokuCells, j, i));
			}
		}
		return bln;
	}
}