package com.xiaoshu.memorandum.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.xiaoshu.memorandum.activity.PlayNoteActivity;
import com.xiaoshu.memorandum.data.MyNoteDataBase;
import com.xiaoshu.memorandum.model.NoteModel;

import java.util.Calendar;



public class MyAlarmReceiver extends BroadcastReceiver {

    public static final String ID_FLAG = "flag";
    private AlarmManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,PlayNoteActivity.class);
        i.putExtra(PlayNoteActivity.NOTE_ID, intent.getStringExtra(ID_FLAG));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        int alarmId = Integer.parseInt(intent.getStringExtra(ID_FLAG));
        MyNoteDataBase db = new MyNoteDataBase(context);

        Log.d("AlarmReceiver","接收到广播");

    }


}
