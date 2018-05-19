package com.example.adaml.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

/**
 * Created by adaml on 17/05/2018.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="ToDoList";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String query = "CREATE TABLE "+
                ItemContract.ItemEntry.DB_TABLE +" (" +
                ItemContract.ItemEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemContract.ItemEntry.DB_COLUMN + " TEXT NOT NULL, " +
                ItemContract.ItemEntry.DB_ISDONE +" ISDONE INT NOT NULL DEFAULT 0 " +
        ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS" + ItemContract.ItemEntry.DB_TABLE;
        db.execSQL(query);
        onCreate(db);
    }
}