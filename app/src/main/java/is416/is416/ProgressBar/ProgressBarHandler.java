package is416.is416.ProgressBar;

import android.widget.ProgressBar;

/**
 * Created by Cheryl on 30/3/2018.
 */

public class ProgressBarHandler {
    // add update to database codes
    // add views to show extra point earned animation
    private ProgressBar happy;
    private ProgressBar steps;

    public ProgressBarHandler(ProgressBar happy, ProgressBar steps){
        this.happy = happy;
        this.steps = steps;
    }

    public void increaseHappyBar(int points){
        synchronized ("happy"){
            happy.incrementProgressBy(points);
        }
    }

    public void increaseStepsBar(int points){
        synchronized ("steps"){
            steps.incrementProgressBy(points);
        }
    }

    // have method to increase the respective bars i.e. plus 10 points

}
