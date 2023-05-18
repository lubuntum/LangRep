package com.stud.langrep.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.stud.langrep.R;
import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.repository.RecordRepository;
import com.stud.langrep.databinding.RemoveRecordDialogBinding;
import com.stud.langrep.fragments.RecordViewModel;

import java.sql.Array;
import java.util.Arrays;

public class RecordSettingsDialog extends DialogFragment {
    public Record record;
    public RecordRepository repository;
    public RecordViewModel recordViewModel;

    RemoveRecordDialogBinding binding;
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
        binding = RemoveRecordDialogBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        MutableLiveData<String> status = new MutableLiveData<>();
        Observer<String> statusObserver = (statusStr) ->{
            Toast.makeText(getContext(), statusStr, Toast.LENGTH_SHORT).show();
            recordViewModel.uploadRecords();
            this.dismiss();
        };
        status.observe(getViewLifecycleOwner(), statusObserver);

        binding.deleteBtn.setOnClickListener((v)-> {
            repository.deleteRecordAsync(record, status);
        });
        binding.deleteWordsBtn.setOnClickListener((view1 -> {
            repository.deleteWordsByRecordIdAsync(record, status);
        }));
        speechSettingsInit(view);
    }

    public void speechSettingsInit(View view){
        int speedMS = recordViewModel.getPreferences().getInt(RecordViewModel.RECORD_DELAY, 500);
        binding.pauseSpeech.setText(String.valueOf(speedMS));

        String rating = String.valueOf(recordViewModel.getPreferences().getFloat(RecordViewModel.RECORD_RATING,1));
        int index = Arrays.asList(getContext().getResources().getStringArray(R.array.speech_pause)).indexOf(rating);
        if(index == -1) index = 0;
        binding.ratingSpeech.setSelection(index);

        //speechSpeed.setFilters(new InputFilter[]{filter});
    //speechSpeed.set
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (binding.pauseSpeech.getText() == null || !binding.pauseSpeech.getText().toString().matches("\\d+")) return;
        int speechSpeedData = Integer.parseInt(binding.pauseSpeech.getText().toString());

        recordViewModel.getPreferences().edit()
                .putInt(RecordViewModel.RECORD_DELAY, speechSpeedData)
                .apply();
        float recordRating = Float.parseFloat(binding.ratingSpeech.getSelectedItem().toString());
        recordViewModel.getPreferences().edit()
                .putFloat(RecordViewModel.RECORD_RATING, recordRating)
                .apply();
    }
}
