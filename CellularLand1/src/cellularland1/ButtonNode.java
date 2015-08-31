
package cellularland1;

import javafx.scene.control.Button;

/**
 * Wrap around a button, used to easily distinguish the state of the button.
 *
 * @author Jakub Sosnovec
 */
public class ButtonNode {
    private final Button button;
    /** The state - tells if the node is hidden or revealed and if it is
     alive or dead.*/
    private State state;
    
    public ButtonNode(Button b) {
        this.button = b;
        // What should be the initial state?
        // FOR TESTING PURPOSES:
        state = State.REVEALED_DEAD;
        update();
    }
    /**
     * The button was clicked, the proper action is taken.
     * @param autoStopped Was the automaton stopped while the button was clicked.
     */
    public void clicked(boolean autoStopped) {
        if(!autoStopped) {
            switch(state) {
                case HIDDEN_ALIVE: 
                    state = State.REVEALED_ALIVE;;
                    break;
                case HIDDEN_DEAD:
                    state = State.REVEALED_DEAD;
                    break;
                default: return;
            }
            update();
        } else {
            String[] indices = button.getId().split(",");
            switch(state) {
//                case HIDDEN_ALIVE: 
//                    state = State.REVEALED_ALIVE;
//                    break;
//                case HIDDEN_DEAD:
//                    state = State.REVEALED_DEAD;
//                    break;
                case REVEALED_ALIVE:
                    state = State.REVEALED_DEAD;
                    Mechanics.inst.changed(Integer.parseInt(indices[0]),Integer.parseInt(indices[1]));
                    break;
                case REVEALED_DEAD:
                    state = State.REVEALED_ALIVE;
                    Mechanics.inst.changed(Integer.parseInt(indices[0]),Integer.parseInt(indices[1]));
                    break;
                default: return;
            }
            update();
        }
    }
    /** Set the color of the button according to its state. */
    private void update() {
        switch(state) {              
            case REVEALED_ALIVE:
                button.setStyle("-fx-background-color: cornflowerblue");
                break;
            case REVEALED_DEAD:
                button.setStyle("-fx-background-color: #c1c1c1");
                break;
            default:
                button.setStyle("-fx-background-color: #1d1d1d");
                break;
        }
    }
    /**
     * Set the color of the button border according to the rule used.
     * @param rule The number of the rule used. 0 <= i <= 3
     */
    public void seeUsedRule(int rule) {
        if(state == State.REVEALED_ALIVE) {
            String str = "";
            switch(rule) {
                case -1: str = "-fx-background-color: cornflowerblue";
                    break;
                case 0: str = "-fx-background-color: limegreen;";
                    break;
                case 1: str = "-fx-background-color: darkorchid;";
                    break;
                case 2: str = "-fx-background-color: mediumblue;";
                    break;
                case 3: str = "-fx-background-color: olive;";
                    break;
                default:
                    throw new IllegalArgumentException("Only 4 colors for rule usage supported!");
            }
            button.setStyle(str);
        }
    }
    /** Set this node to alive, but keep the status of revelation. */
    public void birth() {
        if(!this.isAlive()) {
            if(this.isRevealed())
                state = State.REVEALED_ALIVE;
            else 
                state = State.HIDDEN_ALIVE;
        } 
        update();
    }   
    /** Set this node to dead, but keep the status of revelation. */
    public void die() {
        if(this.isAlive()) {
            if(this.isRevealed())
                state = State.REVEALED_DEAD;
            else 
                state = State.HIDDEN_DEAD;
        } 
        update();
    }
    /** Is this node alive */
    public boolean isAlive() {
        return state == State.HIDDEN_ALIVE || state == State.REVEALED_ALIVE;
    }
    /** Is this node revealed */
    public boolean isRevealed() {
        return state == State.REVEALED_ALIVE || state == State.REVEALED_DEAD;
    }
    public void setRevealed() {
        state = State.REVEALED_DEAD;
    }
    public void setHidden() {
        state = State.HIDDEN_DEAD;
    }
    
    public Button getButton() {
        return button;
    }
    
    /** Enumeration to easily find the current state of the button nodes. */
    public enum State {
        HIDDEN_ALIVE, HIDDEN_DEAD, REVEALED_ALIVE, REVEALED_DEAD;
    }
}
