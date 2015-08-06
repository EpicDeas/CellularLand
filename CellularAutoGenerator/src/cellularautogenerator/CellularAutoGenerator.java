
package cellularautogenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Deas
 */
public class CellularAutoGenerator {

    /**
     * We will try all different Cellular automatons with transition function 
     * with at most 3 values on each side.
     */
    public static void main(String[] args) {
        generateAutomata();
    }
    
    private static final int size = 30;
    private static final int minimalRuntime = 15;
    private static final String directory = "automata";
    
    private static void generateAutomata() {
        ArrayList<Integer> S,B;    
        
        for(int i = 0; i <= 8; i++) {
            for(int j = 0; j <= 8; j++) {
                for(int k = 0; k <= 8; k++) {
                    for(int l = 0; l <= 7; l++) {
                        
                        S = new ArrayList<>();
                        B = new ArrayList<>();
                        
                        // Create new file
                        try(BufferedWriter bw = new BufferedWriter(new FileWriter(directory + "/" + i + j + k + "ver" + l + ".txt"))) {
                            // Write the transition function and generate a new random position
                            if(l/2 <= 0) {
                                if(i == j || j == k || i == k) continue;
                                S.add(i); S.add(j); S.add(k);
                            } else if (l/2 <= 1) {
                                if(i == j) continue;
                                S.add(i); S.add(j); B.add(k);
                            } else if (l/2 <= 2) {
                                if(j == k) continue;
                                S.add(i); B.add(j); B.add(k);
                            } else {
                                if(i == j || j == k || i == k) continue;
                                B.add(i); B.add(j); B.add(k);
                            }                        
                            boolean[][] initPosition = generatePosition();
                            CAutomaton a = new CAutomaton(initPosition, S, B);
                            if(test(a)) printAutomaton(bw, initPosition, S, B);
                        } catch (IOException e) { }           
                    }
                }
            }
        }

    }
    
    private static boolean[][] generatePosition() throws IOException {
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
    
    private static void printAutomaton(BufferedWriter bw, boolean[][] initPosition, ArrayList<Integer> S, ArrayList<Integer> B) throws IOException {
        for(int i : S) {
            bw.write("" + i);
        }
        bw.write("/");
        for(int i : B) {
            bw.write("" + i);
        }
        bw.write("\n");
        
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                bw.write((initPosition[i][j]) ? "a" : "x");
            }
            bw.write("\n");
        }
        
    }
    
    private static boolean test(CAutomaton a) {
        int i = 0;
        boolean[][] boolGrid = a.initPosition;
        
        while(i < minimalRuntime) {
            boolGrid = step(boolGrid, a);
            if(boolGrid == null) {
                return false;
            }
            i++;
        }
        return true;
    }
    
    public static boolean[][] step(boolean[][] boolGrid, CAutomaton automaton) {
        boolean[][] newBoolGrid = new boolean[size][size];
        boolean isSthingAlive = false;
        
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
                isSthingAlive = isSthingAlive || newBoolGrid[i][j];
            }
        }
        if(isSthingAlive) return newBoolGrid;
        else return null;
    }
}
