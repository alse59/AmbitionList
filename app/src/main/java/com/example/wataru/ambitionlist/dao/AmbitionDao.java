package com.example.wataru.ambitionlist.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wataru on 2014/11/03.
 */

public class AmbitionDao {
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "t_amb";

    public AmbitionDao(SQLiteDatabase db) {
        this.db = db;
    }

    //目標タイトルをデータベースに追加する
    public long insertToAmbTitle(String ambTitle){
        ContentValues cv = new ContentValues();
        cv.put("amb_title", ambTitle);
        return db.insert(TABLE_NAME, null, cv);
    }

    //目標タイトルを削除する
    public int deleteToAmbTitles(String[] ambTitles) {
        return db.delete(TABLE_NAME, "amb_title = ?", ambTitles);
    }

    //目標タイトルをリストに表示する
    public List<String> findAllAmb() {
        List<String> list = new ArrayList<String>();
        String[] cols = {"amb_id", "amb_title"};
        Cursor cs = db.query(TABLE_NAME, cols, null, null, null, null, cols[0]);

        while(cs.moveToNext()) {
            list.add(cs.getString(1));
        }

        return list;
    }

    //目標タイトルから目標を探す
    public String findByAmbTitles(String[] ambTitles) {
        String resultAmbTitle = null;
        String[] cols = {"amb_id", "amb_title"};
        Cursor cs = db.query(TABLE_NAME, cols, "amb_title = ?", ambTitles, null, null, null, null);

        if(cs.moveToFirst()) {
            resultAmbTitle = cs.getString(1);
        }

        return resultAmbTitle;

    }
}
