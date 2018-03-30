package is416.is416.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

import is416.is416.Rewards;
import is416.is416.RewardsDatabase;

/**
 * Created by Cheryl on 29/3/2018.
 */

public class StatsDatabase extends SQLiteOpenHelper {

    private static StatsDatabase dbInstance;
    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "reminder";

    private static final TimeZone SG = TimeZone.getTimeZone("Singapore");

    public static final String TABLE_STATS = "stats";
    // Steps Table and Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_ITEM = "item";
    public static final String KEY_VALUE = "item_value";


    public static final String ITEM_HAPPINESS = "happy";
    public static final String ITEM_STEPS = "steps";
    public static final String ITEM_HIGHSCORE = "score";
    public static final SQLiteDatabase writableDb = null;
    public static final SQLiteDatabase readableDb = null;

    //Handle other database
    private static RewardsDatabase rwDB;

    public StatsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized StatsDatabase getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new StatsDatabase(context.getApplicationContext());
            rwDB = RewardsDatabase.getInstance(context.getApplicationContext());
        }
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STATS_TABLE = "CREATE TABLE " + TABLE_STATS + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ITEM + " TEXT,"
                + KEY_VALUE + " INTEGER" + ")";
        db.execSQL(CREATE_STATS_TABLE);

        String CREATE_STEPS_TABLE = "CREATE TABLE " + StepDatabase.TABLE_STEPS + "( "
                + StepDatabase.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + StepDatabase.KEY_DATE + " REAL,"
                + StepDatabase.KEY_COUNT + " INTEGER" + ")";
        db.execSQL(CREATE_STEPS_TABLE);

        String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + ApptDatabase.TABLE_APPOINTMENTS + "( "
                + ApptDatabase.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ApptDatabase.KEY_DATE + " DATETIME,"
                + ApptDatabase.KEY_TIME + " DATETIME,"
                + ApptDatabase.KEY_DETAILS + " TEXT" + ")";
        db.execSQL(CREATE_APPOINTMENTS_TABLE);

        System.out.println("SADOYW)OEWDNOWJOIWQJDOHEWQ#EDWD");
        String CREATE_REWARDS_TABLE = "CREATE TABLE " + RewardsDatabase.TABLE_REWARDS + "( "
                + RewardsDatabase.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RewardsDatabase.KEY_POLY + " TEXT,"
                + RewardsDatabase.KEY_NAME + " TEXT,"
                + RewardsDatabase.KEY_REWARDNAME + " TEXT,"
                + RewardsDatabase.KEY_CLAIMSTATUS + " INTEGER"+ ")";
//                + KEY_REWARDAMT + " TEXT,"
//                + KEY_REWARDTYPE + " TEXT" + ")";
        db.execSQL(CREATE_REWARDS_TABLE);

        rwDB.initializeDB(db);
        this.initialiseDatabase(db);
    }

    public void initialiseDatabase(SQLiteDatabase db){
        insertValues(ITEM_HAPPINESS, 500,db);
        insertValues(ITEM_STEPS, 500,db);
        insertValues(ITEM_HIGHSCORE, 20,db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed and Create tables again
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS);
        onCreate(db);
    }

    public int getHappy(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c= db.rawQuery("SELECT  * FROM "+ TABLE_STATS + " WHERE " + KEY_ITEM + " = '" + ITEM_HAPPINESS+"'", null);
        int i = 0;

        c.moveToFirst();
        if (c.getCount()>0){
            i = c.getInt(2);// itemvalue
        }
        return i;
    }

    public int getSteps(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM "+ TABLE_STATS + " WHERE " + KEY_ITEM + " = '" + ITEM_STEPS+"'", null);
        c.moveToFirst();
        int i = 0;
        String debug = "";
        if (c.getCount()>0){
            i = c.getInt(2);// itemvalue
            debug += c.getInt(0);
            debug += c.getString(1);
            debug += ""+i;
        }
        Log.d("SEE", debug);
        return i;
    }

    public int getHighscore(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM "+ TABLE_STATS + " WHERE " + KEY_ITEM + " = " + "'"  + ITEM_HIGHSCORE +"'", null);
        c.moveToFirst();
        if (c.getCount()>0){
            return c.getInt(2);// itemvalue
        }else{
            return 0;
        }
    }

    public void setHappy(int happy){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, happy); // Steps
        db.update(TABLE_STATS, values, KEY_ITEM +"=?", new String[]{ITEM_HAPPINESS});
    }

    public void setHighscore(int highscore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, highscore); // Steps
        db.update(TABLE_STATS, values, KEY_ITEM +"=?", new String[]{ITEM_HIGHSCORE});
    }

    public void setSteps(int steps){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, steps); // Steps
        db.update(TABLE_STATS, values, KEY_ITEM +"=?", new String[]{ITEM_STEPS});
    }

    public void insertValues(String item, int score, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM, item); // Date Time
        values.put(KEY_VALUE, score); // Steps
        db.insert(TABLE_STATS, null, values);
    }
}
