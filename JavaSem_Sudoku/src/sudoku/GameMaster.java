package sudoku;

import java.util.Random;
import java.util.Scanner;

/**
 * kontrolliert den Spielablauf.
 * 
 * @version: 24/01/2016 <br>
 */

public class GameMaster {
	// Array, in dem alle Zellen-Objects des Sudokus gespeichert werden
	private static Cells[][] sudokuCells = new Cells[9][9];
	/*
	 * Untere und obere Grenze der leeren Zellen, abh�ngig von
	 * Schwierigkeitsgrad
	 */
	private static int minBlankCells;
	private static int maxBlankCells;
	// Objects f�r Pr�fen, L�sen und Eingabe initialisieren
	private static SudokuChecker check = new SudokuChecker();
	private static SudokuSolver solve = new SudokuSolver();
	private static Scanner scanner = new Scanner(System.in);

	/**
	 * Die Main Methode steuert den Spielablauf dieses Sudokuspiels.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Nutzerauswahl im Hauptmen�
		int usrChoice;
		// Willkommensnachricht an Spieler
		System.out.println("\nWillkommen bei Sudokufy - von LTML");
		/*
		 * Aufruf des Hauptmen�s. Wiederholung in do-while-Schleife bis Spieler
		 * eine Option 1 - 4 w�hlt. Wird die Spielerkl�rung (3) aufgerufen und
		 * kehrt der Spieler darauf hin ins Hauptmen� zur�ck, wird usrChoice
		 * manuell zur�ckgesetzt.
		 */
		do {
			usrChoice = mainMenu();
			// Spieler w�hlt Erkl�rung
			if (usrChoice == 3) {
				explanation();
				// zur�cksetzen um erneuten Loop zu erzwingen
				usrChoice = 0;
			}
		} while (usrChoice == 0);
		// Spielablauf abh�ngig von der Auswahl des Spielers steuern.
		switch (usrChoice) {
		case 1: // Sudoku generieren und l�sen
			// leeres Sudoku Spielfeld generieren
			genSudoku();
			// Zuf�llige Werte in erster Zeile einf�gen
			fillRndVal();
			// L�sungsalgorithmus "l�st" Sudoku
			solve.solveSudoku(sudokuCells, firstEmptyCell(sudokuCells[0][0]));
			// Ausgabe an Spieler
			System.out.println("\nAlles klar! Ein neues Spiel beginnt: \n");
			// Spieler w�hlt Schwierigkeitsgrad
			difficultyLevel();
			// Sudoku generieren durch L�cher graben
			solve.digHoles(sudokuCells, minBlankCells, maxBlankCells);
			// Ausgabe an Spieler
			System.out.println("Okay, dann los! \n");
			// Ausgabe Sudoku Spielfeld
			drawBoard();
			// Pr�fe ob Sudoku gel�st wurde
			while (!check.sudokuIsSolved(sudokuCells)) {
				// Spieler gibt Werte ein
				playerInput(sudokuCells);
			}
			// Erfolgsmeldung an Spieler
			System.out.println("Sehr gut, Sudoku gel�st!");
			break;
		case 2: // Eigenes Sudoku eingeben und l�sen lassen
			// leeres Sudoku Spielfeld generieren
			genSudoku();
			// Ausgabe an Spieler
			System.out.println("Gib jetzt bitte dein Sudoku ein:");
			// Ausgabe Sudoku Spielfeld
			drawBoard();
			// Pr�fe ob Sudoku gel�st wurde
			while (!check.sudokuIsSolved(sudokuCells)) {
				// Spieler gibt Werte ein
				playerInput(sudokuCells);
			}
			break;
		case 4: // Sudokufy beenden
			exit();
			break;
		}
	}

	/**
	 * Die Methode mainMenu stellt ein Hauptmen� f�r den Spieler dar und
	 * empf�ngt dessen Auswahl. Evtl. Fehler in der Eingabe werden durch default
	 * oder try/catch abgefangen.
	 * 
	 * @return die Auswahl des Spielers
	 */
	private static int mainMenu() {
		// Spieler Eingabe
		int intInp;
		// Ausgabe Hauptmen� mit Optionen 1 - 4
		System.out.println("\nBitte w�hle eine der folgenden Optionen indem du die entsprechende Zahl eingibst: \n"
				+ "1. Sudoku spielen \n" + "2. Sudoku l�sen lassen \n" + "3. Wie spielt man Sudoku? \n"
				+ "4. Sudokufy beenden");
		// Exception handling f�r Eingabe von Buchstaben/Strings
		try {
			// Scanner liest n�chste Zeile, parse von String zu int
			intInp = Integer.parseInt(scanner.nextLine());
			// R�ckgabe in Abh�ngigkeit von Spieler Eingabe
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
				// Mitteilung fehlerhafte Eingabe
				System.err.println("\nUng�ltige Eingabe. Bitte w�hle eine der vier Optionen.");
			}
		} catch (NumberFormatException e) {
			// Mitteilung fehlerhafte Eingabe
			System.err.println("\nUng�ltige Eingabe. Bitte w�hle eine der oben genannten Optionen.");
		}
		return 0;
	}

	/**
	 * Die Methode difficultyLevel stellt ein Men� zur Auswahl des
	 * Schwierigkeitsgrades zwischen sehr leicht und schwer dar. Evtl. Fehler in
	 * der Eingabe werden durch default oder try/catch abgefangen.
	 */
	private static void difficultyLevel() {
		// Spieler Eingabe
		int intInp;
		// Ausgabe Schwierigkeitsgrade mit Option 1 - 4
		System.out.println("Bitte w�hle deine Schwierigkeitsstufe: \n" + "1. Sehr leicht \n" + "2. Leicht \n"
				+ "3. Mittel \n" + "4. Schwer");
		// Exception handling f�r Eingabe von Buchstaben/Strings
		try {
			// Scanner liest n�chste Zeile, parse von String zu int
			intInp = Integer.parseInt(scanner.nextLine());
			/*
			 * Untere und obere Grenze der leeren Zellen in Abh�ngigkeit der
			 * Spieler Auswahl
			 */
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
				// Mitteilung fehlerhafte Eingabe
				System.err.println("Ung�ltige Eingabe. Bitte w�hle eine der vier Optionen.");
				// Neustart der Auswahl des Schwierigkeitsgrades
				difficultyLevel();
				break;
			}
		} catch (NumberFormatException e) {
			// Mitteilung fehlerhafte Eingabe
			System.err.println("Ung�ltige Eingabe. Bitte gib eine Zahl ein.\n");
			// Neustart der Auswahl des Schwierigkeitsgrades
			difficultyLevel();
		}
	}

	/**
	 * Die Methode explanation gibt dem Spieler eine kurze Erkl�rung der Regeln
	 * eines Sudoku Puzzles und beschreibt die Auswahlm�glichkeit im Hauptmen�.
	 */
	private static void explanation() {
		// Spieler Eingabe
		int intInp;
		// Ausgabe der Erkl�rungen
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
				+ "3. Diese Erkl�rung nochmals aufrufen \n" + "4. Dieses Spiel beenden \n\n"
				+ "Bitte w�hle nun eine der folgenden beiden Optionen: \n" + "1. Zur�ck ins Hauptmen� \n"
				+ "2. Sudokufy beenden.");
		// Exception handling f�r Eingabe von Buchstaben/Strings
		try {
			// Scanner liest n�chste Zeile, parse von String zu int
			intInp = Integer.parseInt(scanner.nextLine());
			// Auswahl in Abh�ngigkeit von Spieler Eingabe
			switch (intInp) {
			case 1:
				break;
			case 2:
				// Sudokufy beenden
				exit();
				break;
			default:
				// Mitteilung fehlerhafte Eingabe
				System.err.println("Ung�ltige Eingabe. Bitte w�hle eine der beiden Optionen.\n");
				// Neustart der Erkl�rung
				explanation();
				break;
			}
		} catch (NumberFormatException e) {
			// Mitteilung fehlerhafte Eingabe
			System.err.println("Ung�ltige Eingabe. Bitte w�hle eine der beiden Optionen.\n");
			// Neustart der Erkl�rung
			explanation();
		}
	}

	/**
	 * Stellt die L�sungseingabe im laufenden Spiel dar und gibt dem Spieler
	 * gezielte Hinweise bei falscher Eingabe
	 */
	private static void playerInput(Cells[][] sudokuCells) {
		// Anweisungen f�r Spieler
		System.out.println(
				"\n" + "Welche Zelle m�chtest du bef�llen? Bitte gib deine Wahl in der Form 'A3 3' (Koordinate + Leerzeichen + deinen Wert) ein.\n"
						+ "Mit '1' kannst du das Spiel jederzeit beenden");
		// Eingabe auslesen, Konvertierung zu Kleinbuchstaben
		String completeInput = scanner.nextLine().toLowerCase();
		// Exception handling f�r Eingabe von Buchstaben/Strings
		try {
			// Fehler in der Eingabe'form' (L�nge, Leerzeichen) behandeln
			switch (completeInput) {
			case "1":
				// Sudokufy beenden
				exit();
				break;
			default:
				// Zu kurze Eingabe abfangen
				if (completeInput.length() < 4) {
					// Mitteilung fehlerhafte Eingabe
					System.err.println(
							"Deine Eingabe war leider zu kurz. Keep in mind: Koordinate (z.B. A3) + Leerzeichen + dein Wert (z.B. 6)");
					// Spieler Eingabe neu starten
					playerInput(sudokuCells);
				} else if (completeInput.equalsIgnoreCase("L�sen")) {
					// L�sung des Sudokus berechnen
					solveOwnSudoku();
				} else if (completeInput.length() > 4) {
					// Mitteilung fehlerhafte Eingabe
					System.err.println("Deine Eingabe war leider zu lang. Vielleicht eine Zahl zu viel?");
					// Spieler Eingabe neu starten
					playerInput(sudokuCells);
				} else if (completeInput.charAt(2) != ' ') {
					// Mitteilung fehlerhafte Eingabe
					System.err.println("Deine Eingabe hatte kein Leerzeichen zwischen Koordinate und Wert...Nochmal?");
					// Spieler Eingabe neu starten
					playerInput(sudokuCells);
				} else {
					/*
					 * Input in ein Array splitten, um beide Teile weiter
					 * bearbeiten zu k�nnen und als Strings speichern.
					 * \\s+ regex f�r Whitespace (regular expression)
					 */
					String input[] = completeInput.split("\\s+");
					/*
					 * Den eingegebenen Wert zur Zellenf�llung aus String lesen
					 * und auf formelle Korrektheit pr�fen
					 */
					int playerValue = Integer.parseInt(input[1]);
					// Pr�fe ob die Zahl zwischen 1 - 9 ist
					if (playerValue < 0 || playerValue > 9) {
						// Mitteilung fehlerhafte Eingabe
						System.err.println("Deine Eingabe war leider fehlerhaft.\n"
								+ "Der Wert, den du f�r die Zelle setzen m�chtest, sollte die Zahlen 1-9 abdecken.");
						// Spieler Eingabe neu starten
						playerInput(sudokuCells);
					} else {
						/*
						 * Den Buchstaben (Zeilenbezeichnung) an erster Stelle
						 * der Eingabe auslesen Buchstaben in jeweiliges
						 * ASCII-�quivalent umwandeln (a = 97, b = 98...), als
						 * int speichern und auf formelle Korrektheit pr�fen
						 */
						int chosenRow = (int) input[0].charAt(0) - 97;
						// Pr�fe ob Koordinate f�r Zeilen existiert
						if (chosenRow < 0 || chosenRow > 8) {
							// Mitteilung fehlerhafte Eingabe
							System.err
									.println("Deine Zeilekoordinate hat leider nicht gepasst. Bitte w�hle Zeile A-I!");
							// Spieler Eingabe neu starten
							playerInput(sudokuCells);
						} else {
							/*
							 * Liest aus der eingegebenen Koordinate den
							 * character an der zweiten Stelle, wandelt es in
							 * einen int um und speichert diesen. Ebenfalls wird
							 * auf formelle Korrektheit gepr�ft
							 */
							String numberOnly = input[0].charAt(1) + "";
							// parse String zu int der x-Koordinate
							int chosenColumn = Integer.parseInt(numberOnly) - 1;
							// Pr�fe ob Koordinate f�r Spalte existiert
							if (chosenColumn < 0 || chosenColumn > 8) {
								// Mitteilung fehlerhafte Eingabe
								System.err.println(
										"Deine Spaltenkoordinate hat leider nicht gepasst. Bitte w�hle Spalte 1-9!");
								// Spieler Eingabe neu starten
								playerInput(sudokuCells);
							} else {
								// Pr�fe Wert der Spieler Eingabe
								if (check.valIsAllowed(sudokuCells, sudokuCells[chosenRow][chosenColumn], playerValue)
										&& !sudokuCells[chosenRow][chosenColumn].isFixVal()) {
									/*
									 * Einzusetzender Wert ist nach aktuellem
									 * L�sungsstand erlaubt und die ausgew�hlte
									 * Zelle enth�lt keinen Startwert. Wert
									 * speichern und R�ckmeldung an Spieler,
									 * dass Eingabe erfolgreich war.
									 */
									sudokuCells[chosenRow][chosenColumn].setValue(playerValue);
									System.out.println("Wert " + playerValue + " in Reihe " + (chosenRow + 1)
											+ " und Spalte " + (chosenColumn + 1) + " eingetragen.");
									// Ausgabe Sudoku Spielfeld
									drawBoard();
								} else if (playerValue == 0 && !sudokuCells[chosenRow][chosenColumn].isFixVal()) {
									/*
									 * Spieler m�chte Zelle l�schen (Wert = 0)
									 * und die Zelle enth�lt keinen Startwert.
									 * Wert speichern und R�ckmeldung an
									 * Spieler, dass Eingabe erfolgreich war.
									 */
									sudokuCells[chosenRow][chosenColumn].setValue(playerValue);
									System.out.println("Wert " + playerValue + " in Reihe " + (chosenRow + 1)
											+ " und Spalte " + (chosenColumn + 1) + " eingetragen.");
									// Ausgabe Sudoku Spielfeld
									drawBoard();
								} else {
									// Mitteilung fehlerhafte Eingabe
									System.err.println(
											"Leider ist dein gew�nschter Wert hier nicht m�glich. Versuche es nocheinmal!");
									// Spieler Eingabe neu starten
									playerInput(sudokuCells);
								}
							}
						}
					}
				}
				break;
			}
		} catch (NumberFormatException e) {
			// Mitteilung fehlerhafte Eingabe
			System.err.println("Leider ist dein gew�nschter Wert hier nicht m�glich. Versuche es nocheinmal!");
			// Spieler Eingabe neu starten
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
		// Verkn�pfe Linked List neu
		solve.resetLinkedList(sudokuCells, false);
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

	/**
	 * L�st das Spieler-Sudoku, indem die eingegebenen Werte als fix gesetzt
	 * werden, die Linked-List zur�ckgesetzt wird und der Algorithmus seine
	 * Arbeit macht.
	 */
	private static void solveOwnSudoku() {
		/*
		 * Schleife durch das Sudoku und setze alle eingetragenen Werte als fix
		 * ein.
		 */
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (sudokuCells[i][j].getValue() != 0) {
					// Ist Wert vorhanden, �berschreibe als fixen Wert
					insertFixVal(sudokuCells, j, i, sudokuCells[i][j].getValue());
				}
			}
		}
		// Verkn�pfe Linked List neu
		solve.resetLinkedList(sudokuCells, false);
		// L�sungsalgorithmus l�st Sudoku
		solve.solveSudoku(sudokuCells, firstEmptyCell(sudokuCells[0][0]));
		// Ausgabe Sudoku Spielfeld
		drawBoard();
		// Mitteilung an Spieler, dass L�sung gefunden wurde.
		System.out.println("Dies ist die L�sung!");
		exit();
	}

	/**
	 * wann immer das Spiel beendet werden soll, reicht jetzt exit();
	 */
	private static void exit() {
		// Abschiedsnachricht an Spieler
		System.out.println("Sch�ss!");
		// Beenden
		System.exit(1);
	}
}