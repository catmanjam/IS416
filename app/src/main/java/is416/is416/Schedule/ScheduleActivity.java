package is416.is416.Schedule;

import android.app.ListActivity;
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

import java.util.List;

import is416.is416.Database.Appointment;
import is416.is416.Database.Database;
import is416.is416.R;
import is416.is416.Schedule.ScheduleListAdapter;

public class ScheduleActivity extends AppCompatActivity {

    private Database myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        myDb = Database.getInstance(this);
        final ListView lv = findViewById(R.id.schedule_list);
        SQLiteDatabase db = myDb.getWritableDatabase();
        Log.d("Database", ""+db);

        Cursor cursor = db.rawQuery("SELECT  * FROM appointments", null);

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
                        //Delete of record from Database and List view.
                        cursor.moveToPosition(pos);
                        myDb.delete(cursor.getInt(cursor.getColumnIndex(Database.KEY_ID)));
                        cursor=myDb.getAll();
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
                return false;
            }
        });

    }
}
