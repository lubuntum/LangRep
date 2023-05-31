package com.stud.langrep.fragments.lesson;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.stud.langrep.R;
import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.entity.Word;
import com.stud.langrep.databinding.LessonSessionFragmentBinding;

import java.text.DecimalFormat;

public class LessonFragment extends Fragment {
    private Record record;
    private int currentWord=0;
    private int mistakes = 0;
    private int correct = 0;
    LessonSessionFragmentBinding binding;
    public static LessonFragment getInstance(Record record){
        Bundle args = new Bundle();
        args.putSerializable("record", record);
        LessonFragment fragment = new LessonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getArguments() != null && getArguments().containsKey("record"))
            record = (Record) getArguments().getSerializable("record");

        binding = LessonSessionFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateSession(record.getWords().get(currentWord));
        nextBtnInit();
    }
    private void updateSession(Word word){
        if(word == null) return;
        binding.translatedWord.setText(word.getTranslatedWord());
        binding.microBtn.setColorFilter(
                ContextCompat.getColor(
                        getContext(),
                        com.beardedhen.androidbootstrap.R.color.bootstrap_gray_lighter),
                PorterDuff.Mode.MULTIPLY);
    }
    private void nextBtnInit(){
        binding.nextWordBtn.setOnClickListener((view)->{
            if(record.getWords().size() == 0) return;
            if(record.getWords().size()-1 == currentWord){
                showResult();
                return;
            }
            if(!binding.nativeWord.getText().toString()
                    .equals(record.getWords().get(currentWord).getNativeWord())){
                Toast.makeText(getContext(), "Подумайте еще", Toast.LENGTH_SHORT).show();
                binding.nextWordBtn.setColorFilter(ContextCompat.getColor(getContext(), com.beardedhen.androidbootstrap.R.color.bootstrap_brand_danger));
                mistakes++;
                return;
            }
            currentWord++;
            correct++;
            Toast.makeText(getContext(), "Верно", Toast.LENGTH_SHORT).show();
            binding.nextWordBtn.setColorFilter(ContextCompat.getColor(getContext(), com.beardedhen.androidbootstrap.R.color.bootstrap_brand_success));
            updateSession(record.getWords().get(currentWord));
        });
    }
    private void showResult(){
        binding.contentContainer.setVisibility(View.GONE);
        binding.endSessionContainer.setVisibility(View.VISIBLE);
        binding.mistakes.setText(String.format(getResources().getString(R.string.mistakes_count),mistakes));
        binding.correct.setText(String.format(getResources().getString(R.string.correct_answers),correct));
        DecimalFormat dF = new DecimalFormat("#.##");
        String resultStr = getResources().getString(R.string.success_rate);
        //!!!binding.correct.setText(String.format(resultStr, dF.format(correct / record.getWords().size() * 100)));
        binding.successRate.setText(String.format(resultStr, dF.format((float) Math.abs(mistakes - record.getWords().size()) / (float)record.getWords().size() * 100)) + "%");
    }
}
