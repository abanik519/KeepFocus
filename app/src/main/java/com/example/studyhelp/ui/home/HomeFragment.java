package com.example.studyhelp.ui.home;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.studyhelp.R;

import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView text;
    private EditText user;
    private EditText userWork;
    private EditText userRest;
    private Button start;
    private Button reset;
    private Button work;
    private Button rest;
    private ProgressBar bar;
    private long input;

    private CountDownTimer timer;
    private long startTime;
    private long timeLeft;
    private boolean running;
    private int counter = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        user = root.findViewById(R.id.input);
        userWork = root.findViewById(R.id.workInput);
        userRest = root.findViewById(R.id.restInput);
        text = root.findViewById(R.id.timer);
        start = root.findViewById(R.id.start);
        reset = root.findViewById(R.id.reset);
        work = root.findViewById(R.id.work);
        rest = root.findViewById(R.id.rest);
        bar = root.findViewById(R.id.bar);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = user.getText().toString();
                String[] time = str.split(":");
                if(time.length == 3){
                    input = Long.parseLong(time[0]) * 3600000;
                    input += Long.parseLong(time[1]) * 60000;
                    input += Long.parseLong(time[2]) * 1000;
                }
                else if(time.length == 2){
                    input = Long.parseLong(time[0]) * 60000;
                    input += Long.parseLong(time[1]) * 1000;
                }
                else{
                    input = Long.parseLong(time[0]) * 1000;
                }
                if (counter == 1){
                    startTime = input;
                }
                counter++;
                timeLeft = input;
                startStop();
            }
        });

        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    String str = userWork.getText().toString();
                    user.setText(str);
                }
            }
        });

        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    String str = userRest.getText().toString();
                    user.setText(str);
                }
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        return root;
    }

    private void startStop(){
        if(running){
            stopTimer();
        }
        else{
            startTimer();
        }
    }


    private void startTimer(){
        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                running = false;
                start.setText("START");
            }
        }.start();

        start.setText("PAUSE");
        reset.setVisibility(View.INVISIBLE);
        user.setVisibility(View.INVISIBLE);
        text.setVisibility(View.VISIBLE);
        running = true;
    }

    private void stopTimer(){
        timer.cancel();
        start.setText("START");
        reset.setVisibility(View.VISIBLE);
        user.setVisibility(View.VISIBLE);
        text.setVisibility(View.INVISIBLE);
        running = false;
    }

    private void resetTimer(){
        timeLeft = startTime;
        updateTimer();
        text.setVisibility(View.INVISIBLE);
        user.setVisibility(View.VISIBLE);
        reset.setVisibility(View.INVISIBLE);
    }

    private void updateTimer(){
        int hours = (int) (timeLeft / 1000) / 3600;
        int minutes = (int) ((timeLeft / 1000) % 3600) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeLeftText;
        if(hours > 0){
            timeLeftText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        }else {
            timeLeftText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
        user.setText(timeLeftText);
        text.setText(timeLeftText);
        counter++;
        int status;
        if(counter%2 ==0){
            status = 100;
        }
        else{
            status = 0;
        }
        bar.setProgress(status);
    }
}
