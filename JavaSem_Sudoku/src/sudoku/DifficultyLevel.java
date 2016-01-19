package sudoku;

import java.util.*;
/**
 * 
 * @version 19/01/16
 * Klasse für (erstmal nur) die Schwerigkeitsstufen + die Range (min max) der leeren Zellen
 *
 */
public class DifficultyLevel {

	SEHR_LEICHT(28,30),
	LEICHT(31,44),
	MITTEL(45,49),
	SCHWER(49,54),
	SCHWER_DES_TODES(55,61);

	private final int min;
	private final int max;
	
	//Konstruktor
	DifficultyLevel (int min, int max){
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Bewirkt eine unterschiedliche Anzahl von freien Zellen in jedem Spiel
	 * 
	 * @return random Nummer im Bereich der minimalen bis maximalen Zahl der freien Zellen
	 */
	public int getBlankCellsAmount(){
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
	
}
