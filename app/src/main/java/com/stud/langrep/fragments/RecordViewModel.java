package com.stud.langrep.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.entity.Word;
import com.stud.langrep.database.repository.RecordRepository;

import java.util.List;
//API NLP d84df178f5cd8bb7c3ee2ba082a00feb1ebcc822
public class RecordViewModel extends AndroidViewModel {
    RecordRepository repository;
    private MutableLiveData<List<Record>> recordLive = new MutableLiveData<>();
    private MutableLiveData<List<Word>> wordLive = new MutableLiveData<>();
    private MutableLiveData<String> status = new MutableLiveData<>();

    public RecordViewModel(@NonNull Application application) {
        super(application);
        repository = RecordRepository.getInstance(application);
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
    }
    public String translateWord(Word word){

        return null;
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
