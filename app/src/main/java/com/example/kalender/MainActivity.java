package com.example.kalender;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_EDIT_EVENT = 1;

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

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = formatDate(year, month, dayOfMonth);
            displayEvents(selectedDate);
        });

        addEventButton.setOnClickListener(view -> openAddEditEventActivity(null));

        displayEvents(getTodayDate());
    }

    private void openAddEditEventActivity(String eventId) {
        Intent intent = new Intent(this, AddEditEventActivity.class);
        intent.putExtra(AddEditEventActivity.EXTRA_DATE, getSelectedDate());
        if (eventId != null) {
            intent.putExtra(AddEditEventActivity.EXTRA_EVENT_ID, eventId);
        }
        startActivityForResult(intent, REQUEST_ADD_EDIT_EVENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_EDIT_EVENT && resultCode == RESULT_OK) {
            displayEvents(getSelectedDate());
        }
    }

    private void displayEvents(String date) {
        Cursor cursor = databaseHelper.getEventsByDate(date);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{DatabaseHelper.COLUMN_DESCRIPTION},
                new int[]{android.R.id.text1},
                0);

        eventListView.setAdapter(adapter);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Tidak ada event pada tanggal ini", Toast.LENGTH_SHORT).show();
        }
    }

    private String getTodayDate() {
        return formatDate(new Date());
    }

    private String getSelectedDate() {
        long selectedDateMillis = calendarView.getDate();
        return formatDate(new Date(selectedDateMillis));
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(year - 1900, month, dayOfMonth));
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
