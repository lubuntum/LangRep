package com.stud.langrep.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.stud.langrep.R;
import com.stud.langrep.api.translateapi.TranslateAPI;
import com.stud.langrep.database.entity.Word;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordViewHolder>{
    private final LayoutInflater inflater;
    private final List<Word> wordList;
    private TranslateAPI translateAPI;

    public WordsAdapter(Context context, List<Word> wordList) {
        this.wordList = wordList;
        this.inflater = LayoutInflater.from(context);
        this.translateAPI = new TranslateAPI();
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.word_item,parent,false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = wordList.get(position);
        //Запомним какое слово было изначальным, что бы избежать бесмысленых запросов сети
        //при смене фокуса, что бы не переводить одно и то же число (ниже использование)
        String previous = word.getNativeWord();
        holder.setNativeWordTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                word.setNativeWord(editable.toString());
            }
        });
        holder.setTranslatedWordTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                word.setTranslatedWord(editable.toString());//Привет мир
            }
        });
        holder.nativeWord.setText(word.getNativeWord());
        holder.translatedWord.setText(word.getTranslatedWord());

        holder.nativeWord.setOnFocusChangeListener((view,focus)->{

            if(!focus && !previous.equals(word.getNativeWord().trim())){
                Runnable translateRnb = ()->{
                    view.post(()-> holder.translatedWordProgress.setVisibility(View.VISIBLE));
                    String translatedText = translateAPI.translateText(word.getNativeWord(),"ru","en").trim();
                    view.post(()->{
                        if (translatedText.equals(TranslateAPI.NETWORK_ERROR)){
                            Toast.makeText(view.getContext(), TranslateAPI.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                            holder.translatedWordProgress.setVisibility(View.GONE);
                            return;
                        }
                        word.setTranslatedWord(translatedText);
                        holder.translatedWord.setText(translatedText);
                        holder.translatedWordProgress.setVisibility(View.INVISIBLE);
                        this.notifyItemChanged(position);
                        //previous = translatedText;
                    });
                };
                Thread thread = new Thread(translateRnb);
                thread.start();
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull WordViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }
    public void addWord(Word word){
        wordList.add(word);
        notifyItemInserted(wordList.indexOf(word));
    }

    public List<Word> getWordList() {
        return wordList;
    }

    static class WordViewHolder extends RecyclerView.ViewHolder{
        public BootstrapEditText nativeWord;
        public ProgressBar nativeWordProgress;
        public BootstrapEditText translatedWord;
        public ProgressBar translatedWordProgress;
        private TextWatcher nativeWordTextWatcher;
        private TextWatcher translatedWordTextWatcher;
        public WordViewHolder(@Nullable View itemView){
            super(itemView);
            nativeWord = itemView.findViewById(R.id.native_word);
            nativeWordProgress = itemView.findViewById(R.id.native_word_progress);
            translatedWord = itemView.findViewById(R.id.translated_word);
            translatedWordProgress = itemView.findViewById(R.id.translated_word_progress);
        }

        public void setNativeWordTextWatcher(TextWatcher textWatcher) {
            nativeWord.removeTextChangedListener(this.nativeWordTextWatcher);
            nativeWord.addTextChangedListener(textWatcher);

            this.nativeWordTextWatcher = textWatcher;
        }

        public void setTranslatedWordTextWatcher(TextWatcher textWatcher) {
            translatedWord.removeTextChangedListener(this.translatedWordTextWatcher);
            translatedWord.addTextChangedListener(textWatcher);

            this.translatedWordTextWatcher = textWatcher;
        }
    }
}
