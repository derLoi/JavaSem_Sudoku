package sudoku;

/**
 * Löst ein gegebenes Sudoku mit Hilfe eines brute force Algorithmus.
 * 
 * @version 17/01/2016
 */
public class SudokuSolver {
	// SudokuChecker Object initialisieren
	SudokuChecker check = new SudokuChecker();

	/**
	 * Der Algorithmus versucht durch zufälliges einsetzen der Zahlen 1 - 9
	 * unter den Rahmenbedingungen des Sudokuregelwerkes eine Lösung für ein
	 * Sudokurätsel zu finden. Dabei werden die Zellen nacheinander abgearbeitet
	 * und bei einem Fehler in umgekehrter Reihenfolge korrigiert.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param currentCell
	 *            die betrachtete Zelle.
	 */
	public void solveSudoku(Cells[][] sudokuCells, Cells currentCell) {
		// Zählvariable steps initialisieren
		int steps;
		// steps zurücksetzen
		steps = 0;
		// prüfen ob Sudoku gelöst ist. Wenn nicht Schleife ausführen
		while (!check.sudokuIsSolved(sudokuCells)) {
			/*
			 * prüfen ob in der Zelle ein ungültiger Wert gespeichert ist oder
			 * ob kein Wert vorhanden ist (0)
			 */
			if (!check.valIsAllowed(sudokuCells, currentCell, currentCell.getValue()) || currentCell.getValue() == 0) {
				// alle möglichen Werte für die Zelle ermitteln
				check.findPosVals(sudokuCells, currentCell);
				// prüfen ob mögliche Werte gefunden wurden
				if (currentCell.posVal.isEmpty()) {
					/*
					 * Backtracking wenn keine Werte gefunden wurden.
					 * Lösungswert der Zelle leeren (0)
					 */
					currentCell.setValue(0);
					// prüfen ob für die Zelle ausgeschlossene Werte existieren
					if (!currentCell.excVal.isEmpty()) {
						// wenn ausgeschlossene Werte existieren, lösche diesee
						currentCell.excVal.clear();
					}
					/*
					 * füge zur letzen Zelle den Lösungswert der letzten Zelle
					 * zur Liste der ausgeschlossenen Werte hinzu
					 */
					currentCell.getLastCell().addValueToExcVals(currentCell.getLastCell().getValue());
					/*
					 * mache einen Schritt zurück: neue betrachtete Zelle ist
					 * die letzte Zelle
					 */
					currentCell = currentCell.getLastCell();
				} else if (!currentCell.posVal.isEmpty()) {
					/*
					 * wurden mögliche Werte gefunden, wähle einen Wert als
					 * Lösungswert
					 */
					currentCell.pickValueFromList();
					/*
					 * mache einen Schritt vorwärts: neue betrachtete Zelle ist
					 * die nächste Zelle
					 */
					currentCell = currentCell.getNextCell();
				}
			} else {
				// setze den Lösungswert der aktuellen Zelle zurück
				currentCell.setValue(0);
			}
			// incrementiere die Zählvariable
			steps++;
		}
		// Nutzer-Feedback: wie schnell konnte das Sudoku gelöst werden
		System.out.println("Solved: it took me " + steps + " iterations to generate this sudoku for you!");
	}
}
