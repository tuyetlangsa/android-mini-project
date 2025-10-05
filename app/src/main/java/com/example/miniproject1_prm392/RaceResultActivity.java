package com.example.miniproject1_prm392;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RaceResultActivity extends AppCompatActivity {

    private String[] horseNames = {
            "🐴 Red Thunderss",
            "🐎 Blue Lightning",
            "🐴 Green Storm",
            "🐎 Golden Wind",
            "🐴 Purple Dash"
    };


    private double finalBalance; // Biến lưu số dư cuối cùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_result);

        // Lấy tất cả dữ liệu từ Intent
        Intent intent = getIntent();
        ArrayList<Integer> finishedHorses = intent.getIntegerArrayListExtra("finishedHorses");
        ArrayList<Bet> playerBets = (ArrayList<Bet>) intent.getSerializableExtra("playerBets");
        double initialBalance = intent.getDoubleExtra("CURRENT_BALANCE", 0.0);

        // Validate dữ liệu
        if (finishedHorses != null && !finishedHorses.isEmpty() && playerBets != null) {
            displayRaceStandings(finishedHorses);
            calculateAndUpdateBalance(finishedHorses, playerBets, initialBalance);
        } else {
            // Xử lý lỗi nếu không nhận được dữ liệu
            TextView tvResultStatus = findViewById(R.id.tvResultStatus);
            tvResultStatus.setText("Lỗi: Không có dữ liệu cuộc đua");
        }

        // Thiết lập nút quay về trang chủ
        Button btnReturnHome = findViewById(R.id.btnReturnHome);
        btnReturnHome.setOnClickListener(v -> {
            // Tạo intent để quay về HomePageActivity
            Intent returnIntent = new Intent(RaceResultActivity.this, HomePageActivity.class);

            // Các cờ này giúp xóa các Activity trung gian (Bet, Race) và cập nhật HomePageActivity đã có
            returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            // Gửi số dư cuối cùng về
            returnIntent.putExtra("UPDATED_BALANCE", finalBalance);
            startActivity(returnIntent);
            finish(); // Đóng Activity hiện tại
        });
    }

    /**
     * Hiển thị bảng xếp hạng cuộc đua
     */
    private void displayRaceStandings(ArrayList<Integer> finishedHorses) {
        int[] nameIds = {R.id.tvHorse1Name, R.id.tvHorse2Name, R.id.tvHorse3Name, R.id.tvHorse4Name, R.id.tvHorse5Name};
        int[] timeIds = {R.id.tvHorse1Time, R.id.tvHorse2Time, R.id.tvHorse3Time, R.id.tvHorse4Time, R.id.tvHorse5Time};

        for (int position = 0; position < finishedHorses.size(); position++) {
            int horseIndex = finishedHorses.get(position);
            TextView nameView = findViewById(nameIds[position]);
            TextView timeView = findViewById(timeIds[position]);

            if (nameView != null && horseIndex >= 0 && horseIndex < horseNames.length) {
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
                double baseTime = 45.0;
                double timeVariation = position * 1.5 + Math.random() * 0.5;
                double finalTime = baseTime + timeVariation;
                timeView.setText(String.format("Time: %.2fs", finalTime));
            }
        }
    }

    /**
     * Tính toán số dư mới, cập nhật UI và hiển thị thông báo thắng/thua.
     */
    private void calculateAndUpdateBalance(ArrayList<Integer> finishedHorses, ArrayList<Bet> playerBets, double initialBalance) {
        int winnerIndex = finishedHorses.get(0);
        double totalWinnings = 0;
        double totalLosses = 0;
        int winRate = 2; // Tỷ lệ thắng (1 ăn 2)

        // Duyệt qua các cược của người chơi
        for (Bet bet : playerBets) {
            if (bet.getHorseIndex() == winnerIndex) {
                // Thắng cược
                totalWinnings += bet.getAmount() * winRate;
            } else {
                // Thua cược
                totalLosses += bet.getAmount();
            }
        }

        // Số dư ban đầu trừ đi tổng số tiền đã cược, sau đó cộng lại tiền thắng
        finalBalance = initialBalance - totalLosses + totalWinnings;

        // Cập nhật giao diện
        updateBalanceUI(finalBalance);
        displayResultCard(totalWinnings, totalLosses, horseNames[winnerIndex]);
    }

    /**
     * Hiển thị thông báo tổng kết thắng/thua trên CardView.
     */
    private void displayResultCard(double winnings, double losses, String winnerName) {
        CardView resultCard = findViewById(R.id.resultCard);
        TextView tvResultStatus = findViewById(R.id.tvResultStatus);
        TextView tvResultMessage = findViewById(R.id.tvResultMessage);
        TextView tvResultIcon = findViewById(R.id.tvResultIcon);

        double netResult = winnings - losses;

        if (netResult > 0) {
            // Người chơi lời tiền
            resultCard.setCardBackgroundColor(Color.parseColor("#4CAF50")); // Green
            tvResultIcon.setText("🎉");
            tvResultStatus.setText("BẠN THẮNG!");
            tvResultMessage.setText("Bạn lời được " + formatCurrency(netResult));
        } else if (netResult < 0) {
            // Người chơi lỗ tiền
            resultCard.setCardBackgroundColor(Color.parseColor("#F44336")); // Red
            tvResultIcon.setText("😢");
            tvResultStatus.setText("BẠN THUA!");
            tvResultMessage.setText("Bạn lỗ " + formatCurrency(Math.abs(netResult)));
        } else {
            // Người chơi hòa vốn
            resultCard.setCardBackgroundColor(Color.parseColor("#FF9800")); // Orange
            tvResultIcon.setText("😐");
            tvResultStatus.setText("HÒA VỐN!");
            tvResultMessage.setText(winnerName + " đã thắng, nhưng bạn không mất tiền.");
        }
    }

    /**
     * Cập nhật TextView hiển thị số dư
     */
    private void updateBalanceUI(double newBalance) {
        TextView tvBalance = findViewById(R.id.tvBalance);
        if (tvBalance != null) {
            tvBalance.setText("Số dư mới: " + formatCurrency(newBalance));
        }
    }

    /**
     * Định dạng số thành chuỗi tiền tệ
     */
    private String formatCurrency(double amount) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        return currencyFormatter.format(amount);
    }
}
