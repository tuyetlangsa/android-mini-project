package com.example.miniproject1_prm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView; // Thêm import cho TextView
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat; // Thêm import để định dạng tiền tệ
import java.util.ArrayList;
import java.util.List;
import java.util.Locale; // Thêm import cho Locale

public class BetActivity extends AppCompatActivity {

    private CheckBox[] horseChecks;
    private EditText[] horseBets;
    private String[] horseNames = {
            "🐴 Red Thunderss",
            "🐎 Blue Lightning",
            "🐴 Green Storm",
            "🐎 Golden Wind",
            "🐴 Purple Dash"
    };
    private Button btnBet;
    private TextView tvCurrentBalance; // MỚI: TextView để hiển thị số dư

    private double currentBalance; // MỚI: Biến để lưu số dư

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet);
        Intent intent = getIntent();
        currentBalance = intent.getDoubleExtra("CURRENT_BALANCE", 0.0);
        tvCurrentBalance = findViewById(R.id.tv_current_balance);
        updateBalanceUI();


        horseChecks = new CheckBox[] {
                findViewById(R.id.cb_horse1),
                findViewById(R.id.cb_horse2),
                findViewById(R.id.cb_horse3),
                findViewById(R.id.cb_horse4),
                findViewById(R.id.cb_horse5)
        };

        horseBets = new EditText[] {
                findViewById(R.id.et_bet1),
                findViewById(R.id.et_bet2),
                findViewById(R.id.et_bet3),
                findViewById(R.id.et_bet4),
                findViewById(R.id.et_bet5)
        };

        btnBet = findViewById(R.id.btn_bet);
        setHorseNamesOnCheckBoxes();
        btnBet.setOnClickListener(v -> {
            List<Bet> bets = getSelectedBets();
            int totalBetAmount = 0;
            for (Bet bet : bets) {
                totalBetAmount += bet.getAmount();
            }

            if (bets.isEmpty()) {
                Toast.makeText(this, "Bạn chưa chọn ngựa nào!", Toast.LENGTH_SHORT).show();
            } else if (totalBetAmount > currentBalance) {
                Toast.makeText(this, "Tổng tiền cược không thể lớn hơn số dư!", Toast.LENGTH_SHORT).show();
            } else if (totalBetAmount == 0) {
                Toast.makeText(this, "Bạn phải đặt cược số tiền lớn hơn 0!", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent raceIntent = new Intent(BetActivity.this, RaceActivity.class);
                raceIntent.putExtra("playerBets", new ArrayList<>(bets)); // List<Bet> cần ArrayList
                raceIntent.putExtra("CURRENT_BALANCE", currentBalance); // Gửi số dư sang RaceActivity
                startActivity(raceIntent);
            }
        });
    }

    private void setHorseNamesOnCheckBoxes() {
        if (horseChecks.length == horseNames.length) {
            for (int i = 0; i < horseChecks.length; i++) {
                horseChecks[i].setText(horseNames[i]);
            }
        }
    }
    private List<Bet> getSelectedBets() {
        List<Bet> bets = new ArrayList<>();

        for (int i = 0; i < horseChecks.length; i++) {
            if (horseChecks[i].isChecked()) {
                int amount = parseAmount(horseBets[i].getText().toString());
                bets.add(new Bet(i , horseNames[i], amount));
            }
        }
        return bets;
    }

    private int parseAmount(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateBalanceUI() {
        if (tvCurrentBalance != null) {
            tvCurrentBalance.setText("Số dư: " + formatCurrency(currentBalance));
        }
    }

    private String formatCurrency(double amount) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        return currencyFormatter.format(amount);
    }
}
