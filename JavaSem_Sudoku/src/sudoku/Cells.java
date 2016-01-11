package sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cells {
	private int value; // current value
	private Cells lastCell;
	private Cells nextCell;
	private int x; // x-Index of cell
	private int y; // y-Index of cell
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

	// constructor
	public Cells(int x, int y, Cells lastCell) {
		this.x = x;
		this.y = y;
		this.lastCell = lastCell;
		this.value = 0;
	}

	public void pickValueFromList() {
		Random rnd = new Random();
		int temp = -1;
		if (posVal.isEmpty()) {
			value = 0;
		} else {
			value = posVal.get(rnd.nextInt(posVal.size()));
			if (posVal.get(0) != value) {
				temp = posVal.get(0);
				posVal.set(0, value);
				posVal.add(temp);
			}
			// System.out.println("Value picked: "+value+" Value moved: "+temp);
		}
	}

	public int[] listToArray(List<Integer> list){
		int[] ary = new int[list.size()];
		if (list.size() == 0){
			return ary;
		}
		for (int i = 0; i <= (list.size() -1); i++){
			ary[i] = list.get(i);
		}
		return ary;
	}
}
