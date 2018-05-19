package com.example.adaml.todolist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by adaml on 17/05/2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void OnItemClick(RecyclerView.ViewHolder holder,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ItemAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public TextView itemName;
        public ImageView checkMark;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_title);
            checkMark = itemView.findViewById(R.id.checkMark);

        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item,parent,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.DB_COLUMN));
        int isDone = mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry.DB_ISDONE));
        long id = mCursor.getLong(mCursor.getColumnIndex(ItemContract.ItemEntry._ID));
        holder.itemName.setText(name);
        holder.itemView.setTag(isDone);
        holder.itemView.setTag(id);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mListener != null){

                    if(position != RecyclerView.NO_POSITION){
                        mListener.OnItemClick(holder, position);
                    }
                }
            }
        });
        if(isDone == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.checkMark.setVisibility(View.GONE);
            //Change Background color to grey and ic_check_green
        }
        if(isDone == 1) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            holder.checkMark.setVisibility(View.VISIBLE);
            //Change Background color to grey and ic_check_green
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor != null){
            mCursor.close();
        }

        mCursor = newCursor;

        if(newCursor != null){
            notifyDataSetChanged();
        }
    }
}
