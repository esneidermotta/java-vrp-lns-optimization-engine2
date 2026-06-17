package com.esneider.vrp.model;

public record TimeWindow(int startMinute, int endMinute) {
    public boolean contains(int minute) {
        return minute >= startMinute && minute <= endMinute;
    }

    public int waitUntilOpen(int arrivalMinute) {
        return Math.max(arrivalMinute, startMinute);
    }
}