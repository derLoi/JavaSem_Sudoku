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
	 * Untere und obere Grenze der leeren Zellen, abhängig von
	 * Schwierigkeitsgrad
	 */
	private static int minBlankCells;
	private static int maxBlankCells;
	// Objects für Prüfen, Lösen und Eingabe initialisieren
	private static SudokuChecker check = new SudokuChecker();
	private static SudokuSolver solve = new SudokuSolver();
	private static Scanner scanner = new Scanner(System.in);

	/**
	 * Die Main Methode steuert den Spielablauf dieses Sudokuspiels.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Nutzerauswahl im Hauptmenü
		int usrChoice;
		// Willkommensnachricht an Spieler
		System.out.println("\nWillkommen bei Sudokufy - von LTML");
		/*
		 * Aufruf des Hauptmenüs. Wiederholung in do-while-Schleife bis Spieler
		 * eine Option 1 - 4 wählt. Wird die Spielerklärung (3) aufgerufen und
		 * kehrt der Spieler darauf hin ins Hauptmenü zurück, wird usrChoice
		 * manuell zurückgesetzt.
		 */
		do {
			usrChoice = mainMenu();
			// Spieler wählt Erklärung
			if (usrChoice == 3) {
				explanation();
				// zurücksetzen um erneuten Loop zu erzwingen
				usrChoice = 0;
			}
		} while (usrChoice == 0);
		// Spielablauf abhängig von der Auswahl des Spielers steuern.
		switch (usrChoice) {
		case 1: // Sudoku generieren und lösen
			// leeres Sudoku Spielfeld generieren
			genSudoku();
			// Zufällige Werte in erster Zeile einfügen
			fillRndVal();
			// Lösungsalgorithmus "löst" Sudoku
			solve.solveSudoku(sudokuCells, firstEmptyCell(sudokuCells[0][0]));
			// Ausgabe an Spieler
			System.out.println("\nAlles klar! Ein neues Spiel beginnt: \n");
			// Spieler wählt Schwierigkeitsgrad
			difficultyLevel();
			// Sudoku generieren durch Löcher graben
			solve.digHoles(sudokuCells, minBlankCells, maxBlankCells);
			// Ausgabe an Spieler
			System.out.println("Okay, dann los! \n");
			// Ausgabe Sudoku Spielfeld
			drawBoard();
			// Prüfe ob Sudoku gelöst wurde
			while (!check.sudokuIsSolved(sudokuCells)) {
				// Spieler gibt Werte ein
				playerInput(sudokuCells);
			}
			// Erfolgsmeldung an Spieler
			System.out.println("Sehr gut, Sudoku gelöst!");
			break;
		case 2: // Eigenes Sudoku eingeben und lösen lassen
			// leeres Sudoku Spielfeld generieren
			genSudoku();
			// Ausgabe an Spieler
			System.out.println("Gib jetzt bitte dein Sudoku ein:");
			// Ausgabe Sudoku Spielfeld
			drawBoard();
			// Prüfe ob Sudoku gelöst wurde
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
	 * Die Methode mainMenu stellt ein Hauptmenü für den Spieler dar und
	 * empfängt dessen Auswahl. Evtl. Fehler in der Eingabe werden durch default
	 * oder try/catch abgefangen.
	 * 
	 * @return die Auswahl des Spielers
	 */
	private static int mainMenu() {
		// Spieler Eingabe
		int intInp;
		// Ausgabe Hauptmenü mit Optionen 1 - 4
		System.out.println("\nBitte wähle eine der folgenden Optionen indem du die entsprechende Zahl eingibst: \n"
				+ "1. Sudoku spielen \n" + "2. Sudoku lösen lassen \n" + "3. Wie spielt man Sudoku? \n"
				+ "4. Sudokufy beenden");
		// Exception handling für Eingabe von Buchstaben/Strings
		try {
			// Scanner liest nächste Zeile, parse von String zu int
			intInp = Integer.parseInt(scanner.nextLine());
			// Rückgabe in Abhängigkeit von Spieler Eingabe
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
				System.err.println("\nUngültige Eingabe. Bitte wähle eine der vier Optionen.");
			}
		} catch (NumberFormatException e) {
			// Mitteilung fehlerhafte Eingabe
			System.err.println("\nUngültige Eingabe. Bitte wähle eine der oben genannten Optionen.");
		}
		return 0;
	}

	/**
	 * Die Methode difficultyLevel stellt ein Menü zur Auswahl des
	 * Schwierigkeitsgrades zwischen sehr leicht und schwer dar. Evtl. Fehler in
	 * der Eingabe werden durch default oder try/catch abgefangen.
	 */
	private static void difficultyLevel() {
		// Spieler Eingabe
		int intInp;
		// Ausgabe Schwierigkeitsgrade mit Option 1 - 4
		System.out.println("Bitte wähle deine Schwierigkeitsstufe: \n" + "1. Sehr leicht \n" + "2. Leicht \n"
				+ "3. Mittel \n" + "4. Schwer");
		// Exception handling für Eingabe von Buchstaben/Strings
		try {
			// Scanner liest nächste Zeile, parse von String zu int
			intInp = Integer.parseInt(scanner.nextLine());
			/*
			 * Untere und obere Grenze der leeren Zellen in Abhängigkeit der
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
				System.err.println("Ungültige Eingabe. Bitte wähle eine der vier Optionen.");
				// Neustart der Auswahl des Schwierigkeitsgrades
				difficultyLevel();
				break;
			}
		} catch (NumberFormatException e) {
			// Mitteilung fehlerhafte Eingabe
			System.err.println("Ungültige Eingabe. Bitte gib eine Zahl ein.\n");
			// Neustart der Auswahl des Schwierigkeitsgrades
			difficultyLevel();
		}
	}

	/**
	 * Die Methode explanation gibt dem Spieler eine kurze Erklärung der Regeln
	 * eines Sudoku Puzzles und beschreibt die Auswahlmöglichkeit im Hauptmenü.
	 */
	private static void explanation() {
		// Spieler Eingabe
		int intInp;
		// Ausgabe der Erklärungen
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
				+ "3. Diese Erklärung nochmals aufrufen \n" + "4. Dieses Spiel beenden \n\n"
				+ "Bitte wähle nun eine der folgenden beiden Optionen: \n" + "1. Zurück ins Hauptmenü \n"
				+ "2. Sudokufy beenden.");
		// Exception handling für Eingabe von Buchstaben/Strings
		try {
			// Scanner liest nächste Zeile, parse von String zu int
			intInp = Integer.parseInt(scanner.nextLine());
			// Auswahl in Abhängigkeit von Spieler Eingabe
			switch (intInp) {
			case 1:
				break;
			case 2:
				// Sudokufy beenden
				exit();
				break;
			default:
				// Mitteilung fehlerhafte Eingabe
				System.err.println("Ungültige Eingabe. Bitte wähle eine der beiden Optionen.\n");
				// Neustart der Erklärung
				explanation();
				break;
			}
		} catch (NumberFormatException e) {
			// Mitteilung fehlerhafte Eingabe
			System.err.println("Ungültige Eingabe. Bitte wähle eine der beiden Optionen.\n");
			// Neustart der Erklärung
			explanation();
		}
	}

	/**
	 * Stellt die Lösungseingabe im laufenden Spiel dar und gibt dem Spieler
	 * gezielte Hinweise bei falscher Eingabe
	 */
	private static void playerInput(Cells[][] sudokuCells) {
		// Anweisungen für Spieler
		System.out.println(
				"\n" + "Welche Zelle möchtest du befüllen? Bitte gib deine Wahl in der Form 'A3 3' (Koordinate + Leerzeichen + deinen Wert) ein.\n"
						+ "Mit '1' kannst du das Spiel jederzeit beenden");
		// Eingabe auslesen, Konvertierung zu Kleinbuchstaben
		String completeInput = scanner.nextLine().toLowerCase();
		// Exception handling für Eingabe von Buchstaben/Strings
		try {
			// Fehler in der Eingabe'form' (Länge, Leerzeichen) behandeln
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
				} else if (completeInput.equalsIgnoreCase("Lösen")) {
					// Lösung des Sudokus berechnen
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
					 * bearbeiten zu können und als Strings speichern.
					 * \\s+ regex für Whitespace (regular expression)
					 */
					String input[] = completeInput.split("\\s+");
					/*
					 * Den eingegebenen Wert zur Zellenfüllung aus String lesen
					 * und auf formelle Korrektheit prüfen
					 */
					int playerValue = Integer.parseInt(input[1]);
					// Prüfe ob die Zahl zwischen 1 - 9 ist
					if (playerValue < 0 || playerValue > 9) {
						// Mitteilung fehlerhafte Eingabe
						System.err.println("Deine Eingabe war leider fehlerhaft.\n"
								+ "Der Wert, den du für die Zelle setzen möchtest, sollte die Zahlen 1-9 abdecken.");
						// Spieler Eingabe neu starten
						playerInput(sudokuCells);
					} else {
						/*
						 * Den Buchstaben (Zeilenbezeichnung) an erster Stelle
						 * der Eingabe auslesen Buchstaben in jeweiliges
						 * ASCII-Äquivalent umwandeln (a = 97, b = 98...), als
						 * int speichern und auf formelle Korrektheit prüfen
						 */
						int chosenRow = (int) input[0].charAt(0) - 97;
						// Prüfe ob Koordinate für Zeilen existiert
						if (chosenRow < 0 || chosenRow > 8) {
							// Mitteilung fehlerhafte Eingabe
							System.err
									.println("Deine Zeilekoordinate hat leider nicht gepasst. Bitte wähle Zeile A-I!");
							// Spieler Eingabe neu starten
							playerInput(sudokuCells);
						} else {
							/*
							 * Liest aus der eingegebenen Koordinate den
							 * character an der zweiten Stelle, wandelt es in
							 * einen int um und speichert diesen. Ebenfalls wird
							 * auf formelle Korrektheit geprüft
							 */
							String numberOnly = input[0].charAt(1) + "";
							// parse String zu int der x-Koordinate
							int chosenColumn = Integer.parseInt(numberOnly) - 1;
							// Prüfe ob Koordinate für Spalte existiert
							if (chosenColumn < 0 || chosenColumn > 8) {
								// Mitteilung fehlerhafte Eingabe
								System.err.println(
										"Deine Spaltenkoordinate hat leider nicht gepasst. Bitte wähle Spalte 1-9!");
								// Spieler Eingabe neu starten
								playerInput(sudokuCells);
							} else {
								// Prüfe Wert der Spieler Eingabe
								if (check.valIsAllowed(sudokuCells, sudokuCells[chosenRow][chosenColumn], playerValue)
										&& !sudokuCells[chosenRow][chosenColumn].isFixVal()) {
									/*
									 * Einzusetzender Wert ist nach aktuellem
									 * Lösungsstand erlaubt und die ausgewählte
									 * Zelle enthält keinen Startwert. Wert
									 * speichern und Rückmeldung an Spieler,
									 * dass Eingabe erfolgreich war.
									 */
									sudokuCells[chosenRow][chosenColumn].setValue(playerValue);
									System.out.println("Wert " + playerValue + " in Reihe " + (chosenRow + 1)
											+ " und Spalte " + (chosenColumn + 1) + " eingetragen.");
									// Ausgabe Sudoku Spielfeld
									drawBoard();
								} else if (playerValue == 0 && !sudokuCells[chosenRow][chosenColumn].isFixVal()) {
									/*
									 * Spieler möchte Zelle löschen (Wert = 0)
									 * und die Zelle enthält keinen Startwert.
									 * Wert speichern und Rückmeldung an
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
											"Leider ist dein gewünschter Wert hier nicht möglich. Versuche es nocheinmal!");
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
			System.err.println("Leider ist dein gewünschter Wert hier nicht möglich. Versuche es nocheinmal!");
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
	 * Generiert ein leeres Sudoku Spielfeld indem die benötigten Cells Objects
	 * initialisiert werden und die Linked List verknüpft wird.
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
		// Verknüpfe Linked List neu
		solve.resetLinkedList(sudokuCells, false);
	}

	/**
	 * Fügt in der ersten Zeile des Sudoku Spielfelds die Zahlen 1 - 9 zufällig
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
	private static void insertFixVal(Cells[][] sudokuCells, int x, int y, int value) {
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
	private static Cells firstEmptyCell(Cells currentCell) {
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
	 * Löst das Spieler-Sudoku, indem die eingegebenen Werte als fix gesetzt
	 * werden, die Linked-List zurückgesetzt wird und der Algorithmus seine
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
					// Ist Wert vorhanden, überschreibe als fixen Wert
					insertFixVal(sudokuCells, j, i, sudokuCells[i][j].getValue());
				}
			}
		}
		// Verknüpfe Linked List neu
		solve.resetLinkedList(sudokuCells, false);
		// Lösungsalgorithmus löst Sudoku
		solve.solveSudoku(sudokuCells, firstEmptyCell(sudokuCells[0][0]));
		// Ausgabe Sudoku Spielfeld
		drawBoard();
		// Mitteilung an Spieler, dass Lösung gefunden wurde.
		System.out.println("Dies ist die Lösung!");
		exit();
	}

	/**
	 * wann immer das Spiel beendet werden soll, reicht jetzt exit();
	 */
	private static void exit() {
		// Abschiedsnachricht an Spieler
		System.out.println("Schüss!");
		// Beenden
		System.exit(1);
	}
}