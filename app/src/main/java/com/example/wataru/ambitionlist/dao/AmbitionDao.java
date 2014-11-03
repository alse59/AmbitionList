package com.example.wataru.ambitionlist.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wataru.ambitionlist.model.AmbitionDto;

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
    public List<AmbitionDto> findAllAmb() {
        List<AmbitionDto> list = new ArrayList<AmbitionDto>();
        AmbitionDto dto = null;
        String[] cols = {"amb_id", "amb_title"};
        Cursor cs = db.query(TABLE_NAME, cols, null, null, null, null, cols[0]);

        while(cs.moveToNext()) {
            dto = new AmbitionDto();
            dto.setAmbId(cs.getInt(0));
            dto.setAmbTitle(cs.getString(1));
            list.add(dto);
        }

        return list;
    }

    //目標タイトルから目標を探す
    public AmbitionDto findByAmbTitles(String[] ambTitles) {
        AmbitionDto dto = null;
        String[] cols = {"amb_id", "amb_title"};
        Cursor cs = db.query(TABLE_NAME, cols, "amb_title = ?", ambTitles, null, null, null, null);

        if(cs.moveToFirst()) {
            dto = new AmbitionDto();
            dto.setAmbId(cs.getInt(0));
            dto.setAmbTitle(cs.getString(1));
        }

        return dto;

    }


}
