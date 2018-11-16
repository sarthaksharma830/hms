package com.example.sarthak.hms;

import java.util.Random;

public class Utils {
    public static int getRandomColor() {
        int colors[] = new int[]{R.color.lightBlue, R.color.pink, R.color.yellow, R.color.orange, R.color.green, R.color.red, R.color.violet, R.color.lightRed, R.color.lightGreen};
        Random random = new Random();
        int max = colors.length - 1;
        int min = 0;
        return colors[random.nextInt((max - min) + 1) + min];
    }
}
