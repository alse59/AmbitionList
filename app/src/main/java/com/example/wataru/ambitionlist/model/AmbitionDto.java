package com.example.wataru.ambitionlist.model;

import android.database.Cursor;

/**
 * Created by wataru on 2014/11/03.
 */
public class AmbitionDto {
    private int ambId;
    private String ambTitle;

    public int getAmbId() {
        return ambId;
    }

    public void setAmbId(int ambId) {
        this.ambId = ambId;
    }

    public String getAmbTitle() {
        return ambTitle;
    }

    public void setAmbTitle(String ambTitle) {
        this.ambTitle = ambTitle;
    }

    public AmbitionDto getDtoByCursor(Cursor cs) {
        AmbitionDto dto = new AmbitionDto();
        dto.setAmbId(Integer.parseInt(cs.getString(0)));
        dto.setAmbTitle(cs.getString(1));
        return dto;
    }

    @Override
    public String toString() {
        return getAmbTitle();
    }
}
