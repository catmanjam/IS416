package is416.is416.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Cheryl on 27/3/2018.
 */

public class StepDatabase extends SQLiteOpenHelper {
    private static StepDatabase dbInstance;
    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "reminder";

    private static final TimeZone SG = TimeZone.getTimeZone("Singapore");

    public static final String TABLE_STEPS = "steps";
    // Steps Table and Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_COUNT = "stepCount";


    public StepDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized StepDatabase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (dbInstance == null) {
            dbInstance = new StepDatabase(context.getApplicationContext());
        }
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STEPS_TABLE = "CREATE TABLE " + TABLE_STEPS + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DATE + " REAL,"
                + KEY_COUNT + " INTEGER" + ")";
        db.execSQL(CREATE_STEPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed and Create tables again
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        onCreate(db);
    }

    public void updateStepRecord(Calendar today, int steps){
        today.setTimeZone(SG);
        SQLiteDatabase db = this.getWritableDatabase();

        Integer day = today.get(today.DAY_OF_MONTH);
        Integer month =  today.get(today.MONTH);
        Integer year = today.get(today.YEAR);
        today.set(year, month, day, 0,0,0);
        today.set(Calendar.MILLISECOND,0);

        // sql to retrieve to check
        String selectQuery = "SELECT  * FROM " + TABLE_STEPS + " WHERE " + KEY_DATE + " = " + today.getTimeInMillis();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0){
            // update record
            cursor.getPosition();
            cursor.moveToNext();
            cursor.getPosition();
            Integer id = cursor.getInt(0);
            Integer dbDate = cursor.getInt(1);
            Integer dbSteps = steps; //override the DB value with new step value

            ContentValues values = new ContentValues();
            Log.d("SEE stored time in mils", ""+today.getTimeInMillis());
            values.put(KEY_DATE, today.getTimeInMillis()); // Date Time
            values.put(KEY_COUNT, dbSteps); // Date Time
            db.update(TABLE_STEPS, values, "_id = ? ", new String[] { Integer.toString(id) } );
        }else{
            // insert new record
            Log.d("SEE stored time in mils", ""+today.getTimeInMillis());
            ContentValues values = new ContentValues();
            values.put(KEY_DATE, today.getTimeInMillis()); // Date Time
            values.put(KEY_COUNT, steps); // Steps
            db.insert(TABLE_STEPS, null, values);
            db.toString();
        }

        // Inserting Row
        db.close(); // Closing database connection
    }

    public Cursor getAllSteps(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM "+ TABLE_STEPS + " ORDER BY " + KEY_DATE + " desc", null);
        return c;
    }

    public int getStepsToday(Calendar today){
        today.setTimeZone(SG);
        SQLiteDatabase db = this.getReadableDatabase();
        Integer day = today.get(today.DAY_OF_MONTH);
        Integer month =  today.get(today.MONTH);
        Integer year = today.get(today.YEAR);
        today.set(Calendar.MILLISECOND,0);
        today.set(year, month, day, 0,0,0);

        Cursor c = db.rawQuery("SELECT  * FROM "+ TABLE_STEPS + " WHERE " +  KEY_DATE + " = " + today.getTimeInMillis(), null);
        c.moveToNext();
        int stepsNow = 0;
        if (c.getCount()>0){
            stepsNow = c.getInt(2);
        }
        return stepsNow;
    }


}
