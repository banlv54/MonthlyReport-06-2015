package com.example.rayleigh.monthlyreport_06_2015;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;




public class MainActivity extends ActionBarActivity {
    private Button b1,b2,b3,b4;
    private ImageView iv;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;
    private SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    int []songs;
    int currentIndex = 0;
    TypedValue value;
    AssetFileDescriptor afd;
    int MAX_VOLUME = 5;
    int volume = 2;

    public static int oneTimeOnly = 0;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songs = new int[] {R.raw.ldta, R.raw.rp28_3, R.raw.tmtyh};

        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3=(Button)findViewById(R.id.button3);
        b4=(Button)findViewById(R.id.button4);
        iv=(ImageView)findViewById(R.id.imageView);

        tx1=(TextView)findViewById(R.id.textView2);
        tx2=(TextView)findViewById(R.id.textView3);
        tx3=(TextView)findViewById(R.id.textView4);
        tx3.setText("ldta");
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        mediaPlayer = MediaPlayer.create(this, R.raw.ldta);

        surfaceView.setBackground(getDrawable(R.drawable.fr_logo));
        seekbar=(SeekBar)findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        b2.setEnabled(false);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }
                tx2.setText(String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                );

                tx1.setText(String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                );

                seekbar.setProgress((int)startTime);
                myHandler.postDelayed(UpdateSongTime,100);
                b2.setEnabled(true);
                b3.setEnabled(false);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                b2.setEnabled(false);
                b3.setEnabled(true);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp-backwardTime)>0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec",

                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    public void updateTime(String song_name) {
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        seekbar.setMax((int) finalTime);
        tx2.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
        );

        tx1.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
        );
        tx3.setText(song_name);
        seekbar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);
        updateStatus();
    }

       public void updateStatus() {
           if (mediaPlayer.isPlaying()){
               b2.setEnabled(true);
               b3.setEnabled(false);
           } else {
               b2.setEnabled(false);
               b3.setEnabled(true);
           }
       }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        String song_name = "";
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.next:
                currentIndex = (currentIndex + 1) % 3;
                afd = this.getResources().openRawResourceFd(songs[currentIndex]);
                mediaPlayer.reset();
                value = new TypedValue();
                this.getResources().getValue(songs[currentIndex], value, true);
                song_name = this.getResources().getResourceEntryName(songs[currentIndex]);
//                Toast.makeText(this, song_name, Toast.LENGTH_LONG).show();
                try {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),  afd.getDeclaredLength());
                    if (isVideo(value.string.toString())) {
                        surfaceView.setBackground(null);
                        surfaceHolder = surfaceView.getHolder();
                        mediaPlayer.setDisplay(surfaceHolder);
                        mediaPlayer.prepare();
//                        Toast.makeText(this, "VIDEO", Toast.LENGTH_LONG).show();
                    } else {
                        surfaceView.setBackground(this.getDrawable(R.drawable.fr_logo));
                        mediaPlayer.prepare();
                    }
                    mediaPlayer.start();
                    Toast.makeText(this, song_name, Toast.LENGTH_LONG).show();
                    updateTime(song_name);
                } catch (IOException e) {
                    Log.e("Loi next", e.getMessage(), e);
                    e.printStackTrace();
                }
                return true;
            case R.id.back:
                currentIndex = (currentIndex - 1) % 3;
                if (currentIndex < 0) currentIndex = 2;
                afd = this.getResources().openRawResourceFd(songs[currentIndex]);
                mediaPlayer.reset();
                value = new TypedValue();
                this.getResources().getValue(songs[currentIndex], value, true);
                song_name = this.getResources().getResourceEntryName(songs[currentIndex]);
                try {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),  afd.getDeclaredLength());
                    if (isVideo(value.string.toString())) {
                        surfaceView.setBackground(null);
                        surfaceHolder = surfaceView.getHolder();
                        mediaPlayer.setDisplay(surfaceHolder);
                        mediaPlayer.prepare();
                    } else {
                        surfaceView.setBackground(this.getDrawable(R.drawable.fr_logo));
                        mediaPlayer.prepare();
                    }
                    mediaPlayer.start();
                    updateTime(song_name);
                } catch (IOException e) {
                    Log.e("Loi next", e.getMessage(), e);
                    e.printStackTrace();
                }
                Toast.makeText(this, "Back", Toast.LENGTH_LONG).show();
                return true;
            case R.id.v_up:
                if (volume < MAX_VOLUME) {
                    volume += 1;
                }
                float v = (float)volume / (float)MAX_VOLUME;
                mediaPlayer.setVolume(v, v);
                Toast.makeText(this, volume + "", Toast.LENGTH_LONG).show();
                return true;
            case R.id.v_down:
                if (volume > 0){
                    volume -= 1;
                }
                float v2 = (float)volume / (float)MAX_VOLUME;
                mediaPlayer.setVolume(v2, v2);
                Toast.makeText(this, volume + "", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }

    public boolean isVideo(String file_name) {
        return file_name.contains("mp4");
    }
}