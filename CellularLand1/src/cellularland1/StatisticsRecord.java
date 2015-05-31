
package cellularland1;

import javafx.beans.property.SimpleStringProperty;

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
}
