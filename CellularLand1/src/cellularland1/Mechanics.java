
package cellularland1;

import java.util.Arrays;

/**
 * The singleton class the contains the main mechanics, interface between the
 * graphics and the automaton.
 *
 * @author Deas
 */
public final class Mechanics {
    /** Currently used automaton. */
    private CAutomaton automaton;
    /** Current position in the main board. */
    private boolean[][] boolGrid;    
    /** Array of the button nodes that are used to play the game. */
    private ButtonNode[][] buttons;
    /** Level of the game, used to decide which automaton to generate. */
    private int level = 2;
    
    /** This size is different from the size of buttons, because I want to 
     * simulate the automaton on a larger grid than just the grid displayed.
     */
    private final int size = 30;
    /** Size of the square of cells that are revealed in the initial position. */
    private final int revealedInit = 16;
    /** Number of cells on the margin of grid that are not displayed.
     * size of displayed grid = 2 * offset + size
     */
    private final int offset = 5;
    
    /** Singleton instance */
    public static Mechanics inst = new Mechanics();
    private Mechanics() { }
        
    /** Generate new random automaton with the current level and set it as the
     * used automaton.
     */
    public void newAutomaton() {
        // Generate random automaton
        automaton = AutomatonGenerator.generate(level);
        // And set the position
        boolGrid = automaton.initPosition;
        
        // And set the buttons to their initial state
        int revOffset = (buttons.length - revealedInit) / 2;
        for(int i = 0; i < buttons.length; i++) 
            for(int j = 0; j < buttons[i].length; j++) {
                if(i < revOffset || i >= revOffset + revealedInit ||
                        j < revOffset || j >= revOffset + revealedInit) {
                    buttons[i][j].setHidden();
                } else {
                    buttons[i][j].setRevealed();
                }
            }
    }
    /** Used only once in the beginning to set the button array from the 
     * initialization.
     * @param buttons 
     */ 
    public void setButtons(ButtonNode[][] buttons) {
        this.buttons = buttons;
    }
    
    /** Called when the transition function has to be applied.
     * Recomputes the position.
     */
    public void step() {
        // The new position
        boolean[][] newBoolGrid = new boolean[size][size];
        // The array of rules used to determine the state of cells.
        int[][] ruleGrid = new int[size][size];
        
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                // Compute the number of living cells around:
                int alive = 0;
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        if ((k != 0 || l != 0)
                                && i + k >= 0 && i + k < size
                                && j + l >= 0 && j + l < size) {
                            alive += boolGrid[i + k][j + l] ? 1 : 0;
                        }
                    }
                }
                if(boolGrid[i][j]) {
                    ruleGrid[i][j] = automaton.willSurvive(alive);
                    newBoolGrid[i][j] = ruleGrid[i][j] != -1;
                } else {
                    ruleGrid[i][j] = automaton.willBeBorn(alive);
                    newBoolGrid[i][j] = ruleGrid[i][j] != -1;             
                }
            }
        }
        
        boolGrid = newBoolGrid;
        update(ruleGrid);
    }
    
    /** Update the status of button nodes AND set the border according to the
     * rule used.
     * @param ruleGrid Number of the rule. -1 &lt;= i &lt;= 4
     */
    public void update(int[][] ruleGrid) {
        for(int i = offset; i < offset + buttons.length; i++) {
            for(int j = offset; j < offset + buttons.length; j++) {
                if(boolGrid[i][j]) {
                    buttons[i-offset][j-offset].birth();
                    if(ruleGrid != null)
                        buttons[i-offset][j-offset].seeUsedRule(ruleGrid[i][j]);
                } else {
                    buttons[i-offset][j-offset].die();
                }
            }
        }
    }
    /** Is the transition function in argument correct? */
    public boolean isCorrect(String S,String B) {
        char[] Sch = S.toCharArray();
        Arrays.sort(Sch);
        char[] Bch = B.toCharArray();
        Arrays.sort(Bch);
        
        return Arrays.equals(Sch, automaton.getS()) 
                && Arrays.equals(Bch, automaton.getB());
    }
    /** Reset the automaton to random initial position. */
    public void resetAutomaton() {
        boolean[][] b = null;
        while(b == null) 
            b = AutomatonGenerator.generatePosition(automaton.S, automaton.B, 100);
        boolGrid = b;
        update(null);
    }
    
    public void changed(int x, int y) {
        boolGrid[x + offset][y + offset] ^= true;
    }
    
    /** Returns the string with rules for survival. */
    public String getS() {
        String str = "";
        for(char ch : automaton.getS()) 
            str += ch;
        return str;
    }
    /** Returns the string with rules for birth. */
    public String getB() {
        String str = "";
        for(char ch : automaton.getB()) 
            str += ch;
        return str;
    }
    /** Set the level to generate automata with. */
    public void setLevel(int level) {
        this.level = level;
    }
}
