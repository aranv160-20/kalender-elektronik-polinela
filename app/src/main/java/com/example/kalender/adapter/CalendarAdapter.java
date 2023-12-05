package com.example.kalender.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kalender.R;
import com.example.kalender.holder.CalendarViewHolder;
import com.example.kalender.helper.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private final Context context;
    private final DatabaseHelper dbHelper;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, Context context) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, (CalendarViewHolder.OnItemListener) onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayText = daysOfMonth.get(position);
        if (!dayText.equals("")) {
            Calendar selectedDate = Calendar.getInstance();
            String formattedDate = getFormattedDate(selectedDate, dayText);

            // Use dbHelper to check if there is an event on the selected date
            if (dbHelper.eventExists(formattedDate)) {
                holder.itemView.setBackgroundResource(R.drawable.event_background);
                // Jika Anda ingin memberikan informasi lebih lanjut, tambahkan di sini
            } else {
                // Atau atur latar belakang ke null jika tidak ada acara
                holder.itemView.setBackground(null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    private String getFormattedDate(Calendar selectedDate, String dayText) {
        selectedDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayText));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(selectedDate.getTime());
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }
}
