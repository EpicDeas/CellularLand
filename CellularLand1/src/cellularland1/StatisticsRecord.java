
package cellularland1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

/**
 * The encapsulation of 
 *
 * @author Jakub Sosnovec
 */
public class StatisticsRecord {
    private final SimpleStringProperty nameProperty;
    private final SimpleStringProperty scoreProperty;
    private final SimpleStringProperty dateProperty;
 
    public StatisticsRecord(String name, String score, String date) {
        this.nameProperty = new SimpleStringProperty(name);
        this.scoreProperty = new SimpleStringProperty(score);
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
    
    private static String filename;
    
    public static void loadStats(String filename, TableView tabulka) {
        StatisticsRecord.filename = filename;
        
        ArrayList<StatisticsRecord> records = new ArrayList<>();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while((line = br.readLine()) != null) {
                records.add(StatisticsRecord.parse(line));
            }
            br.close();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        
        //tabulka.setItems(FXCollections.observableArrayList(new StatisticsRecord("Matfyzak01","42","3.14.1592")));
        tabulka.setItems(FXCollections.observableArrayList(records));
        
        
    }
    
    private static StatisticsRecord parse(String line) {
        String[] tokens = line.split(":");
        return new StatisticsRecord(tokens[0],tokens[1],tokens[2]);
    }
    
    public static void newRecord(String name,TableView tabulka,int points) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = dateFormat.format(new Date());
                
        tabulka.getItems().add(new StatisticsRecord(name,String.valueOf(points),dateString));
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(StatisticsRecord.filename,true));
            bw.append("\n" + name + ":" + points + ":" + dateFormat.format(new Date()));
            bw.close();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
