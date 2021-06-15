package com.example.musicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Playsong extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.stop();
        mediaplayer.release();
        updateseek.interrupt();
    }

    TextView text;
    ImageView play,pause,prev,next;
    ArrayList<File> songs;
    MediaPlayer mediaplayer;
    String textcontent;
    int position;
    Thread updateseek;
    SeekBar seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);

        text = findViewById(R.id.textView);
        play = findViewById(R.id.pause);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        seek = findViewById(R.id.seek);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songlist");
        textcontent = intent.getStringExtra("currentSong");
        text.setText(textcontent);
        text.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaplayer = MediaPlayer.create(this,uri);
        mediaplayer.start();
        seek.setMax(mediaplayer.getDuration());

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaplayer.seekTo(seekBar.getProgress());
            }
        });

        updateseek = new Thread(){
            @Override
            public void run() {
                int current = 0;
                try{
                    while(current < mediaplayer.getDuration()){
                        current = mediaplayer.getCurrentPosition();
                        seek.setProgress(current);
                        sleep(800);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        };
        updateseek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaplayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaplayer.pause();
                }else{
                    play.setImageResource(R.drawable.pause);
                    mediaplayer.start();
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.stop();
                mediaplayer.release();
                if(position!=0){
                    position=position-1;
                }else{
                    position=songs.size()-1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaplayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaplayer.start();
                play.setImageResource(R.drawable.pause);
                seek.setMax(mediaplayer.getDuration());
                textcontent = songs.get(position).getName().toString();
                text.setText(textcontent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.stop();
                mediaplayer.release();
                if(position!=songs.size()-1){
                    position=position+1;
                }else{
                    position=0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaplayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaplayer.start();
                play.setImageResource(R.drawable.pause);
                seek.setMax(mediaplayer.getDuration());
                textcontent = songs.get(position).getName().toString();
                text.setText(textcontent);
            }
        });





    }
}