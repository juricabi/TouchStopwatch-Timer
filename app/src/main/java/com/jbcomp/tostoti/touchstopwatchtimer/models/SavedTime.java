package com.jbcomp.tostoti.touchstopwatchtimer.models;

import com.jbcomp.tostoti.touchstopwatchtimer.database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

@Table(database = MyDatabase.class)
public class SavedTime extends BaseModel implements Serializable {

    @Column
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    Long time;


    public SavedTime() {
    }

    public SavedTime(Long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
