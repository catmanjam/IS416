package is416.is416;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MapMainActivity extends AppCompatActivity {

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
