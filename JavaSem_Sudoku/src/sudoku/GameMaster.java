package sudoku;

import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * kontrolliert den Spielablauf.
 * 
 * @version: 19/01/2016 <br>
 * @changelog <br>
 *            <ul>
 *            <li>MW: Abfrage der Schwierigkeitsstufe eingebaut</li>
 *            <li>TM: Überarbeitungen der Spielfeldanzeige</li>
 *            <li>LS: Test mit Gültigkeitsprüfung für zufällig generiertes
 *            Sudoku. Ausgabe Ergebnisse</li>
 *            <li>LS: Methoden fillRndVal(), insertFixVal() und firstEmptyCell()
 *            hinzugefügt</li>
 *            <li>MW: Interaktive Spielereingabe während des Spiels eingebaut
 *            </li>
 *            <li>TM: Menü und exception handling eingebaut</li>
 *            </ul>
 */

public class GameMaster {
	// Array, in dem alle Zellen-Objects des Sudokus gespeichert werden
	private static Cells[][] sudokuCells = new Cells[9][9];
	private static int minBlankCells;
	private static int maxBlankCells;

	public static void main(String[] args) {
		SudokuSolver solve = new SudokuSolver();
		SudokuChecker check = new SudokuChecker();
		// generate objects in array
		genSudoku();
		// Fill first random numbers
		fillRndVal();
		// solve given sudoku
		solve.solveSudoku(sudokuCells, firstEmptyCell(sudokuCells[0][0]));
		hauptmenu();
		drawBoard();
		while (!check.sudokuIsSolved(sudokuCells)) {
			playerEingabe(sudokuCells);
		}
		System.out.println("Gelöst");
	}

	public static void hauptmenu() {
		int inpInt;
		String inpString;
		System.out.println("\nWillkommen bei Sudokufy - von LTML \n\n"
				+ "Bitte wähle eine der folgenden Optionen indem du die entsprechende Zahl eingibst: \n"
				+ "1. Sudoku spielen \n" + "2. Sudoku lösen lassen \n" + "3. Wie spielt man Sudoku? \n"
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
			schwierigkeitsgrade();
			break;
		case 2:
			System.out.println("Hallo, wir sind die Cantina Band. Wenn ihr Songwünsche habt, ruft sie einfach!");
			// TODO: Methodenaufruf Eingabe eigenes Sudoku
			break;
		case 3:
			erklaerung();
			break;
		case 4:
			scanner.close();
			exit();
			break;
		default:
			System.out.println("Ungültige Eingabe. Bitte wähle eine der vier Optionen.");
			hauptmenu();
			break;
		}
		System.out.println();
		System.out.println("Ok... Dann los!");
	}

	/**
	 * Generiert ein leeres Sudoku Spielfeld indem die benötigten Cells Objects
	 * initialisiert werden und die Linked List verknüpft wird.
	 */
	public static void genSudoku() {
		// Hilfsvariable zum Zwischenspeichern der Objects
		Cells lastCell = null;
		// Schleife durch das Sudoku Spielfeld Array
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				/*
				 * erstelle ein neues Cells Object mit den x/y-Koordinaten der
				 * aktuellen Zelle im sudokuCells Array
				 */
				sudokuCells[i][j] = new Cells(j, i);
			}
		}
		// speichere letzte Zelle als erste "letzte Zelle"
		lastCell = sudokuCells[8][8];
		// Schleife durch das Sudoku Spielfeld Array
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				/*
				 * erzeuge die Linked List indem die letzte Zelle mit der
				 * betrachteten Zelle verbunden werden
				 */
				sudokuCells[i][j].setLastCell(lastCell);
				lastCell.setNextCell(sudokuCells[i][j]);
				// speichere die betrachtete Zelle als neue letzte Zelle
				lastCell = sudokuCells[i][j];
			}
		}
		// verknüpfe die letzte Zelle mit der ersten Zelle
		sudokuCells[8][8].setNextCell(sudokuCells[0][0]);
	}

	/**
	 * Fügt in der ersten Zeile des Sudoku Spielfelds die Zahlen 1 - 9 zufällig
	 * ein.
	 */
	public static void fillRndVal() {
		// Ein neues Object der Random Class erstellen
		Random rnd = new Random(); // neues Random-Object erstellen
		// Hilfsvariablen erstellen
		int num;
		boolean bln;
		// i := Zellen in erster Zeile
		for (int i = 0; i <= 8; i++) {
			// führe Code aus. prüfe ob bln false ist
			do {
				// neue Zufallszahl zwischen 1 - 9 erzeugen
				num = rnd.nextInt(9) + 1;
				// Fehler-Indikator bln zurücksetzen
				bln = true;
				// Schleife durch die erste Zeile bis zur betrachteten Zelle
				for (int j = 0; j <= i; j++) {
					// prüfe ob generierter Zufallswert bereits verwendet wurde
					if (num == sudokuCells[0][j].getValue()) {
						/*
						 * wurde der Wert bereits verwendet, setze den
						 * Fehler-Indikator auf 0
						 */
						bln = false;
						// beende die for-Schleife um neuen Wert zu suchen
						break;
					}
				}
			} while (!bln);
			/*
			 * ist der zufällig generierte Wert gültig, speichere diesen in der
			 * betrachteten Zelle
			 */
			insertFixVal(sudokuCells, i, 0, num);
		}
	}

	/**
	 * Setzt bei dem durch die X-Y-Koordinaten gewählten Class Object das
	 * Attribut fixVal auf true. Speichert den übergebenen Wert im Attribut
	 * value und löscht das Object aus der linked list
	 * 
	 * @param sudokuCells
	 *            das 9x9-Array mit einem Cells-Objects in jedem Feld
	 * @param x
	 *            der x-Index des Class-Objects im Sudoku Spielfeld
	 * @param y
	 *            der y-Index des Class-Objects im Sudoku Spielfeld
	 * @param value
	 *            der Wert des Class-Objects: Startwert für ein Feld im Sudoku
	 *            Spielfeld
	 */
	public static void insertFixVal(Cells[][] sudokuCells, int x, int y, int value) {
		// Startwert speichern
		sudokuCells[y][x].setValue(value);
		// Attribut fixVal zu true ändern
		sudokuCells[y][x].setFixVal(true);
		/*
		 * Das aktuelle Object aus der linked list löschen, indem die Verweise
		 * der vorherigen und nächsten Zelle verbogen werden. Bsp: Zelle A - B -
		 * C (B löschen) a) die nächste Zelle für Zelle A sei Zelle C b) die
		 * vorherige Zelle für Zelle C sei Zelle A Somit wird B beim springen
		 * durch die Zellen übersprungen
		 */
		sudokuCells[y][x].getLastCell().setNextCell(sudokuCells[y][x].getNextCell());
		sudokuCells[y][x].getNextCell().setLastCell(sudokuCells[y][x].getLastCell());
	}

	/**
	 * Findet ausgehend von der übergebenen Zelle, die erste Zelle, die keinen
	 * unveränderlichen Startwert (Attribut: fixVal) enthält.
	 * 
	 * @param currentCell
	 *            die aktuelle Zelle vom Typ Cells, von der ausgehend gesucht
	 *            werden soll
	 * @return Gibt die erste Zelle zurück, die keinen Startwert enthält
	 */
	public static Cells firstEmptyCell(Cells currentCell) {
		// Überprüfe, ob aktuelle Zelle einen Startwert enthält (Attribut fixVal
		// = true)
		while (currentCell.isFixVal()) {
			// Gehe einen Schritt weiter in der linked list
			currentCell = currentCell.getNextCell();
		}
		// Übergebe gefundene Zelle
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
				System.out.println(
						"       1   2   3   4   5   6   7   8   9\n" + "     _____________________________________ \n"
								+ "    |                                     |");
			}
			// j := Spalten in Zeile i
			for (int j = 0; j <= 8; j++) {
				if (j == 0) {
					/*
					 * Erste Zelle in aktueller Zeile mit Koordinate A - I und
					 * führendem Verkettungszeichen (senkrechter Strich)
					 */
					System.out.print(" " + abc[i] + "  |  " + sudokuCells[i][j].getValue() + "  ");
				} else if (j == 8) {
					/*
					 * letzte Zelle in aktueller Zeile mit abschließendem
					 * Verkettungszeichen
					 */
					System.out.print(" " + sudokuCells[i][j].getValue() + "  |");
					// Zeilenumbruch
					System.out.println("");
				} else if (j == 2 || j == 5) {
					/*
					 * Damit die Kästchen klar erkennbar sind, wird jeweils nach
					 * dem 3. und dem 6. Zahlenfeld ein Rahmen angezeigt.
					 */
					System.out.print(" " + sudokuCells[i][j].getValue() + " |");
				} else {
					/*
					 * Die übrigen Zellen werden mit Leerzeichen als
					 * abstandshalter zwischen den Zahlen dargestellt.
					 */
					System.out.print(" " + sudokuCells[i][j].getValue() + "  ");
				}
			}
			// letzte Zeile mit abschließendem Rahmen
			if (i == 8) {
				System.out.println("    |_____________________________________|");
				System.out.println();
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
			 * Werten, eine leere Zeile als Abstandhalter eingefügt. Die
			 * Verkettungszeichen stellen die 3x3-Boxen dar.
			 */
			if (i < 8 && i != 2 && i != 5) {
				System.out.println("    |            |           |            |");
			}
		}
	}

	/**
	 * Stellt die Lösungseingabe im laufenden Spiel dar und gibt dem Spieler
	 * gezielte Hinweise bei falscher Eingabe
	 */
	public static void playerEingabe(Cells[][] sudokuCells) {
		Scanner playerCoordinate = new Scanner(System.in);
		SudokuChecker check = new SudokuChecker();
		System.out.println();
		System.out.println(
				"Welche Zelle möchtest du befüllen? Bitte gib deine Wahl in der Form 'A3 3' (Koordinate + Leerzeichen + dein Wert) ein.");
		// Eingabe auslesen und Zeilenkoordinate zu Kleinbuchstaben
		String completeInput = playerCoordinate.nextLine().toLowerCase();
		// System.out.println(completeInput);
		try {
			// Fehler in der Eingabe'form' (Länge, Leerzeichen) behandeln
			if (completeInput.length() < 4) {
				System.out.println(
						"Deine Eingabe war leider zu kurz. Keep in mind: Koordinate (z.B. A3) + Leerzeichen + dein Wert (z.B. 6)");
				playerEingabe(sudokuCells);
			} else if (completeInput.length() > 4) {
				System.out.println("Deine Eingabe war leider zu lang. Vielleicht eine Zahl zu viel?");
				playerEingabe(sudokuCells);
			} else if (completeInput.charAt(2) != ' ') {
				System.out.println("Deine Eingabe hatte kein Leerzeichen zwischen Koordinate und Wert...Nochmal?");
				playerEingabe(sudokuCells);
			} else {

				// Input in ein Array splitten, um beide Teile weiter bearbeiten
				// zu
				// können und als Strings speichern
				String input[] = completeInput.split("\\s+");
				String coorInput = input[0];
				String temp = input[1];
				// Den eingegebenen Wert zur Zellenfüllung aus String lesen und
				// auf
				// formelle Korrektheit prüfen
				int playerValue = Integer.parseInt(temp);
				if (playerValue < 0 || playerValue > 9) {
					System.out.println("Deine Eingabe war leider fehlerhaft.\n"
							+ "Der Wert, den du für die Zelle setzen möchtest, sollte die Zahlen 1-9 abdecken.");
					playerEingabe(sudokuCells);
				} else {

					// Den Buchstaben (Zeilenbezeichnung) an erster Stelle der
					// Eingabe auslesen
					// Buchstaben in jeweiliges ASCII-Äquivalent umwandeln (a =
					// 97,
					// b = 98...), als int speichern und
					// auf formelle Korrektheit prüfen
					int chosenRow = (int) coorInput.charAt(0) - 97;
					if (chosenRow < 0 || chosenRow > 8) {
						System.out.println("Deine Zeilekoordinate hat leider nicht gepasst. Bitte wähle Zeile A-I!");
						playerEingabe(sudokuCells);
					} else {

						// Liest aus der eingegebenen Koordinate den character
						// an
						// der zweiten
						// Stelle, wandelt es in einen int um und speichert
						// diesen.
						// Ebenfalls wird auf formelle Korrektheit geprüft
						String numberOnly = coorInput.charAt(1) + "";
						int chosenColumn = Integer.parseInt(numberOnly) - 1;
						if (chosenColumn < 0 || chosenColumn > 8) {
							System.out.println(
									"Deine Spaltenkoordinate hat leider nicht gepasst. Bitte wähle Spalte 1-9!");
							playerEingabe(sudokuCells);
						} else {
							if (check.valIsAllowed(sudokuCells, sudokuCells[chosenRow][chosenColumn], playerValue)
									&& !sudokuCells[chosenRow][chosenColumn].isFixVal()) {
								sudokuCells[chosenRow][chosenColumn].setValue(playerValue);
								System.out.println("Wert " + playerValue + " in Reihe " + (chosenRow + 1)
										+ " und Spalte " + (chosenColumn + 1) + " eingetragen.");
								drawBoard();
							} else if (playerValue == 0 && !sudokuCells[chosenRow][chosenColumn].isFixVal()) {
								sudokuCells[chosenRow][chosenColumn].setValue(playerValue);
								System.out.println("Wert " + playerValue + " in Reihe " + (chosenRow + 1)
										+ " und Spalte " + (chosenColumn + 1) + " eingetragen.");
								drawBoard();
							} else {
								System.out.println(
										"Leider ist dein gewünschter Wert hier nicht möglich. Versuche es nocheinmal!");
								playerEingabe(sudokuCells);
							}
						}
					}
				}
			}
		}

		catch (NumberFormatException ex) { // Falls der Spieler einen
											// Buchstaben/String statt der
											// Zahl/Int eingibt
			System.err.println("Leider ist dein gewünschter Wert hier nicht möglich. Versuche es nocheinmal!");
			playerEingabe(sudokuCells);
		}
	}

	private static void schwierigkeitsgrade() {
		int inpInt;
		String inpString;

		System.out.println("\nAlles klar, ein neues Spiel! \n \n" + "Bitte wähle deine Schwierigkeitsstufe: \n"
				+ "1. Sehr leicht \n" + "2. Leicht \n" + "3. Mittel \n" + "4. Schwer \n" + "5. Schwer des Todes");

		Scanner scanner = new Scanner(System.in);
		SudokuSolver solve = new SudokuSolver();

		inpInt = scanner.nextInt();
		inpString = scanner.nextLine();

		switch (inpInt) {
		case 1:
			minBlankCells = 4;
			maxBlankCells = 5;
			solve.digHoles(sudokuCells, minBlankCells, maxBlankCells);
			System.out.println("Ein sehr leichtes Sudoku wird gestartet");
			break;
		case 2:
			minBlankCells = 31;
			maxBlankCells = 44;
			solve.digHoles(sudokuCells, minBlankCells, maxBlankCells);
			System.out.println("Ein leichtes Sudoku wird gestartet");
			break;
		case 3:
			minBlankCells = 45;
			maxBlankCells = 49;
			solve.digHoles(sudokuCells, minBlankCells, maxBlankCells);
			System.out.println("Ein mittleres Sudoku wird gestartet");
			break;
		case 4:
			minBlankCells = 50;
			maxBlankCells = 54;
			solve.digHoles(sudokuCells, minBlankCells, maxBlankCells);
			System.out.println("Ein schweres Sudoku wird gestartet");
			break;
		case 5:
			minBlankCells = 55;
			maxBlankCells = 59;
			solve.digHoles(sudokuCells, minBlankCells, maxBlankCells);
			System.out.println("Ein sehr schweres Sudoku wird gestartet");
			break;
		default:
			System.out.println("Ungültige Eingabe. Bitte wähle eine der fünf Optionen.");
			schwierigkeitsgrade();
			break;
		}
	}

	private static void erklaerung() {
		int inpInt;
		String inpString;

		System.out.println("\nErklärung des Spiels und des Hauptmenüs: \n\n"
				+ "Ein Sudoku besteht aus 9x9 Feldern, die zusätzlich in 3x3 Blöcken \n"
				+ "mit je 3x3 Feldern aufgeteilt sind. Jede Zeile, jede Spalte und \n"
				+ "jeder Block enthält alle Zahlen von 1 bis 9 jeweils genau einmal. \n"
				+ "Ausgangspunkt ist ein Gitter, in dem bereits mehrere Ziffern vorgegeben sind. \n"
				+ "Da jede Zahl pro Zeile, Spalte und Block nur einmal vorkommen darf, \n"
				+ "können durch vorgegebene Zahlen die Positionen für andere Zahlen dieses \n"
				+ "Wertes ausgeschlossen werden. \n"
				+ "Der Schwierigkeitsgrad eines Sudokus kann sowohl von der Anzahl der vorgegebenen \n"
				+ "als auch von der Position der angegebenen Zahlen abhängen. \n\n" + "Im Hauptmenü kannst du: \n"
				+ "1. Dir ein neues, ganz persönliches Sudoku generieren lassen und es lösen \n"
				+ "2. Die vorgegebenen Zahlen eines Sudokus eingeben, um es von Sudokufy lösen zu lassen \n"
				+ "3. Diese Erklärung verzückt anschmachten - so oft es dir beliebt \n"
				+ "4. Schweren Herzens Sudokufy beenden (um nachts davon zu träumen) \n\n"
				+ "Bitte wähle nun eine der folgenden beiden Optionen: \n" + "1. Zurück ins Hauptmenü \n"
				+ "2. Sudokufy beenden.");

		Scanner scanner = new Scanner(System.in);

		inpInt = scanner.nextInt();
		inpString = scanner.nextLine();
		inpString = null;

		switch (inpInt) {
		case 1:
			hauptmenu();
			break;
		case 2:
			scanner.close();
			exit();
			break;
		default:
			System.out.println("Ungültige Eingabe. Bitte wähle eine der beiden Optionen.");
			erklaerung();
			break;
		}
	}

	/**
	 * wann immer das Spiel beendet werden soll, reicht jetzt exit();
	 */
	private static void exit() {
		System.out.println("Schüss!");
		System.exit(1);
	}
}