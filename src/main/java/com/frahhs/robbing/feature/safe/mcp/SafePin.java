package com.frahhs.robbing.feature.safe.mcp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SafePin {
    private final List<Integer> pin;

    public SafePin(String pin) {
        this.pin = new ArrayList<>();

        if(pin.length() < 4) {
            throw new RuntimeException("The pin must be long 4");
        }

        for(char digit : pin.toCharArray()) {
            this.pin.add(Integer.parseInt(String.valueOf(digit)));
        }
    }

    public SafePin(List<Integer> pin) {
        this.pin = pin;
    }

    public static String getRandom() {
        Random random = new Random();

        return String.format("%04d", random.nextInt(10000));
    }

    public List<Integer> getDigits() {
        return pin;
    }

    public String toString() {
        return String.format("%d%d%d%d", pin.get(0), pin.get(1), pin.get(2), pin.get(3));
    }

    public boolean equals(SafePin pin2) {
        return this.getDigits().equals(pin2.getDigits());
    }
}
