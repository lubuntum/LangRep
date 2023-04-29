package com.stud.langrep.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;
@Entity(tableName = "record",
        indices = {@Index(value = {"record_name"},unique = true)})
public class Record implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "record_name")
    @NonNull
    private String recordName;
    @ColumnInfo(name = "basic_duration")
    private int basicDuration;
    @Ignore
    private List<Word> words;

    public Record(){}
    @Ignore
    public Record(String recordName, int basicDuration) {
        this.recordName = recordName;
        this.basicDuration = basicDuration;
    }
    @Ignore
    public Record(String recordName,int basicDuration, List<Word> words){
        this.recordName = recordName;
        this.basicDuration = basicDuration;
        this.words = words;
    }
    public String composePhraseForPlaying(){
        if (words == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        for (Word word: words) {
            stringBuilder.append(word.getTranslatedWord()).append(" ");
        }
        return stringBuilder.toString();
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public int getTotalWords() {
        if(words == null) return 0;
        return words.size();
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public List<Word> getWords() {
        return words;
    }

    public int getBasicDuration() {
        return basicDuration;
    }

    public void setBasicDuration(int basicDuration) {
        this.basicDuration = basicDuration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
