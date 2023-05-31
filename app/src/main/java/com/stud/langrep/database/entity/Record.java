package com.stud.langrep.database.entity;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.stud.langrep.fragments.RecordViewModel;

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
    @ColumnInfo(name = "word_count")
    private int wordCount;
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
            stringBuilder.append(word.getTranslatedWord()).append(", ");
        }
        return stringBuilder.toString();
    }
    public String composePhraseForPlayingWithPrefs(SharedPreferences preferences){
        if (words == null) return null;
        int pause_time = preferences.getInt(RecordViewModel.RECORD_DELAY, 500);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<speak>");
        for (Word word: words){
            stringBuilder.append(word.getTranslatedWord())
                    .append(String.format("<break time=\"%dms\"/>",pause_time));
        }
        stringBuilder.append("</speak>");
    return stringBuilder.toString();
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public int getWordCount() {
        return wordCount;
    }
    public void setWordCount(int wordCount){
        this.wordCount = wordCount;
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
