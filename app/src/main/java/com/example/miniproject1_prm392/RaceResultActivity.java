package com.example.miniproject1_prm392;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;

public class RaceResultActivity extends AppCompatActivity {

    private String[] horseNames = {
            "Red Thunder",
            "Blue Lightning",
            "Green Storm",
            "Golden Wind",
            "Purple Dash"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_result);

        // Get finished horses from intent
        Intent intent = getIntent();
        ArrayList<Integer> finishedHorses = intent.getIntegerArrayListExtra("finishedHorses");

        // Validate data
        if (finishedHorses != null && !finishedHorses.isEmpty()) {
            showResults(finishedHorses);
        } else {
            // Handle error - no data received
            TextView tvResultStatus = findViewById(R.id.tvResultStatus);
            if (tvResultStatus != null) {
                tvResultStatus.setText("Error: No race data");
            }
        }

        // Setup return button
        Button btnReturnHome = findViewById(R.id.btnReturnHome);
        if (btnReturnHome != null) {
            btnReturnHome.setOnClickListener(v -> {
                finish(); // Go back to race screen
            });
        }
    }

    private void showResults(ArrayList<Integer> finishedHorses) {
        // Array of TextView IDs for horse names
        int[] nameIds = {
                R.id.tvHorse1Name,
                R.id.tvHorse2Name,
                R.id.tvHorse3Name,
                R.id.tvHorse4Name,
                R.id.tvHorse5Name
        };

        // Array of TextView IDs for horse times
        int[] timeIds = {
                R.id.tvHorse1Time,
                R.id.tvHorse2Time,
                R.id.tvHorse3Time,
                R.id.tvHorse4Time,
                R.id.tvHorse5Time
        };

        // Display results in order of finish (1st, 2nd, 3rd, 4th, 5th)
        for (int position = 0; position < finishedHorses.size() && position < 5; position++) {
            int horseIndex = finishedHorses.get(position);

            // Get the TextViews
            TextView nameView = findViewById(nameIds[position]);
            TextView timeView = findViewById(timeIds[position]);

            if (nameView != null && horseIndex >= 0 && horseIndex < horseNames.length) {
                // Add emoji based on position
                String emoji = "";
                switch (position) {
                    case 0: emoji = "🥇 "; break;
                    case 1: emoji = "🥈 "; break;
                    case 2: emoji = "🥉 "; break;
                    default: emoji = (position + 1) + ". "; break;
                }

                nameView.setText(emoji + horseNames[horseIndex]);
            }

            if (timeView != null) {
                // Generate time based on position (earlier positions = faster times)
                double baseTime = 45.0;
                double timeVariation = position * 1.5 + Math.random() * 0.5;
                double finalTime = baseTime + timeVariation;

                timeView.setText(String.format("Time: %.2fs", finalTime));
            }
        }

        // Set winner info in the result card
        displayWinnerInfo(finishedHorses);

        // Update wallet/balance (optional)
        updateBalance();
    }

    private void displayWinnerInfo(ArrayList<Integer> finishedHorses) {
        CardView resultCard = findViewById(R.id.resultCard);
        TextView tvResultStatus = findViewById(R.id.tvResultStatus);
        TextView tvResultMessage = findViewById(R.id.tvResultMessage);
        TextView tvResultIcon = findViewById(R.id.tvResultIcon);


        if (finishedHorses.isEmpty()) return;

        int winnerIndex = finishedHorses.get(0);
        int playerChoice = getIntent().getIntExtra("playerChoice", 0);
        boolean playerWon = (winnerIndex == playerChoice);

        if (resultCard != null && tvResultStatus != null && tvResultMessage != null && tvResultIcon != null) {
            if (playerWon) {
                // Player won
                resultCard.setCardBackgroundColor(Color.parseColor("#4CAF50")); // Green
                tvResultIcon.setText("🎉");
                tvResultStatus.setText("YOU WIN!");
                tvResultMessage.setText("Your horse " + horseNames[winnerIndex] + " won!");
            } else {
                // Player lost
                resultCard.setCardBackgroundColor(Color.parseColor("#F44336")); // Red
                tvResultIcon.setText("😢");
                tvResultStatus.setText("YOU LOSE!");
                tvResultMessage.setText(horseNames[winnerIndex] + " won the race!");
            }
        }
    }

    private void updateBalance() {
        TextView tvBalance = findViewById(R.id.tvBalance);
        if (tvBalance != null) {
            // TODO: Calculate actual balance based on bet and result
            // For now, just showing a sample value
            tvBalance.setText("$1,250");
        }
    }
}