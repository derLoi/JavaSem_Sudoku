package sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Die Cells Klasse erfasst alle Informationen die zu den einzelnen Zellen des
 * Sudokus gehören und stellt Funktionen zur Manipulation der Inhalte bereit.
 * 
 * @version 16/01/2016
 * <br>LS: Methoden setFixVal(), pickValueFromList(), listToArray() und
 *             remValVromPosVals() hinzugefügt
 */
public class Cells {
	// Lösungswert der Zelle, 0 für leere Zellen
	private int value;
	// Object der zuvor bearbeiteten Zelle
	private Cells lastCell;
	// Object der als nächstes bearbeiteten Zelle
	private Cells nextCell;
	// X/Y-Index der Zelle im Sudoku Array
	private int x;
	private int y;
	// Kennzeichnet einen Startwert (true) oder Spielerwert (false)
	private boolean fixVal;
	// Liste aller möglichen Werte dieser Zelle
	List<Integer> posVal = new ArrayList<Integer>();
	// Liste aller ausgeschlossenen Werte dieser Zelle
	List<Integer> excVal = new ArrayList<Integer>();

	/**
	 * Gibt die Lösungszahl der Zelle zurück.
	 * 
	 * @return der Wert der Zelle
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Speichere einen neuen wert als Lösungszahl der Zelle.
	 * 
	 * @param value
	 *            der Wert, der als Lösungswert gespeichert wird.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Gibt das Cells Object der letzten Zelle zurück.
	 * 
	 * @return die letzte Zelle.
	 */
	public Cells getLastCell() {
		return lastCell;
	}

	/**
	 * Speichere ein Cells Object als letzte Zelle der Zelle.
	 * 
	 * @param lastCell
	 *            das Cells Object, dass als letzte Zelle gespeichert wird.
	 */
	public void setLastCell(Cells lastCell) {
		this.lastCell = lastCell;
	}

	/**
	 * Gibt das Cells Object der nächsten Zelle zurück.
	 * 
	 * @return die nächste Zelle.
	 */
	public Cells getNextCell() {
		return nextCell;
	}

	/**
	 * Speichere ein Cells Object als nächste Zelle der Zelle
	 * 
	 * @param nextCell
	 *            das Cells Object, dass als nächste Zelle gespeichert wird.
	 */
	public void setNextCell(Cells nextCell) {
		this.nextCell = nextCell;
	}

	/**
	 * Gibt die X-Koordinate der Zelle zurück.
	 * 
	 * @return die X-Koordinate der Zelle.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Speichert eine neue X-Koordinate der Zelle.
	 * 
	 * @param x
	 *            die neue X-Koordinate.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gibt die Y-Koordinate der Zelle zurück.
	 * 
	 * @return die Y-Koordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Speichert eine neue Y-Koordinate der Zelle.
	 * 
	 * @param y die neue Y-Koordinate.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gibt den Status des Wertes der Zelle zurück. <br>
	 * <ul>
	 * <li>true: der Wert darf nicht mehr verändert werden und ist ein
	 * Startwert.</li>
	 * <li>false: der Wert darf verändert werden und ist kein Startwert.</li>
	 * </ul>
	 * 
	 * @return der Status der Zelle
	 */
	public boolean isFixVal() {
		return fixVal;
	}

	/**
	 * Speichert, ob die Zelle einen Startwert enthält, der nicht mehr verändert
	 * werden darf.
	 * <ul>
	 * <li>true: der Wert darf nicht mehr verändert werden und ist ein
	 * Startwert.</li>
	 * <li>false: der Wert darf verändert werden und ist kein Startwert.</li>
	 * </ul>
	 * 
	 * @param fixVal
	 *            der neue Status der Zelle.
	 */
	public void setFixVal(boolean fixVal) {
		this.fixVal = fixVal;
	}

	/**
	 * Constructor der Cells Class. Legt die Startwerte für ein Cells Object
	 * fest.
	 * 
	 * @param x
	 *            die X-Koordinate der Zelle.
	 * @param y
	 *            die Y-Koordinate der Zelle.
	 */
	public Cells(int x, int y) {
		// Koordinaten
		this.x = x;
		this.y = y;
		// Leere Zelle
		this.value = 0;
		// Linked List leer erstellen
		this.lastCell = null;
		this.nextCell = null;
		// Status der Zelle setzen (kein Startwert)
		this.fixVal = false;
	}

	/**
	 * Wählt aus der Liste der möglichen Werte (posVal) einen zufälligen Wert
	 * und speichert diesen als neuen Lösungswert der Zelle. Ist die Liste der
	 * möglichen Werte leer, wird einen 0 als Lösungswert gespeichert.
	 */
	public void pickValueFromList() {
		// Random Object erstellen
		Random rnd = new Random();
		// Prüfen ob posVal Werte enthält
		if (posVal.isEmpty()) {
			// Ist posVal leer, wird 0 gespeichert
			value = 0;
		} else {
			/*
			 * Enthält posVal Werte, wird eine zufällige Zahl zwischen [0;länge
			 * posVal[ gewählt und als Lösungswert gespeichert.
			 */
			value = posVal.get(rnd.nextInt(posVal.size()));
			// Entferne den gefundenen Lösungswert aus posVal
			posVal.remove(posVal.indexOf(value));
		}
	}

	/**
	 * Wandelt eine übergebene Liste in ein Array vom Typ int um.
	 * 
	 * @param list
	 *            die Liste, aus der ein Array erstellt werden soll.
	 * @return das Array, dass die Werte der Liste enthält.
	 */
	public int[] listToArray(List<Integer> list) {
		// Array initialisieren mit der Länge der Liste
		int[] ary = new int[list.size()];
		// Prüfen ob die Liste leer ist
		if (list.size() == 0) {
			// Ist die Liste leer, wird ein leeres Array zurück gegeben
			return ary;
		}
		// Schleife von [0;länge list[
		for (int i = 0; i < list.size(); i++) {
			// Jedes Element mit Index i der Liste wird mit Index i im Array
			// gespeichert
			ary[i] = list.get(i);
		}
		// Rückgabe des Array
		return ary;
	}

	/**
	 * Fügt einen Wert zur Liste der ausgeschlossenen Werte hinzu.
	 * 
	 * @param remVal
	 *            neuer ausgeschlossener Wert.
	 */
	public void addValueToExcVals(int remVal) {
		// Neuen Wert zu Liste excVal hinzufügen
		this.excVal.add(remVal);
		// Lösungswert der Zelle zurücksetzen
		this.value = 0;
	}
}
