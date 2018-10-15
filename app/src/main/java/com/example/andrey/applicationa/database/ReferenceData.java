package com.example.andrey.applicationa.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;


@Entity(tableName = "reference_data")
public class ReferenceData implements Comparable<ReferenceData>{

    @NonNull
    @PrimaryKey
    String url;
    int status;
    Date lastOpened;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getLastOpened() {
        return lastOpened;
    }

    public void setLastOpened(Date lastOpened) {
        this.lastOpened = lastOpened;
    }

    @Override
    public int compareTo(@NonNull ReferenceData o) {
        return this.status - o.getStatus();
    }
}
