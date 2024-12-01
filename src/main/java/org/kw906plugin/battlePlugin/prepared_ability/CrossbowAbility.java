package org.kw906plugin.battlePlugin.prepared_ability;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;

public class CrossbowAbility extends Ability {
    public static Set<UUID> multishotArrows;
    public static Set<Player> hitPlayers;
    private final Player master;

    public final double CROSSBOW_SPEED = config.crossbowAbilityConfig.crossbowSpeed;
    public static final int SPEED_POTION_DURATION = config.crossbowAbilityConfig.speedDuration;
    public static final int INVISIBILITY_POTION_DURATION = config.crossbowAbilityConfig.invisibilityDuration;
    public static final int SPEED_AMPLIFIER = config.crossbowAbilityConfig.speedAmplifier;

    public CrossbowAbility(Player player) {
        setName("쇠뇌");
        setDescription("잃은 체력에 비례해 이동 속도가 증가합니다.");

        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        ItemMeta crossbowMeta = crossbow.getItemMeta();
        crossbowMeta.addEnchant(Enchantment.MULTISHOT, 1, false);
        crossbow.setItemMeta(crossbowMeta);
        addRequiredItems(crossbow);
        multishotArrows = new HashSet<>();
        hitPlayers = new HashSet<>();
        master = player;
        AttributeInstance attr = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (attr != null) {
            attr.setBaseValue(CROSSBOW_SPEED);
        }
        player.getInventory().addItem(crossbow);
        detectCrossbow();
    }

    public void detectCrossbow() {
        BattlePlugin.getPlugin(BattlePlugin.class).getServer().getScheduler().scheduleSyncRepeatingTask(
                BattlePlugin.getPlugin(BattlePlugin.class), () -> {
                    if (hitPlayers.size() >= 2) {
                        applyBuffToShooter(master);
                        hitPlayers.clear();
                    }
                }, 0L, 1L);
    }

    public static void applyBuffToShooter(Player shooter) {
        shooter.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * SPEED_POTION_DURATION, SPEED_AMPLIFIER, false, false));
        shooter.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * INVISIBILITY_POTION_DURATION, 1, false, false));
        SendMessage.sendActionBar(shooter, Component.text("이동속도 증가 및 투명화가 적용되었습니다.")
                                                    .color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.BOLD));
    }

    public static String getAbilityName() {
        return "crossbow";
    }
}
