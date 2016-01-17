package sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/*
 * @author: LS 
 * @version: 16/01/2016
 * @changelog:
 * 		LS: Methoden setFixVal(), pickValueFromList(), 
 * 		listToArray() und remValVromPosVals() hinzugefügt		
 * @desc: Die Cells Klasse erfasst alle Informationen die 
 * zu den einzelnen Zellen des Sudokus gehören und stellt 
 * Funktionen zur Manipulation der Inhalte bereit.
 */
public class Cells {
	private int value; // Aktueller Wert, 0 für leere Zellen
	private Cells lastCell; // Object der zuvor bearbeiteten Zelle
	private Cells nextCell; // Object der als nächstes bearbeiteten Zelle
	private int x; // x-Index der Zelle im Sudoku Array
	private int y; // y-Index der Zelle im Sudoku Array
	private boolean fixVal; // 
	List<Integer> posVal = new ArrayList<Integer>(); // List of possible Values
														// (int) for this cell
	List<Integer> excVal = new ArrayList<Integer>(); // List of excluded Values
														// (int) for this cell

	// Getters and Setters no documentation yet
	// @author: LS 07/01/2016
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Cells getLastCell() {
		return lastCell;
	}

	public void setLastCell(Cells lastCell) {
		this.lastCell = lastCell;
	}

	public Cells getNextCell() {
		return nextCell;
	}

	public void setNextCell(Cells nextCell) {
		this.nextCell = nextCell;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isFixVal() {
		return fixVal;
	}

	public void setFixVal(boolean fixVal) {
		this.fixVal = fixVal;
	}

	// constructor
	public Cells(int x, int y) {
		this.x = x;
		this.y = y;
		this.value = 0;
		this.lastCell = null;
		this.nextCell = null;
		this.fixVal = false;
	}

	public void pickValueFromList() {
		Random rnd = new Random();
		if (posVal.isEmpty()) {
			value = 0;
		} else {
			value = posVal.get(rnd.nextInt(posVal.size()));
			posVal.remove(posVal.indexOf(value));
		}
	}

	public int[] listToArray(List<Integer> list) {
		int[] ary = new int[list.size()];
		if (list.size() == 0) {
			return ary;
		}
		for (int i = 0; i <= (list.size() - 1); i++) {
			ary[i] = list.get(i);
		}
		return ary;
	}

	public void remValFromPosVals(int remVal) {
		// System.out.println("posVal: "+this.posVal.toString());
		this.excVal.add(remVal);
		// System.out.println("excVal: "+this.excVal.toString());
		this.value = 0;
	}
}
