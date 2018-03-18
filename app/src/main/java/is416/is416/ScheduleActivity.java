package is416.is416;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import is416.is416.Database.Appointment;
import is416.is416.Database.Database;

public class ScheduleActivity extends AppCompatActivity {

    private Database myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        myDb = Database.getInstance(this);


        myDb.addAppointment(new Appointment("2018-03-19","19:00:00","Project Meeting"));
        myDb.addAppointment(new Appointment("2018-03-19","15:00:00","Lunch Date"));
        TextView tv = findViewById(R.id.textView);
        List<Appointment> appts = myDb.getAllAppointments();
        String str = "";
        for (Appointment apt : appts){
            str += apt + "\n";
        }
        tv.setText(str);
    }
}
