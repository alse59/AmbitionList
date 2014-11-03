package com.example.wataru.ambitionlist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wataru on 2014/11/03.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ambition_list.sqlite";
    private static final int VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=true;");
        db.execSQL("DROP TABLE IF EXISTS t_amb");
        db.execSQL("CREATE TABLE t_amb (amb_id INTEGER PRIMARY KEY AUTOINCREMENT, amb_title TEXT, cleared TEXT)");
        db.execSQL("CREATE TABLE t_benefit (benefit_id INTEGER PRIMARY KEY AUTOINCREMENT, amb_id INTEGER, benefit_title TEXT, FOREIGN KEY(amb_id) REFERENCES t_amb(amb_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_v, int new_v) {
        db.execSQL("DROP TABLE IF EXISTS t_amb");
        onCreate(db);
    }
}
