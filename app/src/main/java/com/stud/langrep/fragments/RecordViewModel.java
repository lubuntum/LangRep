package com.stud.langrep.fragments;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.entity.Word;
import com.stud.langrep.database.repository.RecordRepository;

import java.util.List;
//API NLP d84df178f5cd8bb7c3ee2ba082a00feb1ebcc822
public class RecordViewModel extends AndroidViewModel {
    public static final String PLAY_RECORD_ID = "record_play";
    public static final String RECORD_DELAY = "speed";
    public static final String RECORD_RATING = "rating";
    public static final String SETTINGS = "settings";
    public static final int AVERAGE_TTS_SPEED = 13;
    RecordRepository repository;
    SharedPreferences preferences;
    private MutableLiveData<List<Record>> recordLive = new MutableLiveData<>();
    private MutableLiveData<List<Word>> wordLive = new MutableLiveData<>();
    private MutableLiveData<String> status = new MutableLiveData<>();

    public RecordViewModel(@NonNull Application application) {
        super(application);
        repository = RecordRepository.getInstance(application);
        preferences = application.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }
    public void liveDataInit(){
        this.status = new MutableLiveData<>();
        this.recordLive = new MutableLiveData<>();
        this.wordLive = new MutableLiveData<>();
    }

    public void uploadRecords(){
        repository.findAllRecordsAsync(recordLive);
    }
    public void uploadWords(Record record){
        if(record == null && record.getId() == 0){
            status.setValue("Невозможно загрузить запись, попробуйте снова");
        }
        repository.findAllWordsByRecordIdAsync(record.getId(), wordLive);
    }
    public void saveWords(List<Word> words){
        repository.updateAllWordsAsync(words);
        for(Word word : words){
            if (word.getId() == 0) repository.insertWordAsync(word);
            else repository.updateWord(word);
        }
    }
    public void updateRecord(Record record){
        repository.updateRecordAsync(record);
    }
    public int calculateTTSBasicDuration(List<Word> words){
        if(words == null) return 0;
        int durationSec = 0;
        for(Word word : words){
            durationSec += word.getTranslatedWord().length();
        }
        if (durationSec == 0) return 0;
        return durationSec/AVERAGE_TTS_SPEED;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public MutableLiveData<List<Record>> getRecordLive() {
        return recordLive;
    }

    public MutableLiveData<List<Word>> getWordLive() {
        return wordLive;
    }

    public MutableLiveData<String> getStatus() {
        return status;
    }
}
