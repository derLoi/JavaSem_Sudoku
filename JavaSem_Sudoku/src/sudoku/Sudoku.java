package sudoku;

import java.util.Random;

/*
 * @author: LS
 * @version: 08/12/2015
 * @changelog:
 *	LS - Methode setSudoku angepasst
 * @desc: Object, welches die Informationen zum aktuell gespielten Sudoku enth�lt.
 */

public class Sudoku {
	/*
	 * @desc: L�sungsfeld (aktueller Stand) als 2-dimmensionales Integer-Array
	 */
	private int[][] arySudoku = new int[9][9];
	private int[][] aryFixSudoku = new int[9][9];	// enth�lt gegebene Zahlen des Sudokus, die der Spieler nicht ver�ndern kann.
	
	public int[][] getFixSudoku(){
		return aryFixSudoku;
	}
	
	/*
	 * @author: LS
	 * 
	 * @version: 07/12/2015
	 * 
	 * @changelog:
	 * 
	 * @return: 2-dimensionales Integer-Array
	 * 
	 * @desc: get-Methode f�r arySudoku
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
	 * @param: Integer x bestimmt Zeile der �nderung
	 * 
	 * @param: Char c bestimmt Spalte der �nderung
	 * 
	 * @param: Integer value neuer Wert f�r Zelle x/y
	 * 
	 * @desc: set-Methode f�r Position x/y in arySudoku
	 */
	public void setSudokuABC(int x, char c, int value) {
		// Konvertiere Char c in Integer (A = 65 in UTF-16, B = 66, usw.)
		// y Wert in Array berechnen durch c - 65 (bspw. c = "A" -> y = 65 - 65 = 0)
		int y = c - 65;
		// Decrement x (Array-Lowerbound ist 0, eingegebene Koordinaten beginnend bei 1)
		--x;
		arySudoku[y][x] = value;
	}

	public void setSudoku(int y, int x, int value) {
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
		int num;
		boolean bln;
		// i := Zeilen
		for (int i = 0; i <= 8; i++) {
			do {
				num = rnd.nextInt(9) + 1;	// neue Zufallszahl zw. 1 - 9 erzeugen
				bln = true;					// Fehler-Indikator zur�cksetzen
				for (int j = 0; j <= i; j++) {
					if (num == arySudoku[0][j]) {
						bln = false;
						break;
					}
				}
			} while (bln == false);
			arySudoku[0][i] = num;
			aryFixSudoku[0][i] = num;
		}
	} 
}