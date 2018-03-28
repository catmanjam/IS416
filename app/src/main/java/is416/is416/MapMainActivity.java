package is416.is416;

import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

import is416.is416.Database.StepDatabase;

public class MapMainActivity extends AppCompatActivity {

    private StepDatabase stepDb;
    private GLSurfaceView mGLView;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapmain);
        mTextView = (TextView) findViewById(R.id.text_step);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepDb = new StepDatabase(this);
        Calendar today = Calendar.getInstance();
        Integer day = today.get(today.DAY_OF_MONTH);
        Integer month =  today.get(today.MONTH);
        Integer year = today.get(today.YEAR);
        Log.d("SEE THIS", ""+day+"-"+month+"-"+year);
        today.set(year, month, 27, 0,0,0);
        Log.d("SEE THAT", ""+today.get(today.DAY_OF_MONTH)+"-"+today.get(today.MONTH)+"-"+today.get(Calendar.YEAR));
        stepDb.updateStepRecord(today,444);
        Cursor c = stepDb.getAllSteps();
       // Cursor c = stepDb.getStepsToday(today);
        String str = "";
        c.moveToNext();
        while(c.getCount()-c.getPosition() > 0){

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(c.getLong(1));
            Log.d("SEE retrieved time", ""+cal.getTimeInMillis() );
            cal.set(Calendar.MILLISECOND,0);

            str += c.getInt(0)+ ",";
            str += cal.get(cal.DAY_OF_MONTH) + "-" + cal.get(cal.MONTH) + "-"+ cal.get(cal.YEAR)+ ",";
            str += c.getInt(2) + "\n";
            c.moveToNext();
        }
        TextView tv = findViewById(R.id.textView);
        tv.setText(str);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
//        mGLView = new MyGLSurfaceView(this);
//        setContentView(mGLView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorEventListener,mSensor,mSensorManager.SENSOR_DELAY_NORMAL);
    }

    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        private float mStepOffset;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (mStepOffset == 0){
                mStepOffset = sensorEvent.values[0];
            }
            mTextView.setText(Float.toString(sensorEvent.values[0]-mStepOffset));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

//    class MyGLSurfaceView extends GLSurfaceView {
//
//        private final MyGLRenderer mRenderer;
//
//        public MyGLSurfaceView(Context context){
//            super(context);
//
//            // Create an OpenGL ES 2.0 context
//            setEGLContextClientVersion(2);
//
//            mRenderer = new MyGLRenderer();
//
//            // Set the Renderer for drawing on the GLSurfaceView
//            setRenderer(mRenderer);
//        }
//    }


}
