package sudoku;

import java.util.*;

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
			if (steps > 10000000) {
				System.out.println("Nä! Dat dauert mir zulang. Verfatz disch!");
				break;
			}
		}
		// Nutzer-Feedback: wie schnell konnte das Sudoku gelöst werden
		System.out.println("Solved: it took me " + steps + " iterations to solve this sudoku puzzle for you!");
		Cells lastCell = sudokuCells[8][8];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				currentCell = sudokuCells[i][j];
				if (!currentCell.excVal.isEmpty())
					currentCell.excVal.clear();
				currentCell.setFixVal(false);
				currentCell.setLastCell(lastCell);
				currentCell.getLastCell().setNextCell(currentCell);
			}
		}
		currentCell.setNextCell(sudokuCells[0][0]);
	}

	public void digHoles(Cells[][] sudokuCells, int min, int max) {
		Random rnd = new Random();
		int rndY;
		int rndX;
		Cells currentCell;
		// Anzahl Zellen, die ausgeschnitten werden
		int rndNum = rnd.nextInt((max - min) + 1) + min;
		// Zellen ausschneiden
		while (rndNum > 0) {
			rndY = rnd.nextInt(9);
			rndX = rnd.nextInt(9);
			currentCell = sudokuCells[rndY][rndX];
			if (currentCell.getValue() != 0) {
				// Wert 0 = Zelle leer
				currentCell.setValue(0);
				// rndNum decrement
				rndNum--;
			}
		}
		resetLinkedList(sudokuCells);
	}

	public void resetLinkedList(Cells[][] sudokuCells) {
		Cells lastCell;
		Cells currentCell;
		/*
		 * Linked List zurücksetzen. Speichere letzte Zelle als erste
		 * "letzte Zelle"
		 */
		lastCell = sudokuCells[8][8];
		// Schleife durch das Sudoku Spielfeld Array
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				/*
				 * erzeuge die Linked List indem die letzte Zelle mit der
				 * betrachteten Zelle verbunden werden
				 */
				currentCell = sudokuCells[i][j];
				currentCell.setLastCell(lastCell);
				lastCell.setNextCell(currentCell);
				// speichere die betrachtete Zelle als neue letzte Zelle
				lastCell = currentCell;
			}
		}
		// verknüpfe die letzte Zelle mit der ersten Zelle
		sudokuCells[8][8].setNextCell(sudokuCells[0][0]);

		/* Linked List neu verknüpfen */
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				currentCell = sudokuCells[i][j];
				if (currentCell.getValue() != 0) {
					// Wert ist Startwert = fix
					currentCell.setFixVal(true);
					// Linked List anpassen
					currentCell.getLastCell().setNextCell(currentCell.getNextCell());
					currentCell.getNextCell().setLastCell(currentCell.getLastCell());
				} else if (currentCell.getValue() == 0) {
					currentCell.setFixVal(false);
				}
			}
		}
	}
}
