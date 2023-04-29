package com.stud.langrep.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.stud.langrep.R;
import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.entity.Word;
import com.stud.langrep.database.repository.RecordRepository;
import com.stud.langrep.fragments.RecordViewModel;

import java.util.Objects;

public class RecordSettingsDialog extends DialogFragment {
    public Record record;
    public RecordRepository repository;
    public RecordViewModel recordViewModel;
    public static RecordSettingsDialog getInstance(Record record){
        Bundle args = new Bundle();
        args.putSerializable("Record", record);
        RecordSettingsDialog recordSettingsDialog = new RecordSettingsDialog();
        recordSettingsDialog.setArguments(args);
        return recordSettingsDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().containsKey("Record"))
            this.record = (Record) getArguments().getSerializable("Record");
        repository = RecordRepository.getInstance(requireActivity().getApplication());
        recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        return inflater.inflate(R.layout.remove_record_dialog,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button deleteBtn = view.findViewById(R.id.delete_btn);
        Button deleteItemsBtn = view.findViewById(R.id.delete_words_btn);

        MutableLiveData<String> status = new MutableLiveData<>();
        Observer<String> statusObserver = (statusStr) ->{
            Toast.makeText(getContext(), statusStr, Toast.LENGTH_SHORT).show();
            recordViewModel.uploadRecords();
            this.dismiss();
        };
        status.observe(getViewLifecycleOwner(), statusObserver);

        deleteBtn.setOnClickListener((v)-> {
            repository.deleteRecordAsync(record, status);
        });
        deleteItemsBtn.setOnClickListener((view1 -> {
            repository.deleteWordsByRecordIdAsync(record, status);
        }));
        speechSpeedInitEditText(view);
    }

    public void speechSpeedInitEditText(View view){
        EditText speechSpeed = view.findViewById(R.id.speech_speed);
        InputFilter filter = new InputFilter() {
            int min = 0;
            int max = 100;
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                try{
                    int input = Integer.parseInt(spanned.toString() + charSequence.toString());
                    if (input >= 0 && input <= 100){
                        return null;
                    }
                }catch (NumberFormatException e){}
                return "";
            }
        };
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().contains("%")) {
                    String text = charSequence+"%";
                    speechSpeed.setText(text);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        speechSpeed.setFilters(new InputFilter[]{filter});
    //speechSpeed.set
    }
}
