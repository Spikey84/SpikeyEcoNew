package io.github.spikey84.spikeyeco2.targets;

import io.github.spikey84.spikeyeco2.EcoManager;
import io.github.spikey84.spikeyeco2.targets.types.FarmTarget;
import io.github.spikey84.spikeyeco2.targets.types.MineTarget;
import io.github.spikey84.spikeyeco2.targets.types.MobKillTarget;
import io.github.spikey84.spikeyeco2.targets.types.SmeltTarget;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum  TargetType {
    MINE((byte) 0, MineTarget.class),
    KILL((byte) 1, MobKillTarget.class),
    SMELT((byte) 2, SmeltTarget.class),
    FARM((byte) 3, FarmTarget.class);

    private byte id;
    private Class<?> c;

    TargetType(byte id, Class<?> cl) {
        this.id = id;
        this.c =cl;
    }

    public byte getid() {
        return id;
    }

    public Class<?> getTypeClass() {
        return c;
    }

    public static BaseTarget createNew (TargetType targetType, String type, double r, boolean bypass, EcoManager eM) {
        switch (targetType.getid()) {
            case (byte) 0:
                return new MineTarget(Material.valueOf(type), r, bypass, eM);
            case (byte) 1:
                return new MobKillTarget(EntityType.valueOf(type), r, bypass, eM);
            case (byte) 2:
                return new SmeltTarget(Material.valueOf(type), r, eM);
            case (byte) 3:
                return new FarmTarget(Material.valueOf(type), r, bypass, eM);

        }
        return new MobKillTarget(EntityType.SHULKER, 1, false, eM);

    }
}
