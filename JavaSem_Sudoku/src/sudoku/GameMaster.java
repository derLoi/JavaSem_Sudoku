package sudoku;

import java.util.Random;

// import java.util.Scanner;
/*
 * @author: LS 
 * @version: 11/12/2015
 * @changelog:
 * 		TM: Überarbeitetungen der Spielfeldanzeige
 * 		LS: Test mit Gültigkeitsprüfung für zufällig generiertes Sudoku. Ausgabe Ergebnisse
 * @desc: kontrolliert den Spielablauf.
 */
public class GameMaster {
// Test 
	private static Cells[][] sudokuCells = new Cells[9][9];

	public static void main(String[] args) {

		System.out.println("Willkommen bei Sudokufy - von LTML");

		// Scanner scanner = new Scanner(System.in);
		SudokuSolver solve = new SudokuSolver();
		// generate objects in array
		genSudoku();
		// Fill first random numbers
		fillRndVal();
		drawBoard();

		// solve given sudoku
		solve.solveSudoku(sudokuCells, firstEmptyCell(sudokuCells[0][0]));
		drawBoard();
		}

	public static void genSudoku() {
		// initialize Objects in array
		Cells currentCell = null;
		Cells lastCell = null;
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				sudokuCells[i][j] = new Cells(j, i);
			}
		}
		// set first last cell
		lastCell = sudokuCells[8][8];
		// link objects as double linked list
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				currentCell = sudokuCells[i][j];
				currentCell.setLastCell(lastCell);
				lastCell.setNextCell(currentCell);
				lastCell = currentCell;			
			}
		}
		// correctly link last cell
		sudokuCells[8][8].setNextCell(sudokuCells[0][0]);
	}
	public static void fillRndVal(){
	// fill first row with values
		Random rnd = new Random(); // neues Random-Object erstellen
		int num;
		boolean bln;
		// i := Zeilen
		for (int i = 0; i <= 8; i++) {
			do {
				num = rnd.nextInt(9) + 1; // neue Zufallszahl zw. 1 - 9 erzeugen
				bln = true; // Fehler-Indikator zurücksetzen
				for (int j = 0; j <= i; j++) {
					if (num == sudokuCells[0][j].getValue()) {
						bln = false;
						break;
					}
				}
			} while (bln == false);
			insertFixVal(sudokuCells, i, 0, num);
		}
	}
	
	public static void insertFixVal(Cells[][] sudokuCells, int x, int y, int value){
		sudokuCells[y][x].setValue(value);
		sudokuCells[y][x].setFixVal(true);
		sudokuCells[y][x].getLastCell().setNextCell(sudokuCells[y][x].getNextCell());
		sudokuCells[y][x].getNextCell().setLastCell(sudokuCells[y][x].getLastCell());
	}
	
	public static Cells firstEmptyCell(Cells currentCell){
		while(currentCell.isFixVal()){
			currentCell = currentCell.getNextCell();
		}
		return currentCell;
	}

	/*
	 * @author: TM
	 * 
	 * @version: 08/12/2015
	 * 
	 * @param: arySudoku 2-dimensionales Integer-Array, in dem der Lösungsstand
	 * des Sudoku-Spiels hinterlegt ist.
	 * 
	 * @desc: zeichnet das Sudoku-Spielfeld in der Konsole, Beispiel:
	 */
	public static void drawBoard() {
		String[] abc = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };
		// i := Zeilen
		for (int i = 0; i <= 8; i++) {
			// erste Zeile Kopfbereich zeichnen
			if (i == 0) {
				System.out.println("");
				// Koordinaten A - I im Kopf
				System.out.println("       1   2   3   4   5   6   7   8   9");
				System.out.println("     _____________________________________ ");
				System.out.println("    |                                     |");
			}
			// j := Spalten in Zeile i
			for (int j = 0; j <= 8; j++) {
				// erste Zelle in Zeile i mit Koordinaten 1 - 9
				if (j == 0) {
					System.out.print(" " + abc[i] + "  |  " + sudokuCells[i][j].getValue() + "  ");

					// Mit +(i+1) lassen wir uns die jeweilige Zeile anzeigen,
					// um das Spielen zu erleichtern.
					// letzte Zelle in Zeile i mit abschließendem Rahmen &
					// Zeilenumbruch
				} else if (j == 8) {
					System.out.print(" " + sudokuCells[i][j].getValue() + "  |");
					System.out.println("");

					// Damit die Kästchen klar erkennbar sind, wird
					// jeweils nach dem 3. und dem 6. Zahlenfeld "!" durch "|"
					// ersetzt.
				} else if (j == 2 || j == 5) {
					System.out.print(" " + sudokuCells[i][j].getValue() + " |");
					// verbleibende Zellen
				} else {
					System.out.print(" " + sudokuCells[i][j].getValue() + "  ");
				}
			}
			// letzte Zeile i mit abschließendem Rahmen
			if (i == 8) {
				System.out.println("    |_____________________________________|");
			}

			// Durch Anpassen der 3. und der 6. Zwischenzeile wird die
			// optische Abgrenzung der Kästchen dargestellt
			if (i == 2 || i == 5) {
				System.out.println("    | -----------+-----------+----------- |");
			}
			// verbleibende Zeilen
			if (i < 8 & i != 2 & i != 5) {
				System.out.println("    |            |           |            |");
			}
		}
	}
}