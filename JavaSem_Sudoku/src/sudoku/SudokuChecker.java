package sudoku;

import java.util.Arrays;

/**
 * Diese Klasse enth�lt Methoden, die �berpr�fen ob Zahlen nach dem Sudokuregelwerk in
 * beliebigen Zellen erlaubt sind, welche m�glich sind und ob das Sudoku bereits gel�st ist.
 * 
 * @version 25/01/16
 *
 */

public class SudokuChecker {
	
	/**
	 * Gibt die Reihe der L�sungswerte als Array zur�ck, in der die Zelle liegt.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param y
	 *            der y-Index der Zelle.
	 * @return das Array mit allen L�sungswerten der Reihe.
	 */
	public int[] getCurrentRow(Cells[][] sudokuCells, int y) {
		// Array, in dem alle L�sungswerte der Reihe gespeichert werden
		int[] row = new int[9];
		// i := Spalten x in der Reihe y
		for (int i = 0; i <= 8; i++) {
			// L�sungswerte der Zellen aus Sudoku-Array in row speichern
			row[i] = sudokuCells[y][i].getValue();
		}
		// R�ckgabe der Zeile
		return row;
	}

	/**
	 * Gibt die Spalte der L�sungswerte als Array zur�ck, in der die Zelle
	 * liegt.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param x
	 *            der x-Index der Zelle.
	 * @return das Array mit allen L�sungswerten der Spalte.
	 */
	public int[] getCurrentCol(Cells[][] sudokuCells, int x) {
		// Array, in dem alle L�sungswerte der Spalte gespeichert werden
		int[] col = new int[9];
		// i := Zeilen y in der Spalte x
		for (int i = 0; i <= 8; i++) {
			// L�sungswerte der Zellen aus Sudoku-Array in col speichern
			col[i] = sudokuCells[i][x].getValue();
		}
		// R�ckgabe der Spalte
		return col;
	}

	/**
	 * Gibt die Box der L�sungswerte als Array zur�ck, in der die Zelle liegt.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param x
	 *            der x-Index der Zelle.
	 * @param y
	 *            der y-Index der Zelle.
	 * @return das Array mit allen L�sungswerten der Box.
	 */
	public int[] getCurrentBox(Cells[][] sudokuCells, int x, int y) {
		// Hilfsvariable zum Z�hlen
		int temp = 0;
		// Array, in dem alle L�sungswerte der Box gespeichert werden
		int[] box = new int[9];
		/*
		 * Startkoordinaten der Box ermitteln (bspw. erste Box ->
		 * Startkoordinaten 0/0) Durch Modulo 3 und Multiplikation 3 wird aus
		 * den Koordinaten jeder beliebigen Zelle die Startkoordinaten der Box
		 * ermittelt
		 */
		int startZ = (y / 3) * 3; // Start Zeile
		int startS = (x / 3) * 3; // Start Spalte
		// i:= Zeilen der Box
		for (int i = startZ; i <= (startZ + 2); i++) {
			// j:= Zellen der Box in Zeile i
			for (int j = startS; j <= (startS + 2); j++) {
				// L�sungswerte der Zellen aus Sudoku-Array in box speichern
				box[temp] = sudokuCells[i][j].getValue();
				// Z�hlvariable temp incrementieren
				temp++;
			}
		}
		// R�ckgabe der box
		return box;
	}

	/**
	 * Pr�ft unter Ber�cksichtigung der Reihe/Spalte/Box/excVal, ob ein
	 * �bergebener Wert als L�sungswert in der Zelle eingesetzt werden darf.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param currentCell
	 *            die betrachtete Zelle.
	 * @param value
	 *            der Wert, der als L�sungswert eingesetzt werden soll.
	 * @return true wenn der Wert erlaubt ist, false wenn nicht.
	 */
	public Boolean valIsAllowed(Cells[][] sudokuCells, Cells currentCell, int value) {
		/*
		 *  UND-Verkettung der Ergebnisse der Pr�fung der Zeile/Reihe/Box und ausgeschlossenen Werte
		 */
		return checkNum(getCurrentRow(sudokuCells, currentCell.getY()), value)
				&& checkNum(getCurrentCol(sudokuCells, currentCell.getX()), value)
				&& checkNum(getCurrentBox(sudokuCells, currentCell.getX(), currentCell.getY()), value)
				&& checkNum(currentCell.listToArray(currentCell.excVal), value);
	}

	/**
	 * Findet alle Werte, die als L�sungswert der Zelle in Frage kommen und
	 * speichert diese in der Liste posVal.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param currentCell
	 *            die betrachtete Zelle.
	 */
	public void findPosVals(Cells[][] sudokuCells, Cells currentCell) {
		// Pr�fe ob die Liste der m�glichen Wert leer ist
		if (!currentCell.posVal.isEmpty()) {
			// Ist die Liste nicht leer, l�sche den Inhalt
			currentCell.posVal.clear();
		}
		// Schleife durch die Werte i von 1 bis 9
		for (int i = 1; i <= 9; i++) {
			// pr�fe ob der Wert i als L�sungswert in Frage kommt
			if (valIsAllowed(sudokuCells, currentCell, i)) {
				// ist i ein m�glicher L�sungswert, f�ge ihn der Liste der
				// m�glichen Werte hinzu
				currentCell.posVal.add(i);
			}
		}
	}

	/**
	 * Pr�ft ob ein �bergebener Wert in dem �bergebenen Array bereits vorkommt
	 * oder nicht.
	 * 
	 * @param row
	 *            das Array der Werte einer Zeile/Spalte/Box
	 * @param num
	 *            der gepr�fte Wert.
	 * @return true wenn der Wert g�ltig ist, false wenn nicht.
	 */
	public boolean checkNum(int[] row, int num) {
		// i := Zelle des Arrays. Schleife von [0;l�nge Array[
		for (int i = 0; i < row.length; i++) {
			// pr�fe ob num gleich Wert des Arrays an Index i
			if (row[i] == num) {
				// wenn Werte gleich sind, R�ckgabe false, da Wert ung�ltig
				return false;
			}
		}
		// R�ckgabewert: true (keine Fehler)
		return true;
	}

	/**
	 * Pr�ft ob eine Zeile/Spalte/Box korrekt gel�st wurde. D.h.: Jede Zahl von
	 * 1 bis 9 ist genau einmal vorhanden.
	 * 
	 * @param row
	 *            das Array der Werte einer Zeile/Spalte/Box
	 * @return true wenn der Wert g�ltig ist, false wenn nicht.
	 */
	public boolean checkNum(int[] row) {
		// Sortiere row aufsteigend
		Arrays.sort(row);
		// Schleife durch Array row von 0 - 8
		for (int i = 0; i < 8; i++) {
			/*
			 * Pr�fe ob i+1 (entspricht den Zahlen 1 bis 9) ungleich dem Wert
			 * mit Index i im Array row ist, oder dieser 0 ist
			 */
			if (row[i] != (i + 1) || row[i] == 0) {
				/*
				 * Wenn bei Index i im Array row ein Wert steht, der
				 * gr��er/kleiner ist als i oder 0, so muss ein Wert fehlen und
				 * die Zeile/Spalte/Box ist nicht gel�st und es wird false
				 * zur�ckgegeben
				 */
				return false;
			}
		}
		// Sind alle Werte richtig, ist die Reihe/Spalte/Box gel�st
		return true;
	}

	/**
	 * Pr�ft ob das Sudoku bereits gel�st wurde.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @return true wenn das Sudoku gel�st ist, false wenn nicht.
	 */
	public boolean sudokuIsSolved(Cells[][] sudokuCells) {
		// Schleife durch alle Indizes der Zeilen und Spalten
		for (int i = 0; i <= 8; i++) {
			// Pr�fe ob die Zeile/Spalte gel�st ist
			if (!checkNum(getCurrentRow(sudokuCells, i)) || !checkNum(getCurrentCol(sudokuCells, i))) {
				/*
				 * ist eine Zeile oder Spalte nicht g�ltig, wird false
				 * zur�ckgegeben
				 */
				return false;
			}
		}
		/*
		 * Schleife durch alle Boxen des Sudoku Spielfelds: Indizes werden mit 3
		 * incrementiert um jeweils die Startkoordinaten aller 9 Boxen zu
		 * erhalten.
		 */
		for (int i = 0; i <= 6; i = i + 3) {
			for (int j = 0; j <= 6; j = j + 3) {
				// pr�fe ob die Box gel�st ist
				if (!checkNum(getCurrentBox(sudokuCells, j, i))) {
					// ist die Box ung�ltig, gebe false zur�ck
					return false;
				}
			}
		}
		// Sind alle Zeilen/Spalten/Boxen gel�st, ist das Sudoku fertig
		return true;
	}

}