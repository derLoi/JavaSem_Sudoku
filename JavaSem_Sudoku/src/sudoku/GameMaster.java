package sudoku;

import java.util.Random;
import java.util.Scanner;
/*
 * @author: LS 
 * @version: 11/12/2015
 * @changelog:
 * 		MW: Abfrage der Schwierigkeitsstufe eingebaut
 * 		TM: �berarbeitetungen der Spielfeldanzeige
 * 		LS: Test mit G�ltigkeitspr�fung f�r zuf�llig generiertes Sudoku. Ausgabe Ergebnisse
 * 		LS: Methoden fillRndVal(), insertFixVal() und firstEmptyCell() hinzugef�gt
 * @desc: kontrolliert den Spielablauf.
 */

public class GameMaster {
	// Array, in dem alle Zellen-Objects des Sudokus gespeichert werden
	private static Cells[][] sudokuCells = new Cells[9][9];

	public static void main(String[] args) {

		hauptmenu();

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

	public static void hauptmenu() {
		int inpInt;
		String inpString;
		System.out.println("Willkommen bei Sudokufy - von LTML");

		System.out.println();

		// Hauptmen�

		System.out.println("Bitte w�hle eine der folgenden Optionen indem du die entsprechende Zahl eingibst:");
		System.out.println("1. Sudoku spielen \n" + "2. Sudoku l�sen lassen \n" + "3. Wie spielt man Sudoku? \n"
				+ "4. Sudokufy beenden");

		Scanner scanner = new Scanner(System.in);

		inpInt = scanner.nextInt(); // nextInt() bewirkt keinen
									// Zeilenumbruch, sodass der Scanner
									// immer noch in derselben Zeile bleibt.
									// Deswegen nextLine(), um input
									// skipping zu verhindern
		inpString = scanner.nextLine();

		switch (inpInt) {
		case 1:
			System.out.println("Alles klar, ein neues Spiel! \n \n"
					+ "Bitte w�hle deine Schwierigkeitsstufe: Sehr leicht, leicht, mittel, schwer, schwer des todes");

			inpString = scanner.nextLine();

			if (inpString.equalsIgnoreCase("Sehr leicht")) {
				System.out.println("Ausgew�hlt: sehr leicht");
			} else if (inpString.equalsIgnoreCase("leicht")) {
				System.out.println("leicht");
			} else if (inpString.equalsIgnoreCase("mittel")) {
				System.out.println("mittel");
			} else if (inpString.equalsIgnoreCase("schwer")) {
				System.out.println("schwer");
			} else if (inpString.equalsIgnoreCase("schwer des todes")) {
				System.out.println("schwer des todes");
			}
			break;
		case 2:
			System.out.println("Hallo, wir sind die Cantina Band. Wenn ihr Songw�nsche habt, ruft sie einfach!");
			// TODO: Methodenaufruf Eingabe eigenes Sudoku
			break;
		case 3:
			/*
			 * Auslagern in eigene Methode: System.out.println(
			 * "Erkl�rung des Spiels und des Hauptmen�s: \n \n");
			 * 
			 * System.out.println(
			 * "Indem du 0 eingibst, kehrst du ins Hauptmen� zur�ck, mit 4 beendest du Sudokufy"
			 * );
			 * 
			 * inpInt = scanner.nextInt(); inpString = scanner.nextLine();
			 * 
			 * switch (inpInt) { case 0: break; case 4: exit(); break; }
			 */
			break;
		case 4:
			exit();
			break;
		default:
			System.out.println("Ung�ltige Eingabe.");
			break;
		}
		System.out.println("Ok..dann los!");
	}

/**
 * 
 */
	public static void genSudoku() {
		// Hilfsvariablen
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

	/**
	 * F�gt in der ersten Zeile des Sudoku Spielfelds die Zahlen 1 - 9 zuf�llig
	 * ein.
	 */
	public static void fillRndVal() {
		// Ein neues Object der Random Class erstellen
		Random rnd = new Random(); // neues Random-Object erstellen
		// Hilfsvariablen erstellen
		int num;
		boolean bln;
		// i := Zellen in Zeile
		for (int i = 0; i <= 8; i++) {
			do {
				num = rnd.nextInt(9) + 1; // neue Zufallszahl zw. 1 - 9 erzeugen
				bln = true; // Fehler-Indikator zur�cksetzen
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

	/**
	 * Setzt bei dem durch die X-Y-Koordinaten gew�hlten Class Object das
	 * Attribut fixVal auf true. Speichert den �bergebenen Wert im Attribut
	 * value und l�scht das Object aus der linked list
	 * 
	 * @param sudokuCells
	 *            9x9-Array mit einem Cells-Objects in jedem Feld
	 * @param x
	 *            X-Index des Class-Objects im Sudoku Spielfeld
	 * @param y
	 *            Y-Index des Class-Objects im Sudoku Spielfeld
	 * @param value
	 *            Wert des Class-Objects: Startwert f�r ein Feld im Sudoku
	 *            Spielfeld
	 */
	public static void insertFixVal(Cells[][] sudokuCells, int x, int y, int value) {
		// Startwert speichern
		sudokuCells[y][x].setValue(value);
		// Attribut fixVal zu true �ndern
		sudokuCells[y][x].setFixVal(true);
		/*
		 * Das aktuelle Object aus der linked list l�schen, indem die Verweise
		 * der vorherigen und n�chsten Zelle verbogen werden. Bsp: Zelle A - B -
		 * C (B l�schen) a) die n�chste Zelle f�r Zelle A sei Zelle C b) die
		 * vorherige Zelle f�r Zelle C sei Zelle A Somit wird B beim springen
		 * durch die Zellen �bersprungen
		 */
		sudokuCells[y][x].getLastCell().setNextCell(sudokuCells[y][x].getNextCell());
		sudokuCells[y][x].getNextCell().setLastCell(sudokuCells[y][x].getLastCell());
	}

	/**
	 * Findet ausgehend von der �bergebenen Zelle, die erste Zelle, die keinen
	 * unver�nderlichen Startwert (Attribut: fixVal) enth�lt.
	 * 
	 * @param currentCell
	 *            die aktuelle Zelle vom Typ Cells, von der ausgehend gesucht
	 *            werden soll
	 * @return Gibt die erste Zelle zur�ck, die keinen Startwert enth�lt
	 */
	public static Cells firstEmptyCell(Cells currentCell) {
		// �berpr�fe, ob aktuelle Zelle einen Startwert enth�lt (Attribut fixVal
		// = true)
		while (currentCell.isFixVal()) {
			// Gehe einen Schritt weiter in der linked list
			currentCell = currentCell.getNextCell();
		}
		// �bergebe gefundene Zelle
		return currentCell;
	}

	/**
	 * Zeichnet ein Sudoku Spielfeld. In der ersten Zeile und der ersten Spalte
	 * werden die Koordinaten 1 - 9 und A - I dargestellt. Die dargestellten
	 * Werte werden aus den Cells Objects in den Feldern des Arrays sudokuCells
	 * ausgelesen.
	 */
	public static void drawBoard() {
		String[] abc = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };
		// i := Zeilen
		for (int i = 0; i <= 8; i++) {
			// erste Zeile Kopfbereich zeichnen
			if (i == 0) {
				System.out.println("");
				// Koordinaten 1 - 9 im Kopf
				System.out.println("       1   2   3   4   5   6   7   8   9");
				System.out.println("     _____________________________________ ");
				System.out.println("    |                                     |");
			}
			// j := Spalten in Zeile i
			for (int j = 0; j <= 8; j++) {
				if (j == 0) {
					/*
					 * Erste Zelle in aktueller Zeile mit Koordinate A - I und
					 * f�hrendem Verkettungszeichen (senkrechter Strich)
					 */
					System.out.print(" " + abc[i] + "  |  " + sudokuCells[i][j].getValue() + "  ");
				} else if (j == 8) {
					/*
					 * letzte Zelle in aktueller Zeile mit abschlie�endem
					 * Verkettungszeichen
					 */
					System.out.print(" " + sudokuCells[i][j].getValue() + "  |");
					// Zeilenumbruch
					System.out.println("");
				} else if (j == 2 || j == 5) {
					/*
					 * Damit die K�stchen klar erkennbar sind, wird jeweils nach
					 * dem 3. und dem 6. Zahlenfeld ein Rahmen angezeigt.
					 */
					System.out.print(" " + sudokuCells[i][j].getValue() + " |");
				} else {
					/*
					 * Die �brigen Zellen werden mit Leerzeichen als
					 * abstandshalter zwischen den Zahlen dargestellt.
					 */
					System.out.print(" " + sudokuCells[i][j].getValue() + "  ");
				}
			}
			// letzte Zeile mit abschlie�endem Rahmen
			if (i == 8) {
				System.out.println("    |_____________________________________|");
			}
			/*
			 * Durch einen Rahmen nach der 3. und der 6. Zeile wird die optische
			 * Abgrenzung der 3x3-Boxen dargestellt.
			 */
			if (i == 2 || i == 5) {
				System.out.println("    | -----------+-----------+----------- |");
			}
			/*
			 * In den verbleibenden Zeilen wird zwischen den Zeilen mit den
			 * Werten, eine leere Zeile als Abstandhalter eingef�gt. Die
			 * Verkettungszeichen stellen die 3x3-Boxen dar.
			 */
			if (i < 8 && i != 2 && i != 5) {
				System.out.println("    |            |           |            |");
			}
		}
	}
	/**
	 * wann immer das Spiel beendet werden soll, reicht jetzt exit();
	 */
	private static void exit() {
		System.out.println("Tsch���");
		System.exit(1);
	}
}