package com.xiaoshu.memorandum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoshu.memorandum.R;
import com.xiaoshu.memorandum.model.NoteModel;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private LayoutInflater mInflater;
    private ItemOnLongClickListener itemListener;
    private ItemOnClickListener itemOnClickListener;
    private List<NoteModel> noteModels;


    public RecyclerAdapter(Context context, List<NoteModel> list) {
        noteModels = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return noteModels.size();
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final NoteModel notemodel = noteModels.get(position);
        if (notemodel.getActive().equals("true")) {
            holder.tvTime.setText(notemodel.getDate() + "  " + notemodel.getTime());
        } else {
            holder.tvTime.setText("");
        }

        holder.tvNote.setText(notemodel.getTitle());
        holder.tvNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //回调
                itemListener.itemLongClick(notemodel);
                return false;
            }
        });

        holder.tvNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClickListener.itemOnClick(notemodel);

            }
        });


        if (notemodel.getTop() == 1) {
            holder.imgTop.setVisibility(View.VISIBLE);
        } else {
            holder.imgTop.setVisibility(View.GONE);
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(mInflater.inflate(R.layout.items_layout, parent, false));
    }

    public void setItemListener(ItemOnLongClickListener listener) {
        itemListener = listener;
    }

    public void setOnclickListener(ItemOnClickListener listener) {
        itemOnClickListener = listener;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView tvNote;
        TextView tvTime;
        ImageView imgTop;
        View mItemView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            tvNote = (TextView) itemView.findViewById(R.id.note_text);
            tvTime = (TextView) itemView.findViewById(R.id.time_text);
            imgTop = (ImageView) itemView.findViewById(R.id.img_top);
        }
    }

    public interface ItemOnLongClickListener {
        void itemLongClick(NoteModel session);
    }

    public interface ItemOnClickListener {
        void itemOnClick(NoteModel session);

    }
}
