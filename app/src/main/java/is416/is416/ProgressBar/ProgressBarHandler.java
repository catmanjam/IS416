package is416.is416.ProgressBar;

import android.widget.ProgressBar;

/**
 * Created by Cheryl on 30/3/2018.
 */

public class ProgressBarHandler {
    // add update to database codes
    // add views to show extra point earned animation
    private static ProgressBar happy;
    private static ProgressBar steps;

    public ProgressBarHandler(ProgressBar happy, ProgressBar steps){
        this.happy = happy;
        this.steps = steps;
    }

    public static void increaseHappyBar(int points){
        synchronized ("happy"){
            happy.incrementProgressBy(points);
        }
    }

    public static void increaseStepsBar(int points){
        synchronized ("steps"){
            steps.incrementProgressBy(points);
        }
    }

    // have method to increase the respective bars i.e. plus 10 points

}
