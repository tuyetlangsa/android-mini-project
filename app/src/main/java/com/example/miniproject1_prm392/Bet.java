package com.example.miniproject1_prm392;

import java.io.Serializable;

public class Bet implements Serializable { // Đảm bảo class có implements Serializable
    private int horseIndex;
    private String horseName;
    private int amount;

    public Bet(int horseIndex, String horseName, int amount) {
        this.horseIndex = horseIndex;
        this.horseName = horseName;
        this.amount = amount;
    }

    // GETTER METHODS
    public String getHorseName() {
        return horseName;
    }

    public int getAmount() {
        return amount;
    }

    // THÊM PHƯƠNG THỨC NÀY VÀO
    public int getHorseIndex() {
        return horseIndex;
    }
}
