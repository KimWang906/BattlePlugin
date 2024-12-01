package org.kw906plugin.battlePlugin.events;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.Status;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
import org.kw906plugin.battlePlugin.player.TeamManager;
import org.kw906plugin.battlePlugin.prepared_ability.*;

import java.util.ArrayList;
import java.util.List;

import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;
import static org.kw906plugin.battlePlugin.prepared_ability.CrossbowAbility.*;
import static org.kw906plugin.battlePlugin.prepared_ability.ExplorerAbility.*;
import static org.kw906plugin.battlePlugin.prepared_ability.ExplorerAbility.abilityUsedMap;
import static org.kw906plugin.battlePlugin.prepared_ability.FistAbility.MAX_ALLOWED_HEALTH;
import static org.kw906plugin.battlePlugin.prepared_ability.FistAbility.NETHERITE_INCREASE_HEALTH;
import static org.kw906plugin.battlePlugin.prepared_ability.WizardAbility.*;

public class InGameListener implements Listener {
    public InGameListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void explorerItemInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (hasAbility(player, ExplorerAbility.class)) {
            String buffType = itemToBuffMap.get(item.getType());
            if (buffType != null && !abilityUsedMap.get(buffType)) {
                for (Player targetPlayer : teamPlayers) {
                    applyBuffToAllies(targetPlayer, buffType);
                }
                abilityUsedMap.put(buffType, true);
                item.setAmount(Math.max(item.getAmount() - 1, 0));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void wizardItemInteract(PlayerInteractEvent event) {
        if (!hasAbility(event.getPlayer(), WizardAbility.class)) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.FERMENTED_SPIDER_EYE) {
            return;
        }

        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setMaxStackSize(64);
        if (event.getAction().isRightClick()) {
            meta.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, Integer.MAX_VALUE, instantDamageAmplifier), false);
            meta.setCustomName("투척용 고통 물약");
        } else if (event.getAction().isLeftClick()) {
            meta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 20 * poisonDuration, poisonAmplifier), false);
            meta.setCustomName("투척용 독 물약");
        }
        item.setAmount(item.getAmount() - 1);
        potion.setItemMeta(meta);
        player.getInventory().addItem(potion);
        event.setCancelled(true);
    }

    @EventHandler
    public void wizardPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!hasAbility(player, WizardAbility.class)) return;

        if (event.getNewEffect() != null &&
                event.getNewEffect().getType() == PotionEffectType.POISON) {
            event.setCancelled(true);
            SendMessage.logConsole("마법사: 독 효과가 무효화 되었습니다.");
        }
    }

    @EventHandler
    public void wizardEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!hasAbility(player, WizardAbility.class)) return;

        Entity damager = event.getDamager();

        if (damager instanceof ThrownPotion potion) {
            if (potion.getEffects().stream().anyMatch(effect -> effect.getType().equals(PotionEffectType.INSTANT_DAMAGE))) {
                event.setCancelled(true);
            }
        }

        if (damager instanceof AreaEffectCloud cloud) {
            List<PotionEffect> potionEffectTypes = new ArrayList<>();
            if (cloud.getBasePotionType() != null) {
                potionEffectTypes = cloud.getBasePotionType().getPotionEffects();
            }
            for (PotionEffect effect : potionEffectTypes) {
                if (effect.getType().equals(PotionEffectType.INSTANT_DAMAGE)) {
                    SendMessage.logConsole("마법사: 고통의 포션 효과가 제거됨");
                    event.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    public void fistItemInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (hasAbility(player, FistAbility.class)) {
            ItemStack item = event.getItem();
            if (item != null && item.getType() == Material.NETHERITE_INGOT) {
                AttributeInstance attr = player.getAttribute(Attribute.MAX_HEALTH);
                if (attr != null) {
                    double newHealth = Math.min(attr.getBaseValue() + NETHERITE_INCREASE_HEALTH, MAX_ALLOWED_HEALTH);
                    attr.setBaseValue(newHealth);
                    item.setAmount(item.getAmount() - 1);
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow arrow &&
                arrow.getShooter() instanceof Player player &&
                hasAbility(player, CrossbowAbility.class)) {
            if (player.getInventory().getItemInMainHand().getType() == Material.CROSSBOW) {
                ItemStack crossbow = player.getInventory().getItemInMainHand();
                if (crossbow.containsEnchantment(Enchantment.MULTISHOT)) {
                    multishotArrows.add(arrow.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow arrow &&
                hasAbility((Player) arrow.getShooter(), CrossbowAbility.class)) {
            if (multishotArrows.contains(arrow.getUniqueId())) {
                Entity victim = event.getEntity();
                hitPlayers.add((Player) victim);
                SendMessage.logConsole("맞은 사람: " + victim);
                multishotArrows.remove(arrow.getUniqueId());
            }
        }
    }


    // Global
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player respawnedPlayer = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Status.isRunning()) {
                    explorerRestorePlayerState(respawnedPlayer);
                    LighterAbility.applyEffect();
                    if (hasAbility(respawnedPlayer, FistAbility.class)) {
                        FistAbility.applyEffect(respawnedPlayer);
                    }
                }
            }
        }.runTaskLater(BattlePlugin.getPlugin(BattlePlugin.class), 20L);
    }

    // Global
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!Status.isRunning())
            return;

        Player deadPlayer = event.getEntity();
        Player killer = deadPlayer.getKiller();

        if (killer != null) {
            BattlePlayer battlePlayerKiller = AbilityManager.findPlayer(killer);
            if (battlePlayerKiller != null) {
                int teamIndex = battlePlayerKiller.getTeamIndex();
                TeamManager.addTeamScore(teamIndex, 1);
                SendMessage.broadcastMessage(
                        Component.text(teamIndex + "팀이 " + deadPlayer.getName() +
                                               "을 죽이고 " + 1 + "점을 획득하였습니다!"));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!Status.isRunning()) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                for (BattlePlayer battlePlayer : AbilityManager.getPlayers()) {
                    if (battlePlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                        battlePlayer.setPlayer(player);
                        if (TeamManager.getScoreboard() != null) {
                            player.setScoreboard(TeamManager.getScoreboard());
                            Objective objective = TeamManager.getObjective();
                            if (objective != null) {
                                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                            }
                        }
                        break;
                    }
                }
            }
        }.runTaskLater(BattlePlugin.getPlugin(BattlePlugin.class), 40L);
    }
}
