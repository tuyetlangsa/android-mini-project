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
    private Button btnStart, btnMute;
    private TextView tvCountdown, name1, name2, name3, name4, name5;
    private LinearLayout mainLayout;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Random random = new Random();

    private MediaPlayer backgroundMusicPlayer;
    private MediaPlayer raceMusicPlayer;
    private MediaPlayer finishSoundPlayer;

    private boolean isRacing = false;
    private boolean isMuted = false;
    private int[] horseSpeeds = new int[5];
    private ArrayList<Integer> finishedHorses = new ArrayList<>();
    private int finishLine = 100;
    ArrayList<Bet> playerBets ;
    private double currentBalance;
    private String[] horseNames = {
            "🐴 Red Thunderss",
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
        Intent intent = getIntent();
        playerBets = (ArrayList<Bet>) intent.getSerializableExtra("playerBets");
        currentBalance = intent.getDoubleExtra("CURRENT_BALANCE", 0.0);
        setupUI();
        setupListeners();

        initMediaPlayers();
        playBackgroundMusic();
    }

    private void initViews() {
        mainLayout = findViewById(R.id.race);
        seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar3 = findViewById(R.id.seekBar3);
        seekBar4 = findViewById(R.id.seekBar4);
        seekBar5 = findViewById(R.id.seekBar5);
        btnStart = findViewById(R.id.btnStart);
        btnMute = findViewById(R.id.btnMute);
        tvCountdown = findViewById(R.id.tvCountdown);
        name1 = findViewById(R.id.name1);
        name2= findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        name4 = findViewById(R.id.name4);
        name5 = findViewById(R.id.name5);
    }
    private void setupUI() {
        name1.setText(horseNames[0]);
        name2.setText(horseNames[1]);
        name3.setText(horseNames[2]);
        name4.setText(horseNames[3]);
        name5.setText(horseNames[4]);
        mainLayout.setBackgroundColor(Color.parseColor("#1E293B"));
        btnStart.setBackgroundColor(Color.parseColor("#10B981"));
        btnStart.setTextColor(Color.WHITE);
        btnStart.setTextSize(18);
        btnStart.setPadding(40, 20, 40, 20);


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

    private void initMediaPlayers() {
        try {
            backgroundMusicPlayer = MediaPlayer.create(this, R.raw.background_music);
            if (backgroundMusicPlayer != null) {
                backgroundMusicPlayer.setLooping(true);
                backgroundMusicPlayer.setVolume(0.5f, 0.5f);
            }

            raceMusicPlayer = MediaPlayer.create(this, R.raw.race_music);
            if (raceMusicPlayer != null) {
                raceMusicPlayer.setLooping(true);
                raceMusicPlayer.setVolume(1.0f, 1.0f);
            }

            finishSoundPlayer = MediaPlayer.create(this, R.raw.finish_sound);
            if (finishSoundPlayer != null) {
                finishSoundPlayer.setLooping(false); // Chỉ phát một lần
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playBackgroundMusic() {
        if (backgroundMusicPlayer != null && !backgroundMusicPlayer.isPlaying() && !isMuted) {
            backgroundMusicPlayer.start();
        }
    }

    private void setupListeners() {
        btnStart.setOnClickListener(v -> startRace());

        if (btnMute != null) {
            btnMute.setOnClickListener(v -> toggleMute());
        }
    }


    private void startRace() {
        if (isRacing) return;

        isRacing = true;
        finishedHorses.clear();
        btnStart.setEnabled(false);
        btnStart.setAlpha(0.5f);


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
        for (int i = 0; i < 5; i++) {
            horseSpeeds[i] = random.nextInt(2) + 1;
        }

        if (backgroundMusicPlayer != null && backgroundMusicPlayer.isPlaying()) {
            backgroundMusicPlayer.pause();
        }
        if (raceMusicPlayer != null && !raceMusicPlayer.isPlaying() && !isMuted) {
            raceMusicPlayer.start();
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
                        int speedVariation = random.nextInt(1);
                        int newProgress = Math.min(currentProgress + horseSpeeds[i] + speedVariation, finishLine);
                        seekBars[i].setProgress(newProgress);

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

        if (isMuted) {
            if (backgroundMusicPlayer != null && backgroundMusicPlayer.isPlaying()) {
                backgroundMusicPlayer.pause();
            }
            if (raceMusicPlayer != null && raceMusicPlayer.isPlaying()) {
                raceMusicPlayer.pause();
            }
        } else {
            if (isRacing) {
                if (raceMusicPlayer != null && !raceMusicPlayer.isPlaying()) {
                    raceMusicPlayer.start();
                }
            } else {
                if (backgroundMusicPlayer != null && !backgroundMusicPlayer.isPlaying()) {
                    backgroundMusicPlayer.start();
                }
            }
        }
    }
    private void endRace() {
        isRacing = false;

        if (raceMusicPlayer != null && raceMusicPlayer.isPlaying()) {
            raceMusicPlayer.pause();
        }

        if (finishSoundPlayer != null && !isMuted) {
            finishSoundPlayer.start();
        }

        btnStart.setVisibility(View.GONE);

        handler.postDelayed(this::showRaceResultScreen, 15000);
    }

    private void showRaceResultScreen() {
        Intent intent = new Intent(this, RaceResultActivity.class);
        intent.putIntegerArrayListExtra("finishedHorses", finishedHorses);
        intent.putExtra("playerBets", playerBets);
        intent.putExtra("CURRENT_BALANCE", currentBalance);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.release();
            backgroundMusicPlayer = null;
        }
        if (raceMusicPlayer != null) {
            raceMusicPlayer.release();
            raceMusicPlayer = null;
        }
        if (finishSoundPlayer != null) {
            finishSoundPlayer.release();
            finishSoundPlayer = null;
        }
    }
}
