package com.stud.langrep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.stud.langrep.R;
import com.stud.langrep.database.entity.Record;
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
    private RecordViewHolder.PlayRecordClickListener recordClickListener;
    private FragmentManager fragmentManager;
    public RecordsAdapter(Context context, List<Record> recordList, FragmentManager fragmentManager){
        this.recordList = recordList;
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
        durationPattern = context.getResources().getString(R.string.duration_of_record);
        wordsCountPattern = context.getResources().getString(R.string.words_count);
    }
    public RecordsAdapter(Context context, List<Record> recordList,
                          FragmentManager fragmentManager,
                          RecordViewHolder.OnRecordItemClickListener listener,
                          RecordViewHolder.PlayRecordClickListener playListener){
        this.recordList = recordList;
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
        durationPattern = context.getResources().getString(R.string.duration_of_record);
        wordsCountPattern = context.getResources().getString(R.string.words_count);
        this.itemClickListener = listener;
        this.recordClickListener = playListener;
    }
    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.record_item,parent,false);
        RecordViewHolder holder = new RecordViewHolder(view);
        holder.setOnRecordItemClickListener(itemClickListener);
        holder.setOnPlayRecordClickListener(recordClickListener);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = recordList.get(position);
        holder.recordName.setText(record.getRecordName());
        holder.totalWords.setText(String.format(wordsCountPattern,record.getWordCount()));
        holder.averageDuration.setText(String.format(durationPattern,record.getBasicDuration()));
        holder.setOnSettingsClickListener((view)->{
            RecordSettingsDialog settingsDialog = RecordSettingsDialog.getInstance(record);
            settingsDialog.show(fragmentManager, "settings");
        });
    }
    /**
     * Если у нас большой список и мы листаем, повторное использование View должно быть обработано
     * Если играет какая то музыка и эта вьюшка используется повторно, тогда необходимо
     * переключить ее в исходную позицию ведь она перерабатывается пока что, а если мы мотаем
     * обратно в верх, тогда необходимо включить ее обратно (спорно)
     * */

    @Override
    public void onViewRecycled(@NonNull RecordViewHolder holder) {
        super.onViewRecycled(holder);

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
        /**Необходима что бы переключать анимацию записи, и включать аудио*/
        public boolean isPlaying = false;
        public ConstraintLayout container;
        public TextView recordName;
        public TextInputEditText totalRepeats;
        public ImageView loop;
        public ImageView settings;
        public TextView totalWords;
        public TextView averageDuration;
        public ProgressBar uploadWordsProgress;
        public OnRecordItemClickListener listener;
        public PlayRecordClickListener playListener;
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
            uploadWordsProgress = itemView.findViewById(R.id.upload_words_progress);

            playBtnInit();
            //containerInit(itemView);
            loopInit(itemView);
        }
        public void switchPlayState(){
            ((GifDrawable)playBtn.getDrawable()).start();
            Runnable runnable = () -> {
                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                playBtn.post(()->{
                    ((GifDrawable)playBtn.getDrawable()).stop();
                    if(isPlaying)  {
                        ((GifDrawable)playBtn.getDrawable()).seekToFrame(10);
                        playListener.stopSpeech();
                    }
                    else  {
                        ((GifDrawable)playBtn.getDrawable()).seekToFrame(0);
                        playListener.playSpeech(getAdapterPosition(),this);
                    }
                    isPlaying = !isPlaying;
                });
            };
            new Thread(runnable).start();
        }
        /*Анимация плавно переключается между состояниями play(кадр 10) & pause(кадр 0)*/
        private void playBtnInit(){
            playBtn.setOnClickListener(view->{
                if(playListener == null) return;
                switchPlayState();
                //playBtn.performClick();
            });
        }
        /*
         * Событие для loop, добавить бесконечное зацикливание*/
        private void loopInit(View itemView){
            loop.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), "Loop click", Toast.LENGTH_SHORT).show();
            });
        }

        /*Событие отобразить все содержимое записи (открыть тоже окно которое будет использоваться
        при создании Record, но только уже заполненное данными этой записи)*/
        public void setOnRecordItemClickListener(OnRecordItemClickListener listener){
            this.listener = listener;
            container.setOnClickListener(view -> listener.click(getAdapterPosition()));
        }
        public void setOnPlayRecordClickListener(PlayRecordClickListener playListener){
            this.playListener = playListener;
        }
        /**
         * Interface for all view in adapter, whenever
         * user clicks on viewItem its trigger this listener
         * implements in Fragments and etc..., where adapter in use
         * */
        public interface OnRecordItemClickListener{
            void click(int position);
        }
        /**
         *interface for play button, whenever user
         * click on play button, after animation start
         * play record, interface implements where data stored
         * because its help transfer text from Record to speech
         * implements in Fragments and etc..., where adapter in use
         */
        public interface PlayRecordClickListener{
            void playSpeech(int position, RecordViewHolder recordViewHolder);
            void stopSpeech();

        }
        public void setOnSettingsClickListener(View.OnClickListener listener){
            settings.setOnClickListener(null);
            settings.setOnClickListener(listener);
        }
    }
}
