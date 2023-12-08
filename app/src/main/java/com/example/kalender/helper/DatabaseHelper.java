package com.example.kalender.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "calendar_db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DESCRIPTION = "description";

    private static final String CREATE_EVENTS_TABLE =
            "CREATE TABLE " + TABLE_EVENTS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    // Metode untuk menambahkan event
    public long addEvent(String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_DESCRIPTION, description);

        long newRowId = db.insert(TABLE_EVENTS, null, values);
        db.close();

        return newRowId;
    }

    // Metode untuk membaca semua event
    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_EVENTS, null, null, null, null, null, null);
    }

    // Metode untuk mendapatkan semua event pada tanggal tertentu
    public Cursor getEventsByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID,
                COLUMN_DATE,
                COLUMN_DESCRIPTION
        };
        String selection = COLUMN_DATE + " = ?";
        String[] selectionArgs = {date};

        return db.query(
                TABLE_EVENTS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    // Metode untuk mengupdate event
    public int updateEvent(long eventId, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_DESCRIPTION, description);

        return db.update(TABLE_EVENTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(eventId)});
    }

    // Metode untuk menghapus event
    public int deleteEvent(long eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EVENTS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(eventId)});
    }

    // Metode untuk memeriksa apakah ada event pada tanggal tertentu
    public boolean eventExists(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, null, COLUMN_DATE + " = ?", new String[]{date}, null, null, null);
        boolean eventExists = cursor.getCount() > 0;
        cursor.close();
        return eventExists;
    }
}
