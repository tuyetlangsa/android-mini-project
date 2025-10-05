package com.example.miniproject1_prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.NumberFormat;
import java.util.Locale;

public class HomePageActivity extends AppCompatActivity {

    private TextView balanceAmountTextView;
    private EditText amountToDepositEditText;
    private Button depositButton;
    private Button startGameButton;

    private double currentBalance = 1000000; // Số dư khởi tạo, bạn có thể thay đổi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        balanceAmountTextView = findViewById(R.id.balance_amount);
        amountToDepositEditText = findViewById(R.id.amount_to_deposit);
        depositButton = findViewById(R.id.deposit_button);
        startGameButton = findViewById(R.id.start_game_button);

        updateBalanceUI();

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositMoney();
            }
        });

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("UPDATED_BALANCE")) {
            currentBalance = intent.getDoubleExtra("UPDATED_BALANCE", currentBalance);
            updateBalanceUI();
            Toast.makeText(this, "Số dư đã được cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }


    private String formatCurrency(double amount) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        return currencyFormatter.format(amount);
    }


    private void updateBalanceUI() {
        balanceAmountTextView.setText(formatCurrency(currentBalance));
    }


    private void depositMoney() {
        String amountString = amountToDepositEditText.getText().toString();

        if (amountString.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền cần nạp", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amountToDeposit = Double.parseDouble(amountString);
            if (amountToDeposit <= 0) {
                Toast.makeText(this, "Số tiền nạp phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }
            currentBalance += amountToDeposit;

            updateBalanceUI();
            amountToDepositEditText.setText("");
            Toast.makeText(this, "Nạp tiền thành công!", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tiền nhập không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void startGame() {
        Intent intent = new Intent(HomePageActivity.this, BetActivity.class);
        intent.putExtra("CURRENT_BALANCE", currentBalance);
        startActivity(intent);
    }
}
