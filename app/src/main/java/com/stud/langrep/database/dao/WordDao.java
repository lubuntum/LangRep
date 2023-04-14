package com.stud.langrep.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.stud.langrep.database.entity.Word;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM word WHERE record_id == :id")
    List<Word> findWordsByRecordId(long id);
    @Query("SELECT * FROM word")
    List<Word> findAllWords();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Word word);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Word> words);
    @Update
    void updateAll(List<Word> words);
    @Update
    void update(Word word);
    @Delete()
    void delete(Word word);
}
