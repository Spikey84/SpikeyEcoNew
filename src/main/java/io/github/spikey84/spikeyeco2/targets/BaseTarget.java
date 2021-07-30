package io.github.spikey84.spikeyeco2.targets;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

public class BaseTarget implements Listener {
    protected double reward;

    public double getReward() {
        return reward;
    }

}
