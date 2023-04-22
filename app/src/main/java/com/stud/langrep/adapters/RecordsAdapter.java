package com.stud.langrep.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.stud.langrep.CreateRecordActivity;
import com.stud.langrep.R;
import com.stud.langrep.database.entity.Record;
import com.stud.langrep.database.repository.RecordRepository;
import com.stud.langrep.dialog.RecordSettingsDialog;

import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageButton;
/*TODO
*  Сделать интерфейс в RecordViewHolder и реализовать его в RecordFragment, это присвоить главному контейнеру в RVH,
* Поскольку так получится обратиться к самим данным напрямую*/
public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordViewHolder> {
    private final LayoutInflater inflater;
    private List<Record> recordList;
    public final String durationPattern;
    public final String wordsCountPattern;
    private RecordViewHolder.OnRecordItemClickListener itemClickListener;
    private FragmentManager fragmentManager;
    public RecordsAdapter(Context context, List<Record> recordList, FragmentManager fragmentManager){
        this.recordList = recordList;
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
        durationPattern = context.getResources().getString(R.string.duration_of_record);
        wordsCountPattern = context.getResources().getString(R.string.words_count);
    }
    public RecordsAdapter(Context context, List<Record> recordList, FragmentManager fragmentManager, RecordViewHolder.OnRecordItemClickListener listener){
        this.recordList = recordList;
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
        durationPattern = context.getResources().getString(R.string.duration_of_record);
        wordsCountPattern = context.getResources().getString(R.string.words_count);
        this.itemClickListener = listener;

    }
    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.record_item,parent,false);
        RecordViewHolder holder = new RecordViewHolder(view);
        holder.setOnRecordItemClickListener(itemClickListener);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = recordList.get(position);
        holder.recordName.setText(record.getRecordName());
        holder.totalWords.setText(String.format(wordsCountPattern,record.getTotalWords()));
        holder.averageDuration.setText(String.format(durationPattern,record.getBasicDuration()));
        holder.setOnSettingsClickListener((view)->{
            RecordSettingsDialog settingsDialog = RecordSettingsDialog.getInstance(record);
            settingsDialog.show(fragmentManager, "settings");
        });

    }
    @Override
    public int getItemCount() {
        return recordList.size();
    }
    public Record getItemAtPosition(int position){
        return recordList.get(position);
    }
    public void setRecordList(List<Record> records){
        this.recordList = records;
        notifyDataSetChanged();
    }
    public static class RecordViewHolder extends RecyclerView.ViewHolder{
        public GifImageButton playBtn;
        public boolean isPlaying = false;
        public ConstraintLayout container;
        public TextView recordName;
        public TextInputEditText totalRepeats;
        public ImageView loop;
        public ImageView settings;
        public TextView totalWords;
        public TextView averageDuration;
        public OnRecordItemClickListener listener;
        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            playBtn = itemView.findViewById(R.id.play);
            ((GifDrawable)playBtn.getDrawable()).stop();
            ((GifDrawable)playBtn.getDrawable()).seekToFrame(10);
            //((GifDrawable)playBtn.getDrawable()).setLoopCount(1);
            recordName = itemView.findViewById(R.id.record_name);
            totalRepeats = itemView.findViewById(R.id.total_repeats);
            loop = itemView.findViewById(R.id.loop);
            settings = itemView.findViewById(R.id.settings);
            totalWords = itemView.findViewById(R.id.words);
            averageDuration = itemView.findViewById(R.id.duration);
            container = itemView.findViewById(R.id.record_container);

            playBtnInit();
            //containerInit(itemView);
            loopInit(itemView);
        }
        /*Анимация плавно переключается между состояниями play(кадр 10) & pause(кадр 0)*/
        private void playBtnInit(){
            playBtn.setOnClickListener(view->{
                ((GifDrawable)playBtn.getDrawable()).start();
                Runnable runnable = () -> {
                    try {
                        Thread.sleep(750);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    playBtn.post(()->{
                        ((GifDrawable)playBtn.getDrawable()).stop();
                        if(isPlaying)  ((GifDrawable)playBtn.getDrawable()).seekToFrame(10);
                        else  ((GifDrawable)playBtn.getDrawable()).seekToFrame(0);
                        isPlaying = !isPlaying;
                    });

                };
                new Thread(runnable).start();
            });

        }
        /*Событие отобразить все содержимое записи (открыть тоже окно которое будет использоваться
        при создании Record, но только уже заполненное данными этой записи)*/
        public void setOnRecordItemClickListener(OnRecordItemClickListener listener){
            this.listener = listener;
            container.setOnClickListener(view -> listener.click(getAdapterPosition()));
        }

        /*
        * Событие для loop, добавить бесконечное зацикливание*/
        private void loopInit(View itemView){
            loop.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Loop click", Toast.LENGTH_SHORT).show();
            });
        }
        public interface OnRecordItemClickListener{
            void click(int position);
        }
        public void setOnSettingsClickListener(View.OnClickListener listener){
            settings.setOnClickListener(null);
            settings.setOnClickListener(listener);
        }
    }
}
