package sudoku;

import java.util.Random;

/**
 * L�st ein gegebenes Sudoku mit Hilfe eines Backtracking Algorithmus.
 * 
 * @version 24/01/2016
 */
public class SudokuSolver {
	// SudokuChecker Object initialisieren
	SudokuChecker check = new SudokuChecker();

	/**
	 * Der Algorithmus versucht durch zuf�lliges einsetzen der Zahlen 1 - 9
	 * unter den Rahmenbedingungen des Sudokuregelwerkes eine L�sung f�r ein
	 * Sudokur�tsel zu finden. Dabei werden die Zellen nacheinander abgearbeitet
	 * und bei einem Fehler in umgekehrter Reihenfolge korrigiert.
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param currentCell
	 *            die betrachtete Zelle.
	 */
	public void solveSudoku(Cells[][] sudokuCells, Cells currentCell) {
		// Z�hlvariable steps initialisieren
		int steps;
		// steps zur�cksetzen
		steps = 0;
		// pr�fen ob Sudoku gel�st ist. Wenn nicht Schleife ausf�hren
		while (!check.sudokuIsSolved(sudokuCells)) {
			/*
			 * pr�fen ob in der Zelle ein ung�ltiger Wert gespeichert ist oder
			 * ob kein Wert vorhanden ist (0)
			 */
			if (!check.valIsAllowed(sudokuCells, currentCell, currentCell.getValue()) || currentCell.getValue() == 0) {
				// alle m�glichen Werte f�r die Zelle ermitteln
				check.findPosVals(sudokuCells, currentCell);
				// pr�fen ob m�gliche Werte gefunden wurden
				if (currentCell.posVal.isEmpty()) {
					/*
					 * Backtracking wenn keine Werte gefunden wurden.
					 * L�sungswert der Zelle leeren (0)
					 */
					currentCell.setValue(0);
					// pr�fen ob f�r die Zelle ausgeschlossene Werte existieren
					if (!currentCell.excVal.isEmpty()) {
						// wenn ausgeschlossene Werte existieren, l�sche diesee
						currentCell.excVal.clear();
					}
					/*
					 * f�ge zur letzen Zelle den L�sungswert der letzten Zelle
					 * zur Liste der ausgeschlossenen Werte hinzu
					 */
					currentCell.getLastCell().addValueToExcVals(currentCell.getLastCell().getValue());
					/*
					 * mache einen Schritt zur�ck: neue betrachtete Zelle ist
					 * die letzte Zelle
					 */
					currentCell = currentCell.getLastCell();
				} else if (!currentCell.posVal.isEmpty()) {
					/*
					 * wurden m�gliche Werte gefunden, w�hle einen Wert als
					 * L�sungswert
					 */
					currentCell.pickValueFromList();
					/*
					 * mache einen Schritt vorw�rts: neue betrachtete Zelle ist
					 * die n�chste Zelle
					 */
					currentCell = currentCell.getNextCell();
				}
			} else {
				// setze den L�sungswert der aktuellen Zelle zur�ck
				currentCell.setValue(0);
			}
			// incrementiere die Z�hlvariable
			steps++;
			if (steps > 10000000) {
				System.out.println("N�! Dat dauert mir zu lang. Ich brech ab.");
				break;
			}
		}
		resetLinkedList(sudokuCells, true);
	}

	/**
	 * Methode 'gr�bt L�cher' in das komplett gel�ste Sudoku, die Anzahl der
	 * freien Stellen wird zuf�llig, je nach Schwierigkeitslevel, bestimmt und
	 * betroffene Stellen in eine 0 umgewandlt
	 * 
	 * @param sudokuCells
	 *            das Cells-Array des Sudoku Spielfelds.
	 * @param min
	 *            minimalte Anzahl an freien Zellen im Sudoku
	 * @param max
	 *            maximalte Anzahl an freien Zellen im Sudoku
	 */
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
				// dekrementiere ndNum
				rndNum--;
			}
		}
		// Verkn�pfe Linked List neu
		resetLinkedList(sudokuCells, false);
	}

	/**
	 * Die Methode resetLinkedList verkn�pft die Linked List der Cells Objects
	 * neu. Dazu werden zuerst alle Zellen als Kette verkn�pft und dann durch
	 * fixieren der Startwerte und beugen der Verlinkung eine Linked List aller
	 * leeren Zellen erstellt.
	 * 
	 * @param sudokuCells
	 *            ein zweidimensionale Cells Array
	 */
	public void resetLinkedList(Cells[][] sudokuCells, boolean param) {
		// Cells Objects
		Cells lastCell;
		Cells currentCell;
		/*
		 * Linked List zur�cksetzen. Speichere letzte Zelle als erste
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
		// verkn�pfe die letzte Zelle mit der ersten Zelle
		sudokuCells[8][8].setNextCell(sudokuCells[0][0]);
		// Pr�fe param
		if (param) {
			// Schleife durch das Sudoku Spielfeld Array
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					currentCell = sudokuCells[i][j];
					// Pr�fe ob Liste excVal leer ist
					if (!currentCell.excVal.isEmpty()) {
						// l�sche Liste excVal
						currentCell.excVal.clear();
					}
					// Aktueller Wert kein Startwert
					currentCell.setFixVal(false);
				}
			}
		} else if (!param) {
			// Linked List neu verkn�pfen
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
}
