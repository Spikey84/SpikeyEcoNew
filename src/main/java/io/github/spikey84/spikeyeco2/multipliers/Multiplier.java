package io.github.spikey84.spikeyeco2.multipliers;

import java.sql.Time;
import java.util.UUID;

public class Multiplier {
    private UUID uuid;
    private double multiplier;
    private long time;
    private int ID;

    public Multiplier(UUID uuid, double multiplier, long time) {
        this.uuid = uuid;
        this.multiplier = multiplier;
        this.time = time;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public long getTime() {
        return time;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

//    public String getTimeFormatted() {
//        StringBuilder tmp = "";
//        if(time >= 3600) {
//            tmp.append(String.format("%s Hours"))
//        }
//    }
}
