package com.example.miniproject1_prm392;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BetActivity extends AppCompatActivity {

    private TextView tvBalance;
    private CheckBox cbHorse1, cbHorse2, cbHorse3, cbHorse4, cbHorse5;
    private EditText etBet1, etBet2, etBet3, etBet4, etBet5;
    private Button btnBet;

    private int balance = 1000; // số dư khởi tạo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet); // layout bạn đã gửi

        // Ánh xạ view
        tvBalance = findViewById(R.id.tv_balance);
        cbHorse1 = findViewById(R.id.cb_horse1);
        cbHorse2 = findViewById(R.id.cb_horse2);
        cbHorse3 = findViewById(R.id.cb_horse3);
        cbHorse4 = findViewById(R.id.cb_horse4);
        cbHorse5 = findViewById(R.id.cb_horse5);

        etBet1 = findViewById(R.id.et_bet1);
        etBet2 = findViewById(R.id.et_bet2);
        etBet3 = findViewById(R.id.et_bet3);
        etBet4 = findViewById(R.id.et_bet4);
        etBet5 = findViewById(R.id.et_bet5);

        btnBet = findViewById(R.id.btn_bet);

        updateBalanceText();

        btnBet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeBets();
            }
        });
    }

    private void updateBalanceText() {
        tvBalance.setText("Số dư: " + balance + "$");
    }

    private void placeBets() {
        int totalBet = 0;
        StringBuilder betSummary = new StringBuilder();

        if (cbHorse1.isChecked()) {
            totalBet += getBetAmount(etBet1, "Ngựa 1", betSummary);
        }
        if (cbHorse2.isChecked()) {
            totalBet += getBetAmount(etBet2, "Ngựa 2", betSummary);
        }
        if (cbHorse3.isChecked()) {
            totalBet += getBetAmount(etBet3, "Ngựa 3", betSummary);
        }
        if (cbHorse4.isChecked()) {
            totalBet += getBetAmount(etBet4, "Ngựa 4", betSummary);
        }
        if (cbHorse5.isChecked()) {
            totalBet += getBetAmount(etBet5, "Ngựa 5", betSummary);
        }

        if (totalBet == 0) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 ngựa và nhập số tiền!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (totalBet > balance) {
            Toast.makeText(this, "Số dư không đủ!", Toast.LENGTH_SHORT).show();
            return;
        }

        balance -= totalBet;
        updateBalanceText();

        Toast.makeText(this, "Đặt cược thành công!\n" + betSummary.toString(), Toast.LENGTH_LONG).show();

        // Chuyển sang màn hình đua
        Intent intent = new Intent(BetActivity.this, RaceActivity.class);
        intent.putExtra("bets", betSummary.toString());
        startActivity(intent);
    }

    private int getBetAmount(EditText editText, String horseName, StringBuilder summary) {
        String input = editText.getText().toString().trim();
        if (input.isEmpty()) return 0;
        int amount = Integer.parseInt(input);
        if (amount > 0) {
            summary.append(horseName).append(": ").append(amount).append("$\n");
        }
        return amount;
    }
}
