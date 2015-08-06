
package cellularland1;

import java.io.File;
import java.util.Random;

/**
 *
 * @author Deas
 */
public final class Mechanics {
    public static Mechanics inst = new Mechanics();
    private Mechanics() {
        newAutomaton();
    }
    
    private final String directory = "automataDB";
    public void newAutomaton() {
        // Here I have to somehow generate the random automaton
        File dir = new File(directory);
        File[] dirListing = dir.listFiles();
        Random r = new Random();
        String chosen = dirListing[r.nextInt(dirListing.length)].getPath();
        
        Loader l = new Loader(chosen);
        automaton = l.load(size);
        boolGrid = automaton.initPosition;
    }
    
    public void setButtons(ButtonNode[][] buttons) {
        this.buttons = buttons;
    }
    
    private CAutomaton automaton;
    private boolean[][] boolGrid;
    
    private ButtonNode[][] buttons;
    
    /** This size is different from the size of buttons, because I want to 
     * simulate the automaton on a larger grid than just the grid displayed.
     */
    
    private final int size = 30;
    
    /** Number of cells on the margin of grid that are not displayed.
     * size of displayed grid = 2 * offset + size
     */
    private final int offset = 5;
    
    public void step() {
        boolean[][] newBoolGrid = new boolean[size][size];
        
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
                    newBoolGrid[i][j] = automaton.willSurvive(alive);
                } else {
                    newBoolGrid[i][j] = automaton.willBeBorn(alive);
                }
            }
        }
        
        boolGrid = newBoolGrid;
        update();
    }
    
    private void update() {
        for(int i = offset; i < offset + buttons.length; i++) {
            for(int j = offset; j < offset + buttons.length; j++) {
                if(boolGrid[i][j]) {
                    buttons[i-offset][j-offset].birth();
                } else {
                    buttons[i-offset][j-offset].die();
                }
            }
        }
    }
    
    public boolean isCorrect(String S,String B) {
        return S.equals(automaton.getS()) && B.equals(automaton.getB());
    }
    
    public void resetAutomaton() {
        boolGrid = automaton.initPosition;
        update();
    }
    
    public void changed(int x, int y) {
        boolGrid[x + offset][y + offset] ^= true;
    }
    
    public String getS() {
        return automaton.getS();
    }
    public String getB() {
        return automaton.getB();
    }
}
