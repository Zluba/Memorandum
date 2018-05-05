package com.xiaoshu.memorandum.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xiaoshu.memorandum.R;
import com.xiaoshu.memorandum.adapter.RecyclerAdapter;
import com.xiaoshu.memorandum.data.MyNoteDataBase;
import com.xiaoshu.memorandum.fragment.PopupDialogFragment;
import com.xiaoshu.memorandum.model.NoteModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private MyNoteDataBase db;
    private Toolbar mToolBar;
    private FloatingActionButton mAddAlarmBtn;
    private RecyclerView mRecyclerView;
    private TextView mNoAlarmTextView;
    private RecyclerAdapter mRecyclerAdapter;
    private List<NoteModel> mAlarmList = new ArrayList<NoteModel>() {
    };

    public static String TOP_STATES = "TOP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MyNoteDataBase(getApplicationContext());

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mAddAlarmBtn = (FloatingActionButton) findViewById(R.id.add_reminder);
        mRecyclerView = (RecyclerView) findViewById(R.id.alarm_list);
        mNoAlarmTextView = (TextView) findViewById(R.id.no_alarm_text);

        mAlarmList = db.getAllAlarms();

        if (mAlarmList.isEmpty()) {
            mNoAlarmTextView.setVisibility(View.VISIBLE);
        }

        mToolBar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolBar);
        mToolBar.setTitle(R.string.app_name);

        mRecyclerView = (RecyclerView) findViewById(R.id.alarm_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerAdapter = new RecyclerAdapter(this, mAlarmList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mAlarmList, i, i + 1);
                    }
                } else {
                    for (int i = toPosition; i < fromPosition; i++) {
                        Collections.swap(mAlarmList, i, i + 1);
                    }
                }

                mRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        registerListener();
        refreshView();

        mAddAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoteActivity.class);
                startActivity(intent);
            }
        });

    }


    private void registerListener() {

        mRecyclerAdapter.setOnclickListener(new RecyclerAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(NoteModel session) {
                int CheckedAlarm = session.getID();
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra(NoteActivity.ALARM_ID, CheckedAlarm + "");
                startActivity(intent);

            }
        });

        mRecyclerAdapter.setItemListener(new RecyclerAdapter.ItemOnLongClickListener() {
            @Override
            public void itemLongClick(final NoteModel noteModel) {
                Bundle bundle = new Bundle();
                bundle.putInt(TOP_STATES, noteModel.getTop());
                PopupDialogFragment popupDialog = new PopupDialogFragment();
                popupDialog.setArguments(bundle);
                popupDialog.setItemOnClickListener(new PopupDialogFragment.DialogItemOnClickListener() {
                    @Override
                    public void onTop() {
                        //置顶
                        noteModel.setTop(1);
                        noteModel.setTopTime(System.currentTimeMillis());
                        refreshView();
                        db.updateAlarm(noteModel);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                popupDialog.show(getFragmentManager(), "popup");
            }
        });

        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "hello", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                finish();
                break;
            case R.id.action_about:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshView() {
        //排序的
        Collections.sort(mAlarmList);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


}
