package is416.is416;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import is416.is416.Schedule.DialogueFlowActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_main);
    }


    public void launchWalkActivity(View view) {
        Intent walkLaunch = new Intent(this, WalkActivity.class);
        startActivity(walkLaunch);
    }

    public void launchScheduleActivity(View view) {
        Intent scheduleLaunch = new Intent(this, DialogueFlowActivity.class);
        startActivity(scheduleLaunch);
    }

    public void launchGameActivity(View view) {
        Intent gameLaunch = new Intent(this, GameActivity.class);
        startActivity(gameLaunch);
    }
}
