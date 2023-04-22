package com.stud.langrep.database.repository;

import android.app.Application;
import android.bluetooth.le.AdvertiseData;

import androidx.lifecycle.MutableLiveData;

import com.stud.langrep.R;
import com.stud.langrep.database.Database;
import com.stud.langrep.database.dao.RecordDao;
import com.stud.langrep.database.dao.WordDao;
import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.entity.Word;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecordRepository {
    private Database database;
    private Executor threadIOExecutor;

    private RecordDao recordDao;
    private WordDao wordDao;
    public static volatile RecordRepository INSTANCE = null;
    public static RecordRepository getInstance(Application app){
        if(INSTANCE == null){
            synchronized (RecordRepository.class){
                if(INSTANCE == null)
                    INSTANCE = new RecordRepository(app);
            }
        }
        return INSTANCE;
    }
    public RecordRepository(Application app){
        threadIOExecutor = Executors.newCachedThreadPool();
        database = Database.getInstance(app);
        recordDao = database.recordDao();
        wordDao = database.wordDao();
    }
    /*Запросы к базе*/
    public void findAllRecordsAsync(MutableLiveData<List<Record>> mutableRecords){
        Runnable recordsRnb = () -> mutableRecords.postValue(recordDao.findAllRecords());
        threadIOExecutor.execute(recordsRnb);
    }
    public List<Record> findAllRecords(){
        return recordDao.findAllRecords();
    }

    public long insertRecord(Record record){
        return recordDao.insertRecord(record);
    }

    public void insertRecordAsync(Record record, MutableLiveData<String> status){
        Runnable insertRecordAsyncRnb = () -> {
            recordDao.insertRecord(record);
            status.postValue("Запись сохранена успешно");
        };
        threadIOExecutor.execute(insertRecordAsyncRnb);
    }
    //Words
    public void findAllWordsByRecordIdAsync(long id, MutableLiveData<List<Word>> wordsLive){
        Runnable fundAllWordsRnb = ()->{
            wordsLive.postValue(wordDao.findWordsByRecordId(id));
        };
        threadIOExecutor.execute(fundAllWordsRnb);
    }
    public void deleteRecordAsync(Record record, MutableLiveData<String> status){
        Runnable deleteRecordRnb = ()-> {
            recordDao.deleteRecord(record);
            status.postValue("Запись успешно удалена");
        };
        threadIOExecutor.execute(deleteRecordRnb);
    }
    public void insertWordAsync(Word word){
        Runnable insertRnb = ()-> wordDao.insert(word);
        threadIOExecutor.execute(insertRnb);
    }
    public void insertAllWordsAsync(List<Word> words){
        Runnable insertRnb = ()-> wordDao.insertAll(words);
        threadIOExecutor.execute(insertRnb);
    }
    public void updateAllWordsAsync(List<Word> words){
        Runnable updateRnb = ()-> wordDao.updateAll(words);
        threadIOExecutor.execute(updateRnb);
    }
    public void updateWord(Word word){
        Runnable updateRnb = ()-> wordDao.update(word);
        threadIOExecutor.execute(updateRnb);
    }
    public void deleteWordsByRecordIdAsync(Record record, MutableLiveData<String> status){
        Runnable deleteWordsRnb = ()->{
            wordDao.deleteWordsByRecordId(record.getId());
            status.postValue("Слова удалены");
        };
        threadIOExecutor.execute(deleteWordsRnb);
    }
}
