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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.Timer;

import is416.is416.Database.StatsDatabase;
import is416.is416.ProgressBar.Depleter;
import is416.is416.ProgressBar.ProgressBarHandler;
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

    private ProgressBar happyBar;
    private ProgressBar stepsBar;

    ProgressBarHandler progressBarHandler;

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
    protected void onStop() {
        super.onStop();
        myDb.setHappy(happyBar.getProgress());
        myDb.setSteps(stepsBar.getProgress());
    }

    @Override
    protected void onStart() {
        super.onStart();
        int happyPoints = getIntent().getIntExtra("happyPoints",0);
        getIntent().removeExtra("happyPoints");
        if (happyPoints > 0){
            happyBar.incrementProgressBy(happyPoints);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = StatsDatabase.getInstance(this);
        setContentView(R.layout.activity_cat_main);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        // ----- PROGRESS BAR INITIALISATION AND SETTING -----
        happyBar = (ProgressBar) findViewById(R.id.happyBar) ;
        stepsBar = (ProgressBar) findViewById(R.id.walkBar) ;

//        int happyPoints = getIntent().getIntExtra("happyPoints",0);
//        getIntent().removeExtra("happyPoints");
//        synchronized ("happy"){
//            happyBar.incrementProgressBy(happyPoints);
//        }

        ImageButton micButton = (ImageButton) findViewById(R.id.micButton);
        TextView questionView = (TextView) findViewById(R.id.question);
        TextView answerView = (TextView) findViewById(R.id.answer);
        View speechBubbleView = findViewById(R.id.answerbubble);
        View questionBubbleView = findViewById(R.id.questionBubble);

      //  ProgressBarHandler progressBarHandler = new ProgressBarHandler(happyBar, stepsBar);

        int happy = myDb.getHappy();
        int steps = myDb.getSteps();

        happyBar.setProgress(happy);
        stepsBar.setProgress(steps);

    // ----- PROGRESS BAR TIMER UPDATE -----
        Timer timer = new Timer();
        timer.schedule(new Depleter(happyBar, stepsBar), 0, 5000);

    // ----- WRAP TEXT for CAT ANSWER VIEW -----
        AutofitHelper autofitHelper = AutofitHelper.create(answerView);
        autofitHelper.setTextSize(answerView.getTextSize());
        loadImageView();


        mic = new Mic(this, questionView, answerView, speechBubbleView, questionBubbleView);
        mic.micStart();
    }

    public void loadImageView() {
        ImageView imgViewLeft = findViewById(R.id.maincat);
        Glide.with(this)
                .load(R.drawable.maincat)
                .dontAnimate()
                .into(new GlideDrawableImageViewTarget(imgViewLeft));
    }


    public void goTask(View view) {
        Intent it = new Intent(this, ScheduleActivity.class);
        startActivity(it);
    }

    public void goWalk(View view) {
        Intent walkLaunch = new Intent(this, MapsActivity.class);
//        startActivity(walkLaunch);
        startActivityForResult(walkLaunch,1234);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            int healthToAdd = data.getIntExtra("healthToAdd", 0);
            stepsBar.incrementProgressBy(healthToAdd);
        }
    }

    public void addSteps(View view) {
        stepsBar.incrementProgressBy(10);
    }

    public void addHappy(View view) {
        happyBar.incrementProgressBy(10);

    }
}
