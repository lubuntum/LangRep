package com.stud.langrep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.stud.langrep.R;
import com.stud.langrep.entity.Record;

import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordViewHolder> {
    private final LayoutInflater inflater;
    private final List<Record> recordList;
    public RecordsAdapter(Context context, List<Record> recordList){
        this.recordList = recordList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.record_item,parent,false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = recordList.get(position);
        holder.recordName.setText(record.getRecordName());
        holder.totalWords.setText(record.getTotalWords());
        holder.averageDuration.setText(record.getBasicDuration());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder{
        public TextView recordName;
        public TextInputEditText totalRepeats;
        public ImageView loop;
        public ImageView recordContent;
        public TextView totalWords;
        public TextView averageDuration;
        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            recordName = itemView.findViewById(R.id.record_name);
            totalRepeats = itemView.findViewById(R.id.total_repeats);
            loop = itemView.findViewById(R.id.loop);
            recordContent = itemView.findViewById(R.id.record_content);
            totalWords = itemView.findViewById(R.id.words);
            averageDuration = itemView.findViewById(R.id.duration);

            loopInit(itemView);
            recordContentInit(itemView);
        }
        /*
        * Событие для loop, добавить бесконечное зацикливание*/
        private void loopInit(View itemView){
            loop.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Loop click", Toast.LENGTH_SHORT).show();
            });
        }
        /*Событие отобразить все содержимое записи (открыть тоже окно которое будет использоваться
        при создании Record, но только уже заполненное текстом этой записи)*/
        private void recordContentInit(View itemView){
            recordContent.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Show record content", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
