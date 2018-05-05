package com.xiaoshu.memorandum.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xiaoshu.memorandum.model.NoteModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 数据库
 */

public class MyNoteDataBase {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = " Notedatabase";
    private SQLiteDatabase db;
    private MyNoteOpenHelper myNoteOpenHelper;
    private static final String TABLE_NOTE = " Notetable";
    private static final String KEY_ID = "id";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_ACTIVE = "active";
    private static final String TOP = "top";
    private static final String TOP_TIME = "topTime";


    public MyNoteDataBase(Context context) {
        MyNoteOpenHelper openHelper = new MyNoteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myNoteOpenHelper = openHelper;
        db = openHelper.getWritableDatabase();
    }

    public int addAlarm(NoteModel noteModel) {
        ContentValues values = new ContentValues();

        values.put(KEY_TEXT, noteModel.getTitle());
        values.put(KEY_TIME, noteModel.getTime());
        values.put(KEY_DATE, noteModel.getDate());
        values.put(KEY_ACTIVE, noteModel.getActive());
        values.put(TOP, noteModel.getTop());
        values.put(TOP_TIME, noteModel.getTopTime());

        if (!db.isOpen()) {
            db = myNoteOpenHelper.getWritableDatabase();
        }

        long ID = db.insert(TABLE_NOTE, null, values);
        db.close();
        return (int) ID;
    }


    //获取单个
    public NoteModel getAlarm(int id) {
        if (!db.isOpen()) {
            db = myNoteOpenHelper.getWritableDatabase();
        }
        Cursor cursor = db.query(TABLE_NOTE, new String[]
                {
                        KEY_ID,
                        KEY_TEXT,
                        KEY_TIME,
                        KEY_DATE,
                        KEY_ACTIVE,
                        TOP,
                        TOP_TIME
                }, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);


        NoteModel noteModel = null;
        if (cursor != null && cursor.moveToFirst()) {

            noteModel = new NoteModel(Integer.parseInt(cursor.getString(0))
                    , cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), Integer.parseInt(cursor.getString(5)),
                    cursor.getLong(6));

            cursor.close();
        }

        return noteModel;
    }


    //获取所有NOTE
    public List<NoteModel> getAllAlarms() {
        List<NoteModel> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NOTE;

        if (!db.isOpen()) {
            db = myNoteOpenHelper.getWritableDatabase();
        }

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                NoteModel noteModel = new NoteModel();
                noteModel.setID(Integer.parseInt(cursor.getString(0)));
                noteModel.setTitle(cursor.getString(1));
                noteModel.setTime(cursor.getString(2));
                noteModel.setDate(cursor.getString(3));
                noteModel.setActive(cursor.getString(4));
                noteModel.setTop(cursor.getInt(5));
                noteModel.setTopTime(cursor.getLong(6));

                list.add(noteModel);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return list;
    }


    public int updateAlarm(NoteModel noteModel) {
        ContentValues values = new ContentValues();

        values.put(KEY_TEXT, noteModel.getTitle());
        values.put(KEY_TIME, noteModel.getTime());
        values.put(KEY_DATE, noteModel.getDate());
        values.put(KEY_ACTIVE, noteModel.getActive());
        values.put(TOP, noteModel.getTop());
        values.put(TOP_TIME, noteModel.getTopTime());

        if (!db.isOpen()) {
            db = myNoteOpenHelper.getWritableDatabase();
        }

        return db.update(TABLE_NOTE, values, KEY_ID + "=?",
                new String[]{String.valueOf(noteModel.getID())});
    }

    public void deleteAlarm(int id) {
        if (!db.isOpen()) {
            db = myNoteOpenHelper.getWritableDatabase();
        }
        db.delete(TABLE_NOTE, KEY_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }


}
