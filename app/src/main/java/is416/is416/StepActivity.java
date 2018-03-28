package is416.is416;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import is416.is416.Database.StepDatabase;

public class StepActivity extends AppCompatActivity {
    private static final TimeZone SG = TimeZone.getTimeZone("Singapore");
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

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
        barChart.getAxisLeft().setTextColor(yellowInt);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(dates));
        xAxis.setTextColor(yellowInt);
        int whiteInt = ContextCompat.getColor(this, R.color.offWhite);
        barChart.getLegend().setTextColor(whiteInt);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getDescription().setEnabled(false);
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
       // barChart.getAxisLeft().setValueFormatter(new MyYAxisValueFormatter()); // set Y axis whole number only
//    static class MyYAxisValueFormatter implements IAxisValueFormatter  {
//
//        @Override
//        public String getFormattedValue(float value, AxisBase axis) {
//            return String.valueOf((int)value);
//        }
//    };
