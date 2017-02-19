/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.TRANSPARENT;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Admin1
 */
public class SudokuFXMLController implements Initializable {
    @FXML
    private GridPane grid;
    @FXML
    private Button solveButton;
    @FXML
    private Label sudokuStatus;
    
    private final int[][] sudokuBoard = new int[9][9];
    private final boolean[][] isCorrectBoard = new boolean[9][9];
    
    private final Sudoku sudoku = new Sudoku(sudokuBoard, isCorrectBoard);
    
    //metodo per ottenere una label date le sue coordinate nella griglia
    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (int i = 1; i < childrens.size(); i++) {
            Node node = childrens.get(i);
            if((GridPane.getRowIndex(node) == row) && (GridPane.getColumnIndex(node) == column)) {
                result = node;
                break;
            }
        }
        return result;
    }
    
    public void checkCella (int riga, int colonna){
        Label cellachecked = (Label) getNodeByRowColumnIndex(riga, colonna, grid);
            if (sudoku.isValid(riga, colonna)){
                isCorrectBoard [riga][colonna] = true;
                cellachecked.setTextFill(BLACK);
            }
            else{
                isCorrectBoard [riga][colonna] = false;
                cellachecked.setTextFill(Paint.valueOf("RED"));
            }
    }
    
    // Initializes the controller class.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grid.setGridLinesVisible(true);
        for(int i = 0; i < 9; i++) {
            grid.getRowConstraints().add(new RowConstraints(50));
            grid.getColumnConstraints().add(new ColumnConstraints(50));
            for(int j = 0; j < 9; j++) {
                
                Label cella = new Label();
                cella.setFont(new Font("Arial", 50)); 
                cella.setTextFill(TRANSPARENT);
                cella.setText("0");
                cella.setOnMouseClicked((MouseEvent click) -> {
                    int riga = GridPane.getRowIndex(cella);
                    int colonna = GridPane.getColumnIndex(cella);
                    
                    //CLICK SINISTRO
                    if(click.getButton().equals(MouseButton.PRIMARY)) {
                        sudokuBoard[riga][colonna] = (sudokuBoard[riga][colonna] + 1) %10;
                    }
                    //CLICK DESTRO
                    else if(click.getButton().equals(MouseButton.SECONDARY)) {
                        sudokuBoard[riga][colonna] = 0;
                    }                                           
                    
                    if(sudokuBoard[riga][colonna]==0) {
                        isCorrectBoard [riga][colonna] = true;
                        cella.setTextFill(TRANSPARENT);
                    }
                    
                    //controlla la correttezza reciproca dei valori sulla riga dell'ultimo inserimento:                        
                    for (int c = 0; c < 9; c++){
                        if (sudokuBoard[riga][c] != 0){    //salta il check se la cella vale 0;
                            checkCella (riga, c);
                        }
                    }
                    //sulla colonna:
                    for (int r = 0; r < 9; r++){
                        if (r != riga){ //if r == riga -> sudokuBoard[riga][colonna], che è già stato controllato prima;
                            if (sudokuBoard[r][colonna] != 0){
                                checkCella (r, colonna);
                            }
                        }
                    }
                    //e sul blocco 3x3:
                    for (int bloccoH = riga / 3 * 3; bloccoH < riga / 3 * 3 + 3; bloccoH++){
                        for (int bloccoV = colonna / 3 * 3; bloccoV < colonna / 3 * 3 + 3; bloccoV++){
                            if ((bloccoH != riga) && (bloccoV != colonna)){ //salto i check già effettuati prima
                                if (sudokuBoard[bloccoH][bloccoV] != 0){
                                    checkCella (bloccoH, bloccoV);
                                }
                            }
                        }
                    }
                    cella.setText(String.valueOf(sudokuBoard[riga][colonna]));
                    if (sudoku.isCorrect()){
                        sudokuStatus.setTextFill(TRANSPARENT);
                    }
                });
                
                grid.add(cella, j, i);
                GridPane.setHalignment(cella, HPos.CENTER);
                GridPane.setValignment(cella, VPos.CENTER);
                
                //bottone per la risoluzione del sudoku:
                solveButton.setOnMouseClicked((MouseEvent click) -> {
                    if(click.getButton().equals(MouseButton.PRIMARY)) {
                        if (sudoku.isCorrect()){
                            //avvia solve dall'inizio
                            if (sudoku.solve(0, 0, sudokuBoard)){
                                for (int r = 0; r < 9; r++){
                                    for (int c = 0; c < 9; c++){
                                        Label cellaDaColorare = (Label) getNodeByRowColumnIndex(r, c, grid);
                                        cellaDaColorare.setText(String.valueOf(sudokuBoard[r][c]));
                                        cellaDaColorare.setTextFill(BLACK);
                                    }
                                }
                            }
                            else{
                                sudokuStatus.setTextFill(Paint.valueOf("RED"));
                            }
                        }
                        else{
                            sudokuStatus.setTextFill(Paint.valueOf("RED"));
                        }
                    }
                });
            }
        }
    }    
    
}
