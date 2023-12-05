package com.example.kalender;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kalender.helper.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_EVENT = 1;

    private DatabaseHelper databaseHelper;
    private CalendarView calendarView;
    private ListView eventListView;
    private FloatingActionButton addEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        calendarView = findViewById(R.id.calendarView);
        eventListView = findViewById(R.id.eventListView);
        addEventButton = findViewById(R.id.addEventButton);

        // Set listener untuk CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = formatDate(year, month, dayOfMonth);
            displayEvents(selectedDate);
        });

        // Set listener untuk tombol tambah event
        addEventButton.setOnClickListener(view -> openAddEventActivity());

        // Set awal untuk menampilkan event pada tanggal hari ini
        displayEvents(getTodayDate());
    }

    private void openAddEventActivity() {
        Intent intent = new Intent(this, AddEditEventActivity.class);
        startActivityForResult(intent, REQUEST_ADD_EVENT);
    }

    // Override onActivityResult untuk menangani hasil dari AddEditEventActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_EVENT && resultCode == RESULT_OK) {
            // Refresh tampilan jika event berhasil ditambahkan atau diupdate
            displayEvents(getSelectedDate());
        }
    }

    // Metode untuk mendapatkan semua event pada tanggal tertentu
    private void displayEvents(String date) {
        Cursor cursor = databaseHelper.getEventsByDate(date);

        // Gunakan SimpleCursorAdapter untuk menampilkan data di ListView
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{DatabaseHelper.COLUMN_DESCRIPTION},
                new int[]{android.R.id.text1},
                0);

        eventListView.setAdapter(adapter);

        // Tampilkan pesan jika tidak ada event pada tanggal tertentu
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Tidak ada event pada tanggal ini", Toast.LENGTH_SHORT).show();
        }
    }

    private String getTodayDate() {
        // Mendapatkan tanggal hari ini dalam format "YYYY-MM-DD"
        return formatDate(new Date());
    }

    private String getSelectedDate() {
        // Mendapatkan tanggal yang dipilih pada CalendarView
        long selectedDateMillis = calendarView.getDate();
        return formatDate(new Date(selectedDateMillis));
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        // Format tanggal dari year, month, dayOfMonth menjadi "YYYY-MM-DD"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(year - 1900, month, dayOfMonth));
    }

    private String formatDate(Date date) {
        // Format tanggal dari objek Date menjadi "YYYY-MM-DD"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}