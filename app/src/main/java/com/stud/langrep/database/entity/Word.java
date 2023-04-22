package com.stud.langrep.database.entity;

import android.support.v4.media.MediaMetadataCompat;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "word",
        foreignKeys = {@ForeignKey(entity = Record.class, parentColumns = "id",childColumns = "record_id",onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = {"record_id"})})
public class Word implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "record_id")
    private long recordId;
    @ColumnInfo(name = "native_word")
    private String nativeWord;
    @ColumnInfo(name = "translated_word")
    private String translatedWord;
    @Nullable
    @ColumnInfo(name = "transcription")
    private String transcription;//на будущее
    public Word(){}

    public Word(String nativeWord, String translatedWord, long recordId) {
        this.nativeWord = nativeWord;
        this.translatedWord = translatedWord;
        this.recordId = recordId;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getNativeWord() {
        return nativeWord;
    }

    public void setNativeWord(String nativeWord) {
        this.nativeWord = nativeWord;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }
}
