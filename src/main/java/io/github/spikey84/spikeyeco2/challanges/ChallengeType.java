package io.github.spikey84.spikeyeco2.challanges;

import com.google.common.collect.Lists;
import io.github.spikey84.spikeyeco2.challanges.implementation.KillMob;
import io.github.spikey84.spikeyeco2.challanges.implementation.MineOre;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.List;


public  class ChallengeType {
    private Challenge challengeClass;
    private String name;
    private int unitPrice;
    private int averagePoints;
    private int pointsMaxDeviation;
    private long time;
    private List<String> otherNames;

    private Material targetMat = Material.DIRT;
    private EntityType targetEntity = EntityType.ZOMBIE;

    public ChallengeType(String challengeType, String Name, int UnitPrice, int AveragePoints, int PointsMaxDeviation, long time) {
        if(challengeType.equals("MineOre")) {
            challengeClass = new MineOre();
        } else if(challengeType.equals("KillMob")) {
            challengeClass = new KillMob();
        } else {
            challengeClass = new Challenge();
        }
        name = Name;
        unitPrice = UnitPrice;
        averagePoints = AveragePoints;
        pointsMaxDeviation = PointsMaxDeviation;
        this.time = time;
    }

    public static List<String> challengeClasses() {
        List<String> tmp = Lists.newArrayList();
        tmp.add("MineOre");
        tmp.add("KillMob");
        return tmp;
    }

    public Challenge getChallengeClass() {
        return challengeClass;
    }
    public String getName() {
        return name;
    }
    public int getUnitPrice() {
        return unitPrice;
    }
    public int getAveragePoints() {
        return averagePoints;
    }
    public int getPointsMaxDeviation() {
        return pointsMaxDeviation;
    }
    public Material getTargetMat() {
        return targetMat;
    }
    public void setTargetMat(Material mat) {
        targetMat = mat;
    }

    public long getTime() {
        return time;
    }

    public EntityType getTargetEntity() {
        return targetEntity;
    }
    public void setTargetEntity(EntityType entity) {
        targetEntity = entity;
    }


    public void setTargetMatString(String mat) {
        targetMat = Material.valueOf(mat.toUpperCase());
    }
    public void setTargetEntityString(String entity) {
        targetEntity = EntityType.valueOf(entity.toUpperCase());
    }

    public List<String> getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(List<String> otherNames) {
        this.otherNames = otherNames;
    }
}

/*public enum ChallengeType {
    NONE(0),
    DIAMOND(1),
    REDSTONE(2),
    ZOMBIE(3),
    SKELETON(4)
    ;


    private Challenge challengeClass;
    private String name;
    private int unitPrice;
    private int averagePoints;
    private int pointsMaxDeviation;

    private Material targetMat = Material.DIRT;
    private EntityType targetEntity = EntityType.ZOMBIE;

    private ChallengeType(int chalNum) {
        switch (chalNum) {
            case(0):
                challengeClass = new Challenge();
                name = "None";
                unitPrice = 0;
                averagePoints = 0;
                pointsMaxDeviation = 0;
                break;
            case(1):
                challengeClass = new MineOre();
                targetMat = Material.DIAMOND_ORE;
                name = "Diamond";
                unitPrice = 10;
                averagePoints = 10;
                pointsMaxDeviation = 4;
                break;
            case(2):
                challengeClass = new MineOre();
                targetMat = Material.REDSTONE_ORE;
                name = "Redstone";
                unitPrice = 1;
                averagePoints = 10;
                pointsMaxDeviation = 4;
                break;
            case(3):
                challengeClass = new KillMob();
                targetEntity = EntityType.ZOMBIE;
                name = "Zombie";
                unitPrice = 7;
                averagePoints = 20;
                pointsMaxDeviation = 10;
                break;
            case(4):
                challengeClass = new KillMob();
                targetEntity = EntityType.SKELETON;
                name = "Skeleton";
                unitPrice = 7;
                averagePoints = 20;
                pointsMaxDeviation = 10;
                break;
        }
    }

    public Challenge getChallengeClass() {
        return challengeClass;
    }
    public String getName() {
        return name;
    }
    public int getUnitPrice() {
        return unitPrice;
    }
    public int getAveragePoints() {
        return averagePoints;
    }
    public int getPointsMaxDeviation() {
        return pointsMaxDeviation;
    }
    public Material getTargetMat() {
        return targetMat;
    }
    public EntityType getTargetEntity() {
        return targetEntity;
    }
}*/
