package com.xiaoshu.memorandum.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.xiaoshu.memorandum.R;
import com.xiaoshu.memorandum.data.MyNoteDataBase;
import com.xiaoshu.memorandum.model.NoteModel;
import com.xiaoshu.memorandum.utils.AlarmService;

import java.util.Calendar;

/**
 *
 * 网络编辑
 */

public class NoteActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    private Toolbar mToolbar;
    private EditText mTitleText;
    private TextView mTimeText;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;

    private Calendar mCalendar;
    private int mHour, mMinute, ID;
    private int mYear, mMon, mDay;
    private String mActive = "false";
    private String mTitle;
    private String mTime;
    private String mDate;
    private int mTop;
    private Long mTopTime;
    private boolean IS_EDIT = false;//判断是新建 还是 编辑

    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_ACTIVE = "active_key";

    public static final String ALARM_ID = "Alarm_ID";

    private ServiceConnection connection = null;
    private AlarmService.MyBinder binder;

    private MyNoteDataBase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        init();

        // 得到上次设置状态
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;
            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;
            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }

        mTitleText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    //保存当前设置状态
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);

    }


    public void init() {


        if (getIntent().getStringExtra(ALARM_ID) != null) {
            ID = Integer.valueOf(getIntent().getStringExtra(ALARM_ID));
            IS_EDIT = true;
        }


        //初始化View
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (EditText) findViewById(R.id.alarm_title);
        mTimeText = (TextView) findViewById(R.id.time_text);


        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);

        //配置ToolBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_add_alarm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //设置默认值
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMon = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        mTime = mHour + ":" + mMinute;
        mDate = mYear + "-" + mMon + "-" + mDay;

        mTimeText.setText("定时提醒： " + mDate + "  " + mTime);

        db = new MyNoteDataBase(this);

        if (IS_EDIT) {
            NoteModel mCheckedAlarm = db.getAlarm(ID);
            mTitle = mCheckedAlarm.getTitle();
            mTitleText.setText(mCheckedAlarm.getTitle() + "");
            if (mCheckedAlarm.getActive().equals("true")) {
                mFAB1.setVisibility(View.GONE);
                mFAB2.setVisibility(View.VISIBLE);
                mTimeText.setText("定时提醒： " + mCheckedAlarm.getDate() + "  " + mCheckedAlarm.getTime());
            } else {
                mFAB1.setVisibility(View.VISIBLE);
                mFAB2.setVisibility(View.GONE);
            }


        } else {
            ID = db.addAlarm(new NoteModel(mTitle, mTime, mDate, mActive, mTop, mTopTime));
            mFAB1.setVisibility(View.VISIBLE);
            mFAB2.setVisibility(View.GONE);
        }
    }


    public void selectTime(View v) {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                NoteActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), " Datepickerdialog ");


    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        mTimeText.append("  " + mTime);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Calendar now = Calendar.getInstance();
        TimePickerDialog timeDialog = TimePickerDialog.newInstance(
                this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
        timeDialog.setThemeDark(false);
        timeDialog.show(getFragmentManager(), "选择时间");

        mDate = year + "-" + monthOfYear + "-" + dayOfMonth;
        mTimeText.setText("定时提醒： " + mDate);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();


                return true;

            case R.id.save_Alarm:
                mTitleText.setText(mTitle);
                if (mTitleText.getText().toString().length() == 0)
                    mTitleText.setError("备忘名不能为空");
                else {
                    saveNote();
                    finish();
                }
                return true;


            case R.id.discard_alarm:

                if (IS_EDIT) {
                    db.deleteAlarm(ID);
                    Intent intent2 = new Intent();
                    intent2.setClass(NoteActivity.this, MainActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                } else {
                    Toast.makeText(getApplicationContext(), "取消设置",
                            Toast.LENGTH_SHORT).show();

                    onBackPressed();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //保存设置
    private void saveNote() {

        NoteModel model = db.getAlarm(ID);
        model.setTitle(mTitle);
        model.setTime(mTime);
        model.setDate(mDate);
        model.setActive(mActive);
        model.setTop(mTop);
        model.setTopTime(mTopTime);

        db.updateAlarm(model);

        if (mActive.equals("true")) {
            connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    binder = (AlarmService.MyBinder) service;

                    binder.setSingleAlarm(getApplicationContext(), mDate, mTime, ID);

                    Log.d("AddActivity", "绑定服务");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
        }

        Intent intent = new Intent(this, AlarmService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);

        Intent intent2 = new Intent();
        intent2.setClass(NoteActivity.this, MainActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent2);
    }


    public void selectFab1(View v) {
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.GONE);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.VISIBLE);
        mActive = "true";
    }

    public void selectFab2(View v) {
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.GONE);
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.VISIBLE);
        mActive = "false";
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_alarm, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
       if(IS_EDIT){
           finish();
       }else {
           db.deleteAlarm(ID);
       }

        Log.d("AddActivity", "用户取消创建Alarm");

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (connection != null) {
            unbindService(connection);
        }
        super.onDestroy();
    }


}
