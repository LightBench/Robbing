package com.frahhs.robbing.feature.safe;

import com.frahhs.robbing.Robbing;
import com.frahhs.robbing.block.RobbingBlock;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SafePin {
    private final List<Integer> pin;
    private final RobbingBlock safe;

    public SafePin(RobbingBlock safe) {
        this.safe = safe;

        PersistentDataContainer container = safe.getArmorStand().getPersistentDataContainer();
        NamespacedKey pinKey = new NamespacedKey(Robbing.getInstance(), "pin");

        String pinString;
        if(container.has(pinKey, PersistentDataType.STRING))
            pinString = container.get(pinKey, PersistentDataType.STRING);
        else {
            Random random = new Random();
            pinString = String.format("%04d", random.nextInt(10000));
            container.set(pinKey, PersistentDataType.STRING, pinString);
        }


        this.pin = new ArrayList<>();
        assert pinString != null;
        for(char digit : pinString.toCharArray()) {
            this.pin.add(Integer.parseInt(String.valueOf(digit)));
        }
    }

    public RobbingBlock getSafe() {
        return safe;
    }

    public List<Integer> getDigits() {
        return pin;
    }

    public String toString() {
        return String.valueOf(pin.get(0)) + pin.get(1) + pin.get(2) + pin.get(3);
    }
}
