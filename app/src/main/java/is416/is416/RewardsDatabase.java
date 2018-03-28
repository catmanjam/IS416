package is416.is416;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.util.Scanner;

import is416.is416.Database.Database;

/**
 * Created by aispa on 3/27/2018.
 */

public class RewardsDatabase extends SQLiteOpenHelper {

    private static RewardsDatabase dbInstance;
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "reminder";

    public static final String TABLE_REWARDS = "rewards";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_REWARDAMT = "reward_amt";
    public static final String KEY_REWARDTYPE = "reward_type";

    private static Resources res;

    public RewardsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized RewardsDatabase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx

        if (dbInstance == null) {
            dbInstance = new RewardsDatabase(context.getApplicationContext());
            res = context.getResources();
        }
        return dbInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + TABLE_REWARDS + "( "
                + KEY_ID + " TEXT PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_REWARDAMT + " TEXT,"
                + KEY_REWARDTYPE + " TEXT" + ")";
        db.execSQL(CREATE_APPOINTMENTS_TABLE);

        Gson gson = new Gson();
//        JsonReader reader = new JsonReader(new FileReader(filename));
        InputStream is = res.openRawResource(R.raw.attraction_reward);
        String jsonString = new Scanner(is).useDelimiter("\\A").next();
        JsonObject jsonObject = gson.fromJson(jsonString,JsonObject.class);
        JsonArray arr = (JsonArray) jsonObject.get("data");
        if (arr.iterator().hasNext()){
            JsonObject row = (JsonObject) arr.iterator().next();
            ContentValues values = new ContentValues();
            String test = row.get("PolyId").getAsString();
//            values.put(KEY_ID,row.get("PolyId").getAsString());
//            values.put(KEY_NAME,row.get("Name").getAsString());
//            values.put(KEY_REWARDAMT,row.get("RewardAmt").getAsString());
//            values.put(KEY_REWARDTYPE,row.get("RewardType").getAsString());

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed and Create tables again
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REWARDS);
        onCreate(db);
    }

//    public void addAppointment(Appointment appt) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_DATE, appt.getDate()); // Date Time
//        values.put(KEY_TIME, appt.getTime()); // Date Time
//        values.put(KEY_DETAILS, appt.getDetails()); // Details
//
//        // Inserting Row
//        db.insert(TABLE_REWARDS, null, values);
//        db.close(); // Closing database connection
//    }

//    public Appointment getAppointment(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_REWARDS,
//                new String[] { KEY_ID, KEY_DATE, KEY_TIME, KEY_DETAILS },
//                KEY_ID + "=?",
////                new String[] { String.valueOf(id) },
//                null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Appointment appt = new Appointment(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
//                cursor.getString(2), cursor.getString(3));
//        // return contact
//        return appt;
//    }

//    public void updateAppointment (Appointment appt) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_DATE, appt.getDate()); // Date Time
//        values.put(KEY_TIME, appt.getTime()); // Date Time
//        values.put(KEY_DETAILS, appt.getDetails()); // Details
//        db.update(TABLE_REWARDS, values, "_id = ? ", new String[] { Integer.toString(appt.getId()) } );
//    }

//    public List<Appointment> getAllAppointments() {
//        List<Appointment> apptList = new ArrayList<Appointment>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_REWARDS;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Appointment appt = new Appointment(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
//                        cursor.getString(2), cursor.getString(3));
//                // Adding contact to list
//                apptList.add(appt);
//            } while (cursor.moveToNext());
//        }
//
//        // return contact list
//        return apptList;
//    }

//    public void delete(int anInt) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        db.delete(TABLE_REWARDS,KEY_ID+"=?",new String[]{String.valueOf(anInt)});
//        db.close();
//    }

    public Cursor getAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM "+TABLE_REWARDS, null);
        return c;
    }


}
