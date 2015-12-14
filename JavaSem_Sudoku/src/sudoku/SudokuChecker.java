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
	 * @version: 11/12/2015
	 * 
	 * @changelog: LS - �berpr�fung f�r 3x3-Boxen hinzugef�gt
	 * 
	 * @param: 2-dim Integer Array, dass den aktuellen Spielstand enth�lt
	 * 
	 * @return: Boolean zeigt an, ob Sudoku g�ltig ist
	 * 
	 * @desc: �berpr�ft, ob das Sudoku g�ltig ist
	 */
	public void checkSudoku(int[][] sudoku) {
		// Arrays speichern Status der Reihen/Spalten/Boxen
		int[] row = new int[9]; // Reihen
		int[] col = new int[9]; // Spalten
		int[] box = new int[9]; // Boxen
		int temp = 0; // Hilfsvariable
		/*
		 * Teil 1: Zerschneide Sudoku-Array in 9 Reihen und 9 Spalten, pr�fe
		 * Reihen/Spalten auf G�ltigkeit, speichere Status (true/false)
		 * G�ltigkeit ab
		 */
		// i := Zeilen
		for (int i = 0; i <= 8; i++) {
			// j := Spalten
			for (int j = 0; j <= 8; j++) {
				// Zeilen 0 - 8 aus Sudoku-Array in row speichern
				row[j] = sudoku[i][j];
				// Spalten 0 - 8 aus Sudoku-Array in col speichern
				col[j] = sudoku[j][i];
			}
			// �berpr�fe G�ltigkeit der aktuellen Zeile und Spalte
			errInRow[i] = checkRow(row);
			errInCol[i] = checkRow(col);
		}
		/*
		 * Teil 2: Zerschneide Sudoku-Array in 9 Boxen
		 */
		// k := Reihen aus Boxen {1, 2, 3}, {4, 5, 6}, {7, 8, 9}
		for (int k = 0; k <= 2; k++) {
			// l := Boxen in Reihen k
			for (int l = 0; l <= 2; l++) {
				// m := Zeilen in Box
				for (int m = 0; m <= 2; m++) {
					// n := Spalten in Zeile m in Box
					for (int n = 0; n <= 2; n++) {
						// temp Index f�r Reihen-Array box
						box[temp] = sudoku[k * 3 + m][l * 3 + n];
						// temp inkrementieren
						temp++;
					}
				}
				// �berpr�fe Box auf Fehler, speichere Status in errInBox
				errInBox[3 * k + l] = checkRow(box);
				// temp zur�cksetzen
				temp = 0;
			}
		}
	}

	/*
	 * @author: LS
	 * 
	 * @version: 11/12/2015
	 * 
	 * @changelog: LS - Kommentare hinzugef�gt
	 * 
	 * @param: Integer Array, dass eine Reihe enth�lt, die eine Zeile, Spalte
	 * oder Box darstellt
	 * 
	 * @return: Boolean zeigt an, ob Reihe g�ltig ist
	 * 
	 * @desc: �berpr�ft, ob das Sudoku g�ltig ist
	 */
	public boolean checkRow(int[] row) {
		// Hilfsvariable
		boolean bln = true;
		// i := Index f�r aktuell verglichene Zahl
		for (int i = 0; i <= 7; i++) {
			// j := Index f�r Zahl, mit der verglichen wird
			for (int j = i + 1; j <= 8; j++) {
				// Vergleiche row[i] mit jeder Zahl row[j]
				// kommen gleiche Zahlen vor (ausgenommen 0) ist die Zeile
				// ung�ltig
				if (row[i] == row[j] && row[i] > 0) {
					// Hilfsvariable bln auf false setzen
					bln = false;
				}
			}
		}
		// R�ckgabewert: true (keine Fehler), false (mind. 1 Fehler)
		return bln;
	}

	public boolean checkNumInCell(int[][] sudoku, int x, int y, int num) {
		int[] col = new int[9];
		int[] row = new int[9];
		int[] box = new int[9];
		int temp = 0;
		for (int i = 0; i <= 8; i++) {
			row[i] = sudoku[y][i];
		}
		for (int i = 0; i <= 8; i++) {
			col[i] = sudoku[i][x];
		}
		int startZ = (y / 3) * 3;
		int startS = (x / 3) * 3;

		for (int i = startZ; i <= (startZ + 2); i++) {
			// n := Spalten in Zeile m in Box
			for (int j = startS; j <= (startS + 2); j++) {
				// temp Index f�r Reihen-Array box
				box[temp] = sudoku[i][j];
				// temp inkrementieren
				temp++;
			}
		}
		return checkRow(row) && checkRow(col) && checkRow(box);
	}
}
