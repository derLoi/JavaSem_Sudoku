package sudoku;

public class SudokuChecker {
	// Fehler-Variablen
	private boolean[] errInRow = new boolean[9];
	private boolean[] errInCol = new boolean[9];
	private boolean[] errInBox = new boolean[9];

	// Get-Methoden
	public boolean[] getErrInRow() {
		return errInRow;
	}

	public boolean[] getErrInCol() {
		return errInCol;
	}

	public boolean[] getErrInBox() {
		return errInBox;
	}

	/*
	 * @author: LS
	 * 
	 * @version: 10/12/2015
	 * 
	 * @changelog:
	 * 
	 * @param: 2-dim Integer Array, dass den aktuellen Spielstand enthält
	 * 
	 * @return: Boolean zeigt an, ob Sudoku gültig ist
	 * 
	 * @desc: Überprüft, ob das Sudoku gültig ist
	 */
	public void checkSudoku(int[][] sudoku) {
		int[] row = new int[9];
		int[] col = new int[9];
		int[] box = new int[9];
		int index = 0; 

		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				row[j] = sudoku[i][j];
				col[j] = sudoku[j][i];
			}
			errInRow[i] = checkRow(row);
			errInCol[i] = checkRow(col);
		}
		for (int k = 0; k <= 2; k++) {
			for (int l = 0; l <= 2; l++) {
				for (int m = 0; m <= 2; m++) {
					for (int n = 0; n <= 2; n++){
						box[index] = sudoku[k*3+m][l*3+n];
						index++;
					}
				}
				errInBox[3*k+l] = checkRow(box);
				index = 0;
			}
		}
	}

	public boolean checkRow(int[] row) {
		boolean bln = true;
		for (int i = 0; i <= 8; i++) {
			for (int j = i + 1; j <= 8; j++) {
				if (row[i] == row[j] && row[i] > 0) {
					bln = false;
				}
			}
		}
		return bln;
	}
}
