package com.example.kalender;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kalender.helper.DatabaseHelper;

public class AddEditEventActivity extends AppCompatActivity {

    private EditText dateEditText;
    private EditText descriptionEditText;
    private Button saveButton;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_event);

        dateEditText = findViewById(R.id.dateEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);

        databaseHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent();
            }
        });
    }

    private void saveEvent() {
        String date = dateEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (date.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Isi semua kolom!", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = databaseHelper.addEvent(date, description);

        if (result > 0) {
            Toast.makeText(this, "Event berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Gagal menambahkan event", Toast.LENGTH_SHORT).show();
        }
    }
}
