
package cellularland1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Static class that provides generating new random automata with the
 * parameters specified. The automata are generated according to the level and 
 * some constants that specify when the generated transition function and initial
 * position are viable.
 *
 * @author Deas
 */
public class AutomatonGenerator {
    /** Size of the board. */
    private static final int size = 30;
    /** Size of the visible board. */
    private static final int visibleSize = 18;
    /** Minimal number of steps the generated automaton has to last (i.e.
     * a cell is alive afterwards).
     */
    private static final int minimalRuntime = 20;
    /** Number of tries to generate different initial position for a fixed
     * transition function. This is important, because some automata might
     * be impossible with any initial position.
     */
    private static final int triesCount = 10;
    /** Minimal number of times that every rule of the transition function 
     * has to be used. Needed to avoid generating automata where it a rule is 
     * never used and therefore the player cannot recognize it.
     */
    private static final int minimalRuleUsage = 50;
    
    /**
     * Generates a new random cellular automaton with the level specified.
     * @param level Number of rules in the transition function to generate.
     * @return New randomly generated automaton.
     */
    public static CAutomaton generate(int level) {
        while(true) {
            // Generate the step function randomly
            ArrayList<Integer> S, B;
            S = new ArrayList<>();
            B = new ArrayList<>();
            Random r = new Random();

            // The value of level is used to determine the count of number generated
            for (int j = 0; j < level; j++) {
                int q = r.nextInt(9);
                if(q <= 1) q = r.nextInt(9);
                if (r.nextInt(2) == 0 && !S.contains(q)) {
                    S.add(q);
                } else if (!B.contains(q)) {
                    B.add(q);
                } else {
                    j--;
                }
            }

            // Generate random initial position and test its viability.
            int i = 0;
            boolean[][] initPosition;
            CAutomaton a;
            while (i++ < triesCount) {
                initPosition = generatePosition();
                if (test(initPosition, S, B)) {
                    return new CAutomaton(initPosition, S, B);
                }
            }
        }
    }
    
    /** Test if the generated automaton satisfies the conditions. */
    private static boolean test(boolean[][] initPosition, ArrayList<Integer> S, ArrayList<Integer> B) {
        int i = 0;
        boolean[][] boolGrid = initPosition;
        
        int[] Susage = new int[S.size()];
        int[] Busage = new int[B.size()];
        
        while(i < minimalRuntime) {
            boolGrid = step(boolGrid, S, B, Susage, Busage);
            if(boolGrid == null) {
                return false;
            }
            i++;
        }
        
        return Arrays.stream(Busage).allMatch(j -> j >= minimalRuleUsage) && 
                Arrays.stream(Susage).allMatch(j -> j >= minimalRuleUsage);
    }
    /** A modified version of step method from CAutomaton, also changes the 
     * integer arrays that contains info about the number of times each rule
     * was used.
     * If null is returned, the automaton did not last enough generations.
     */
    public static boolean[][] step(boolean[][] boolGrid, ArrayList<Integer> S, ArrayList<Integer> B, int[] Susage, int[] Busage) {
        boolean[][] newBoolGrid = new boolean[size][size];
        // Is a cell in the grid alive?
        boolean isSthingAlive = false;
        // Has something changed from the previous generation?
        boolean hasSthingChanged = false;
        
        int offset = (size - visibleSize) / 2;
        
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
                // Update the rule usage arrays
                if(boolGrid[i][j]) {
                    if(S.contains(alive)) {
                        newBoolGrid[i][j] = true;
                        Susage[S.indexOf(alive)]++;
                    } else {
                        newBoolGrid[i][j] = false;
                    }
                } else {
                    if(B.contains(alive)) {
                        newBoolGrid[i][j] = true;
                        Busage[B.indexOf(alive)]++;
                    } else {
                        newBoolGrid[i][j] = false;
                    }
                }
                // Update the bools
                if(i > offset && i <= size - offset && j > offset && j <= size - offset) {
                    isSthingAlive = isSthingAlive || newBoolGrid[i][j];
                    hasSthingChanged = hasSthingChanged || (newBoolGrid[i][j] != boolGrid[i][j]);
                }
            }
        }
        // If everything is dead, return null, the position is not viable
        if(isSthingAlive) return newBoolGrid;
        else return null;
    }
    
    /** Generate random position position. */
    private static boolean[][] generatePosition() {
        Random r = new Random();
        
        int x = 5 + r.nextInt(size - 10);
        int y = 5 + r.nextInt(size - 10);
        
        int xoffset = (size - x) /2;
        int yoffset = (size - y) /2;
        
        double p = r.nextDouble();
        boolean[][] initPosition = new boolean[size][size];
        
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(i >= xoffset && i < x + xoffset && j >= yoffset && j < y + yoffset && r.nextDouble() <= p) {
                    initPosition[i][j] = true;
                } else {
                    initPosition[i][j] = false;
                } 
            }
        }
        
        return initPosition;
    }
}
