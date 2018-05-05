package com.xiaoshu.memorandum.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xiaoshu.memorandum.data.MyNoteDataBase;
import com.xiaoshu.memorandum.model.NoteModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * 提醒的服务
 */

public class AlarmService extends Service {

    private static final int ONE_DAY_TIME = 1000 * 60 * 60 * 24;
    private static final int CONFIG_ONE = 0;
    private static final int CONFIG_TWO = 1;
    private static AlarmManager alarmManager;

    private MyBinder myBinder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        Log.d("Service", "启动服务");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Intent service = new Intent(this, AlarmService.class);
        this.startService(service);
        Log.d("Service", "重启服务");
        super.onDestroy();
    }

    public static class MyBinder extends Binder {
        private String[] mTimeSplit;
        private String[] mDateSplit;
        private int mHour;
        private int mMinute;
        private int mYear;
        private int mMounth;
        private int mDay;

        public void setSingleAlarm(
                Context context, String date, String time, int id) {

            mTimeSplit = time.split(":");
            mDateSplit = date.split("-");
            mHour = Integer.parseInt(mTimeSplit[0]);
            mMinute = Integer.parseInt(mTimeSplit[1]);

            mYear = Integer.parseInt(mDateSplit[0]);
            mMounth = Integer.parseInt(mDateSplit[1]);
            mDay = Integer.parseInt(mDateSplit[2]);

            Calendar c = Calendar.getInstance();
            c.set(mYear, mMounth, mDay, mHour, mMinute, 0);

            long mTimeInfo = c.getTimeInMillis();
            long actualTime = mTimeInfo > System.currentTimeMillis()
                    ? mTimeInfo : mTimeInfo + ONE_DAY_TIME;

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            int flagDayOfWeek = 0;
            alarmManager.set(AlarmManager.RTC_WAKEUP, actualTime, getIntent(CONFIG_ONE, context, id, flagDayOfWeek));

            Log.d("Service", "设置完成");
        }


        private PendingIntent getIntent(int config, Context context, int id, int flagDayOfWeek) {

            Intent intent = new Intent(context, MyAlarmReceiver.class);
            intent.putExtra(MyAlarmReceiver.ID_FLAG, Integer.toString(id));

            switch (config) {
                case CONFIG_ONE:

                    return PendingIntent.getBroadcast(context,
                            id + flagDayOfWeek, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                case CONFIG_TWO:
                    return PendingIntent.getBroadcast(context,
                            id + flagDayOfWeek, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            }
            return null;
        }
    }


    public static class RebootReceiver extends BroadcastReceiver {

        private String mTime, mDate,mActive;
        private int mID;
        private AlarmService.MyBinder binder = new MyBinder();

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

                Log.d("RebootReceiver", "接收到开机广播");

                MyNoteDataBase db = new MyNoteDataBase(context);
                List<NoteModel> alarm = db.getAllAlarms();

                for (NoteModel am : alarm) {
                    mTime = am.getTime();
                    mID = am.getID();
                    mDate = am.getDate();
                    mActive = am.getActive();

                    if (mActive.equals("true")) {
                        binder.setSingleAlarm(context, mDate, mTime, mID);

                        Log.v("RebootReceiver", "完成重启后设置");

                    }
                }
            }
        }


    }
}
