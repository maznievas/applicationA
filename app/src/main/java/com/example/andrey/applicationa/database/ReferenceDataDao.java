package com.example.andrey.applicationa.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ReferenceDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ReferenceData referenceData);

    @Delete
    void delete(ReferenceData referenceData);

    @Query("SELECT * FROM reference_data WHERE url=:url LIMIT 1")
    ReferenceData getReference(String url);

    @Query("SELECT * FROM reference_data")
    List<ReferenceData> getAllReferenes();
}
