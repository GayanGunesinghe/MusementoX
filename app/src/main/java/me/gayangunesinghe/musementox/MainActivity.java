package me.gayangunesinghe.musementox;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //declare activity elements.
    Button Play, Pause, Next, Previous;
    TextView StartTimer, EndTimer, SongName, Artist;
    MediaPlayer mediaPlayer;
    ImageView AlbumArt;
    SeekBar Seek;

    double startTime, finalTime;
    private Handler myHandler = new Handler();
    public static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign created elements to actual elements.
        Play = findViewById(R.id.Play);
        Pause = findViewById(R.id.Pause);
        Next = findViewById(R.id.Next);
        Previous = findViewById(R.id.Previous);

        Seek = findViewById(R.id.Seek);
        Seek.setClickable(false);

        StartTimer = findViewById(R.id.StartTimer);
        EndTimer = findViewById(R.id.EndTimer);

        //link to the onClick listener.
        Play.setOnClickListener(this);
        Pause.setOnClickListener(this);
        Next.setOnClickListener(this);
        Previous.setOnClickListener(this);

        //assign music file to media player.
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.taki_taki);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //play button onclick action.
            case R.id.Play:
                //set play button invisible and pause button visible.
                Play.setVisibility(View.INVISIBLE);
                Pause.setVisibility(View.VISIBLE);
                mediaPlayer.start();

                //get song duration and current position.
                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if(oneTimeOnly == 0){
                    Seek.setMax((int)finalTime);
                    oneTimeOnly = 1;
                }


                EndTimer.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));
                StartTimer.setText(String.format("%d:0%d", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

                Seek.setProgress((int)startTime);
                myHandler.postDelayed(UpdateSongTime,100);
                break;

            //pause button onclick action.
            case R.id.Pause:
                Pause.setVisibility(View.INVISIBLE);
                Play.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                break;

            case R.id.Next:
                break;

            case R.id.Previous:
                break;
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if(mediaPlayer.isPlaying()){
                startTime = mediaPlayer.getCurrentPosition();
                double seconds = (TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)));
                if(seconds <= 9){
                    StartTimer.setText(String.format("%d:0%d", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
                }
                else{
                    StartTimer.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
                }
                Seek.setProgress((int)startTime);
                myHandler.postDelayed(this, 100);
            }
            else{
                Play.setVisibility(View.VISIBLE);
                Pause.setVisibility(View.INVISIBLE);
                oneTimeOnly = 0;
            }

        }
    };
}
