package com.example.kalender.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kalender.R;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView dayTextView;
    public OnItemListener onItemListener;

    public CalendarViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
        super(itemView);
        dayTextView = itemView.findViewById(R.id.dayTextView); // Gantilah dengan ID yang sesuai
        this.onItemListener = onItemListener;

        // Setel listener klik untuk item kalender
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Panggil metode onItemClick pada objek OnItemListener
        onItemListener.onItemClick(getAdapterPosition(), dayTextView.getText().toString());
    }

    // Antarmuka untuk menangani klik pada item kalender
    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }
}
