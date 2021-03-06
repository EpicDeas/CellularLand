
package cellularland1;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import javafx.scene.control.ProgressBar;

/**
 * Controller of the mana bar. The behavior of the bar is specified in the 
 * constants Capacity, Speed, AutoStopSpeed.
 * 
 * @author Deas
 */
public final class ManaController {
    // SOME CONSTANTS:
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
    
    /** Timeline that controls the bar time-wise. */
    private final Timeline timelineProgress;
    /** Timeline that controls the main board, this reference is needed to
     * control stopping the automaton correctly.
     */
    private Timeline timelineMain;

    /** The object of the bar. */
    private final ProgressBar pb;
    /** The current points of mana available. 0 <= i <= Capacity */
    private final IntegerProperty points = new SimpleIntegerProperty(Capacity);
    /** Is the automaton stopped? */
    private boolean stopped = false;
    
    public ManaController(ProgressBar pb) {
        this.pb = pb;
        timelineProgress = new Timeline();
        // Bind the property points to the progress of the bar
        pb.progressProperty().bind(points.divide(Capacity*1.0));
                
        timelineProgress.getKeyFrames().add(new KeyFrame(Duration.ZERO));
        recalibrate(1,1);
    }
    
    /** Reset the mana bar, set the points to zero. */
    public void reset() {
        if(timelineProgress != null)
            stop();
        points.set(0);
        recalibrate(1,0);
    }
    
    /** The player wants to restart the automaton - the mana in empty afterwards.
     * 
     * @return Returns whether the restarting was successful (whether there is
     * enough mana. If true, the action should be passed to mechanics.
     */
    public boolean restartAuto() {
        if(stopped == false && points.greaterThanOrEqualTo(Capacity/2).get()) {
            reset();
            return true;
        } else return false;
    }
    /** The mana has run out, start the automaton again. */
    private void emptyMana() {
        if(points.get() != 0 || stopped != true) {
            return;
        }
        
        // Start the automaton:
        //Mechanics.inst.step();
        timelineMain.playFromStart();
        
        FXMLDocumentController.status = FXMLDocumentController.Status.RUNNING;
        stopAuto(timelineMain);
    }
    
    /** Cell was clicked - check if mana is sufficient, what to do and does it.
     * also changes the state of the button.
     * 
     * @param b The button that was clicked
     * @return Returns if the action was successful. If true, than the action 
     * should be passed on to game mechanics.
     */
    public boolean cellClick(ButtonNode b) {
        double fullness;
        
        if(stopped == false && b.isRevealed() == false && points.greaterThanOrEqualTo(CellDiscoveryCost).get()) {
            points.set(points.get() - CellDiscoveryCost);
            fullness = points.divide(Capacity*1.0).get();
            
            b.clicked(stopped);        
            recalibrate(1, fullness);
            return true;
        } else if (stopped == true) {
            if (b.isRevealed() == false && points.greaterThanOrEqualTo(CellDiscoveryCost).get()) {
                points.set(points.get() - CellDiscoveryCost);
                fullness = points.divide(Capacity * 1.0).get();

                b.clicked(stopped);
                recalibrate(-1, fullness);
                return true;
            } else if (b.isRevealed() && points.greaterThanOrEqualTo(CellChangeCost).get()) {
                points.set(points.get() - CellChangeCost);
                fullness = points.divide(Capacity * 1.0).get();

                b.clicked(stopped);
                recalibrate(-1, fullness);
                return true;
            }
        }
        return false;
    }
    
    /** Used when player stops the automaton, reverses the flow of mana with
     * specified speed. 
     */
    public void stopAuto(Timeline timelineMain) {
        double fullness = points.divide(Capacity*1.0).get();
        
        this.timelineMain = timelineMain;

        if(stopped == false) {
            recalibrate(-1,fullness);
        } else {
            recalibrate(1,fullness);
            Mechanics.inst.step();
            timelineMain.playFromStart();
        }
        stopped = stopped == false;
    }
    
    /** Recalibrates the timeline so that the progress bar runs with the same
     * speed as before changing the value of points.
     * @param direction The direction the it should run (1=up, -1=down)
     * @param fullness How full the bar is now.
     */
    public void recalibrate(int direction, double fullness) {
        if(direction == -1) {
            timelineProgress.stop();
            timelineProgress.getKeyFrames().remove(0);
            timelineProgress.getKeyFrames().add(new KeyFrame(Duration.seconds(AutoStopSpeed*fullness), new KeyValue(points,0)));
            timelineProgress.setOnFinished(ae -> emptyMana());
            timelineProgress.playFromStart();
        } else if (direction == 1) {
            timelineProgress.stop();
            timelineProgress.getKeyFrames().remove(0);
            timelineProgress.getKeyFrames().add(new KeyFrame(Duration.seconds(Speed*(1-fullness)), new KeyValue(points,Capacity)));
            timelineProgress.setOnFinished(null);
            timelineProgress.playFromStart();
        }
    }
    
    /** Stop the mana bar and the main automaton. Used when the round is over. */
    public void stop() {
        timelineProgress.stop();
        if(timelineMain != null) 
            timelineMain.stop();
    }
}
