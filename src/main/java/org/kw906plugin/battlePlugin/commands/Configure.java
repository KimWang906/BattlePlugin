package org.kw906plugin.battlePlugin.commands;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.kw906plugin.battlePlugin.BattlePlugin;

import static org.bukkit.Bukkit.getWorld;
import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class Configure {
    public long noPVPCount;
    public long maxScore;
    public long addPoints;

    WorldConfig worldConfig;
    WorldBorderConfig worldBorderConfig;

    // Ability 설정
    public AxeAbilityConfig axeAbilityConfig;
    public TridentAbilityConfig tridentAbilityConfig;
    public ArrowAbilityConfig arrowAbilityConfig;
    public StickAbilityConfig stickAbilityConfig;
    public FistAbilityConfig fistAbilityConfig;
    public CrossbowAbilityConfig crossbowAbilityConfig;
    public MaceAbilityConfig maceAbilityConfig;
    public FishingRodAbilityConfig fishingRodAbilityConfig;
    public LighterAbilityConfig lighterAbilityConfig;
    public ExplorerAbilityConfig explorerAbilityConfig;
    public WizardAbilityConfig wizardAbilityConfig;

    private FileConfiguration config = getPlugin(BattlePlugin.class).getConfig();

    public Configure() {
        getPlugin(BattlePlugin.class).saveDefaultConfig();

        // 설정 값 로드
        noPVPCount = config.getLong("no_pvp_count", 10);
        maxScore = config.getLong("max_score", 50);
        addPoints = config.getLong("add_points", 1);

        // World 설정 로드
        worldConfig = new WorldConfig(config);

        // WorldBorder 설정 로드
        worldBorderConfig = new WorldBorderConfig(config);

        // 각 능력별 설정 로드
        axeAbilityConfig = new AxeAbilityConfig(config);
        tridentAbilityConfig = new TridentAbilityConfig(config);
        arrowAbilityConfig = new ArrowAbilityConfig(config);
        stickAbilityConfig = new StickAbilityConfig(config);
        fistAbilityConfig = new FistAbilityConfig(config);
        crossbowAbilityConfig = new CrossbowAbilityConfig(config);
        maceAbilityConfig = new MaceAbilityConfig(config);
        fishingRodAbilityConfig = new FishingRodAbilityConfig(config);
        lighterAbilityConfig = new LighterAbilityConfig(config);
        explorerAbilityConfig = new ExplorerAbilityConfig(config);
        wizardAbilityConfig = new WizardAbilityConfig(config);
        crossbowAbilityConfig = new CrossbowAbilityConfig(config);

        // 최종 설정 저장
        saveAllConfigs();
    }

    // World 설정을 위한 하위 클래스
    public static class WorldConfig {
        public String worldName;
        public String netherName;
        public String endName;

        private final World overworld;
        private final World nether;
        private final World theEnd;

        public WorldConfig(FileConfiguration config) {
            overworld = getWorld(config.getString("worlds.world_name", "world"));
            nether = getWorld(config.getString("worlds.nether_name", "world_nether"));
            theEnd = getWorld(config.getString("worlds.end_name", "world_the_end"));
        }

        // World 관련 Getter 메소드
        public World getOverworld() {
            return overworld;
        }

        public World getNether() {
            return nether;
        }

        public World getTheEnd() {
            return theEnd;
        }

        public void saveWorldConfig(FileConfiguration config) {
            config.set("worlds.world_name", worldName);
            config.set("worlds.nether_name", netherName);
            config.set("worlds.end_name", endName);
        }
    }

    // WorldBorder 설정을 위한 하위 클래스
    public static class WorldBorderConfig {
        public int overworldWorldBorderSize;
        public int netherWorldBorderSize;
        public int endWorldBorderSize;

        public WorldBorderConfig(FileConfiguration config) {
            overworldWorldBorderSize = config.getInt("worldborder.overworld.worldborder_size", 50000);
            netherWorldBorderSize = config.getInt("worldborder.nether.worldborder_size", 12500);
            endWorldBorderSize = config.getInt("worldborder.the-end.worldborder_size", 3000);
        }

        // WorldBorder 관련 Getter 메소드
        public int getOverworldWorldBorderSize() {
            return overworldWorldBorderSize;
        }

        public int getNetherWorldBorderSize() {
            return netherWorldBorderSize;
        }

        public int getEndWorldBorderSize() {
            return endWorldBorderSize;
        }

        public void saveWorldBorderConfig(FileConfiguration config) {
            config.set("worldborder.overworld.world_border_size", overworldWorldBorderSize);
            config.set("worldborder.nether.world_border_size", netherWorldBorderSize);
            config.set("worldborder.end.world_border_size", endWorldBorderSize);
        }
    }

    // Ability 설정
    public static class AbilityConfig {
        protected FileConfiguration config;
        public AbilityConfig(FileConfiguration config) {
            this.config = config;
        }
    }

    // Ability 설정들을 그대로 유지
    public static class AxeAbilityConfig extends AbilityConfig {
        public double healPercentage;
        public long cooldown;

        public AxeAbilityConfig(FileConfiguration config) {
            super(config);
            this.healPercentage = config.getDouble("ability.axe.heal_percentage", 0.3);
            this.cooldown = config.getLong("ability.axe.axe_cooldown", 1);
        }

        public void saveAbilityConfig(FileConfiguration config) {
            config.set("ability.axe.heal_percentage", healPercentage);
            config.set("ability.axe.axe_cooldown", cooldown);
        }
    }

    public static class TridentAbilityConfig extends AbilityConfig {
        public int maxHealth;
        public double waterSubmergedMiningSpeed;
        public double waterMovementEfficiency;
        public double oxygenBonus;
        public int saturationAmplifier;

        public TridentAbilityConfig(FileConfiguration config) {
            super(config);
            this.maxHealth = config.getInt("ability.trident.trident_max_health", 40);
            this.waterSubmergedMiningSpeed = config.getDouble("ability.trident.water_submerged_mining_speed", 1.0);
            this.waterMovementEfficiency = config.getDouble("ability.trident.water_movement_efficiency", 1.0);
            this.oxygenBonus = config.getDouble("ability.trident.oxygen_bonus", 1024);
            this.saturationAmplifier = config.getInt("ability.trident.saturationAmplifier", 1);
        }

        public void saveAbilityConfig(FileConfiguration config) {
            config.set("ability.trident.max_health", maxHealth);
            config.set("ability.trident.water_submerged_mining_speed", waterSubmergedMiningSpeed);
            config.set("ability.trident.water_movement_efficiency", waterMovementEfficiency);
            config.set("ability.trident.water_movement_bonus", oxygenBonus);
            config.set("ability.trident.saturationAmplifier", saturationAmplifier);
        }
    }

    public static class ArrowAbilityConfig extends AbilityConfig {
        public double speed;

        public ArrowAbilityConfig(FileConfiguration config) {
            super(config);
            this.speed = config.getDouble("ability.arrow.speed", 0.18);
        }

        public void saveAbilityConfig(FileConfiguration config) {
            config.set("ability.arrow.speed", speed);
        }
    }

    public static class StickAbilityConfig extends AbilityConfig {
        public double damageMultiplier;
        public double deathDamageReduction;
        public double maxDamage;
        public long damageIncrementThreshold;
        public long maxAccumulatedDamage;

        public StickAbilityConfig(FileConfiguration config) {
            super(config);
            this.damageMultiplier = config.getDouble("ability.stick.damage_multiplier", 0.2);
            this.deathDamageReduction = config.getDouble("ability.stick.death_damage_reduction", 0.3);
            this.maxDamage = config.getDouble("ability.stick.max_damage", 20.0);
            this.damageIncrementThreshold = config.getLong("ability.stick.damage_increment_threshold", 10000);
            this.maxAccumulatedDamage = config.getLong("ability.stick.max_accumulated_damage", 190000);
        }

        public void saveAbilityConfig(FileConfiguration config) {
            config.set("ability.stick.damage_multiplier", damageMultiplier);
            config.set("ability.stick.death_damage_reduction", deathDamageReduction);
            config.set("ability.stick.max_damage", maxDamage);
            config.set("ability.stick.damage_increment_threshold", damageIncrementThreshold);
            config.set("ability.stick.max_accumulated_damage", maxAccumulatedDamage);
            config.set("ability.stick.max_accumulated_damage", maxAccumulatedDamage);
        }
    }

    public static class FistAbilityConfig extends AbilityConfig {
        public double baseHealth;
        public double baseAttackDamage;
        public double increaseHealth;
        public long maximumHealth;

        public FistAbilityConfig(FileConfiguration config) {
            super(config);
            this.baseHealth = config.getDouble("ability.fist.base_health", 10);
            this.baseAttackDamage = config.getDouble("ability.fist.base_attack_damage", 7);
            this.increaseHealth = config.getDouble("ability.fist.increase_health", 2);
            this.maximumHealth = config.getLong("ability.fist.maximum_health", 60);
        }

        public void saveAbilityConfig(FileConfiguration config) {
            config.set("ability.fist.base_health", baseHealth);
            config.set("ability.fist.base_attack_damage", baseAttackDamage);
            config.set("ability.fist.increase_health", increaseHealth);
        }
    }

    public static class CrossbowAbilityConfig extends AbilityConfig {
        public double crossbowSpeed;
        public int speedDuration;
        public int invisibilityDuration;
        public int speedAmplifier;

        public CrossbowAbilityConfig(FileConfiguration config) {
            super(config);
            crossbowSpeed = config.getDouble("ability.crossbow.speed", 0.12);
            speedDuration = config.getInt("ability.crossbow.speed_duration", 60);
            invisibilityDuration = config.getInt("ability.crossbow.invisibility_duration", 60);
            speedAmplifier = config.getInt("ability.crossbow.speed_amplifier", 1);
        }

        public void saveAbilityConfig(FileConfiguration config) {
            config.set("ability.crossbow.speed", crossbowSpeed);
            config.set("ability.crossbow.speed_duration", speedDuration);
            config.set("ability.crossbow.invisibility_duration", invisibilityDuration);
            config.set("ability.crossbow.speed_amplifier", speedAmplifier);
        }
    }

    public static class MaceAbilityConfig extends AbilityConfig {
        public double jumpBoostMultiplier;
        public MaceAbilityConfig(FileConfiguration config) {
            super(config);
            jumpBoostMultiplier = config.getDouble("ability.mace.jump_boost_multiplier", 1.2);
        }

        public void saveAbilityConfig(FileConfiguration config) {
            config.set("ability.mace.jump_boost_multiplier", jumpBoostMultiplier);
        }
    }

    public static class FishingRodAbilityConfig extends AbilityConfig {
        public int diamondAmount;
        public double luckBaseValue;

        public FishingRodAbilityConfig(FileConfiguration config) {
            super(config);
            diamondAmount = config.getInt("ability.fishing.diamond_amount", 1);
            luckBaseValue = config.getDouble("ability.fishing.luck_base_value", 1024);
        }

        public void saveAbilityConfig(FileConfiguration config) {
            config.set("ability.fishing.diamond_amount", diamondAmount);
            config.set("ability.fishing.luck_base_value", luckBaseValue);
        }
    }

    public static class LighterAbilityConfig extends AbilityConfig {
        public int diamondAmount;
        public LighterAbilityConfig(FileConfiguration config) {
            super(config);
            diamondAmount = config.getInt("ability.lighter.diamond_amount", 1);
        }

        public void saveAbilityConfig(FileConfiguration config) {
            config.set("ability.lighter.diamond_amount", diamondAmount);
        }
    }

    public static class ExplorerAbilityConfig extends AbilityConfig {
        public final int strengthAmplifier;
        public final int hasteAmplifier;
        public final int regenerationAmplifier;
        public final int additionalHealth;
        public final int bonusScore;
        public final double fallingDamage;

        public ExplorerAbilityConfig(FileConfiguration config) {
            super(config);
            strengthAmplifier = config.getInt("ability.explorer.strength_amplifier", 1);
            hasteAmplifier = config.getInt("ability.explorer.haste_amplifier", 0);
            regenerationAmplifier = config.getInt("ability.explorer.regeneration_amplifier", 1);
            additionalHealth = config.getInt("ability.explorer.additionalHealth", 10);
            fallingDamage = config.getDouble("ability.explorer.falling_damage", 0.3);
            bonusScore = config.getInt("ability.explorer.bonus_score", 10);
        }

        public void saveAbilityConfig(FileConfiguration config) {
            config.set("ability.explorer.strength_amplifier", strengthAmplifier);
            config.set("ability.explorer.speed_amplifier", hasteAmplifier);
            config.set("ability.explorer.regeneration_amplifier", regenerationAmplifier);
            config.set("ability.explorer.additionalHealth", additionalHealth);
            config.set("ability.explorer.falling_damage", fallingDamage);
            config.set("ability.explorer.bonus_score", bonusScore);
        }
    }

    public static class WizardAbilityConfig extends AbilityConfig {
        public int poisonDuration;
        public int poisonAmplifier;
        public int instantDamageAmplifier;

        public WizardAbilityConfig(FileConfiguration config) {
            super(config);
            poisonDuration = config.getInt("ability.wizard.poison_duration", 20);
            poisonAmplifier = config.getInt("ability.wizard.poison_amplifier", 0);
            instantDamageAmplifier = config.getInt("ability.wizard.instant_damage_amplifier", 1);
        }

        public void saveAbilityConfig(FileConfiguration config) {
            config.set("ability.wizard.poison_duration", poisonDuration);
            config.set("ability.wizard.poison_amplifier", poisonAmplifier);
            config.set("ability.wizard.instant_damage_amplifier", instantDamageAmplifier);
        }
    }

    // Getter Methods
    public WorldConfig getWorldConfig() {
        return worldConfig;
    }

    public WorldBorderConfig getWorldBorderConfig() {
        return worldBorderConfig;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    public void saveAllConfigs() {
        config.set("no_pvp_count", noPVPCount);
        worldConfig.saveWorldConfig(config);
        worldBorderConfig.saveWorldBorderConfig(config);
        axeAbilityConfig.saveAbilityConfig(config);
        tridentAbilityConfig.saveAbilityConfig(config);
        arrowAbilityConfig.saveAbilityConfig(config);
        stickAbilityConfig.saveAbilityConfig(config);
        fistAbilityConfig.saveAbilityConfig(config);
        crossbowAbilityConfig.saveAbilityConfig(config);
        maceAbilityConfig.saveAbilityConfig(config);
        fishingRodAbilityConfig.saveAbilityConfig(config);
        lighterAbilityConfig.saveAbilityConfig(config);
        explorerAbilityConfig.saveAbilityConfig(config);
        wizardAbilityConfig.saveAbilityConfig(config);

        getPlugin(BattlePlugin.class).saveConfig();
    }
}
