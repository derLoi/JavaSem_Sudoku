package sudoku;

import java.util.Arrays;

/**
 * Diese Klasse enthält Methoden, die überprüfen ob Zahlen nach dem Sudokuregelwerk in
 * beliebigen Zellen erlaubt sind, welche möglich sind und ob das Sudoku bereits gelöst ist.
 * 
 * @version 25/01/16
 *
 */

public class SudokuChecker {
	
	/**
	 * Gibt die Reihe der Lösungswerte als Array zurück, in der die Zelle liegt.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param y
	 *            der y-Index der Zelle.
	 * @return das Array mit allen Lösungswerten der Reihe.
	 */
	public int[] getCurrentRow(Cells[][] sudokuCells, int y) {
		// Array, in dem alle Lösungswerte der Reihe gespeichert werden
		int[] row = new int[9];
		// i := Spalten x in der Reihe y
		for (int i = 0; i <= 8; i++) {
			// Lösungswerte der Zellen aus Sudoku-Array in row speichern
			row[i] = sudokuCells[y][i].getValue();
		}
		// Rückgabe der Zeile
		return row;
	}

	/**
	 * Gibt die Spalte der Lösungswerte als Array zurück, in der die Zelle
	 * liegt.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param x
	 *            der x-Index der Zelle.
	 * @return das Array mit allen Lösungswerten der Spalte.
	 */
	public int[] getCurrentCol(Cells[][] sudokuCells, int x) {
		// Array, in dem alle Lösungswerte der Spalte gespeichert werden
		int[] col = new int[9];
		// i := Zeilen y in der Spalte x
		for (int i = 0; i <= 8; i++) {
			// Lösungswerte der Zellen aus Sudoku-Array in col speichern
			col[i] = sudokuCells[i][x].getValue();
		}
		// Rückgabe der Spalte
		return col;
	}

	/**
	 * Gibt die Box der Lösungswerte als Array zurück, in der die Zelle liegt.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param x
	 *            der x-Index der Zelle.
	 * @param y
	 *            der y-Index der Zelle.
	 * @return das Array mit allen Lösungswerten der Box.
	 */
	public int[] getCurrentBox(Cells[][] sudokuCells, int x, int y) {
		// Hilfsvariable zum Zählen
		int temp = 0;
		// Array, in dem alle Lösungswerte der Box gespeichert werden
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
				// Lösungswerte der Zellen aus Sudoku-Array in box speichern
				box[temp] = sudokuCells[i][j].getValue();
				// Zählvariable temp incrementieren
				temp++;
			}
		}
		// Rückgabe der box
		return box;
	}

	/**
	 * Prüft unter Berücksichtigung der Reihe/Spalte/Box/excVal, ob ein
	 * übergebener Wert als Lösungswert in der Zelle eingesetzt werden darf.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param currentCell
	 *            die betrachtete Zelle.
	 * @param value
	 *            der Wert, der als Lösungswert eingesetzt werden soll.
	 * @return true wenn der Wert erlaubt ist, false wenn nicht.
	 */
	public Boolean valIsAllowed(Cells[][] sudokuCells, Cells currentCell, int value) {
		/*
		 *  UND-Verkettung der Ergebnisse der Prüfung der Zeile/Reihe/Box und ausgeschlossenen Werte
		 */
		return checkNum(getCurrentRow(sudokuCells, currentCell.getY()), value)
				&& checkNum(getCurrentCol(sudokuCells, currentCell.getX()), value)
				&& checkNum(getCurrentBox(sudokuCells, currentCell.getX(), currentCell.getY()), value)
				&& checkNum(currentCell.listToArray(currentCell.excVal), value);
	}

	/**
	 * Findet alle Werte, die als Lösungswert der Zelle in Frage kommen und
	 * speichert diese in der Liste posVal.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param currentCell
	 *            die betrachtete Zelle.
	 */
	public void findPosVals(Cells[][] sudokuCells, Cells currentCell) {
		// Prüfe ob die Liste der möglichen Wert leer ist
		if (!currentCell.posVal.isEmpty()) {
			// Ist die Liste nicht leer, lösche den Inhalt
			currentCell.posVal.clear();
		}
		// Schleife durch die Werte i von 1 bis 9
		for (int i = 1; i <= 9; i++) {
			// prüfe ob der Wert i als Lösungswert in Frage kommt
			if (valIsAllowed(sudokuCells, currentCell, i)) {
				// ist i ein möglicher Lösungswert, füge ihn der Liste der
				// möglichen Werte hinzu
				currentCell.posVal.add(i);
			}
		}
	}

	/**
	 * Prüft ob ein übergebener Wert in dem übergebenen Array bereits vorkommt
	 * oder nicht.
	 * 
	 * @param row
	 *            das Array der Werte einer Zeile/Spalte/Box
	 * @param num
	 *            der geprüfte Wert.
	 * @return true wenn der Wert gültig ist, false wenn nicht.
	 */
	public boolean checkNum(int[] row, int num) {
		// i := Zelle des Arrays. Schleife von [0;länge Array[
		for (int i = 0; i < row.length; i++) {
			// prüfe ob num gleich Wert des Arrays an Index i
			if (row[i] == num) {
				// wenn Werte gleich sind, Rückgabe false, da Wert ungültig
				return false;
			}
		}
		// Rückgabewert: true (keine Fehler)
		return true;
	}

	/**
	 * Prüft ob eine Zeile/Spalte/Box korrekt gelöst wurde. D.h.: Jede Zahl von
	 * 1 bis 9 ist genau einmal vorhanden.
	 * 
	 * @param row
	 *            das Array der Werte einer Zeile/Spalte/Box
	 * @return true wenn der Wert gültig ist, false wenn nicht.
	 */
	public boolean checkNum(int[] row) {
		// Sortiere row aufsteigend
		Arrays.sort(row);
		// Schleife durch Array row von 0 - 8
		for (int i = 0; i < 8; i++) {
			/*
			 * Prüfe ob i+1 (entspricht den Zahlen 1 bis 9) ungleich dem Wert
			 * mit Index i im Array row ist, oder dieser 0 ist
			 */
			if (row[i] != (i + 1) || row[i] == 0) {
				/*
				 * Wenn bei Index i im Array row ein Wert steht, der
				 * größer/kleiner ist als i oder 0, so muss ein Wert fehlen und
				 * die Zeile/Spalte/Box ist nicht gelöst und es wird false
				 * zurückgegeben
				 */
				return false;
			}
		}
		// Sind alle Werte richtig, ist die Reihe/Spalte/Box gelöst
		return true;
	}

	/**
	 * Prüft ob das Sudoku bereits gelöst wurde.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @return true wenn das Sudoku gelöst ist, false wenn nicht.
	 */
	public boolean sudokuIsSolved(Cells[][] sudokuCells) {
		// Schleife durch alle Indizes der Zeilen und Spalten
		for (int i = 0; i <= 8; i++) {
			// Prüfe ob die Zeile/Spalte gelöst ist
			if (!checkNum(getCurrentRow(sudokuCells, i)) || !checkNum(getCurrentCol(sudokuCells, i))) {
				/*
				 * ist eine Zeile oder Spalte nicht gültig, wird false
				 * zurückgegeben
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
				// prüfe ob die Box gelöst ist
				if (!checkNum(getCurrentBox(sudokuCells, j, i))) {
					// ist die Box ungültig, gebe false zurück
					return false;
				}
			}
		}
		// Sind alle Zeilen/Spalten/Boxen gelöst, ist das Sudoku fertig
		return true;
	}

}