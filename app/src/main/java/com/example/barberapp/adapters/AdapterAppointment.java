package com.example.barberapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberapp.R;
import com.example.barberapp.objects.Appointment;

import java.util.List;

public class AdapterAppointment extends RecyclerView.Adapter<AdapterAppointment.MyViewHolder> {

    private List<Appointment> appointments;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // _hours is passed into the constructor
    public AdapterAppointment(Context context, List<Appointment> appointments) {
        this.mInflater = LayoutInflater.from(context);
        this.appointments = appointments;
    }

    public void updateOneItem(int position) {
        notifyItemChanged(position);
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.appointments_list, parent, false);
        return new MyViewHolder(view);
    }

    // binds the _hours to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.appointment_date.setText(appointment.getDate());
        holder.appointment_hour.setText(appointment.getHour());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return appointments.size();
    }

    // convenience method for getting _hours at click position
    Appointment getItem(int position) {
        return appointments.get(position);
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

        TextView appointment_date;
        TextView appointment_hour;

        MyViewHolder(View itemView) {
            super(itemView);
            appointment_date = itemView.findViewById(R.id.appointments_date);
            appointment_hour = itemView.findViewById(R.id.appointments_hour);
            itemView.setOnClickListener(v -> {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
