
package cellularland1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;

/**
 * Loads the cellular automaton from specified text file, parser.
 *
 * @author Deas
 */
public class Loader {
    private FileReader fr;
    
    public Loader(String filename) {
        try {
            fr = new FileReader(filename);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
    
    public CAutomaton load(int size) {
        try {
            boolean[][] boolGrid = new boolean[size][size];
            BufferedReader br = new BufferedReader(fr);
            
            String line = br.readLine();
            String tokens[] = line.split("/");
            ArrayList<Integer> B = new ArrayList<>();
            ArrayList<Integer> S = new ArrayList<>();
            if(tokens.length == 2) {
                tokens[0].chars().forEach(ch -> S.add(Character.getNumericValue(ch)));
                tokens[1].chars().forEach(ch -> B.add(Character.getNumericValue(ch)));
            } else if(line.charAt(0) == '/') {
                tokens[0].chars().forEach(ch -> B.add(Character.getNumericValue(ch)));
            } else if(line.charAt(line.length() - 1) == '/') {
                tokens[0].chars().forEach(ch -> S.add(Character.getNumericValue(ch)));
            } else {
                throw new IllegalArgumentException();
            }
            
            line = br.readLine();
            int offset = (size - line.length()) / 2;
            int i = offset;
            
            while(line != null) {
                for(int j = offset; j < line.length() + offset; j++) {
                    boolGrid[i][j] = (line.charAt(j - offset) == 'a');
                }
                i++;
                line = br.readLine();
            }
            return new CAutomaton(boolGrid,S,B);
            
        } catch (IOException e) {
            System.out.println("I/O error.");
            Platform.exit();
            return null;
        }
    }
}
