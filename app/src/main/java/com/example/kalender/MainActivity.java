package com.example.kalender;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kalender.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private CalendarView calendarView;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        calendarView = findViewById(R.id.calendarView);
        eventListView = findViewById(R.id.eventListView);

        // Set listener untuk CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            displayEvents(selectedDate);
        });

        // Set awal untuk menampilkan event pada tanggal hari ini
        displayEvents(getTodayDate());
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
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER); // Perlu menambahkan flag ini

        eventListView.setAdapter(adapter);

        // Tampilkan pesan jika tidak ada event pada tanggal tertentu
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Tidak ada event pada tanggal ini", Toast.LENGTH_SHORT).show();
        }
    }

    private String getTodayDate() {
        // Mendapatkan tanggal hari ini dalam format "YYYY-MM-DD"
        // Misal: 2023-12-05
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new java.util.Date());
    }
}
