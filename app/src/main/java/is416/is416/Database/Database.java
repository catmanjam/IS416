package is416.is416.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cheryl on 18/3/2018.
 */

public class Database extends SQLiteOpenHelper {

    private static Database dbInstance;
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "reminder";

    // Apporintment Table and Columns names
    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_DETAILS = "details";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized Database getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (dbInstance == null) {
            dbInstance = new Database(context.getApplicationContext());
        }
        return dbInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + TABLE_APPOINTMENTS + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DATE + " DATETIME,"
                + KEY_TIME + " DATETIME,"
                + KEY_DETAILS + " TEXT" + ")";
        db.execSQL(CREATE_APPOINTMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed and Create tables again
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        onCreate(db);
    }

    public void addAppointment(Appointment appt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, appt.getDate()); // Date Time
        values.put(KEY_TIME, appt.getTime()); // Date Time
        values.put(KEY_DETAILS, appt.getDetails()); // Details

        // Inserting Row
        db.insert(TABLE_APPOINTMENTS, null, values);
        db.close(); // Closing database connection
    }

    public Appointment getAppointment(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_APPOINTMENTS,
                new String[] { KEY_ID, KEY_DATE, KEY_TIME, KEY_DETAILS },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Appointment appt = new Appointment(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
                cursor.getString(2), cursor.getString(3));
        // return contact
        return appt;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> apptList = new ArrayList<Appointment>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_APPOINTMENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Appointment appt = new Appointment(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
                // Adding contact to list
                apptList.add(appt);
            } while (cursor.moveToNext());
        }

        // return contact list
        return apptList;
    }

    public void delete(int anInt) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_APPOINTMENTS,KEY_ID+"=?",new String[]{String.valueOf(anInt)});
        db.close();
    }

    public Cursor getAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM "+TABLE_APPOINTMENTS, null);
        return c;
    }



}
