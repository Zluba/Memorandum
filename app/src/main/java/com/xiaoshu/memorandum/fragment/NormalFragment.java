package com.xiaoshu.memorandum.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xiaoshu.memorandum.R;
import com.xiaoshu.memorandum.activity.PlayNoteActivity;
import com.xiaoshu.memorandum.data.MyNoteDataBase;
import com.xiaoshu.memorandum.model.NoteModel;

/**
 *
 * 定时提醒页
 */

public class NormalFragment extends Fragment {

    private Button btn;

    private TextView alarmTittle;
    private PlayNoteActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_normal, container, false);

        btn = (Button) view.findViewById(R.id.btn_stopAlarm_normal);
        alarmTittle = (TextView) view.findViewById(R.id.textView3);
        activity = (PlayNoteActivity) getActivity();

        MyNoteDataBase db = new MyNoteDataBase(getActivity());
        NoteModel alarm = db.getAlarm(activity.getmId());

        Log.d("id ", String.valueOf(activity.getmId()));
        alarmTittle.setText(alarm.getTitle());


        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getActivity().finish();
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        System.out.println("已StartNormalFragment");

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
