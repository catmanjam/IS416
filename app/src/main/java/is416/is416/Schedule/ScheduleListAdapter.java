package is416.is416.Schedule;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import is416.is416.Database.Appointment;
import is416.is416.R;

/**
 * Created by Cheryl on 18/3/2018.
 */

public class ScheduleListAdapter extends CursorAdapter {
    private Context context;

    static class ViewHolder {
        TextView id;
        TextView date;
        TextView time;
        TextView details;
        ImageView image;
    }

    public ScheduleListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.schedule_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.date = (TextView) view.findViewById(R.id.date);
        viewHolder.time = (TextView) view.findViewById(R.id.time);
        viewHolder.details = (TextView) view.findViewById(R.id.details);
        viewHolder.id = (TextView) view.findViewById(R.id.appt_id);

        viewHolder.date.setText(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        viewHolder.time.setText(cursor.getString(cursor.getColumnIndexOrThrow("time")));
        viewHolder.details.setText(cursor.getString(cursor.getColumnIndexOrThrow("details")));
        viewHolder.id.setText(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
    }
}

