package com.stronck.appmovil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "appmovil.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "email TEXT NOT NULL COLLATE NOCASE UNIQUE, " +
                "password_hash TEXT NOT NULL, " +
                "password_salt TEXT NOT NULL, " +
                "created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Agregar migraciones aquí al cambiar la versión del esquema.
    }

    public boolean registerUser(String name, String email, String password) {
        String salt = PasswordHasher.newSalt();
        String hash = PasswordHasher.hash(password, salt);
        ContentValues values = new ContentValues();
        values.put("name", name.trim());
        values.put("email", email.trim().toLowerCase(Locale.ROOT));
        values.put("password_hash", hash);
        values.put("password_salt", salt);
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.insertOrThrow("users", null, values) != -1;
        } catch (SQLException exception) {
            return false;
        } finally {
            db.close();
        }
    }

    public User authenticate(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("users",
                    new String[]{"id", "name", "email", "password_hash", "password_salt"},
                    "email = ?", new String[]{email.trim().toLowerCase(Locale.ROOT)},
                    null, null, null);
            if (!cursor.moveToFirst()) return null;
            String salt = cursor.getString(cursor.getColumnIndexOrThrow("password_salt"));
            String expected = cursor.getString(cursor.getColumnIndexOrThrow("password_hash"));
            if (!PasswordHasher.verify(password, salt, expected)) return null;
            return new User(cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")));
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public static final class User {
        public final long id;
        public final String name;
        public final String email;
        public User(long id, String name, String email) {
            this.id = id; this.name = name; this.email = email;
        }
    }
}
