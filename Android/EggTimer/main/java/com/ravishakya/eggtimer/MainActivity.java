package com.ravishakya.eggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SeekBar seekTimer;
    TextView lblTimer;
    boolean counterActive = false;
    Button btnGo;
    CountDownTimer cdt;

    public void updateTimer(int progress){
        //Log.i("Me called", "Me called");
        int mins = (int)progress / 60;
        int secs = progress - mins * 60;

        String strSec = Integer.toString(secs);
        if (secs <= 9){
            strSec = "0" + strSec;
        }

        lblTimer.setText(Integer.toString(mins) + ":" + strSec);
    }

    public void refresh(){
        counterActive = false;
        seekTimer.setEnabled(true);
        btnGo.setText("Go!");
        cdt.cancel();
        lblTimer.setText("00:30");
        seekTimer.setProgress(30);
    }

    public void controlTimer(View view){
        if (counterActive == false) {
            counterActive = true;
            seekTimer.setEnabled(false);
            btnGo.setText("Stop");
            //Log.i("Button pressed", "Button pressed");
           cdt =  new CountDownTimer((seekTimer.getProgress() * 1000) + 100, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    lblTimer.setText("0:00");
                    Log.i("Timer done", "COMPLETE");
                    MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                    mplayer.start();
                    refresh();
                }
            }.start();
        }else{

            refresh();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekTimer = (SeekBar) findViewById(R.id.timerSeekBar);
        lblTimer = (TextView) findViewById(R.id.lblTimer);
        btnGo = (Button) findViewById(R.id.btnGo);

        seekTimer.setMax(600);
        seekTimer.setProgress(30);

        seekTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
