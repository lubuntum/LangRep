package com.stud.langrep.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.stud.langrep.CreateRecordActivity;
import com.stud.langrep.adapters.RecordsAdapter;
import com.stud.langrep.databinding.AllRecordsFragmentBinding;
import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.entity.Word;
import com.stud.langrep.test.Tester;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RecordsFragment extends Fragment implements
        RecordsAdapter.RecordViewHolder.OnRecordItemClickListener,
        RecordsAdapter.RecordViewHolder.PlayRecordClickListener{
    public AllRecordsFragmentBinding binding;
    private RecordViewModel viewModel;
    private RecordsAdapter adapter;
    private TextToSpeech tts;
    public static RecordsFragment getInstance(){
        Bundle args = new Bundle();
        RecordsFragment fragment = new RecordsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        binding = AllRecordsFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //RecordsAdapter recordsAdapter = new RecordsAdapter(getContext(),generateTestRecords());
        //binding.recordsList.setAdapter(recordsAdapter);
        uploadRecordInit();
        addRecordBtnInit();

    }
    public void uploadRecordInit(){
        Observer<List<Record>> recordsObserver = records -> {
            if(adapter == null) {
                //records.add(Tester.generateTestRecord());//TEST
                adapter = new RecordsAdapter(getContext(), records,this.getParentFragmentManager() , this, this);
            }
            else adapter.setRecordList(records);
            binding.recordsList.setAdapter(adapter);

            binding.uploadBar.setVisibility(View.GONE);
        };
        viewModel.getRecordLive().observe(getViewLifecycleOwner(), recordsObserver);
        //viewModel.uploadRecords();
    }
    public void addRecordBtnInit(){
        binding.addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateRecordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.uploadRecords();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void click(int position) {
        //Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), CreateRecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(CreateRecordActivity.RECORD_UPDATE, adapter.getItemAtPosition(position));
        startActivity(intent);
    }
    @Override
    public void stopSpeech(){
        if(tts != null && tts.isSpeaking())
            tts.stop();
    }
    @Override
    public void playSpeech(int position, RecordsAdapter.RecordViewHolder recordViewHolder) {
        binding.uploadBar.setVisibility(View.VISIBLE);
        tts = new TextToSpeech(getContext(), status -> {
            if(status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.ENGLISH);
                Record currentRecord = adapter.getItemAtPosition(position);
                //Вместо этого читать текст из выбранной по position Record
                Observer<List<Word>> findWordsObserver = new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        currentRecord.setWords(words);//Кэшируем
                        tts.setSpeechRate(viewModel.getPreferences().getFloat(RecordViewModel.RECORD_RATING,1));
                        Bundle params = new Bundle();
                        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"");
                        tts.speak(currentRecord.composePhraseForPlayingWithPrefs(viewModel.getPreferences()),
                                TextToSpeech.QUEUE_FLUSH,
                                params, RecordViewModel.PLAY_RECORD_ID);
                        binding.uploadBar.setVisibility(View.INVISIBLE);
                    }
                };
                //tts.speak("Hello, world", TextToSpeech.QUEUE_FLUSH, new Bundle(), null);
                viewModel.getWordLive().observe(getViewLifecycleOwner(), findWordsObserver);
            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                recordViewHolder.itemView.post(()->Toast.makeText(getContext(), "Start", Toast.LENGTH_SHORT).show());

            }

            @Override
            public void onDone(String s) {
                recordViewHolder.switchPlayState();
            }

            @Override
            public void onError(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
        if (adapter.getItemAtPosition(position).getWords() == null)
            viewModel.uploadWords(adapter.getItemAtPosition(position));
        else viewModel.getWordLive().setValue(adapter.getItemAtPosition(position).getWords());
    }
    /*
    public static List<Word> generateTestWordsList(int counter){
        List<Word> words = new LinkedList<>();
        for(int i = 0; i < counter; i++)
            words.add(new Word(i+"ru",i+"en"));
        return words;
    }
    public static List<Record> generateTestRecords(){
        List<Record> recordList = new LinkedList<>();
        Random random = new Random();
        for(int i = 0; i < 10; i++)
            recordList.add(new Record(
                    "Name" + i,
                    random.nextInt(100),
                    generateTestWordsList(10)));

        return recordList;
    }
     */
}
