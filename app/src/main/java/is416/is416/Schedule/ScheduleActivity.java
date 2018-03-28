package is416.is416.Schedule;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import is416.is416.Database.ApptDatabase;
import is416.is416.R;

public class ScheduleActivity extends AppCompatActivity {

    private ApptDatabase myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        myDb = ApptDatabase.getInstance(this);

        final ListView lv = findViewById(R.id.schedule_list);

        Cursor cursor = myDb.getAllAppointments();

        final ScheduleListAdapter adapter = new ScheduleListAdapter(this, cursor);
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView arg0, View v, int position, long arg3) {

                AlertDialog.Builder ad = new AlertDialog.Builder(ScheduleActivity.this);
                ad.setTitle("Delete");
                ad.setMessage("Sure you want to delete record ?");
                final int pos = position;
                ad.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = myDb.getWritableDatabase();
                        Cursor cursor = db.rawQuery("SELECT  * FROM appointments", null);
                        //Delete of record from ApptDatabase and List view.
                        cursor.moveToPosition(pos);
                        myDb.delete(cursor.getInt(cursor.getColumnIndex(ApptDatabase.KEY_ID)));
                        cursor = myDb.getAllAppointments();
                        adapter.swapCursor(cursor);
                        lv.setAdapter(adapter);
                    }
                });
                ad.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                return true;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                DialogFragment newFragment = new UpdateApptDialog();

                View childView = lv.getChildAt(position);
                // retrieve all the text views
                TextView tvdate = (TextView) childView.findViewById(R.id.date);
                TextView tvtime = (TextView) childView.findViewById(R.id.time);
                TextView tvdetails = (TextView) childView.findViewById(R.id.details);
                TextView tvid = (TextView)childView.findViewById(R.id.appt_id);

                // set variables based on text view contents
                String date = tvdate.getText().toString();
                String time = tvtime.getText().toString();
                String details = tvdetails.getText().toString();
                Integer appId = Integer.parseInt(tvid.getText().toString());

                // set arguments
                args.putInt("id", appId);
                args.putString("date",date);
                args.putString("time", time);
                args.putString("details", details);

                // sent arguments to fragment
                newFragment.setArguments(args);
                FragmentManager fm = getFragmentManager();
                newFragment.show(fm, "UPDATE DIALOG");

                // set fragment contents
                // fragment to update db on update click

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        final ListView lv = findViewById(R.id.schedule_list);
        SQLiteDatabase db = myDb.getWritableDatabase();
        Cursor cursor = myDb.getAllAppointments();;
        final ScheduleListAdapter adapter = new ScheduleListAdapter(this, cursor);

        lv.setAdapter(adapter);
    }

}
