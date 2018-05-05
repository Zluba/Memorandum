package com.xiaoshu.memorandum.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;



public class NoteModel implements Serializable, Comparable {


    private int ID;
    private String noteText;
    private String time;
    private String active;
    private String date;
    private int top;
    private Long topTime;


    public NoteModel(int ID, String Title, String Time, String Date, String Active, int Top, Long TopTime) {
        this.ID = ID;
        noteText = Title;
        time = Time;
        active = Active;
        date = Date;
        top = Top;
        topTime = TopTime;
    }

    public NoteModel(String Title, String Time, String Date, String Active, int Top, Long TopTime) {
        noteText = Title;
        time = Time;
        active = Active;
        date = Date;
        top = Top;
        topTime = TopTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public Long getTopTime() {
        return topTime;
    }

    public void setTopTime(Long topTime) {
        this.topTime = topTime;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public NoteModel() {
    }

    ;

    public int getID() {
        return ID;
    }

    public void setID(int mID) {
        this.ID = mID;
    }

    public String getTitle() {
        return noteText;
    }

    public void setTitle(String mTitle) {
        this.noteText = mTitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String mTime) {
        this.time = mTime;
    }


    public String getActive() {
        return active;
    }

    public void setActive(String mActive) {
        this.active = mActive;
    }


    @Override
    public int compareTo(@NonNull Object another) {
        if (another == null || !(another instanceof NoteModel)) {
            return -1;
        }

        NoteModel otherSession = (NoteModel) another;
        /**置顶判断
         * */
        int result = 0 - (top - otherSession.getTop());
        if (result == 0) {
            result = 0 - compareToTime(topTime, otherSession.getTopTime());
        }
        return result;
    }

    /**
     * 根据时间对比
     */
    public static int compareToTime(long lhs, long rhs) {
        Calendar cLhs = Calendar.getInstance();
        Calendar cRhs = Calendar.getInstance();
        cLhs.setTimeInMillis(lhs);
        cRhs.setTimeInMillis(rhs);
        return cLhs.compareTo(cRhs);
    }
}
