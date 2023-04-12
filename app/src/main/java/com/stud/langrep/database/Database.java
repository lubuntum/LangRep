package com.stud.langrep.database;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.stud.langrep.database.dao.RecordDao;
import com.stud.langrep.database.dao.WordDao;
import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.entity.Word;

@androidx.room.Database(entities = {Record.class, Word.class}, version = 1)
public abstract class Database extends RoomDatabase {
    private static final String DATABASE_NAME = "RecordsBase";
    public static Database INSTANCE = null;

    public abstract WordDao wordDao();
    public abstract RecordDao recordDao();

    public static Database getInstance(Context context){
        if (INSTANCE == null){
            synchronized (Database.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, Database.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
