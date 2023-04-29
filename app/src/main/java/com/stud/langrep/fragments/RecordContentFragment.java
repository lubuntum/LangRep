package com.stud.langrep.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.stud.langrep.R;
import com.stud.langrep.adapters.WordsAdapter;
import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.entity.Word;
import com.stud.langrep.databinding.RecordContentFragmentBinding;

import java.util.LinkedList;
import java.util.List;

public class RecordContentFragment extends Fragment {
    RecordContentFragmentBinding binding;
    public static final String RECORD_KEY = "record";
    public boolean isOptionsVisible = false;
    private Record record;
    private WordsAdapter adapter;
    RecordViewModel viewModel;
    public static RecordContentFragment getInstance(Record record){
        Bundle args = new Bundle();
        args.putSerializable(RECORD_KEY,record);
        RecordContentFragment fragment = new RecordContentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RecordViewModel.class);
        if(getArguments()!= null && getArguments().containsKey(RECORD_KEY)){
            this.record = (Record) getArguments().getSerializable(RECORD_KEY);
        }
        binding = RecordContentFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        wordListInit();
        addWordInit();
        startPracticeInit();
        showOptionsInit();
    }
    public void wordListInit(){
        if(record.getWords() != null && record.getWords().size() > 0){
            adapter = new WordsAdapter(getContext(), record.getWords());
            binding.wordsList.setAdapter(adapter);
            return;
        }
        Observer<List<Word>> wordListObserver = (words)->{
            if(words == null) return;
            if(adapter == null) adapter = new WordsAdapter(getContext(), words);

            record.setWords(words);
            binding.wordsList.setAdapter(adapter);
        };
        viewModel.getWordLive().observe(getViewLifecycleOwner(), wordListObserver);
        viewModel.uploadWords(record);
    }
    public void addWordInit(){
        binding.addWord.setOnClickListener((v)->{
            Word word = new Word("","",record.getId());
            if(adapter == null) {
                adapter = new WordsAdapter(getContext(), new LinkedList<>());
                binding.wordsList.setAdapter(adapter);

                //record.setWords(adapter.getWordList());
            }
            adapter.addWord(word);
        });
    }
    public void startPracticeInit(){
        binding.startPractice.setOnClickListener((view)->{
            Toast.makeText(getContext(), "Practice!", Toast.LENGTH_SHORT).show();
        });
    }
    public void showOptionsInit(){
        binding.options.setOnClickListener((view -> {
            changeOptionsState();
        }));
    }
    public void changeOptionsState(){
        if(isOptionsVisible){
            binding.addWord.setVisibility(View.INVISIBLE);
            binding.startPractice.setVisibility(View.INVISIBLE);
            binding.options.setImageDrawable(
                    AppCompatResources.getDrawable(getContext(),R.drawable.ic_arrow_up));
        }
        else {
            binding.addWord.setVisibility(View.VISIBLE);
            binding.startPractice.setVisibility(View.VISIBLE);
            binding.options.setImageDrawable(
                    AppCompatResources.getDrawable(getContext(),R.drawable.ic_arrow_down));
        }
        isOptionsVisible = !isOptionsVisible;
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.saveWords(adapter.getWordList());
    }
}
