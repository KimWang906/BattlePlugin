package org.kw906plugin.battlePlugin.prepared_ability;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.SendMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class SnowballAbility extends Ability implements Listener {
    private final int potionDuration = config.snowballAbilityConfig.potionDuration;
    private final int slownessAmplifier = config.snowballAbilityConfig.slownessAmplifier;
    private final int miningFatigueAmplifier = config.snowballAbilityConfig.miningFatigueAmplifier;
    private final long additionalDamage = config.snowballAbilityConfig.additionalDamage;
    private final int throwSpeed = config.snowballAbilityConfig.throwSpeed;
    private final Set<UUID> frozenPlayers = new HashSet<>();

    public SnowballAbility() {
        setName("눈덩이");
        setDescription("상대에게 눈덩이 적중 시 동상 효과와 슬로우 효과를 부여합니다. 동일한 상대에게 적중 시 동상 상태가 해제되면서 추가 데미지를 입힙니다.");
        addRequiredItems(new ItemStack(Material.SNOWBALL));
    }

    @EventHandler
    public void onPlayerThrowSnowball(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (hasAbility(player, SnowballAbility.class)) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.SNOWBALL) {
                Snowball snowball = player.launchProjectile(Snowball.class);
                Vector direction = player.getEyeLocation().getDirection().normalize().multiply(throwSpeed);
                snowball.setVelocity(direction);
                item.setAmount(item.getAmount() - 1);
            }
        }
    }

    // 눈덩이에 맞을 때 효과
    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasAbility(player, SnowballAbility.class)) {
                if (event.getDamager() instanceof Snowball) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, potionDuration, slownessAmplifier));  // 슬로우 효과
                    player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, potionDuration, miningFatigueAmplifier)); // 채굴 속도 감소
                    frozenPlayers.add(player.getUniqueId());
                    SendMessage.sendMessagePlayer(player, Component.text("당신은 동상에 걸렸습니다!").color(NamedTextColor.AQUA));
                }
            }
        }
    }

    // 화염구에 맞을 때 효과
    @EventHandler
    public void onFireballHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Fireball) {
            Entity hitEntity = event.getEntity();
            if (hitEntity instanceof Player player) {
                if (frozenPlayers.contains(player.getUniqueId())) {
                    event.setDamage(event.getDamage() + additionalDamage); // 추가 데미지 5 적용
                    frozenPlayers.remove(player.getUniqueId()); // 동상 해제
                }
            }
        }
    }
}
