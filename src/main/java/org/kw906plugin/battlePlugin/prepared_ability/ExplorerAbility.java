package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
import org.kw906plugin.battlePlugin.player.TeamManager;

import java.util.HashMap;
import java.util.Map;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class ExplorerAbility extends Ability implements Listener {
    private final Map<Material, String> itemToBuffMap = new HashMap<>();
    private final Map<String, Boolean> abilityUsedMap = new HashMap<>();

    private final int strengthAmplifier = config.explorerAbilityConfig.strengthAmplifier;
    private final int hasteAmplifier = config.explorerAbilityConfig.hasteAmplifier;
    private final int regenerationAmplifier = config.explorerAbilityConfig.regenerationAmplifier;
    private final int absorptionAmplifier = config.explorerAbilityConfig.absorptionAmplifier;
    private final double FALLING_DAMAGE = config.explorerAbilityConfig.fallingDamage;

    public ExplorerAbility() {
        // 아이템과 버프의 관계 설정
        itemToBuffMap.put(Material.DRAGON_EGG, "ScoreBoost");
        itemToBuffMap.put(Material.ELYTRA, "NoFallingDamage");
        itemToBuffMap.put(Material.NETHER_STAR, "Absorption");
        itemToBuffMap.put(Material.ENCHANTED_GOLDEN_APPLE, "Regeneration");
        itemToBuffMap.put(Material.HEAVY_CORE, "Strength");
        itemToBuffMap.put(Material.DEEPSLATE_EMERALD_ORE, "Haste");
        itemToBuffMap.put(Material.NETHERITE_BLOCK, "FireResistance");
        itemToBuffMap.put(Material.WATER_BUCKET, "WaterBreathing");

        // 각 능력에 대한 사용 여부 초기화
        abilityUsedMap.put("ScoreBoost", false);
        abilityUsedMap.put("NoFallingDamage", false);
        abilityUsedMap.put("Absorption", false);
        abilityUsedMap.put("Regeneration", false);
        abilityUsedMap.put("Strength", false);
        abilityUsedMap.put("Haste", false);
        abilityUsedMap.put("FireResistance", false);
        abilityUsedMap.put("WaterBreathing", false);
    }

    private void applyBuffToAllies(Player player, String buffType) {
        BattlePlayer battlePlayer = AbilityManager.findPlayer(player);
        if (battlePlayer == null) return;
        int currentTeam = battlePlayer.getTeamIndex();
        for (BattlePlayer p : AbilityManager.getPlayers()) {
            if (p.getTeamIndex() == currentTeam) {
                switch (buffType) {
                    case "WaterBreathing":
                        p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 1));
                        break;
                    case "FireResistance":
                        p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
                        break;
                    case "Haste":
                        p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HASTE, Integer.MAX_VALUE, hasteAmplifier));
                        break;
                    case "Strength":
                        p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, strengthAmplifier));
                        break;
                    case "Regeneration":
                        p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, regenerationAmplifier));
                        break;
                    case "Absorption":
                        p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, absorptionAmplifier));
                        break;
                    case "NoFallingDamage":
                        if (!p.getAbility().getName().equals("철퇴")) {
                            AttributeInstance attr = p.getPlayer().getAttribute(Attribute.FALL_DAMAGE_MULTIPLIER);
                            if (attr != null) {
                                attr.setBaseValue(0.3);
                            }
                        }
                        break;
                    case "ScoreBoost":
                        int currentScore = TeamManager.getTeamScore().get(battlePlayer.getTeamIndex());
                        TeamManager.addTeamScore(currentScore, 10);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (hasAbility(player, ExplorerAbility.class)) {
            String buffType = itemToBuffMap.get(item.getType());
            if (buffType != null && !abilityUsedMap.get(buffType)) {
                applyBuffToAllies(player, buffType);
                abilityUsedMap.put(buffType, true);
            }
            item.setAmount(Math.max(item.getAmount() - 1, 0));
        }
    }
}
