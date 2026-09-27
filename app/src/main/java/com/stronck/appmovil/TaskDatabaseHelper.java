package com.stronck.appmovil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/** Capa de persistencia SQLite local. No requiere conexión ni permisos de Internet. */
public final class TaskDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "appmovil.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TASKS = "tasks";
    private static final String COL_ID = "_id";
    private static final String COL_TITLE = "title";
    private static final String COL_COMPLETED = "completed";

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TASKS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT NOT NULL, "
                + COL_COMPLETED + " INTEGER NOT NULL DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Al agregar futuras versiones, implementar migraciones sin perder datos.
    }

    public long insertTask(Task task) {
        if (task == null || !TaskValidator.isValidTitle(task.getTitle())) {
            return -1L;
        }
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, task.getTitle().trim());
        values.put(COL_COMPLETED, task.isCompleted() ? 1 : 0);
        return getWritableDatabase().insert(TABLE_TASKS, null, values);
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query(
                TABLE_TASKS,
                new String[]{COL_ID, COL_TITLE, COL_COMPLETED},
                null, null, null, null, COL_ID + " DESC")) {
            while (cursor.moveToNext()) {
                tasks.add(new Task(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMPLETED)) == 1));
            }
        }
        return tasks;
    }

    public boolean updateTask(Task task) {
        if (task == null || task.getId() < 0) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, task.getTitle());
        values.put(COL_COMPLETED, task.isCompleted() ? 1 : 0);
        return getWritableDatabase().update(
                TABLE_TASKS, values, COL_ID + " = ?",
                new String[]{String.valueOf(task.getId())}) == 1;
    }

    public boolean deleteTask(long id) {
        return getWritableDatabase().delete(
                TABLE_TASKS, COL_ID + " = ?",
                new String[]{String.valueOf(id)}) == 1;
    }
}
