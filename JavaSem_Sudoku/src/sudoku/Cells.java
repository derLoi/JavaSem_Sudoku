package sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Die Cells Klasse erfasst alle Informationen die zu den einzelnen Zellen des
 * Sudokus geh�ren und stellt Funktionen zur Manipulation der Inhalte bereit.
 * 
 * @version 16/01/2016
 * <br>LS: Methoden setFixVal(), pickValueFromList(), listToArray() und
 *             remValVromPosVals() hinzugef�gt
 */
public class Cells {
	// L�sungswert der Zelle, 0 f�r leere Zellen
	private int value;
	// Object der zuvor bearbeiteten Zelle
	private Cells lastCell;
	// Object der als n�chstes bearbeiteten Zelle
	private Cells nextCell;
	// X/Y-Index der Zelle im Sudoku Array
	private int x;
	private int y;
	// Kennzeichnet einen Startwert (true) oder Spielerwert (false)
	private boolean fixVal;
	// Liste aller m�glichen Werte dieser Zelle
	List<Integer> posVal = new ArrayList<Integer>();
	// Liste aller ausgeschlossenen Werte dieser Zelle
	List<Integer> excVal = new ArrayList<Integer>();

	/**
	 * Gibt die L�sungszahl der Zelle zur�ck.
	 * 
	 * @return der Wert der Zelle
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Speichere einen neuen wert als L�sungszahl der Zelle.
	 * 
	 * @param value
	 *            der Wert, der als L�sungswert gespeichert wird.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Gibt das Cells Object der letzten Zelle zur�ck.
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
	 * Gibt das Cells Object der n�chsten Zelle zur�ck.
	 * 
	 * @return die n�chste Zelle.
	 */
	public Cells getNextCell() {
		return nextCell;
	}

	/**
	 * Speichere ein Cells Object als n�chste Zelle der Zelle
	 * 
	 * @param nextCell
	 *            das Cells Object, dass als n�chste Zelle gespeichert wird.
	 */
	public void setNextCell(Cells nextCell) {
		this.nextCell = nextCell;
	}

	/**
	 * Gibt die X-Koordinate der Zelle zur�ck.
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
	 * Gibt die Y-Koordinate der Zelle zur�ck.
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
	 * Gibt den Status des Wertes der Zelle zur�ck. <br>
	 * <ul>
	 * <li>true: der Wert darf nicht mehr ver�ndert werden und ist ein
	 * Startwert.</li>
	 * <li>false: der Wert darf ver�ndert werden und ist kein Startwert.</li>
	 * </ul>
	 * 
	 * @return der Status der Zelle
	 */
	public boolean isFixVal() {
		return fixVal;
	}

	/**
	 * Speichert, ob die Zelle einen Startwert enth�lt, der nicht mehr ver�ndert
	 * werden darf.
	 * <ul>
	 * <li>true: der Wert darf nicht mehr ver�ndert werden und ist ein
	 * Startwert.</li>
	 * <li>false: der Wert darf ver�ndert werden und ist kein Startwert.</li>
	 * </ul>
	 * 
	 * @param fixVal
	 *            der neue Status der Zelle.
	 */
	public void setFixVal(boolean fixVal) {
		this.fixVal = fixVal;
	}

	/**
	 * Constructor der Cells Class. Legt die Startwerte f�r ein Cells Object
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
	 * W�hlt aus der Liste der m�glichen Werte (posVal) einen zuf�lligen Wert
	 * und speichert diesen als neuen L�sungswert der Zelle. Ist die Liste der
	 * m�glichen Werte leer, wird einen 0 als L�sungswert gespeichert.
	 */
	public void pickValueFromList() {
		// Random Object erstellen
		Random rnd = new Random();
		// Pr�fen ob posVal Werte enth�lt
		if (posVal.isEmpty()) {
			// Ist posVal leer, wird 0 gespeichert
			value = 0;
		} else {
			/*
			 * Enth�lt posVal Werte, wird eine zuf�llige Zahl zwischen [0;l�nge
			 * posVal[ gew�hlt und als L�sungswert gespeichert.
			 */
			value = posVal.get(rnd.nextInt(posVal.size()));
			// Entferne den gefundenen L�sungswert aus posVal
			posVal.remove(posVal.indexOf(value));
		}
	}

	/**
	 * Wandelt eine �bergebene Liste in ein Array vom Typ int um.
	 * 
	 * @param list
	 *            die Liste, aus der ein Array erstellt werden soll.
	 * @return das Array, dass die Werte der Liste enth�lt.
	 */
	public int[] listToArray(List<Integer> list) {
		// Array initialisieren mit der L�nge der Liste
		int[] ary = new int[list.size()];
		// Pr�fen ob die Liste leer ist
		if (list.size() == 0) {
			// Ist die Liste leer, wird ein leeres Array zur�ck gegeben
			return ary;
		}
		// Schleife von [0;l�nge list[
		for (int i = 0; i < list.size(); i++) {
			// Jedes Element mit Index i der Liste wird mit Index i im Array
			// gespeichert
			ary[i] = list.get(i);
		}
		// R�ckgabe des Array
		return ary;
	}

	/**
	 * F�gt einen Wert zur Liste der ausgeschlossenen Werte hinzu.
	 * 
	 * @param remVal
	 *            neuer ausgeschlossener Wert.
	 */
	public void addValueToExcVals(int remVal) {
		// Neuen Wert zu Liste excVal hinzuf�gen
		this.excVal.add(remVal);
		// L�sungswert der Zelle zur�cksetzen
		this.value = 0;
	}
}
