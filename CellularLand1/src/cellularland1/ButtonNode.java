
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
        state = State.HIDDEN_DEAD;
    }
    
    public boolean isAlive() {
        return state == State.HIDDEN_ALIVE || state == State.REVEALED_ALIVE;
    }
    
    public boolean isRevealed() {
        return state == State.REVEALED_ALIVE || state == State.REVEALED_DEAD;
    }
    
    public Button getButton() {
        return button;
    }
    
    /** Enumeration to easily find the current state of the button nodes. */
    public enum State {
        HIDDEN_ALIVE, HIDDEN_DEAD, REVEALED_ALIVE, REVEALED_DEAD;
    }
}
