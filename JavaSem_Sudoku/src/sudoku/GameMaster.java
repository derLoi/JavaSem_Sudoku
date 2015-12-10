package sudoku;

/*
 * @author: LS 
 * @version: 08/12/2015
 * @changelog:
 * 		TM: Überarbeitetungen der Spielfeldanzeige
 * @desc: kontrolliert den Spielablauf.
 */
public class GameMaster {

	public static void main(String[] args) {
		Sudoku sudoku = new Sudoku();
		SudokuChecker check = new SudokuChecker();

		sudoku.genSudoku();

		drawBoard(sudoku.getSudoku());

		//Test Reihen und Spalten prüfen
		check.checkSudoku(sudoku.getSudoku());
		
		
		/*
		// Test für Änderungen in setSudoku
		sudoku.setSudoku(9, 'I', 0);
		drawBoard(sudoku.getSudoku());
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
			   A   B   C   D   E   F   G   H   I
		     _____________________________________ 
		    |                                     |
		 1  |  4 ! 7 ! 8 | 5 ! 7 ! 3 | 1 ! 1 ! 8  |
		    | ---+---+---|---+---+---|---+---+--- |
		 2  |  1 ! 1 ! 5 | 3 ! 8 ! 4 | 3 ! 1 ! 1  |
		    | ---+---+---|---+---+---|---+---+--- |
		 3  |  5 ! 7 ! 8 | 5 ! 8 ! 5 | 2 ! 3 ! 3  |
		    | ===========|===========|=========== |
		 4  |  2 ! 4 ! 2 | 3 ! 1 ! 2 | 3 ! 7 ! 8  |
		    | ---+---+---|---+---+---|---+---+--- |
		 5  |  8 ! 5 ! 8 | 8 ! 3 ! 4 | 3 ! 7 ! 4  |
		    | ---+---+---|---+---+---|---+---+--- |
		 6  |  6 ! 2 ! 8 | 6 ! 2 ! 3 | 1 ! 7 ! 1  |
		    | ===========|===========|=========== |
		 7  |  1 ! 5 ! 1 | 6 ! 2 ! 7 | 8 ! 5 ! 2  |
		    | ---+---+---|---+---+---|---+---+--- |
		 8  |  7 ! 2 ! 3 | 7 ! 5 ! 7 | 1 ! 3 ! 6  |
		    | ---+---+---|---+---+---|---+---+--- |
		 9  |  7 ! 7 ! 3 | 5 ! 3 ! 8 | 3 ! 7 ! 1  |
		    |_____________________________________| 
	 */
	public static void drawBoard(int[][] arySudoku) {
		// i := Zeilen
		for (int i = 0; i <= 8; i++) {
			// erste Zeile Kopfbereich zeichnen
			if (i == 0) {
				System.out.println("");
				// Koordinaten A - I im Kopf
				System.out.println("       A   B   C   D   E   F   G   H   I");
				System.out.println("     _____________________________________ ");
				System.out.println("    |                                     |");
			}
			// j := Spalten in Zeile i
			for (int j = 0; j <= 8; j++) {
				// erste Zelle in Zeile i mit Koordinaten 1 - 9
				if (j == 0) {
					System.out.print(" " + (i + 1) + "  |  " + arySudoku[i][j] + " !");

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
					System.out.print(" " + arySudoku[i][j] + " !");
				}
			}
			// letzte Zeile i mit abschließendem Rahmen
			if (i == 8) {
				System.out.println("    |_____________________________________|");
			}

			// Durch Anpassen der 3. und der 6. Zwischenzeile wird die
			// optische Abgrenzung der Kästchen dargestellt
			if (i == 2 || i == 5) {
				System.out.println("    | ===========|===========|=========== |");
			}
			// verbleibende Zeilen
			if (i < 8 & i != 2 & i != 5) {
				System.out.println("    | ---+---+---|---+---+---|---+---+--- |");
			}
		}
	}
}