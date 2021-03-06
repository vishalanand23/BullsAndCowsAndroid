package com.games.vishalanand23.bullsandcowsandroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.games.vishalanand23.bullsandcowsandroid.data.PlayResult;

public class DbStorageHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "bulls_and_cows";
    private static final String PLAY_TABLE_NAME = "play_results";
    private static final String ID = "_id";
    private static final String DEVICE_ID = "device" + ID;
    private static final String NUM_OF_DIGITS = "num_of_digits";
    private static final String PLAYING_NUMBER = "playing_number";
    private static final String NUMBER_OF_GUESSES = "number_of_guesses";
    private static final String WIN_GAME = "win_game";
    private static final String TIME_IN_MILLIS = "time_in_millis";
    private static final String NUMBER_IN_SCORE = "50";

    private static final String PLAY_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + PLAY_TABLE_NAME + " (" +
                    ID + " INTEGER primary key autoincrement," +
                    DEVICE_ID + " TEXT ," +
                    NUM_OF_DIGITS + " INTEGER ," +
                    PLAYING_NUMBER + " TEXT ," +
                    NUMBER_OF_GUESSES + " INTEGER ," +
                    WIN_GAME + " INTEGER ," +
                    TIME_IN_MILLIS + " INTEGER );";

    private static final String CHECKED = "checked";
    private static final String RULES_CHECKBOX_TABLE_NAME = "rules_check";
    private static final String RULES_CHECKBOX_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + RULES_CHECKBOX_TABLE_NAME + " (" +
                    ID + " INTEGER primary key autoincrement," +
                    CHECKED + " INTEGER);";

    public DbStorageHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PLAY_TABLE_CREATE);
        db.execSQL(RULES_CHECKBOX_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbStorageHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        onCreate(db);
    }

    public void insertInDb(PlayResult playResult) {
        ContentValues values = new ContentValues();
        values.put(DEVICE_ID, playResult.getDeviceId());
        values.put(NUM_OF_DIGITS, playResult.getNumberOfDigits());
        values.put(PLAYING_NUMBER, playResult.getPlayingNumber());
        values.put(NUMBER_OF_GUESSES, playResult.getNumberOfGuesses());
        values.put(WIN_GAME, playResult.getWinGame());
        values.put(TIME_IN_MILLIS, playResult.getTimeInMillis());
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.insert(PLAY_TABLE_NAME, null, values);
        writableDatabase.close();
    }

    public int fastestTime(int numberOfDigits) {
        String[] projection = {TIME_IN_MILLIS};
        String[] whereArgs = {Integer.toString(1), Integer.toString(numberOfDigits)};
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(
                PLAY_TABLE_NAME,
                projection,
                WIN_GAME + " = ? AND " + NUM_OF_DIGITS + " = ? ",
                whereArgs,
                null,
                null,
                TIME_IN_MILLIS,
                "1");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int fastestTime = cursor.getInt(0);
            cursor.close();
            readableDatabase.close();
            return fastestTime;
        } else {
            cursor.close();
            readableDatabase.close();
            return -1;
        }
    }

    public long score(int numberOfDigits) {
        String subQuery = "select avg(" + TIME_IN_MILLIS + ") from ( select " + TIME_IN_MILLIS
                + " from " + PLAY_TABLE_NAME + " where " + WIN_GAME + " = 1 AND " + NUM_OF_DIGITS
                + " = " + numberOfDigits + " order by " + TIME_IN_MILLIS
                + " limit " + NUMBER_IN_SCORE + ") as t";
        String[] args = {};
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery(subQuery, args);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int score = cursor.getInt(0);
            cursor.close();
            readableDatabase.close();
            return score;
        } else {
            cursor.close();
            readableDatabase.close();
            return -1;
        }
    }

    public int numberOfGames(int numberOfDigits) {
        String[] projection = {ID};
        String[] whereArgs = {Integer.toString(numberOfDigits)};
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(
                PLAY_TABLE_NAME,
                projection,
                NUM_OF_DIGITS + " = ? ",
                whereArgs,
                null,
                null,
                null);
        int number = cursor.getCount();
        cursor.close();
        readableDatabase.close();
        return number;
    }

    public int numberOfWins(int numberOfDigits) {
        String[] projection = {ID};
        String[] whereArgs = {Integer.toString(1), Integer.toString(numberOfDigits)};
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(
                PLAY_TABLE_NAME,
                projection,
                WIN_GAME + " = ? AND " + NUM_OF_DIGITS + " = ? ",
                whereArgs,
                null,
                null,
                null);
        int number = cursor.getCount();
        cursor.close();
        readableDatabase.close();
        return number;
    }

    public boolean checkRules() {
        String[] projection = {CHECKED};
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(
                RULES_CHECKBOX_TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        if (cursor.getCount() == 0) {
            setRulesCheckedTable(true);
            cursor.close();
            readableDatabase.close();
            return true;
        } else {
            cursor.moveToFirst();
            boolean ret = cursor.getInt(0) == 1;
            cursor.close();
            readableDatabase.close();
            return ret;
        }
    }

    public void setRulesCheckedTable(boolean b) {
        cleanCheckRulesTable();
        int check = b ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put(CHECKED, check);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.insert(RULES_CHECKBOX_TABLE_NAME, null, values);
        writableDatabase.close();
    }

    private void cleanCheckRulesTable() {
        String where = ID + ">" + "0";
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(RULES_CHECKBOX_TABLE_NAME, where, null);
        writableDatabase.close();
    }

//    public void sanitizeDb() {
//        ContentValues newValues = new ContentValues();
//        newValues.put(TIME_IN_MILLIS, 2147483647);
//
//        getWritableDatabase().update(PLAY_TABLE_NAME, newValues, "time_in_millis<0", null);
//
//        getWritableDatabase().delete(PLAY_TABLE_NAME, "length(playing_number)" + "<" + "4", null);
//    }

//    public void createFile() {
//        Cursor cursor = getReadableDatabase().query(
//                PLAY_TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                ID,
//                null);
//
//        try {
//            String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
//            String fileName = "abc2.csv";
//            String filePath = baseDir + File.separator + fileName;
//            File f = new File(filePath);
//            FileWriter mFileWriter = new FileWriter(filePath, true);
//            while (cursor.moveToNext()) {
//                String writeRow = cursor.getString(cursor.getColumnIndex(DEVICE_ID)) + ","
//                        + cursor.getInt(cursor.getColumnIndex(NUM_OF_DIGITS)) + ","
//                        + cursor.getInt(cursor.getColumnIndex(PLAYING_NUMBER)) + ","
//                        + cursor.getInt(cursor.getColumnIndex(NUMBER_OF_GUESSES)) + ","
//                        + cursor.getInt(cursor.getColumnIndex(WIN_GAME)) + ","
//                        + cursor.getInt(cursor.getColumnIndex(TIME_IN_MILLIS));
//                mFileWriter.write(writeRow);
//                mFileWriter.write(System.getProperty("line.separator"));
//            }
//            mFileWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        cursor.close();
//    }
}