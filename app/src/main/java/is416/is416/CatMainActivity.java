package is416.is416;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import is416.is416.Database.StatsDatabase;
import is416.is416.Schedule.Mic;
import is416.is416.Schedule.ScheduleActivity;
import me.grantland.widget.AutofitHelper;

public class CatMainActivity extends AppCompatActivity {

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 101;
    private Mic mic;
    private StatsDatabase myDb;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = StatsDatabase.getInstance(this);
        setContentView(R.layout.activity_cat_main);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        ImageButton micButton = (ImageButton) findViewById(R.id.micButton);
        TextView questionView = (TextView) findViewById(R.id.question);
        TextView answerView = (TextView) findViewById(R.id.answer);
        View speechBubbleView = findViewById(R.id.answerbubble);
        View questionBubbleView = findViewById(R.id.questionBubble);

        ProgressBar happyBar = (ProgressBar) findViewById(R.id.happyBar) ;
        ProgressBar stepsBar = (ProgressBar) findViewById(R.id.walkBar) ;

        int happy = myDb.getHappy();
        int steps = myDb.getSteps();

        happyBar.setProgress(happy);
        stepsBar.setProgress(steps);

        AutofitHelper autofitHelper = AutofitHelper.create(answerView);
        autofitHelper.setTextSize(answerView.getTextSize());

        mic = new Mic(this, questionView, answerView, speechBubbleView, questionBubbleView);
        mic.micStart();
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

    public void micClick(View view) {
        mic.micStartListening();
    }

    public void goStep(View view) {
        Intent stepLaunch = new Intent(this, StepActivity.class);
        startActivity(stepLaunch);
    }
}
