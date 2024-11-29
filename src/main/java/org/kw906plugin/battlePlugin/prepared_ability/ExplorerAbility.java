package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.Status;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
import org.kw906plugin.battlePlugin.player.TeamManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;

public class ExplorerAbility extends Ability {
    private static final int strengthAmplifier = config.explorerAbilityConfig.strengthAmplifier;
    private static final int hasteAmplifier = config.explorerAbilityConfig.hasteAmplifier;
    private static final int regenerationAmplifier = config.explorerAbilityConfig.regenerationAmplifier;
    private static final int additionalHealth = config.explorerAbilityConfig.additionalHealth;
    private static final int bonusScore = config.explorerAbilityConfig.bonusScore;

    public static Map<Material, String> itemToBuffMap;
    public static Map<String, Boolean> abilityUsedMap;
    public static ArrayList<Player> teamPlayers;
    public static BattlePlayer master;

    public ExplorerAbility(Player player, int teamIdx) {
        setName("탐험가");
        setDescription("모험을 통해 얻은 각종 아이템을 사용하여 팀원들에게 이로운 효과를 줍니다.");

        itemToBuffMap = new HashMap<>();
        abilityUsedMap = new HashMap<>();

        // 아이템과 버프의 관계 설정
        itemToBuffMap.put(Material.DRAGON_EGG, "ScoreBoost");
        itemToBuffMap.put(Material.ELYTRA, "NoFallingDamage");
        itemToBuffMap.put(Material.NETHER_STAR, "Absorption");
        itemToBuffMap.put(Material.ENCHANTED_GOLDEN_APPLE, "Regeneration");
        itemToBuffMap.put(Material.HEAVY_CORE, "Strength");
        itemToBuffMap.put(Material.DEEPSLATE_EMERALD_ORE, "Haste");
        itemToBuffMap.put(Material.NETHERITE_BLOCK, "FireResistance");
        itemToBuffMap.put(Material.CONDUIT, "WaterBreathing");

        // 각 능력에 대한 사용 여부 초기화
        abilityUsedMap.put("ScoreBoost", false);
        abilityUsedMap.put("NoFallingDamage", false);
        abilityUsedMap.put("Absorption", false);
        abilityUsedMap.put("Regeneration", false);
        abilityUsedMap.put("Strength", false);
        abilityUsedMap.put("Haste", false);
        abilityUsedMap.put("FireResistance", false);
        abilityUsedMap.put("WaterBreathing", false);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Status.isRunning()) {
                    master = AbilityManager.findPlayer(player);
                    teamPlayers = TeamManager.findMyTeams(teamIdx);
                    this.cancel();
                }
            }
        }.runTaskTimer(BattlePlugin.getPlugin(BattlePlugin.class), 0L, 20L);
    }

    public static void applyBuffToAllies(Player player, String buffType) {
        SendMessage.logConsole("Current team: " + master.getTeamIndex());
        SendMessage.logConsole("Buff type: " + buffType);
        SendMessage.logConsole("Requested Player: " + player);

        if (buffType.equals("ScoreBoost") && master.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            TeamManager.addTeamScore(master.getTeamIndex(), bonusScore);
        }

        switch (buffType) {
            case "WaterBreathing":
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 1, false, false));
                SendMessage.logConsole("added water breathing");
                break;
            case "FireResistance":
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
                SendMessage.logConsole("added fire resistance");
                break;
            case "Haste":
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, Integer.MAX_VALUE, hasteAmplifier, false, false));
                SendMessage.logConsole("added haste");
                break;
            case "Strength":
                boolean success = player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, strengthAmplifier, false, false));
                SendMessage.logConsole("added strength" + success);
                break;
            case "Regeneration":
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, regenerationAmplifier, false, false));
                SendMessage.logConsole("added regeneration");
                break;
            case "Absorption":
                AttributeInstance absorptionAttr = player.getAttribute(Attribute.MAX_HEALTH);
                if (absorptionAttr != null) {
                    absorptionAttr.setBaseValue(absorptionAttr.getValue() + additionalHealth);
                }
                break;
            case "NoFallingDamage":
                BattlePlayer targetPlayer = AbilityManager.findPlayer(player);
                if (targetPlayer != null && !targetPlayer.getAbility().getName().equals("철퇴")) {
                    AttributeInstance attr = player.getAttribute(Attribute.FALL_DAMAGE_MULTIPLIER);
                    if (attr != null) {
                        attr.setBaseValue(0.3);
                    }
                }
                break;
            default:
                break;
        }
        SendMessage.logConsole("Player Effects: " + player.getActivePotionEffects());
    }

    public static void explorerRestorePlayerState(Player player) {
        BattlePlayer battlePlayer = AbilityManager.findPlayer(player);
        if (battlePlayer == null || abilityUsedMap == null) return;

        Map<String, Boolean> addBuffs = new HashMap<>(abilityUsedMap);
        addBuffs.remove("NoFallingDamage");
        addBuffs.remove("Absorption");
        addBuffs.remove("ScoreBoost");

        SendMessage.logConsole("addBuffs: " + addBuffs);

        int currentTeam = battlePlayer.getTeamIndex();
        SendMessage.logConsole("Player " + player.getName() + " from team " + currentTeam + " has respawned.");

        for (Player p : teamPlayers) {
            if (!p.equals(player)) return;
            addBuffs.forEach((buffType, isUsed) -> {
                if (isUsed) {
                    SendMessage.logConsole("Player " + p.getName() + " from team " + currentTeam + " has respawned.");
                    applyBuffToAllies(p, buffType);
                }
            });
        }
    }
}
