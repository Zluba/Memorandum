package com.xiaoshu.memorandum.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * 数据库
 */

public class MyNoteOpenHelper extends SQLiteOpenHelper {


    private static final String TABLE_NOTE = " Notetable";
    private static final String KEY_ID = "id";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_ACTIVE = "active";
    private static final String TOP = "top";
    private static final String TOP_TIME = "topTime";

    String CREATE_ALARM_TABLE = "CREATE TABLE" + TABLE_NOTE +

            "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TEXT + " TEXT,"
            + KEY_TIME + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_ACTIVE + " BOOLEAN,"
            + TOP + " INTEGER,"
            + TOP_TIME + " INTEGER" + ")";


    public MyNoteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALARM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        onCreate(db);
    }
}
