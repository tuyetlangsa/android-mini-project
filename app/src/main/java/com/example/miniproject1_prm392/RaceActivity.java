package com.example.miniproject1_prm392;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;

public class RaceActivity extends AppCompatActivity {

    private SeekBar seekBar1, seekBar2, seekBar3, seekBar4, seekBar5;
    private Button btnStart, btnReset, btnMute;
    private TextView tvCountdown;
    private LinearLayout mainLayout;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Random random = new Random();
    private MediaPlayer mediaPlayer;

    private boolean isRacing = false;
    private boolean isMuted = false;
    private int[] horseSpeeds = new int[5];
    private ArrayList<Integer> finishedHorses = new ArrayList<>();
    private int finishLine = 100;

    private String[] horseNames = {
            "🐴 Red Thunder",
            "🐎 Blue Lightning",
            "🐴 Green Storm",
            "🐎 Golden Wind",
            "🐴 Purple Dash"
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_race);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.race), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupUI();
        setupListeners();
        initMediaPlayer();
    }

    private void initViews() {
        mainLayout = findViewById(R.id.race);
        seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar3 = findViewById(R.id.seekBar3);
        seekBar4 = findViewById(R.id.seekBar4);
        seekBar5 = findViewById(R.id.seekBar5);
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        btnMute = findViewById(R.id.btnMute);
        tvCountdown = findViewById(R.id.tvCountdown);
    }
    private void setupUI() {
        // Màu nền gradient
        mainLayout.setBackgroundColor(Color.parseColor("#1E293B"));
        btnStart.setBackgroundColor(Color.parseColor("#10B981"));
        btnStart.setTextColor(Color.WHITE);
        btnStart.setTextSize(18);
        btnStart.setPadding(40, 20, 40, 20);

        if (btnReset != null) {
            btnReset.setBackgroundColor(Color.parseColor("#EF4444"));
            btnReset.setTextColor(Color.WHITE);
            btnReset.setVisibility(View.GONE);
        }

        if (btnMute != null) {
            btnMute.setBackgroundColor(Color.parseColor("#6366F1"));
            btnMute.setTextColor(Color.WHITE);
        }


        if (tvCountdown != null) {
            tvCountdown.setTextSize(72);
            tvCountdown.setTextColor(Color.parseColor("#F59E0B"));
            tvCountdown.setVisibility(View.GONE);
        }
    }

    private void initMediaPlayer() {
        try {
            // Sử dụng nhạc mặc định của hệ thống hoặc thêm file vào res/raw
            // mediaPlayer = MediaPlayer.create(this, R.raw.race_music);
            // Tạm thời dùng notification sound
            mediaPlayer = MediaPlayer.create(this,
                    android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(0.3f, 0.3f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        btnStart.setOnClickListener(v -> startRace());

        if (btnReset != null) {
            btnReset.setOnClickListener(v -> resetRace());
        }

        if (btnMute != null) {
            btnMute.setOnClickListener(v -> toggleMute());
        }
    }

    private void resetRace() {
        isRacing = false;
        finishedHorses.clear();

        SeekBar[] seekBars = {seekBar1, seekBar2, seekBar3, seekBar4, seekBar5};
        for (SeekBar sb : seekBars) {
            sb.setProgress(0);
        }

        btnStart.setVisibility(View.VISIBLE);
        btnStart.setEnabled(true);
        btnStart.setAlpha(1.0f);

        if (btnReset != null) {
            btnReset.setVisibility(View.GONE);
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }



    private void startRace() {
        if (isRacing) return;

        isRacing = true;
        finishedHorses.clear();
        btnStart.setEnabled(false);
        btnStart.setAlpha(0.5f);

        if (btnReset != null) {
            btnReset.setVisibility(View.GONE);
        }

        // Countdown
        startCountdown();
    }
    private void startCountdown() {
        if (tvCountdown != null) {
            tvCountdown.setVisibility(View.VISIBLE);
        }

        final int[] count = {3};

        Runnable countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (count[0] > 0) {
                    if (tvCountdown != null) {
                        tvCountdown.setText(String.valueOf(count[0]));
                        // Animation
                        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvCountdown, "scaleX", 0.5f, 1.5f, 1.0f);
                        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvCountdown, "scaleY", 0.5f, 1.5f, 1.0f);
                        scaleX.setDuration(1000);
                        scaleY.setDuration(1000);
                        scaleX.start();
                        scaleY.start();
                    }
                    count[0]--;
                    handler.postDelayed(this, 1000);
                } else {
                    if (tvCountdown != null) {
                        tvCountdown.setText("GO!");
                        tvCountdown.setTextColor(Color.parseColor("#10B981"));
                    }
                    handler.postDelayed(() -> {
                        if (tvCountdown != null) {
                            tvCountdown.setVisibility(View.GONE);
                        }
                        beginRace();
                    }, 500);
                }
            }
        };
        handler.post(countdownRunnable);
    }


    private void beginRace() {
        // Random speeds cho mỗi con ngựa
        for (int i = 0; i < 5; i++) {
            horseSpeeds[i] = random.nextInt(3) + 2; // Speed từ 2-4
        }

        // Play music
        if (mediaPlayer != null && !isMuted) {
            try {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        raceRunnable.run();
    }

    private Runnable raceRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRacing) return;

            SeekBar[] seekBars = {seekBar1, seekBar2, seekBar3, seekBar4, seekBar5};
            boolean allFinished = true;

            for (int i = 0; i < seekBars.length; i++) {
                if (!finishedHorses.contains(i)) {
                    int currentProgress = seekBars[i].getProgress();

                    if (currentProgress < finishLine) {
                        allFinished = false;
                        // Random variation cho realistic hơn
                        int speedVariation = random.nextInt(2);
                        int newProgress = Math.min(currentProgress + horseSpeeds[i] + speedVariation, finishLine);
                        seekBars[i].setProgress(newProgress);

                        // Check if finished
                        if (newProgress >= finishLine) {
                            finishedHorses.add(i);
                        }
                    }
                }
            }

            if (!allFinished) {
                handler.postDelayed(this, 100);
            } else {
                endRace();
            }
        }
    };

    private void toggleMute() {
        isMuted = !isMuted;
        if (btnMute != null) {
            btnMute.setText(isMuted ? "🔇 Bật nhạc" : "🔊 Tắt nhạc");
        }

        if (mediaPlayer != null) {
            if (isMuted) {
                mediaPlayer.setVolume(0, 0);
            } else {
                mediaPlayer.setVolume(0.3f, 0.3f);
            }
        }
    }
    private void endRace() {
        isRacing = false;

        // Stop music
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }

        btnStart.setVisibility(View.GONE);
        if (btnReset != null) {
            btnReset.setVisibility(View.VISIBLE);
        }
        // Mở màn hình kết quả
        showRaceResultScreen();
    }
    // Add this field to RaceActivity
    private int playerSelectedHorse = 0; // Player's chosen horse
    private void showRaceResultScreen() {
        Intent intent = new Intent(this, RaceResultActivity.class);
        intent.putIntegerArrayListExtra("finishedHorses", finishedHorses);
        intent.putExtra("playerChoice", playerSelectedHorse); // Pass player's choice
        startActivity(intent);
    }
}