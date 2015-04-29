package cellularland1;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Deas
 */
public class GridController {
    private final GridPane grid;
    private final Button[][] buttons;
    private final int size;
    
    public GridController(GridPane grid) {
        this.grid = grid;
        this.size = grid.getColumnConstraints().size();
        this.buttons = new Button[size][];
        
        for(int i = 0; i < size; i++) {
            buttons[i] = new Button[size];
            for(int j = 0; j < size; j++) {
                buttons[i][j] = new Button();         
                
                GridPane.setRowIndex(buttons[i][j], i);
                GridPane.setColumnIndex(buttons[i][j], j);
                grid.getChildren().add(buttons[i][j]);
                
                buttons[i][j].setVisible(true);
                buttons[i][j].getStyleClass().remove("button");
                buttons[i][j].getStyleClass().add("gridbutton");
                
                // Temporary solution to get the pictures:
                buttons[i][j].setOnAction(event -> { 
                    if(((Button)(event.getSource())).getStyleClass().contains("gridbutton")) {
                        ((Button)(event.getSource())).getStyleClass().remove("gridbutton");
                        ((Button)(event.getSource())).getStyleClass().add("gridbuttonpressed");
                    } else {
                        ((Button)(event.getSource())).getStyleClass().remove("gridbuttonpressed");
                        ((Button)(event.getSource())).getStyleClass().add("gridbuttonalive");
                    }
                });
                
                grid.setVisible(true);
            }
        }
        
    }
    
    public GridPane getGrid() { return grid; }
    
    public Node getNode(final int row, final int column) {
        Node result = null;
        ObservableList<Node> childrens = grid.getChildren();
        for(Node node : childrens) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
}
