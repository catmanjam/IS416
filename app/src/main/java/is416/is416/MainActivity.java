package is416.is416;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void launchWalkActivity(View view) {
        Intent walkLaunch = new Intent(this, WalkActivity.class);
        startActivity(walkLaunch);
    }


    public void launchSchduleActivity(View view) {
        Intent schduleLaunch = new Intent(this, ScheduleActivity.class);
        startActivity(schduleLaunch);
    }


    public void launchGameActivity(View view) {
        Intent gameLaunch = new Intent(this, GameActivity.class);
        startActivity(gameLaunch);
    }
}
