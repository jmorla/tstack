package com.jmorla.tstack.entities;

public enum TimeFrame {
    M1(1),   // 1 minute
    M2(2),   // 2 minutes
    M3(3),   // 3 minutes
    M4(4),   // 4 minutes
    M5(5),   // 5 minutes
    M10(6),  // 10 minutes
    M15(7),  // 15 minutes
    M30(8),  // 30 minutes
    H1(9),   // 1 hour
    H4(10),  // 4 hours
    H12(11), // 12 hours
    D1(12),  // 1 day
    W1(13),  // 1 week
    MN1(14); // 1 month

    private final int value;

    TimeFrame(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
