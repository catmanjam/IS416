package is416.is416.ProgressBar;

import android.widget.ProgressBar;

import com.github.mikephil.charting.components.Description;

import java.util.TimerTask;

/**
 * Created by Cheryl on 30/3/2018.
 */

public class Depleter extends TimerTask {
    private ProgressBar happy;
    private ProgressBar steps;


    public Depleter(ProgressBar happy, ProgressBar steps){
        this.happy = happy;
        this.steps = steps;
    }

    public void run() {
        synchronized ("happy"){
            happy.incrementProgressBy( -4);
        }
        synchronized ("steps"){
            steps.incrementProgressBy( -2);
        }
    }
}
