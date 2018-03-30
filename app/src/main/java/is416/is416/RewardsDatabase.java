package is416.is416;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by aispa on 3/27/2018.
 */

public class RewardsDatabase extends SQLiteOpenHelper {

    private static RewardsDatabase dbInstance;
    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "reminder";

    public static final String TABLE_REWARDS = "rewards";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_REWARDAMT = "reward_amt";
    public static final String KEY_REWARDTYPE = "reward_type";
    public static final String KEY_POLY = "polyID";
    public static final String KEY_REWARDNAME = "reward_name";
    public static final String KEY_CLAIMSTATUS = "reward_claim_status";

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
        System.out.println("SADOYW)OEWDNOWJOIWQJDOHEWQ#EDWD");
        String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + TABLE_REWARDS + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_POLY + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_REWARDNAME + " TEXT,"
                + KEY_CLAIMSTATUS + " INTEGER"+ ")";
//                + KEY_REWARDAMT + " TEXT,"
//                + KEY_REWARDTYPE + " TEXT" + ")";
        db.execSQL(CREATE_APPOINTMENTS_TABLE);
        initializeDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed and Create tables again
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REWARDS);
        onCreate(db);
    }

    public void initializeDB(SQLiteDatabase db){
        Gson gson = new Gson();
//        JsonReader reader = new JsonReader(new FileReader(filename));
        InputStream is = res.openRawResource(R.raw.attraction_reward);
        String jsonString = new Scanner(is).useDelimiter("\\A").next();
        JsonObject jsonObject = gson.fromJson(jsonString,JsonObject.class);
        JsonArray arr = (JsonArray) jsonObject.get("data");
        for (int i = 0; i < arr.size();i++){
            JsonObject row = (JsonObject) arr.get(i);
            ContentValues values = new ContentValues();
            values.put(KEY_POLY,row.get("PolyId").getAsString());
            values.put(KEY_NAME,row.get("Name").getAsString());
            values.put(KEY_REWARDNAME,randomRewards());
            values.put(KEY_CLAIMSTATUS,0);
            db.insert(TABLE_REWARDS,null,values);
        }
    }

    private String randomRewards(){
        Gson gson = new Gson();
//        JsonReader reader = new JsonReader(new FileReader(filename));
        InputStream is = res.openRawResource(R.raw.attraction_reward);
        String jsonString = new Scanner(is).useDelimiter("\\A").next();
        JsonObject jsonObject = gson.fromJson(jsonString,JsonObject.class);
        JsonArray arr2 = (JsonArray) jsonObject.get("rewards");
        Random rand = new Random();
        JsonObject randomRewardRow = (JsonObject) arr2.get(rand.nextInt(arr2.size()));
        return randomRewardRow.get("Name").getAsString();
    }

    public String getLocationNameByPolyId(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REWARDS,
                new String[] {KEY_ID,KEY_POLY,KEY_NAME,KEY_REWARDNAME,KEY_CLAIMSTATUS},
                KEY_POLY + "=?",
                new String[] { name },
                null,null,null,null);

        if (cursor != null){
            cursor.moveToFirst();
            return cursor.getString(2);
        }

        return null;
    }

    public Rewards getRewardByPolyId(String name){
        String rewardName = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REWARDS,
                new String[] {KEY_ID,KEY_POLY,KEY_NAME,KEY_REWARDNAME,KEY_CLAIMSTATUS},
                KEY_POLY + "=?",
                new String[] { name },
                null,null,null,null);

        if (cursor != null){
            cursor.moveToFirst();
            rewardName = cursor.getString(3);
        }

        if (rewardName!=null){
            return getRewardDetailsByName(rewardName);
        }

        return null;
    }

    public Rewards getRewardDetailsByName(String name){
        Gson gson = new Gson();
        InputStream is = res.openRawResource(R.raw.attraction_reward);
        String jsonString = new Scanner(is).useDelimiter("\\A").next();
        JsonObject jsonObject = gson.fromJson(jsonString,JsonObject.class);
        JsonArray arr2 = (JsonArray) jsonObject.get("rewards");
        for (int i = 0;i<arr2.size();i++){
            JsonObject row = (JsonObject) arr2.get(i);
            if (Objects.equals(row.get("Name").getAsString(), name)){
                return new Rewards(
                        row.get("Name").getAsString(),
                        row.get("RewardAmt").getAsString(),
                        row.get("RewardType").getAsString()
                );
            };
        }
        return null;
    }

    public void claimRewardByName(String name,String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CLAIMSTATUS, 1); // Date Time
        db.update(TABLE_REWARDS, values, KEY_REWARDNAME+" = ? AND "+KEY_POLY+" = ? ", new String[] { name,id } );
    }

    public boolean checkClaimedByName(String name,String id){
        String rewardName = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REWARDS,
                new String[] {KEY_ID,KEY_POLY,KEY_NAME,KEY_REWARDNAME,KEY_CLAIMSTATUS},
                KEY_REWARDNAME + " = ? AND "+KEY_POLY + " = ? ",
                new String[] { name, id },
                null,null,null,null);

        if (cursor != null){
            cursor.moveToFirst();
            if (cursor.getInt(4)==1){
                return true;
            }else {
                return false;
            }
        }
        return false;
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
