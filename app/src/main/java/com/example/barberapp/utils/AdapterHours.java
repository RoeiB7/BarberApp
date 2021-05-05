package com.example.barberapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberapp.R;

import java.util.List;

public class AdapterHours extends RecyclerView.Adapter<AdapterHours.MyViewHolder> {

    private List<String> hours;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // _hours is passed into the constructor
    public AdapterHours(Context context, List<String> _hours) {
        this.mInflater = LayoutInflater.from(context);
        this.hours = _hours;
    }

    public void updateOneItem(int position) {
        notifyItemChanged(position);
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.hours_list, parent, false);
        return new MyViewHolder(view);
    }

    // binds the _hours to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String hour = hours.get(position);
        holder.hours_text.setText(hour);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return hours.size();
    }

    // convenience method for getting _hours at click position
    String getItem(int position) {
        return hours.get(position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView hours_text;

        MyViewHolder(View itemView) {
            super(itemView);
            hours_text = itemView.findViewById(R.id.hours_text);
            itemView.setOnClickListener(v -> {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
