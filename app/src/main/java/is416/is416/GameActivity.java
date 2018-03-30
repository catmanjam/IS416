package is416.is416;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import is416.is416.ProgressBar.ProgressBarHandler;

public class GameActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private Sensor accelerometer;
    private SensorEventListener gyroscopeEventListener;
    private SensorEventListener accelerometerEventListener;
    private MediaPlayer mp;
    private Button gameStart;
    private RelativeLayout l;

    private float xPos, xAccel, xVel, xRand, xRandx = 0.0f;
    private float yPos, yAccel, yVel, yRand, yRandx = 0.0f;
    private float xMax, yMax;
    private Bitmap ball;
    private Bitmap cross;
    private Bitmap cat;
    private int score = 0;
    private int highScore = 0;

    final Random random = new Random();
    final Point size = new Point();

    private TextView gameOver;
    private TextView tryAgain;
    private Button retry;
    private Button toHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);
        //l = (RelativeLayout) findViewById(R.id.layout);
        mp = MediaPlayer.create(this, R.raw.gamebgm);
        mp.setLooping(true);
        mp.start();
        ProgressBarHandler.increaseHappyBar(20);
//        GameView gameView = new GameView(this);
//        l.addView(gameView);
        //this.setContentView(gameView);
        //setContentView(gameView);

        //final Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        xMax = (float) size.x - 150;
        yMax = (float) size.y - 300;

        //final Random random = new Random();
        xRand = (float) random.nextInt(size.x-150);
        yRand = (float) random.nextInt(size.y-460);
        xRandx = (float) random.nextInt(size.x-150);
        yRandx = (float) random.nextInt(size.y-460);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);



        accelerometerEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    xAccel = sensorEvent.values[0];
                    yAccel = sensorEvent.values[1];
                    updateCat();
                }

            }

            private void updateCat() {
                float frameTime = 0.666f;
                xVel += (xAccel * frameTime);
                yVel += (yAccel * frameTime);

                float xS = (xVel / 2) * frameTime;
                float yS = (yVel / 2) * frameTime;

                xPos -= xS;
                yPos += yS;

                if (xPos > xMax) {
                    xPos = xMax;
                    xVel = 0;
                } else if (xPos < 0) {
                    xPos = 0;
                    xVel = 0;
                }

                if (yPos > yMax) {
                    yPos = yMax;
                    yVel = 0;
                } else if (yPos < 0) {
                    yPos = 0;
                    yVel = 0;
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(accelerometerEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);

    }

    protected void onStop() {
        sensorManager.unregisterListener(accelerometerEventListener);
        super.onStop();

    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(accelerometerEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mp.start();
    }

    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(accelerometerEventListener);
        mp.pause();
    }

    public void goHome (View view) {
        Intent homeLaunch = new Intent(this, CatMainActivity.class);
        startActivity(homeLaunch);
    }

    public void startGame(View view){

        TextView gameTitle = findViewById(R.id.gameTitle);
        ImageView catBg = findViewById(R.id.catbg);
        TextView howToPlay = findViewById(R.id.howtoplay);
        Button gameStart = findViewById(R.id.gameStart);
        ImageView howTo = findViewById(R.id.howto);
        gameTitle.setVisibility(View.INVISIBLE);
        catBg.setVisibility(View.INVISIBLE);
        howToPlay.setVisibility(View.INVISIBLE);
        gameStart.setVisibility(View.INVISIBLE);
        howTo.setVisibility(View.INVISIBLE);

        gameOver = findViewById(R.id.gameoverText);
        tryAgain = findViewById(R.id.gameoverText2);
        retry = findViewById(R.id.replay);
        toHome = findViewById(R.id.returnHome);

        gameOver.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        retry.setVisibility(View.INVISIBLE);
        toHome.setVisibility(View.INVISIBLE);

        score = 0;


        l = findViewById(R.id.layout);
        GameView gameView = new GameView(this,view);
        l.addView(gameView);
    }


    private class GameView extends View {

        private View view;
        private Context context;

        public GameView(Context context,View view) {
            super(context);
            this.context = context;

            final int catWidth = 200;
            final int catHeight = 200;
            final int ballWidth = 100;
            final int ballHeight = 100;
            final int crossWidth = 100;
            final int crossHeight = 100;

            Bitmap catSrc = BitmapFactory.decodeResource(getResources(), R.drawable.catgame);
            cat = Bitmap.createScaledBitmap(catSrc, catWidth, catHeight, true);

            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.ballgame);
            ball = Bitmap.createScaledBitmap(ballSrc, ballWidth, ballHeight, true);

            Bitmap crossSrc = BitmapFactory.decodeResource(getResources(), R.drawable.crossgame);
            cross = Bitmap.createScaledBitmap(crossSrc, crossWidth, crossHeight, true);

        }

        @Override
        protected void onDraw(Canvas canvas) {

            gameOver = ((GameActivity)context).findViewById(R.id.gameoverText);
            tryAgain = ((GameActivity)context).findViewById(R.id.gameoverText2);
            retry = ((GameActivity)context).findViewById(R.id.replay);
            toHome = ((GameActivity)context).findViewById(R.id.returnHome);
            l = ((GameActivity)context).findViewById(R.id.layout);

            Paint p = new Paint();
            p.setTextSize(90);
            p.setColor(Color.parseColor("#4b4b4b"));
            p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("High Score: " + highScore,120,250,p);
            canvas.drawText("Score: " + score,990,250,p);

            canvas.drawBitmap(cat, xPos, yPos, null);
            canvas.drawBitmap(ball,xRand,yRand, null);
            canvas.drawBitmap(cross,xRandx,yRandx,null);
            if ((xPos >= xRand-100f && xPos <= xRand+100f) && (yPos >= yRand-100f && yPos <= yRand+100f)) {
                score++;
                if (score > highScore) {
                    highScore = score;
                }
                xRand = (float) random.nextInt(size.x-150);
                yRand = (float) random.nextInt(size.y-460);
                xRandx = (float) random.nextInt(size.x-150);
                yRandx = (float) random.nextInt(size.y-460);
                canvas.drawBitmap(cross,xRand,yRand,null);
                canvas.drawBitmap(ball,xRandx,yRandx, null);



            }
            if ((xPos >= xRandx-100f && xPos <= xRandx+100f) && (yPos >= yRandx-100f && yPos <= yRandx+100f)) {
                xRand = (float) random.nextInt(size.x-150);
                yRand = (float) random.nextInt(size.y-460);
                xRandx = (float) random.nextInt(size.x-150);
                yRandx = (float) random.nextInt(size.y-460);

                gameOver.setVisibility(View.VISIBLE);
                tryAgain.setVisibility(View.VISIBLE);
                retry.setVisibility(View.VISIBLE);
                toHome.setVisibility(View.VISIBLE);
                l.removeView(this);
            }
            invalidate();
        }


    }
}

