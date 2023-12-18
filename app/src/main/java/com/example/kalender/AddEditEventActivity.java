package com.example.kalender;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kalender.helper.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditEventActivity extends AppCompatActivity {

    private TextView dateLabel;
    private EditText descriptionEditText;
    private Button saveButton;
    private DatabaseHelper databaseHelper;
    private Calendar selectedDate;

    public static final String EXTRA_DATE = "extra_date";
    public static final String EXTRA_EVENT_ID = "extra_event_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_event);

        databaseHelper = new DatabaseHelper(this);
        dateLabel = findViewById(R.id.dateLabel);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);
        selectedDate = Calendar.getInstance();

        updateDateLabel();

        dateLabel.setOnClickListener(view -> showDatePickerDialog());

        saveButton.setOnClickListener(view -> saveEvent());
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (datePicker, year, month, day) -> {
                    selectedDate.set(year, month, day);
                    updateDateLabel();
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateLabel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        dateLabel.setText(dateFormat.format(selectedDate.getTime()));
    }

    private void saveEvent() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = dateFormat.format(selectedDate.getTime());

        String description = descriptionEditText.getText().toString().trim();

        if (description.isEmpty()) {
            Toast.makeText(this, "Isi semua kolom!", Toast.LENGTH_SHORT).show();
            return;
        }

        long result;
        Intent intent = getIntent();
        String eventId = intent.getStringExtra(EXTRA_EVENT_ID);

        if (eventId != null) {
            result = databaseHelper.updateEvent(Long.parseLong(eventId), date, description);
        } else {
            result = databaseHelper.addEvent(date, description);
        }

        if (result > 0) {
            Toast.makeText(this, "Event berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Gagal menambahkan event", Toast.LENGTH_SHORT).show();
        }
    }
}
