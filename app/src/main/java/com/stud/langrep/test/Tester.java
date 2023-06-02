package com.stud.langrep.test;

import com.stud.langrep.R;
import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.entity.Word;

import java.util.LinkedList;
import java.util.List;

public class Tester {
    public static Record generateTestRecord(){
        Record record = new Record("Test", 0, generateWords());
        return record;
    }
    public static List<Word> generateWords(){
        List<Word> wordList = new LinkedList<>();
        for( int i = 0; i < 25; i++)
            wordList.add(new Word("native"+i,"translate"+i,0));
        return wordList;
    }
}
