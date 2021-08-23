package io.github.spikey84.spikeyeco2.challanges;

import com.google.common.collect.Lists;
import io.github.spikey84.spikeyeco2.EcoManager;
import io.github.spikey84.spikeyeco2.challanges.implementation.KillMob;
import io.github.spikey84.spikeyeco2.challanges.implementation.MineOre;
import io.github.spikey84.spikeyeco2.utils.ChatUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ObjectInputFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class ChallangeManager {
    private Plugin plugin;
    public static EcoManager ecoManager;
    public static int counterId;//represents the timer scheduler
    public static ChallengeType currentChallenge;//the type of the current challenge
    public static int chalStartTime = 0;//time the current challenge started
    public static int counter = 0;//the timer in seconds

    public static int requiredPoints = 10;//points required to finish the current goal
    public static int currentPrice = 10;
    public static int minPrice = 10;//current minimum price

    public static ArrayList<ChallengeType> challengeTypes = new ArrayList<ChallengeType>();//initializes the list of all ChallengeTypes to be populated from the config
    public static ArrayList<Challenge> genericChallengeTypes = new ArrayList<Challenge>();//initializes the list of all Classes used in challenges that extend Challenge to be populated as a constant in onEnable


    public static HashMap<UUID, Integer> challengeProgress = new HashMap<UUID, Integer>();//initializes the map used to track the points a player has in the current challenge
    public static ArrayList<UUID> challengeComplete = new ArrayList<UUID>();//initializes the list of players who have completed the current challenge

    public static Long time;
    public static String timeStr;

    public ChallangeManager(FileConfiguration config, Plugin plugin, EcoManager ecoManager) {


        this.ecoManager = ecoManager;
        this.plugin = plugin;

        time = 0L;
        timeStr = DurationFormatUtils.formatDuration(time*1000,"H:mm:ss", true);
        //Adds the challenge types from the config file
        //iterates over all config types

        Bukkit.getPluginManager().registerEvents(new KillMob(), plugin);
        Bukkit.getPluginManager().registerEvents(new MineOre(), plugin);

        for (String key : config.getConfigurationSection("challenges").getKeys(false)) {
            ConfigurationSection challenge = config.getConfigurationSection("challenges." + key);//gets the block for the specific challenge
            challengeTypes.add(new ChallengeType(challenge.getString("type"), key, challenge.getInt("unitPrice"), challenge.getInt("averagePoints"), challenge.getInt("pointsMaxDeviation"), challenge.getLong("time"))); //adds the new type based off the config using the constructor
            if (challenge.getKeys(false).contains("targetEntity"))
                challengeTypes.get(challengeTypes.size() - 1).setTargetEntityString(challenge.getString("targetEntity"));//if a targetEntity is provided this will add it to the challengeType
            if (challenge.getKeys(false).contains("targetMaterial"))
                challengeTypes.get(challengeTypes.size() - 1).setTargetMatString(challenge.getString("targetMaterial"));//if a targetMaterial is provided this will add it to the challengeType
            if (challenge.getKeys(false).contains("others")) {
                List<String> others = challenge.getStringList("others");
                challengeTypes.get(challengeTypes.size() - 1).setOtherNames(others);
            }
        }

        ChallengeType challengeType = getRandomChallangeType();
        challengeStart(challengeType);

        //Main clock running around once a second
        counterId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                //adds one to the timer
                ChallangeManager.counter++;
                //does not run code under this if the current challenge is the blank placeholder challenge
                if (ChallangeManager.currentChallenge == null) return;

                //changes/resets the challenge after a specific amount of time
                if ((ChallangeManager.counter - ChallangeManager.chalStartTime) > ChallangeManager.time) {
                    challengeStart(getRandomChallangeType());
                }
            }
        }, 0, 20);


    }

    public void challengeStart(ChallengeType challengeType) {
        currentChallenge = challengeType;//sets the current challenge to the given type
        requiredPoints = challengeType.getAveragePoints() + (int) (Math.round(((Math.random() * 1000) % (challengeType.getPointsMaxDeviation() * 2)) - challengeType.getPointsMaxDeviation()));//comes up with a semi random amount of points needed to complete the challenge
        currentPrice = challengeType.getUnitPrice() * requiredPoints;//sets the prize for the challenge based off of the points needed and the given unit price
        minPrice = (int) ((int) currentPrice * 0.025);//sets the minimum
        chalStartTime = counter;//logs the time that the challenge started
        challengeComplete = new ArrayList<UUID>();//clears the players who have completed the challenge
        challengeProgress = new HashMap<UUID, Integer>();//clears progress for all players
        time = challengeType.getTime();
        timeStr = DurationFormatUtils.formatDuration(time*1000,"H:mm:ss", true);
        ChatUtils.challengeBroadcast(ChatColor.WHITE + "Starting Challenge " + ChatColor.BLUE + "" + challengeType.getName() + "" + ChatColor.WHITE + " with a reward of " + ChatColor.GREEN + "" + currentPrice + "" + ChatColor.WHITE + " coins! " + String.format("%s %s %s within %s to complete challenge.", currentChallenge.getChallengeClass().getAction(), requiredPoints, currentChallenge.getName(), timeStr));
    }

    public ChallengeType getRandomChallangeType() {
        ChallengeType challengeType = challengeTypes.get((int) ((Math.random() * 1000) % (challengeTypes.size())));
        if (challengeType.getName().equals("None")) return getRandomChallangeType();
        return challengeType;//picks random challenge from all the available challenge types
    }
}