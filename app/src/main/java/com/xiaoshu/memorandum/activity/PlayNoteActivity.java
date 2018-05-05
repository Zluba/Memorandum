package com.xiaoshu.memorandum.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.xiaoshu.memorandum.R;
import com.xiaoshu.memorandum.fragment.NormalFragment;
import com.xiaoshu.memorandum.utils.ActivityManager;

/**
 *
 * 播放音乐提醒用户
 */

public class PlayNoteActivity extends AppCompatActivity {
    public static final String NOTE_ID = "id";
    private NormalFragment normalFragment;
    private int mId;
    private MediaPlayer player;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_alarm);
        ActivityManager.addActivity(this);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (maxVolume/4)*3,
                AudioManager.FLAG_PLAY_SOUND);

        mId = Integer.parseInt(getIntent().getStringExtra(NOTE_ID));
        setmId(mId);

        normalFragment = new NormalFragment();
        initFragment();
        SharedPreferences pf = getSharedPreferences("ringCode",MODE_PRIVATE);

        if (player!=null && player.isPlaying()){
            player.stop();
            player.release();
            player = MediaPlayer.create(this,R.raw.ring02);
        }else {
            player = MediaPlayer.create(this,R.raw.ring02);

        }

        player.start();
        player.setLooping(true);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.start();
                player.setLooping(true);
            }
        });
    }


    private void initFragment() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
                transaction.add(R.id.frag_content,normalFragment);
                transaction.show(normalFragment);
        transaction.commit();
    }

    private void hideFragment(android.support.v4.app.FragmentTransaction transaction) {
        if (normalFragment != null){
            transaction.hide(normalFragment);
        }

    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmId(){
        return mId;
    }

    @Override
    protected void onDestroy() {
        if (player!=null){
            player.release();
        }
        ActivityManager.removeActivity(this);
        super.onDestroy();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
