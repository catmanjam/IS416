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
    private List<Appointment> list;

    static class ViewHolder{

        TextView date;
        TextView time;
        TextView details;
        ImageView image;
    }

    public ScheduleListAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }


//    public ScheduleListAdapter(Context context, List<Appointment> list){
//        this.context = context;
//        this.list = list;
//    }

//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View rowView = convertView;
//
//        ViewHolder viewHolder = new ViewHolder();
//        if(rowView == null){
//            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            rowView = inflater.inflate(R.layout.schedule_list, null );
//            viewHolder.date = (TextView) rowView.findViewById(R.id.date);
//            viewHolder.time = (TextView)rowView.findViewById(R.id.time);
//            viewHolder.details = (TextView) rowView.findViewById(R.id.details);
//            rowView.setTag(viewHolder);
//        }else{
//            viewHolder = (ViewHolder) rowView.getTag();
//        }
//        viewHolder.date.setText(list.get(position).getDate());
//        viewHolder.time.setText(list.get(position).getTime());
//        viewHolder.details.setText(list.get(position).getDetails());
//
//        return rowView;
//    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.schedule_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.date = (TextView) view.findViewById(R.id.date);
        viewHolder.time = (TextView)view.findViewById(R.id.time);
        viewHolder.details = (TextView) view.findViewById(R.id.details);

        viewHolder.date.setText(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        viewHolder.time.setText(cursor.getString(cursor.getColumnIndexOrThrow("time")));
        viewHolder.details.setText(cursor.getString(cursor.getColumnIndexOrThrow("details")));
        view.setTag(viewHolder);
    }
}
