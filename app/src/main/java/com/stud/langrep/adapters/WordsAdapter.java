package com.stud.langrep.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.stud.langrep.R;
import com.stud.langrep.database.entity.Word;

import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordViewHolder>{
    private final LayoutInflater inflater;
    private final List<Word> wordList;

    public WordsAdapter(Context context, List<Word> wordList) {
        this.wordList = wordList;
        this.inflater = LayoutInflater.from(context);
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
        holder.nativeWord.setText(word.getNativeWord());
        holder.translatedWord.setText(word.getTranslatedWord());
        holder.nativeWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                word.setNativeWord(editable.toString());
            }
        });
        holder.translatedWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                word.setTranslatedWord(editable.toString());//Привет мир
            }
        });
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
        public BootstrapEditText translatedWord;
        public WordViewHolder(@Nullable View itemView){
            super(itemView);
            nativeWord = itemView.findViewById(R.id.native_word);
            translatedWord = itemView.findViewById(R.id.translated_word);
        }

    }
}
