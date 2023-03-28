package com.stud.langrep.entity;

public class Record {
    private String recordName;
    private int totalWords;
    private int basicDuration;

    public Record(String recordName, int totalWords, int basicDuration) {
        this.recordName = recordName;
        this.totalWords = totalWords;
        this.basicDuration = basicDuration;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public int getBasicDuration() {
        return basicDuration;
    }

    public void setBasicDuration(int basicDuration) {
        this.basicDuration = basicDuration;
    }
}
