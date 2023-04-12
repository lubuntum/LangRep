package com.stud.langrep.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.stud.langrep.database.entity.Record;

import java.util.List;

@Dao
public interface RecordDao {
    @Query("SELECT * FROM record WHERE record_name == :name")
    Record findRecordByName(String name);
    @Query("SELECT * FROM record WHERE id == :id")
    Record findRecordById(int id);

    @Query("SELECT * FROM record")
    List<Record> findAllRecords();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecord(Record record);
}
