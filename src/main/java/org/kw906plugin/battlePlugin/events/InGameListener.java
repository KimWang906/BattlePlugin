package org.kw906plugin.battlePlugin.events;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.Status;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
import org.kw906plugin.battlePlugin.player.TeamManager;
import org.kw906plugin.battlePlugin.prepared_ability.*;

import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;
import static org.kw906plugin.battlePlugin.prepared_ability.CrossbowAbility.applyBuffToShooter;
import static org.kw906plugin.battlePlugin.prepared_ability.CrossbowAbility.hitPlayers;
import static org.kw906plugin.battlePlugin.prepared_ability.ExplorerAbility.*;
import static org.kw906plugin.battlePlugin.prepared_ability.ExplorerAbility.abilityUsedMap;
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
                    ExplorerAbility.applyBuffToAllies(targetPlayer, buffType);
                }
                abilityUsedMap.put(buffType, true);
                item.setAmount(Math.max(item.getAmount() - 1, 0));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void wizardItemInteract(PlayerInteractEvent event) {
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
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.getShooter() instanceof Player shooter &&
                hasAbility(shooter, CrossbowAbility.class)) {
            if (projectile instanceof Arrow) {
                Entity hitEntity = event.getHitEntity();
                if (hitEntity instanceof Player) {
                    hitPlayers.add((Player) hitEntity);
                }
                if (hitPlayers.size() >= 2) {
                    applyBuffToShooter(shooter);
                    hitPlayers.clear();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerShoot(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().equals(Material.CROSSBOW) && event.getAction().isRightClick()) {
            player.sendMessage("쇠뇌를 발사했습니다!");
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
                    LighterAbility.applyEffect(respawnedPlayer);
                    CrossbowAbility.onDeathCrossbow(respawnedPlayer);
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
            BattlePlayer battlePlayer = AbilityManager.findPlayer(killer);
            if (battlePlayer != null) {
                int teamIndex = battlePlayer.getTeamIndex();
                TeamManager.addTeamScore(teamIndex, 1);
                SendMessage.broadcastMessage(
                        Component.text(teamIndex + "팀이 " + deadPlayer.getName() +
                                               "을 죽이고 " + 1 + "점을 획득하였습니다!"));
            }
        }
    }
}
