package is416.is416;

import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import is416.is416.Database.StepDatabase;

public class StepActivity extends AppCompatActivity {
    private static final TimeZone SG = TimeZone.getTimeZone("Singapore");
    private BarChart barChart;
    private ImageView imgViewLeft;
    private ImageView imgViewRight;
    private Animation catwalkLeftAnimation;
    private Animation catwalkRightAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        // ADD CAT ANIMATION
        loadImageView();
        walkRight();

        // SET TODAY'S STEP COUNT
        StepDatabase stepDatabase = StepDatabase.getInstance(this);
        Calendar now = Calendar.getInstance();
        now.setTimeZone(SG);
        TextView stepTdy = findViewById(R.id.stepsTdy);
        Cursor cTdy = stepDatabase.getStepsToday(now);
        cTdy.moveToNext();
        int stepsNow = 0;
        if (cTdy.getCount()>0){
            stepsNow = cTdy.getInt(2);
        }
        stepTdy.setText(Integer.toString(stepsNow));

        barChart = (BarChart) findViewById(R.id.barGraph);
        Cursor cursor  = stepDatabase.getAllSteps();
        int countEntries = 0;

        List<Long> dates = new ArrayList<>(); // COLUMN LABELS
        ArrayList<BarEntry> barEntries = new ArrayList<>(); // DATA ENTRIES

        while(cursor.moveToNext()){
            Integer id = cursor.getInt(0); //  ID
            Long date = cursor.getLong(1); //  DATE
            Integer steps = cursor.getInt(2); //  STEPCOUNT

            // ADD BAR ENTRIES
            barEntries.add(new BarEntry(countEntries++, steps ));
            // ADD COLUMNS
            dates.add(date);
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Steps");
       // barDataSet.setValueTextColor(16777215);
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.7f);
        barChart.setData(data);

        int yellowInt = ContextCompat.getColor(this, R.color.yellow);
        barChart.getAxisRight().setEnabled(false); // remove right axis
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisLeft().setTextSize(13f);
        barChart.getAxisLeft().setTextColor(yellowInt);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextSize(13f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(dates));
        xAxis.setTextColor(yellowInt);
        int whiteInt = ContextCompat.getColor(this, R.color.offWhite);
        barChart.getLegend().setTextColor(whiteInt);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getDescription().setEnabled(false);
    }

    private void loadImageView(){
        imgViewLeft = (ImageView) findViewById(R.id.putCatLeftGif);
        Glide.with(this)
                .load(R.drawable.catwalk_left)
                .dontAnimate()
                .into(new GlideDrawableImageViewTarget(imgViewLeft));

        imgViewRight = (ImageView) findViewById(R.id.putCatRightGif);
        Glide.with(this)
                .load(R.drawable.catwalk_right2)
                .dontAnimate()
                .into(new GlideDrawableImageViewTarget(imgViewRight));

        // ADD ANIMATION
        catwalkLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.move_left);
        catwalkLeftAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //  imgViewLeft.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgViewLeft.setVisibility(View.INVISIBLE);
                walkRight();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        catwalkRightAnimation = AnimationUtils.loadAnimation(this, R.anim.move_right);
        catwalkRightAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //imgViewRight.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgViewRight.setVisibility(View.INVISIBLE);
                walkLeft();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    public void walkLeft(){
        imgViewLeft.startAnimation(catwalkLeftAnimation);
    }

    public void walkRight(){
        imgViewRight.startAnimation(catwalkRightAnimation);
    }

    static class MyXAxisValueFormatter implements IAxisValueFormatter {

        private List<Long> dateIntList;

        public MyXAxisValueFormatter(List<Long> dateIntList){
            this.dateIntList = dateIntList;
        }

        @Override
        public String getFormattedValue(float position, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            int datePos = (int) position;
            long dateLong = dateIntList.get(datePos);
            Calendar tdy = Calendar.getInstance();
            tdy.setTimeZone(SG);
            tdy.setTimeInMillis(dateLong);
            String dateStr = tdy.get(tdy.DAY_OF_MONTH)+"-"+(tdy.get(tdy.MONTH)+1)+"-"+tdy.get(tdy.YEAR) ;
            return dateStr;
        }
    }

}
