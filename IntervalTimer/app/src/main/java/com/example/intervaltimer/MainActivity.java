package com.example.intervaltimer;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
        private TextView timerTextView;
        private EditText exerciseEditText;
        private EditText workTimeEditText;
        private EditText restTimeEditText;
        private Button startButton;
        private Button resetButton;
        private CountDownTimer countDownTimer;
        private long timeRemainingInMillis;
        private long workTimeInMillis;
        private long restTimeInMillis;
        private boolean isWorkTime;
        private boolean isTimerRunning;
        private int currentRound;
        private LinearLayout mainLayout;

        private MediaPlayer mediaPlayer;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            timerTextView = findViewById(R.id.timerTextView);
            exerciseEditText = findViewById(R.id.exerciseEditText);
            workTimeEditText = findViewById(R.id.workTimeEditText);
            restTimeEditText = findViewById(R.id.restTimeEditText);
            startButton = findViewById(R.id.startButton);
            resetButton = findViewById(R.id.resetButton);
            mainLayout = findViewById(R.id.mainLayout);

            mediaPlayer = MediaPlayer.create(this, R.raw.sound1);

            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTimerRunning) {
                        pauseTimer();
                    } else {
                        startTimer();
                    }
                }
            });

            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetTimer();
                }
            });
        }

        private void startTimer() {
            int exerciseCount = Integer.parseInt(exerciseEditText.getText().toString())*2-1;
            workTimeInMillis = Long.parseLong(workTimeEditText.getText().toString()) * 1000;
            restTimeInMillis = Long.parseLong(restTimeEditText.getText().toString()) * 1000;

            countDownTimer = new CountDownTimer(timeRemainingInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeRemainingInMillis = millisUntilFinished;
                    updateTimerText();

                    if (millisUntilFinished <= 1000) {
                        mediaPlayer.start();
                    }
                }

                @Override
                public void onFinish() {
                    currentRound++;
                    isWorkTime = !isWorkTime;
                    mediaPlayer.start();

                    if (currentRound <= exerciseCount) {
                        if (isWorkTime) {
                            timeRemainingInMillis = workTimeInMillis;
                            mainLayout.setBackgroundColor(getResources().getColor(R.color.pastel_green));
                        } else {
                            timeRemainingInMillis = restTimeInMillis;
                            mainLayout.setBackgroundColor(getResources().getColor(R.color.pastel_blue));
                        }
                        startTimer();
                    } else {
                        resetTimer();
                    }
                }
            };

            countDownTimer.start();
            isTimerRunning = true;
            startButton.setText("Пауза");
        }

        private void pauseTimer() {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            isTimerRunning = false;
            startButton.setText("Продолжить");
        }

        private void resetTimer() {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            isTimerRunning = false;
            timeRemainingInMillis = 0;
            currentRound = 0;
            updateTimerText();
            mainLayout.setBackgroundColor(Color.WHITE);
            startButton.setText("Старт");
        }

        private void updateTimerText() {
            int minutes = (int) (timeRemainingInMillis / 1000) / 60;
            int seconds = (int) (timeRemainingInMillis / 1000) % 60;
            String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);

            if (isWorkTime) {
                timerTextView.setText("Работа\n" + timeLeftFormatted);
            } else {
                timerTextView.setText("Отдых\n" + timeLeftFormatted);
            }
        }
}


