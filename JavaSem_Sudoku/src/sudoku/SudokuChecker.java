package sudoku;

public class SudokuChecker {
	/*
	 * @author: LS
	 * @version: 10/12/2015
	 * @changelog:
	 * @param: 2-dim Integer Array, dass den aktuellen Spielstand enthält
	 * @return: Boolean zeigt an, ob Sudoku gültig ist
	 * @desc: Überprüft, ob das Sudoku gültig ist
	 */
	public boolean checkSudoku(int[][] sudoku) {
		int[] row = new int[9];
		int[] col = new int[9];
		int[][] box = new int[9][9];
		boolean[] errInRow = new boolean[9];
		boolean[] errInCol = new boolean[9];
		boolean[] errInBox = new boolean[9];
		
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				row[j] = sudoku[i][j];
				col[j] = sudoku[j][i];
			}
			errInRow[i] = checkRow(row);
			errInCol[i] = checkRow(col);
		}
		return false;
	}

	public boolean checkRow(int[] row) {
		boolean temp = true;
		for (int i = 0; i <= 4;i++){
			for (int j = i; j <= 8; j++){
				if(row[i]==row[j]){
					temp = false;
				}
			}
		}
		return temp;
	}

	public boolean checkBox(int[][] box) {

	}
}
