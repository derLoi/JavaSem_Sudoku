package sudoku;

/*
 * @author: LS 
 * @version: 11/12/2015
 * @changelog:
 * 		TM: Überarbeitetungen der Spielfeldanzeige
 * 		LS: Test mit Gültigkeitsprüfung für zufällig generiertes Sudoku. Ausgabe Ergebnisse
 * @desc: kontrolliert den Spielablauf.
 */
public class GameMaster {

	public static void main(String[] args) {
		
		System.out.println("Willkommen bei Sudokufy - von LTML");
		
		Sudoku sudoku = new Sudoku();
		SudokuChecker check = new SudokuChecker();

		sudoku.genSudoku();

		drawBoard(sudoku.getSudoku());

		// Test Reihen und Spalten prüfen
		// Um mehr Zahlen im Sudoku zu generieren, muss das obere Limit für j in der genSudoku-Methode angepasst werden
		check.checkSudoku(sudoku.getSudoku());
		for (int i = 0; i <= 8; i++) {
			System.out.println("Reihe " + (i+1) + ": " + check.getErrInRow()[i] + " | Spalte " + (i+1) + ": " + check.getErrInCol()[i] + " | Box "+ (i+1)+ ": "+ check.getErrInBox()[i]);
		}

		/*
		 * // Test für Änderungen in setSudoku 
		 * sudoku.setSudoku(9, 'I', 0);
		 * drawBoard(sudoku.getSudoku());
		 */
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
	 * 
                  A   B   C   D   E   F   G   H   I
                _____________________________________ 
               |                                     |
            1  |  4   5   4 | 2   4   3 | 6   7   1  |
               |            |           |            |
            2  |  5   6   8 | 2   4   8 | 4   5   2  |
               |            |           |            |
            3  |  5   7   8 | 4   2   6 | 8   5   7  |
               | -----------+-----------+----------- |
            4  |  4   6   1 | 3   8   1 | 8   6   3  |
               |            |           |            |
            5  |  7   8   5 | 8   8   2 | 3   5   3  |
               |            |           |            |
            6  |  1   7   5 | 3   6   8 | 5   7   7  |
               | -----------+-----------+----------- |
            7  |  3   6   3 | 3   3   5 | 4   8   5  |
               |            |           |            |
            8  |  2   1   4 | 4   8   6 | 8   4   8  |
               |            |           |            |
            9  |  4   5   7 | 7   8   3 | 8   5   7  |
               |_____________________________________|
	 */
	public static void drawBoard(int[][] arySudoku) {
		String[] abc = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
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
					System.out.print(" " + abc[i] + "  |  " + arySudoku[i][j] + "  ");

					// Mit +(i+1) lassen wir uns die jeweilige Zeile anzeigen,
					// um das Spielen zu erleichtern.
					// letzte Zelle in Zeile i mit abschließendem Rahmen &
					// Zeilenumbruch
				} else if (j == 8) {
					System.out.print(" " + arySudoku[i][j] + "  |");
					System.out.println("");

					// Damit die Kästchen klar erkennbar sind, wird
					// jeweils nach dem 3. und dem 6. Zahlenfeld "!" durch "|"
					// ersetzt.
				} else if (j == 2 || j == 5) {
					System.out.print(" " + arySudoku[i][j] + " |");
					// verbleibende Zellen
				} else {
					System.out.print(" " + arySudoku[i][j] + "  ");
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