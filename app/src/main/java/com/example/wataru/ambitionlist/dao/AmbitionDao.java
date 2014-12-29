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
    private static final String COL_ID = "amb_id";
    private static final String COL_TITLE = "amb_title";
    private static final String[] COLS = {COL_ID, COL_TITLE};

    public AmbitionDao(SQLiteDatabase db) {
        this.db = db;
    }

    //目標タイトルをリストに表示する
    public List<AmbitionDto> findAll() {
        List<AmbitionDto> list = new ArrayList<AmbitionDto>();
        Cursor cs = db.query(TABLE_NAME, COLS, null, null, null, null, COL_ID);

        while(cs.moveToNext()) {
            AmbitionDto dto = new AmbitionDto();
            dto.setAmbId(cs.getInt(0));
            dto.setAmbTitle(cs.getString(1));
            list.add(dto);
        }

        return list;
    }

    //目標タイトルから目標を探す
    public AmbitionDto findByAmbTitles(String[] ambTitles) {
        Cursor cs = db.query(TABLE_NAME, COLS, "amb_title = ?", ambTitles, null, null, null, null);

        AmbitionDto dto = null;
        if(cs.moveToNext()) {
            dto = new AmbitionDto();
            dto.setAmbId(cs.getInt(0));
            dto.setAmbTitle(cs.getString(1));
        }


        return dto;
    }

    //目標タイトルをデータベースに追加する
    public long insert(String ambTitle){
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, ambTitle);
        return db.insert(TABLE_NAME, null, cv);

    }

    //目標タイトルを削除する
    public int deleteToAmbTitles(String[] ambTitles) {
        return db.delete(TABLE_NAME, "amb_title = ?", ambTitles);
    }






}
