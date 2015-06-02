package cellularland1;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * The controller of the grid with the main board. It controls the actions
 * taken after clicking on a button in the board and provides an interface 
 * for access to the specific nodes.
 *
 * @author Jakub Sosnovec
 */
public class GridController {
    /** The container of all the nodes. */
    private final GridPane grid;
    
    /** The array with all the nodes that are children in the grid.
     first index is row, second index is column.*/
    private final ButtonNode[][] buttons;
    
    /** Number of row and columns. Currently the board has to have square 
     shape, maybe it will be changed to rectangular later.*/
    private final int size;
    
    /** Mana controller. I need this instance to properly set the listeners
      to clicking the buttons. */
    private final ManaController manaController;
    
    /** Constructor that initializes the gird. It adds the array buttons as
     children of the grid container. At first, all the nodes are hidden yet.*/
    public GridController(GridPane grid, ManaController mc) {
        this.grid = grid;
        this.size = grid.getColumnConstraints().size();
        this.buttons = new ButtonNode[size][];
        this.manaController = mc;
        
        for(int i = 0; i < size; i++) {
            buttons[i] = new ButtonNode[size];
            for(int j = 0; j < size; j++) {
                buttons[i][j] = new ButtonNode(new Button());         
                
                GridPane.setRowIndex(buttons[i][j].getButton(), i);
                GridPane.setColumnIndex(buttons[i][j].getButton(), j);
                grid.getChildren().add(buttons[i][j].getButton());
                
                buttons[i][j].getButton().setVisible(true);
                buttons[i][j].getButton().getStyleClass().remove("button");
                buttons[i][j].getButton().getStyleClass().add("gridbutton");
                buttons[i][j].getButton().setId(i + "," + j);
                
                buttons[i][j].getButton().minHeightProperty().bind(grid.heightProperty().divide(size*1.0).subtract(4));
                buttons[i][j].getButton().maxHeightProperty().bind(grid.heightProperty().divide(size*1.0).subtract(4));
                
                buttons[i][j].getButton().minWidthProperty().bind(grid.widthProperty().divide(size*1.0).subtract(4));
                buttons[i][j].getButton().maxWidthProperty().bind(grid.widthProperty().divide(size*1.0).subtract(4));
                
                // Temporary solution to get the pictures:
                buttons[i][j].getButton().setOnAction(event -> { 
                    // first I will check if there is enough mana to click the button
                    // if not, nothing happens, if yea, i switch the button
                    // I also need to notify the mechanics of this
                    // problem: how to notify mechanics when stopping the automaton ends?
                    // do I need to? I don't. Just send it what happened (i.e a button was
                    // made alive, or discovered and the mechanics will just accept that
                    Button b = (Button)event.getSource();
                    String[] indices = b.getId().split(",");
                    manaController.cellClick(buttons[Integer.parseInt(indices[0])][Integer.parseInt(indices[1])]);
                });
                
                grid.setVisible(true);
            }
        }
        
    }  
    
    /** The getter of grid. Currently is public. */
    public GridPane getGrid() { return grid; }
    
    /** Gets the node with specified coordinates. */
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
