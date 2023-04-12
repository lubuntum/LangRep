package com.stud.langrep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;

import com.stud.langrep.database.repository.RecordRepository;
import com.stud.langrep.fragments.RecordsFragment;

public class MainActivity extends AppCompatActivity {

    RecordRepository repository;
    MutableLiveData<String> status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_fragment, RecordsFragment.getInstance(),"records")
                .commit();
    }
}