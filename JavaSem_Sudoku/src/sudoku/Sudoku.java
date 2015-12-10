package sudoku;

import java.util.Random;

/*
 * @author: LS
 * @version: 08/12/2015
 * @changelog:
 	LS - Methode setSudoku angepasst
 * @desc: Object, welches die Informationen zum aktuell gespielten Sudoku enthält.
 */

public class Sudoku {
	/*
	 * @desc: Lösungsfeld (aktueller Stand) als 2-dimmensionales Integer-Array
	 */
	private int[][] arySudoku = new int[9][9];

	/*
	 * @author: LS
	 * 
	 * @version: 07/12/2015
	 * 
	 * @changelog:
	 * 
	 * @return: 2-dimensionales Integer-Array
	 * 
	 * @desc: get-Methode für arySudoku
	 */
	public int[][] getSudoku() {
		return arySudoku;
	}

	/*
	 * @author: LS
	 * 
	 * @version: 07/12/2015
	 * 
	 * @changelog:
	 * 
	 * @param: Integer x bestimmt Zeile der Änderung
	 * 
	 * @param: Char c bestimmt Spalte der Änderung
	 * 
	 * @param: Integer value neuer Wert für Zelle x/y
	 * 
	 * @desc: set-Methode für Position x/y in arySudoku
	 */
	public void setSudoku(int x, char c, int value) {
		// Konvertiere Char c in Integer (A = 65 in UTF-16, B = 66, usw.)
		// y Wert in Array berechnen durch c - 65 (bspw. c = "A" -> y = 65 - 65 = 0)
		int y = c - 65;
		// Decrement x (Array-Lowerbound ist 0, eingegebene Koordinaten beginnend bei 1)
		--x;
		arySudoku[y][x] = value;
	}

	/*
	 * @author:LS
	 * 
	 * @version: 07/12/2015
	 * 
	 * @changelog:
	 * 
	 * @desc: Generiert beispielhaft 9x9 Matrix mit Zufallszahlen von 1-9
	 */
	public void genSudoku() {
		// neues Ranom Object erstellen
		Random rnd = new Random();
		// i := Zeilen
		for (int i = 0; i <= 8; i++) {
			// j := Spalten in Zeile i
			for (int j = 0; j <= 8; j++) {
				// Zufallszahl von 1 - 9 in 9x9 Matrix arySudoku schreiben
				arySudoku[i][j] = rnd.nextInt(8) + 1;
			}

		}
	}

}