package com.xiaoshu.memorandum.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public abstract class RecyclerArrayAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private List<T> mObjects;

    public RecyclerArrayAdapter(final List<T> objects) {
        mObjects = objects;
    }


    public void add(final T object) {
        mObjects.add(object);
        notifyItemInserted(getItemCount() - 1);
    }


    public void clear() {
        final int size = getItemCount();
        mObjects.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public T getItem(final int position) {
        return mObjects.get(position);
    }

    public long getItemId(final int position) {
        return position;
    }


    public int getPosition(final T item) {
        return mObjects.indexOf(item);
    }


    public void insert(final T object, int index) {
        mObjects.add(index, object);
        notifyItemInserted(index);

    }


    public void remove(T object) {
        final int position = getPosition(object);
        mObjects.remove(object);
        notifyItemRemoved(position);
    }


    public void sort(Comparator<? super T> comparator) {
        Collections.sort(mObjects, comparator);
        notifyItemRangeChanged(0, getItemCount());
    }

    /**
     * 添加整个链表
     */
    public void addAll(List<T> list) {
        final int size = getItemCount();
        mObjects.addAll(list);
        notifyItemRangeChanged(0, size);
    }

}
