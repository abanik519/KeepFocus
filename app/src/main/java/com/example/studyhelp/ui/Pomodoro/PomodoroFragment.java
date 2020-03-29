package com.example.studyhelp.ui.Pomodoro;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.studyhelp.R;

import java.util.Locale;
import java.util.Random;

//public class HomeFragment extends Fragment {

public class PomodoroFragment extends Fragment {


    private final String[] quotes = {"Don’t let what you cannot do interfere with what you can do",
                                     "Strive for progress, not perfection",
                                     "There are no shortcuts to any place worth going",
                                     "Failure is the opportunity to begin again more intelligently",
                                     "Teachers can open the door, but you must enter it yourself",
                                     "The beautiful thing about learning is that no one can take it away from you"};

    // Physical App elements
    private TextView text;
    private TextView quote;
    private EditText user;
    private EditText userWork;
    private EditText userRest;
    private Button start;
    private Button reset;
    private Button work;
    private Button rest;
    private ProgressBar bar;

    // Background elements
    private long input;
    private long startTime;
    private int counter = 0;
    private CountDownTimer timer;
    private long timeLeft;
    private boolean running;

    public PomodoroFragment() {
        // Required empty public constructor
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pomodoro, container, false);



        user = root.findViewById(R.id.input);
        quote = root.findViewById(R.id.quote);
        userWork = root.findViewById(R.id.workInput);
        userRest = root.findViewById(R.id.restInput);
        text = root.findViewById(R.id.timer);
        start = root.findViewById(R.id.start);
        reset = root.findViewById(R.id.reset);
        work = root.findViewById(R.id.work);
        rest = root.findViewById(R.id.rest);
        bar = root.findViewById(R.id.bar);

        Random rand = new Random();
        int n = rand.nextInt(7);
        quote.setText(quotes[n]);

        user.setTag(null);
        user.addTextChangedListener(timeWatcher);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = user.getText().toString();
                if(str.length() == 0){ // Make sure user inputs something
                    Toast.makeText(getActivity(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] time = str.split(":");
                for(String a: time) {
                    if (a.length() > 2) { // Make sure its a valid time
                        Toast.makeText(getActivity(), "Please enter a valid time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                input = getTime(time);

                if(input == 0){ // Check if positive number
                    Toast.makeText(getActivity(), "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Only change the start time if the user actually changes it
                if(counter == 0){
                    startTime = input;
                    bar.setProgress(0);
                }
                counter = 1;
                timeLeft = input;
                startStop();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running && startTime == 0) {
                    String str = userWork.getText().toString();
                    if(str.length() == 0){ // Make user inputs something
                        Toast.makeText(getActivity(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    user.setText(str);
                    String[] time = str.split(":");
                    for(String a: time) {
                        if (a.length() > 2) {// Make sure its a valid time
                            Toast.makeText(getActivity(), "Please enter a valid time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }else if(!running) {
                    String str = userWork.getText().toString();
                    if(str.length() == 0){ // Make user inputs something
                        Toast.makeText(getActivity(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    user.setText(str);
                    String[] time = str.split(":");
                    for(String a: time) {
                        if (a.length() > 2) { // Make sure its a valid time
                            Toast.makeText(getActivity(), "Please enter a valid time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    startTime = getTime(time);
                    resetTimer();
                }
            }
        });

        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running && startTime == 0) {
                    String str = userRest.getText().toString();
                    if(str.length() == 0){ // Make user inputs something
                        Toast.makeText(getActivity(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    user.setText(str);
                    String[] time = str.split(":");
                    for(String a: time) {
                        if (a.length() > 2) { // Make sure its a valid time
                            Toast.makeText(getActivity(), "Please enter a valid time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }else if(!running) {
                    String str = userRest.getText().toString();
                    if(str.length() == 0){ // Make user inputs something
                        Toast.makeText(getActivity(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    user.setText(str);
                    String[] time = str.split(":");
                    for(String a: time) {
                        if (a.length() > 2) { // Make sure its a valid time
                            Toast.makeText(getActivity(), "Please enter a valid time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    startTime = getTime(time);
                    resetTimer();
                }
            }
        });
        return root;
    }

    // This will check when the time is changed
    private final TextWatcher timeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(user.getTag() == null){
                // This will allows us to change the start time when the user changes the time
                counter = 0;
            }
        }
    };

    // Gets the time from the users input
    private long getTime(String[] time){
        long output;
        if(time.length == 3){
            output = Long.parseLong(time[0]) * 3600000;
            output += Long.parseLong(time[1]) * 60000;
            output += Long.parseLong(time[2]) * 1000;
        }
        else if(time.length == 2){
            output = Long.parseLong(time[0]) * 60000;
            output += Long.parseLong(time[1]) * 1000;
        }
        else{
            output = Long.parseLong(time[0]) * 1000;
        }

        return output;
    }
    private void startStop(){
        if(running){
            stopTimer();
        }
        else{
            startTimer();
        }
    }


    // Starts the timer
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
                bar.setProgress(0);

                // Alarm
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                Ringtone r = RingtoneManager.getRingtone(getActivity(), notification);
                r.play();
            }
        }.start();
        start.setText("PAUSE");                     // Update view
        user.setVisibility(View.INVISIBLE);
        text.setVisibility(View.VISIBLE);
        reset.setVisibility(View.INVISIBLE);
        running = true;
    }

    private void stopTimer(){
        timer.cancel();
        start.setText("START");                    // Update view
        reset.setVisibility(View.VISIBLE);
        user.setVisibility(View.VISIBLE);
        text.setVisibility(View.INVISIBLE);
        running = false;
    }

    private void resetTimer(){
        timeLeft = startTime;
        updateTimer();
        reset.setVisibility(View.INVISIBLE);
    }


    private void updateTimer(){
        int hours = (int) (timeLeft / 1000) / 3600;             // Convert from milliseconds
        int minutes = (int) ((timeLeft / 1000) % 3600) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeLeftText;
        if(hours > 0){
            timeLeftText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        }else {
            timeLeftText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
        user.setTag("SIKE");        // Throw of the timeWatcher
        user.setText(timeLeftText);
        user.setTag(null);
        text.setText(timeLeftText);
        int status = (100*(int)timeLeft)/((int)startTime); // Make the status bar tik
        bar.setProgress(status);
    }
}
