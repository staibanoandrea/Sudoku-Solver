/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

/**
 *
 * @author Admin1
 */

public class Sudoku {
    int[][] sudokuBoard;
    boolean[][] isCorrectBoard;
    
    public Sudoku(int sudokuBoard[][], boolean isCorrectBoard[][]) {
        this.sudokuBoard = sudokuBoard;
        this.isCorrectBoard = isCorrectBoard;
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                isCorrectBoard[i][j] = true;
            }
        }
    }
    
    public boolean rowIsValid(int riga, int colonna){
        for (int i = 0; i < 9 ; i++){
            if (i != colonna){
                if (sudokuBoard [riga][i] == sudokuBoard [riga][colonna]){
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean columnIsValid(int riga, int colonna){
        for (int i = 0; i < 9 ; i++){
            if (i != riga){    
                if (sudokuBoard [i][colonna] == sudokuBoard [riga][colonna]){
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean blockIsValid(int riga, int colonna){
        for (int bloccoH = riga / 3 * 3; bloccoH < riga / 3 * 3 + 3; bloccoH++){
                for (int bloccoV = colonna / 3 * 3; bloccoV < colonna / 3 * 3 + 3; bloccoV++){
                    if ((bloccoH != riga) || (bloccoV != colonna)){
                        if (sudokuBoard [bloccoH][bloccoV] == sudokuBoard [riga][colonna]){
                            return false;
                        }
                    }
                }
            }
        return true;
    }
    
    public boolean isValid(int riga, int colonna){
	return rowIsValid(riga, colonna) && columnIsValid(riga, colonna) && blockIsValid(riga, colonna);
    }

//metodo inutilizzato    
/*    public boolean isFull(){
        for (int riga = 0; riga < 9; riga++){
            for (int colonna = 0; colonna < 9; colonna++){
                if (sudokuBoard [riga][colonna] == 0){
                    return false;
                }
            }
        }
        return true;
    }*/
    
    public boolean isCorrect(){
        for (int riga = 0; riga < 9; riga++){
            for (int colonna = 0; colonna < 9; colonna++){
                if (isCorrectBoard [riga][colonna] == false){
                    return false;
                }
            }
        }
        return true;
    }

//metodo inutilizzato    
/*    public boolean isSolved(){
        return isFull() && isCorrect();
    }*/
    
    //metodo di risoluzione: backtracking
    //il metodo parte dalla riga r e dalla colonna c
    public boolean solve(int r, int c, int[][] sudokuBoard){
        if (c == 9) {
            c = 0;
            if (++r == 9)
                return true;
        }
        if (sudokuBoard[r][c] != 0)  // skip filled cells
            return solve(r, c+1, sudokuBoard);

        for (int i = 1; i <= 9; ++i) {
            sudokuBoard[r][c] = i;
            if (isValid(r,c)) {
                if (solve(r, c+1, sudokuBoard))
                    return true;
            }
        }
        sudokuBoard[r][c] = 0; // reset on backtrack
        return false;
    }
}
