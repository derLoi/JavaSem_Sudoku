package sudoku;

import java.util.Random;
import java.util.Scanner;

/**
 * kontrolliert den Spielablauf.
 * 
 * @version: 19/01/2016 <br>
 * @changelog <br>
 *            <ul>
 *            <li>MW: Abfrage der Schwierigkeitsstufe eingebaut</li>
 *            <li>TM: �berarbeitungen der Spielfeldanzeige</li>
 *            <li>LS: Test mit G�ltigkeitspr�fung f�r zuf�llig generiertes
 *            Sudoku. Ausgabe Ergebnisse</li>
 *            <li>LS: Methoden fillRndVal(), insertFixVal() und firstEmptyCell()
 *            hinzugef�gt</li>
 *            <li>MW: Interaktive Spielereingabe w�hrend des Spiels eingebaut
 *            </li>
 *            <li>TM: Men� und exception handling eingebaut</li>
 *            </ul>
 */

public class GameMaster {
	// Array, in dem alle Zellen-Objects des Sudokus gespeichert werden
	private static Cells[][] sudokuCells = new Cells[9][9];
	private static int minBlankCells;
	private static int maxBlankCells;
	private static SudokuChecker check = new SudokuChecker();
	private static SudokuSolver solve = new SudokuSolver();
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		int usrChoice;

		System.out.println("\nWillkommen bei Sudokufy - von LTML");

		do {
			usrChoice = mainMenu();
			if (usrChoice == 3){
				explanation();
				usrChoice = 0;
			}
		} while (usrChoice == 0);

		switch (usrChoice) {
		case 1:
			genSudoku();
			fillRndVal();
			solve.solveSudoku(sudokuCells, firstEmptyCell(sudokuCells[0][0]));
			System.out.println("\n" + "Alles klar! Ein neues Spiel beginnt: \n");
			difficultylevel();
			solve.digHoles(sudokuCells, minBlankCells, maxBlankCells);
			System.out.println("Okay dann los! \n");
			drawBoard();
			while (!check.sudokuIsSolved(sudokuCells)) {
				playerInput(sudokuCells);
			}
			System.out.println("Sehr gut, Sudoku gel�st!");
			break;
		case 2:
			genSudoku();
			System.out.println("Gib jetzt bitte dein Sudoku ein:");
			drawBoard();
			while (!check.sudokuIsSolved(sudokuCells)) {
				playerInput(sudokuCells);
			}
			break;
		case 4:
			exit();
			break;
		}
	}

	private static int mainMenu() {
		int intInp;
		System.out.println("\n"+"Bitte w�hle eine der folgenden Optionen indem du die entsprechende Zahl eingibst: \n"
				+ "1. Sudoku spielen \n" + "2. Sudoku l�sen lassen \n" + "3. Wie spielt man Sudoku? \n"
				+ "4. Sudokufy beenden");

		// Exception f�r Eingabe von Buchstaben/Strings schmei�en
		try {
			/*
			 * nextInt() bewirkt keinen Zeilenumbruch, sodass der Scanner immer
			 * noch in derselben Zeile bleibt. Deswegen nextLine(), um input
			 * skipping zu verhindern
			 */
			intInp = Integer.parseInt(scanner.nextLine());
			System.out.println(intInp);

			switch (intInp) {
			case 1:
				return 1;
			case 2:
				return 2;
			case 3:
				return 3;
			case 4:
				return 4;
			default:
				System.err.println("Ung�ltige Eingabe. Bitte w�hle eine der vier Optionen.");
			}
		} catch (NumberFormatException e) {
			System.out.println();
			System.err.println("Ung�ltige Eingabe. Bitte w�hle eine der oben genannten Optionen.");
		}
		return 0;
	}

	private static void difficultylevel() {
		int intInp;
		System.out.println("Bitte w�hle deine Schwierigkeitsstufe: \n" + "1. Sehr leicht \n" + "2. Leicht \n"
				+ "3. Mittel \n" + "4. Schwer");

		try {
			intInp = Integer.parseInt(scanner.nextLine());
			System.out.println(intInp);
			switch (intInp) {
			case 1:
				minBlankCells = 28;
				maxBlankCells = 30;
				System.out.println("Ein sehr leichtes Sudoku wird gestartet");
				break;
			case 2:
				minBlankCells = 31;
				maxBlankCells = 44;
				System.out.println("Ein leichtes Sudoku wird gestartet");
				break;
			case 3:
				minBlankCells = 45;
				maxBlankCells = 49;
				System.out.println("Ein mittelschweres Sudoku wird gestartet");
				break;
			case 4:
				minBlankCells = 50;
				maxBlankCells = 54;
				System.out.println("Ein schweres Sudoku wird gestartet.");
				break;
			default:
				System.err.println("Ung�ltige Eingabe. Bitte w�hle eine der vier Optionen.");
				difficultylevel();
				break;
			}
		} catch (NumberFormatException e) {
			System.err.println("Ung�ltige Eingabe. Bitte gib eine Zahl ein.");
			difficultylevel();
		}
	}

	private static void explanation() {
		int intInp;
		System.out.println("\nErkl�rung des Spiels und des Hauptmen�s: \n\n"
				+ "Ein Sudoku besteht aus 9x9 Feldern, die zus�tzlich in 3x3 Bl�cken \n"
				+ "mit je 3x3 Feldern aufgeteilt sind. Jede Zeile, jede Spalte und \n"
				+ "jeder Block enth�lt alle Zahlen von 1 bis 9 jeweils genau einmal. \n"
				+ "Ausgangspunkt ist ein Gitter, in dem bereits mehrere Ziffern vorgegeben sind. \n"
				+ "Da jede Zahl pro Zeile, Spalte und Block nur einmal vorkommen darf, \n"
				+ "k�nnen durch vorgegebene Zahlen die Positionen f�r andere Zahlen dieses \n"
				+ "Wertes ausgeschlossen werden. \n"
				+ "Der Schwierigkeitsgrad eines Sudokus kann sowohl von der Anzahl der vorgegebenen \n"
				+ "als auch von der Position der angegebenen Zahlen abh�ngen. \n\n" + "Im Hauptmen� kannst du: \n"
				+ "1. Dir ein neues, ganz pers�nliches Sudoku generieren lassen und es l�sen \n"
				+ "2. Die vorgegebenen Zahlen eines Sudokus eingeben, um es von Sudokufy l�sen zu lassen \n"
				+ "3. Diese Erkl�rung verz�ckt anschmachten - so oft es dir beliebt \n"
				+ "4. Sudokufy schweren Herzens beenden(, um nachts davon zu tr�umen) \n\n"
				+ "Bitte w�hle nun eine der folgenden beiden Optionen: \n" + "1. Zur�ck ins Hauptmen� \n"
				+ "2. Sudokufy beenden.");

		try {
			intInp = Integer.parseInt(scanner.nextLine());
			System.out.println(intInp);
			switch (intInp) {
			case 1:
				break;
			case 2:
				exit();
				break;
			default:
				System.err.println("Ung�ltige Eingabe. Bitte w�hle eine der beiden Optionen.");
				explanation();
				break;
			}
		} catch (NumberFormatException e) {
			System.err.println("Ung�ltige Eingabe. Bitte w�hle eine der beiden Optionen.");
			explanation();
		}
	}

	/**
	 * Stellt die L�sungseingabe im laufenden Spiel dar und gibt dem Spieler
	 * gezielte Hinweise bei falscher Eingabe
	 */
	private static void playerInput(Cells[][] sudokuCells) {
		SudokuChecker check = new SudokuChecker();
		System.out.println();
		System.out.println(
				"Welche Zelle m�chtest du bef�llen? Bitte gib deine Wahl in der Form 'A3 3' (Koordinate + Leerzeichen + deinen Wert) ein.\n"
						+ "Mit '1' kannst du das Spiel jederzeit beenden");
		// Eingabe auslesen und Zeilenkoordinate zu Kleinbuchstaben
		String completeInput = scanner.nextLine().toLowerCase();

		// Falls der Spieler einen Buchstaben/String statt der Zahl/Int eingibt
		try {
			// Fehler in der Eingabe'form' (L�nge, Leerzeichen) behandeln
			switch (completeInput) {
			case "1":
				exit();
				break;
			default:
				if (completeInput.length() < 4) {
					System.err.println(
							"Deine Eingabe war leider zu kurz. Keep in mind: Koordinate (z.B. A3) + Leerzeichen + dein Wert (z.B. 6)");
					playerInput(sudokuCells);
				} else if (completeInput.equalsIgnoreCase("L�sen")) {
					solveOwnSudoku();
				} else if (completeInput.length() > 4) {
					System.err.println("Deine Eingabe war leider zu lang. Vielleicht eine Zahl zu viel?");
					playerInput(sudokuCells);
				} else if (completeInput.charAt(2) != ' ') {
					System.err.println("Deine Eingabe hatte kein Leerzeichen zwischen Koordinate und Wert...Nochmal?");
					playerInput(sudokuCells);
				} else {
					/*
					 * Input in ein Array splitten, um beide Teile weiter
					 * bearbeiten zu k�nnen und als Strings speichern
					 */
					String input[] = completeInput.split("\\s+");
					String coorInput = input[0];
					String temp = input[1];
					/*
					 * Den eingegebenen Wert zur Zellenf�llung aus String lesen
					 * und auf formelle Korrektheit pr�fen
					 */
					int playerValue = Integer.parseInt(temp);
					if (playerValue < 0 || playerValue > 9) {
						System.err.println("Deine Eingabe war leider fehlerhaft.\n"
								+ "Der Wert, den du f�r die Zelle setzen m�chtest, sollte die Zahlen 1-9 abdecken.");
						playerInput(sudokuCells);
					} else {
						/*
						 * Den Buchstaben (Zeilenbezeichnung) an erster Stelle
						 * der Eingabe auslesen Buchstaben in jeweiliges
						 * ASCII-�quivalent umwandeln (a = 97, b = 98...), als
						 * int speichern und auf formelle Korrektheit pr�fen
						 */
						int chosenRow = (int) coorInput.charAt(0) - 97;
						if (chosenRow < 0 || chosenRow > 8) {
							System.err
									.println("Deine Zeilekoordinate hat leider nicht gepasst. Bitte w�hle Zeile A-I!");
							playerInput(sudokuCells);
						} else {
							/*
							 * Liest aus der eingegebenen Koordinate den
							 * character an der zweiten Stelle, wandelt es in
							 * einen int um und speichert diesen. Ebenfalls wird
							 * auf formelle Korrektheit gepr�ft
							 */
							String numberOnly = coorInput.charAt(1) + "";
							int chosenColumn = Integer.parseInt(numberOnly) - 1;
							if (chosenColumn < 0 || chosenColumn > 8) {
								System.err.println(
										"Deine Spaltenkoordinate hat leider nicht gepasst. Bitte w�hle Spalte 1-9!");
								playerInput(sudokuCells);
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
									System.err.println(
											"Leider ist dein gew�nschter Wert hier nicht m�glich. Versuche es nocheinmal!");
									playerInput(sudokuCells);
								}
							}
						}
					}
				}
				break;
			}
		} catch (NumberFormatException e) {
			System.err.println("Leider ist dein gew�nschter Wert hier nicht m�glich. Versuche es nocheinmal!");
			playerInput(sudokuCells);
		}
	}

	/**
	 * Zeichnet ein Sudoku Spielfeld. In der ersten Zeile und der ersten Spalte
	 * werden die Koordinaten 1 - 9 und A - I dargestellt. Die dargestellten
	 * Werte werden aus den Cells Objects in den Feldern des Arrays sudokuCells
	 * ausgelesen.
	 */
	private static void drawBoard() {
		String[] abc = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };
		// i := Zeilen
		for (int i = 0; i <= 8; i++) {
			// erste Zeile Kopfbereich zeichnen
			if (i == 0) {
				// Koordinaten 1 - 9 im Kopf
				System.out.println("\n" + "       1   2   3   4   5   6   7   8   9\n"
						+ "     _____________________________________ \n"
						+ "    |                                     |");
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
			 * Werten, eine leere Zeile als Abstandhalter eingef�gt. Die
			 * Verkettungszeichen stellen die 3x3-Boxen dar.
			 */
			if (i < 8 && i != 2 && i != 5) {
				System.out.println("    |            |           |            |");
			}
		}
	}

	/**
	 * Generiert ein leeres Sudoku Spielfeld indem die ben�tigten Cells Objects
	 * initialisiert werden und die Linked List verkn�pft wird.
	 */
	private static void genSudoku() {
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
		solve.resetLinkedList(sudokuCells);
	}

	/**
	 * F�gt in der ersten Zeile des Sudoku Spielfelds die Zahlen 1 - 9 zuf�llig
	 * ein.
	 */
	private static void fillRndVal() {
		// Ein neues Object der Random Class erstellen
		Random rnd = new Random(); // neues Random-Object erstellen
		// Hilfsvariablen erstellen
		int num;
		boolean bln;

		// i := Zellen in erster Zeile
		for (int i = 0; i <= 8; i++) {
			// f�hre Code aus. pr�fe ob bln false ist
			do {
				// neue Zufallszahl zwischen 1 - 9 erzeugen
				num = rnd.nextInt(9) + 1;
				// Fehler-Indikator bln zur�cksetzen
				bln = true;
				// Schleife durch die erste Zeile bis zur betrachteten Zelle
				for (int j = 0; j <= i; j++) {
					// pr�fe ob generierter Zufallswert bereits verwendet wurde
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
			 * ist der zuf�llig generierte Wert g�ltig, speichere diesen in der
			 * betrachteten Zelle
			 */
			insertFixVal(sudokuCells, i, 0, num);
		}
	}

	/**
	 * Setzt bei dem durch die X-Y-Koordinaten gew�hlten Class Object das
	 * Attribut fixVal auf true. Speichert den �bergebenen Wert im Attribut
	 * value und l�scht das Object aus der linked list
	 * 
	 * @param sudokuCells
	 *            das 9x9-Array mit einem Cells-Objects in jedem Feld
	 * @param x
	 *            der x-Index des Class-Objects im Sudoku Spielfeld
	 * @param y
	 *            der y-Index des Class-Objects im Sudoku Spielfeld
	 * @param value
	 *            der Wert des Class-Objects: Startwert f�r ein Feld im Sudoku
	 *            Spielfeld
	 */
	private static void insertFixVal(Cells[][] sudokuCells, int x, int y, int value) {
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
	private static Cells firstEmptyCell(Cells currentCell) {
		// �berpr�fe, ob aktuelle Zelle einen Startwert enth�lt (Attribut fixVal
		// = true)
		while (currentCell.isFixVal()) {
			// Gehe einen Schritt weiter in der linked list
			currentCell = currentCell.getNextCell();
		}
		// �bergebe gefundene Zelle
		return currentCell;
	}

	private static void solveOwnSudoku() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (sudokuCells[i][j].getValue() != 0) {
					insertFixVal(sudokuCells, j, i, sudokuCells[i][j].getValue());
				}
			}
		}
		solve.resetLinkedList(sudokuCells);
		solve.solveSudoku(sudokuCells, firstEmptyCell(sudokuCells[0][0]));
		drawBoard();
		System.out.println("Bidde: Dat isset!");
		exit();
	}

	/**
	 * wann immer das Spiel beendet werden soll, reicht jetzt exit();
	 */
	private static void exit() {
		System.out.println("Sch�ss!");
		System.exit(1);
	}
}