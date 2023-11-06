package ir.adak.Vect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ArrayList<Music> arrayList;
    RecyclerView recyclerView;
    music_adapter music_adapter;
    MediaPlayer mediaPlayer;
    TextView Singer;
    TextView Music;
    TextView real_time;
    TextView duration;
    ImageView Img_Poster;
    ImageView Img_Poster_2;
    SeekBar seekBar;
    Timer timer;
    ImageView Play;
    Boolean Check_Play=false;
    ImageView Next_But;
    ImageView Pre_But;
    int Curser=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView=findViewById(R.id.recy_items);
        Next_But=findViewById(R.id.imageView27);
        Pre_But=findViewById(R.id.imageView28);
        Singer=findViewById(R.id.textView12);
        Music=findViewById(R.id.textView13);
        real_time=findViewById(R.id.real_time);
        duration=findViewById(R.id.duration);
        Img_Poster=findViewById(R.id.roundedImage);
        Play=findViewById(R.id.imageView26);
        seekBar=findViewById(R.id.seekBar);
        Img_Poster_2=findViewById(R.id.roundedImage2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList=new ArrayList<>();
        music_adapter=new music_adapter(arrayList);
        recyclerView.setAdapter(music_adapter);
        music_adapter.CLICK(new music_adapter.DATA() {
            @Override
            public void Send(ir.adak.Vect.Music music, int pos) {
                Curser=pos;
                mediaPlayer.release();
                timer.cancel();
                timer.purge();
                PlayMusic(music);
            }
        });
        PlayMusic(music_adapter.arrayList.get(0));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
             real_time.setText(Convert(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
             mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (Check_Play)
            {
                mediaPlayer.pause();
                Play.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                Check_Play=false;
            }else {
                Play.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                mediaPlayer.start();
                Check_Play=true;
            }
            }
        });
        Next_But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Next();
            }
        });
        Pre_But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pre();
            }
        });
    }

    private void Pre() {
        mediaPlayer.release();
        timer.cancel();
        timer.purge();
        if (Curser==0)
        {
             Curser=music_adapter.arrayList.size()-1;
        }else {
            Curser=Curser-1;
        }

        PlayMusic(music_adapter.arrayList.get(Curser));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer.stop();

    }
    public void PlayMusic(Music music)
    {
        Play.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
        mediaPlayer=MediaPlayer.create(this,music.getMusic());
        Img_Poster.setImageResource(music.getImg());
        Img_Poster_2.setImageResource(music.getImg());
        Singer.setText(music.getName_S());
        Music.setText(music.getName());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                duration.setText(Convert(mediaPlayer.getDuration()));
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                     runOnUiThread(new TimerTask() {
                         @Override
                         public void run() {
                             real_time.setText(Convert(mediaPlayer.getCurrentPosition()));
                             seekBar.setProgress(mediaPlayer.getCurrentPosition());
                         }
                     });
                    }
                },1000,1000);
                seekBar.setMax(mediaPlayer.getDuration());
                Check_Play=true;
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Next();
                    }
                });
            }
        });

    }

    private void Next() {
        mediaPlayer.release();
        timer.cancel();
        timer.purge();
        if (Curser!=music_adapter.arrayList.size()-1)
        {
            Curser=Curser+1;
        }else{
            Curser=0;
        }


        PlayMusic(music_adapter.arrayList.get(Curser));

    }

    public String Convert(long I)
    {
        long S=(I/1000)%60;
        long M=(I/(1000*60))%60;
        return String.format(Locale.US,"%02d:%02d",M,S);
    }
}