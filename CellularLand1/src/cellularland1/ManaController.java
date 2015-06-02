
package cellularland1;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 *
 * @author Deas
 */
public class ManaController {
    /** How many distinct states my mana bar can have. */
    private static final Integer Capacity = 1000;
    /** How fast an empty mana bar will get to full (no of seconds). */
    private static final Integer Speed = 8;
    /** The mana cost of cell discovery. */
    private static final Integer CellDiscoveryCost = 150;
    /** The mana cost of cell state change. */
    private static final Integer CellChangeCost = 150;
    /** The speed at which mana diminishes when in automaton stop. */
    private static final Integer AutoStopSpeed = 8;
    
    private final Timeline timeline;
    private final ProgressBar pb;
    private IntegerProperty points = new SimpleIntegerProperty(0);
    
    private boolean stopped = false;
    public boolean isStopped() { return stopped; }
    
    public ManaController(ProgressBar pb) {
        this.pb = pb;
        timeline = new Timeline();
        
        pb.progressProperty().bind(points.divide(Capacity*1.0));
        
        timeline.getKeyFrames().add(
            new KeyFrame(Duration.seconds(Speed), new KeyValue(points,Capacity)));
        timeline.playFromStart();
    }
    public void reset() {
        points.set(0);
        timeline.playFromStart();
    }
    public boolean restartAuto() {
        if(stopped == false && points.greaterThanOrEqualTo(Capacity/2).get()) {
            reset();
            return true;
        } else return false;
    }
    public boolean cellDiscovery() {
        if(points.greaterThanOrEqualTo(CellDiscoveryCost).get()) {
            if(stopped == false) {
                points.set(points.subtract(CellDiscoveryCost).get());
            } else {
                points.set(points.subtract(CellChangeCost).get());
            }
            return true;
        } else return false;
    }
    public void stopAuto() {
        double fullness = points.divide(Capacity*1.0).get();
        if(stopped == false) {
            timeline.stop();
            timeline.getKeyFrames().remove(0);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(AutoStopSpeed*fullness), new KeyValue(points,0)));
            timeline.playFromStart();
        } else {
            timeline.stop();
            timeline.getKeyFrames().remove(0);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(Speed*(1-fullness)), new KeyValue(points,Capacity)));
            timeline.playFromStart();
        }
        stopped = stopped == false;
    }
}
