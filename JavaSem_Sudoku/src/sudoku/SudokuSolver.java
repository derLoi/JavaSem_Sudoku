package sudoku;

public class SudokuSolver {
	// 1. erstes oder nächstes freies Feld (0) wählen
	// 2. Beschränkung Zeile/Spalte/Box: alle möglichen Zahlen auswählen
	public int[] getNextEmptyCell(int[][] matrixAktuellerSpielstand){
		int[] cell = new int[2];
		for (int i = 0; i <= 8; i++){
			for (int j = 0; j <= 8; j++){
				if (matrixAktuellerSpielstand[i][j] == 0){
					cell[0]= i;
					cell[1] = j;
					return cell;
				}
			}
		}
		cell[0] = 9;	// Keine leere Zelle mehr übrig
		return cell; 
	}
}
