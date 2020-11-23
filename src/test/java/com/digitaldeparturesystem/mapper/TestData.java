package com.digitaldeparturesystem.mapper;

import java.util.Calendar;

public class TestData {
    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar instance = Calendar.getInstance();
        instance.set(2090,11,1);
        long timeInMillis = instance.getTimeInMillis();
        System.out.println("timeInMillis.length ==> " + String.valueOf(timeInMillis).length());
        System.out.println(currentTimeMillis);
    }
}
