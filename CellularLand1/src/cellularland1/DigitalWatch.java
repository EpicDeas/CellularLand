
package cellularland1;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Label;

/**
 * Controller of the clock displayed in the menu.
 *
 * @author Deas
 */
final class DigitalClock {
    /** Timeline to generate signal over time. */
    final private Timeline timeline;
    /** The label that is refreshed in constant intervals.*/
    final private Label label;
    
    int minutes;
    int seconds;

    public DigitalClock(Label l) {
        label = l;
        reset();

        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    step();
                }));
    }
    /** Restart the watch. */
    public void reset() {
        minutes = seconds = 0;
    }
    /** Update the label. */
    public void refresh() {
        String str = seconds + "";
        if(str.length() == 1) {
            str = "0" + str;
        }
        label.setText(minutes + ":" + str);
    }
    /** Increment by one second. */
    public void step() {
        if(seconds != 59) {
            seconds += 1;
        } else {
            seconds = 0;
            minutes += 1;
        }
        refresh();
    }
    /** Restart the clock and start counting from zero. */
    public void start() {
        reset();
        refresh();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    /** Start ticking again and keep the current time. */
    public void resume() {
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    /** Stop the clock and keep the current time displayed. */
    public void stop() {
        timeline.stop();
    }
    /** Is the watch running? */
    public boolean isRunning() {
        return timeline.getStatus() == Animation.Status.RUNNING; 
    }
    /**
     * Add the given amount of seconds. 
    */
    public void addSeconds(int s) {
        for(int i = 0; i < s; i++) {
            step();
        }
    }
    /** Returns the total number of seconds from start. */
    public int getTotalSeconds() {
        return minutes * 60 + seconds;
    }
}
