package is416.is416;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import is416.is416.GameActivity;
import is416.is416.R;
import is416.is416.Schedule.ScheduleActivity;
import is416.is416.WalkActivity;

public class CatMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_main);
    }

    public void goTask(View view) {
        Intent it = new Intent(this, ScheduleActivity.class);
        startActivity(it);
    }

    public void goWalk(View view) {
        Intent walkLaunch = new Intent(this, WalkActivity.class);
        startActivity(walkLaunch);
    }

    public void goPlay(View view) {
        Intent gameLaunch = new Intent(this, GameActivity.class);
        startActivity(gameLaunch);
    }
}
