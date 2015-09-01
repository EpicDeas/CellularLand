
package cellularland1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

/**
 * The encapsulation of a record in statistics database.
 *
 * @author Jakub Sosnovec
 */
public class StatisticsRecord {
    private final SimpleStringProperty nameProperty;
    private final SimpleStringProperty scoreProperty;
    private final SimpleStringProperty difficultyProperty;
    private final SimpleStringProperty levelProperty;
    private final SimpleStringProperty dateProperty;
 
    public StatisticsRecord(String name, String score, String difficulty, String level, String date) {
        this.nameProperty = new SimpleStringProperty(name);
        this.scoreProperty = new SimpleStringProperty(score);
        this.difficultyProperty = new SimpleStringProperty(difficulty);
        this.levelProperty = new SimpleStringProperty(level);
        this.dateProperty = new SimpleStringProperty(date);
    }
        
    public final String getName() {
        return this.nameProperty.get();
    }
    public final void setName(String name) {
        this.nameProperty.set(name);
    }
    public final String getScore() {
        return this.scoreProperty.get();
    }
    public final void setScore(String score) {
        this.scoreProperty.set(score);
    }
    public final String getDate() {
        return this.dateProperty.get();
    }
    public final void setDate(String date) {
        this.dateProperty.set(date);
    }
    public final String getDifficulty() {
        return this.difficultyProperty.get();
    }
    public final void setDifficulty(String difficulty) {
        this.difficultyProperty.set(difficulty);
    }
    public final String getLevel() {
        return this.levelProperty.get();
    }
    public final void setLevel(String level) {
        this.levelProperty.set(level);
    }
    private static String filename;
    
    public static void loadStats(String filename, TableView tabulka) {
        StatisticsRecord.filename = filename;
        
        ArrayList<StatisticsRecord> records = new ArrayList<>();
        File f = new File(filename);
        if(!f.exists()){
            // create new file
            try(FileWriter fw = new FileWriter(f)) {} catch(IOException e) {}
        }
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line = br.readLine()) != null) {
                if(!line.equals(""))
                    records.add(StatisticsRecord.parse(line));
            }
            tabulka.setItems(FXCollections.observableArrayList(records));
        } catch (IOException e) {
            throw new IllegalArgumentException();
        } 
    }
    
    private static StatisticsRecord parse(String line) {
        String[] tokens = line.split(":");
        return new StatisticsRecord(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4]);
    }
    
    public static void newRecord(String name,TableView tabulka,int points, int difficulty, int level) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = dateFormat.format(new Date());
                
        tabulka.getItems().add(new StatisticsRecord(name,String.valueOf(points),"" + difficulty, "" + level, dateString));
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(StatisticsRecord.filename,true));
            bw.append("\n" + name + ":" + points + ":" + difficulty + ":" + level + ":" + dateFormat.format(new Date()));
            bw.close();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
