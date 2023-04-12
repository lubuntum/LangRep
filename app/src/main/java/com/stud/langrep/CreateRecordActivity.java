package com.stud.langrep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.repository.RecordRepository;
import com.stud.langrep.databinding.ActivityCreateRecordBinding;
import com.stud.langrep.fragments.RecordContentFragment;

import java.util.List;

public class CreateRecordActivity extends AppCompatActivity {
    public static Handler handler = new Handler(Looper.getMainLooper());
    public static final String RECORD_UPDATE = "update";
    public static final String RECORD_CREATE = "create";
    ActivityCreateRecordBinding binding;
    //Для проверки уникальности имени
    public static RecordRepository repository;
    public MutableLiveData<List<Record>> recordsLive = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = RecordRepository.getInstance(getApplication());
        repository.findAllRecordsAsync(recordsLive);

        Bundle args = getIntent().getExtras();

        if(args != null && args.containsKey(RECORD_UPDATE)){
            //Если запись уже существует и мы ее обновляем
            // то сразу же откроем редактирование ничего лишнего более не делаем
            openRecordContent((Record) args.getSerializable(RECORD_UPDATE));
        }
        else createRecordInit();
    }

    public void createRecordInit(){
        binding.createRecord.setOnClickListener((v)->{
            if(binding.recordNameEditText.getText().toString().matches(" *")) {
                binding.recordNameEditText.setError("Введите имя записи");
                return;
            }
            if(recordsLive.getValue().
                    stream().anyMatch(r->r.getRecordName().equals(binding.recordNameEditText.getText().toString()))){
                binding.recordNameEditText.setError("Запись с таким именем уже есть");
                return;
            }
            if(binding.tags.getText().toString().matches(" *")){
                binding.tags.setError("Введите теги для классификации записи");
                return;
            }
            Record record = new Record(binding.recordNameEditText.getText().toString(),0);
            Runnable saveRecordRnb = () -> {
                record.setId(repository.insertRecord(record));
                recordsLive.getValue().add(record);
                handler.post(()->{
                    //Toast.makeText(CreateRecordActivity.this,String.valueOf(record.getId()), Toast.LENGTH_SHORT).show();
                    openRecordContent(record);
                });
                //openRecordFragment but post cos this Thread
            };
            new Thread(saveRecordRnb).start();

        });
    }

    public void openRecordContent(Record record){
        binding.mainFragment.setVisibility(View.VISIBLE);
        binding.mainInfoContainer.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, RecordContentFragment.getInstance(record),"content")
                .commit();
    }

}