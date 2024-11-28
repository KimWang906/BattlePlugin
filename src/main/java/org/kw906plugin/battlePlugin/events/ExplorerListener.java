package org.kw906plugin.battlePlugin.events;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
import org.kw906plugin.battlePlugin.player.TeamManager;
import org.kw906plugin.battlePlugin.prepared_ability.AbilityManager;
import org.kw906plugin.battlePlugin.prepared_ability.ExplorerAbility;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;
import static org.kw906plugin.battlePlugin.prepared_ability.ExplorerAbility.abilityUsedMap;
import static org.kw906plugin.battlePlugin.prepared_ability.ExplorerAbility.itemToBuffMap;

public class ExplorerListener implements Listener {
    private final int strengthAmplifier = config.explorerAbilityConfig.strengthAmplifier;
    private final int hasteAmplifier = config.explorerAbilityConfig.hasteAmplifier;
    private final int regenerationAmplifier = config.explorerAbilityConfig.regenerationAmplifier;
    private final int absorptionAmplifier = config.explorerAbilityConfig.absorptionAmplifier;
    private final int bonusScore = config.explorerAbilityConfig.bonusScore;

    public ExplorerListener(BattlePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void applyBuffToAllies(Player player, String buffType) {
        BattlePlayer battlePlayer = AbilityManager.findPlayer(player);
        if (battlePlayer == null) return;
        int currentTeam = battlePlayer.getTeamIndex();
        SendMessage.logConsole("Current team: " + currentTeam);
        SendMessage.logConsole("Buff type: " + buffType);
        SendMessage.logConsole("Requested Player: " + battlePlayer.getPlayer());

        if (buffType.equals("ScoreBoost")) {
            TeamManager.addTeamScore(battlePlayer.getTeamIndex(), bonusScore);
        }

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
                item.setAmount(Math.max(item.getAmount() - 1, 0));
                event.setCancelled(true);
            }
        }
    }
}
