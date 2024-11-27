package org.kw906plugin.battlePlugin.prepared_ability;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.player.BattlePlayer;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.getPlayers;
import static org.kw906plugin.battlePlugin.prepared_ability.AbilityManager.hasAbility;

public class MaceAbility extends Ability implements Listener {
    private static final double JUMP_BOOST_MULTIPLIER = config.maceAbilityConfig.jumpBoostMultiplier; // 점프 강화 비율
    private static final float DEFAULT_JUMP_STRENGTH = 0.42f; // 기본 점프 강도
    private static final Material MACE_MATERIAL = Material.MACE; // 철퇴 아이템

    public MaceAbility() {
        setName("철퇴");
        setDescription("철퇴를 들고 있으면 낙하 피해가 무효화되고, 점프가 강화됩니다.");
        addRequiredItems(new ItemStack(MACE_MATERIAL));
        onHasAbility();
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasAbility(player, MaceAbility.class) && event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                event.setCancelled(true);
            }
        }
    }

    public void onHasAbility() {
        BattlePlugin.getPlugin(BattlePlugin.class).getServer().getScheduler().scheduleSyncRepeatingTask(
                BattlePlugin.getPlugin(BattlePlugin.class), () -> {
                    for (BattlePlayer battlePlayer : getPlayers()) {
                        Player player = battlePlayer.getPlayer();
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (hasAbility(player, MaceAbility.class) && item.getType().equals(MACE_MATERIAL)) {
                            enhanceJumpStrength(player);
                        } else {
                            resetJumpStrength(player);
                        }
                    }
                }, 0L, 20L);
    }

    private void enhanceJumpStrength(Player player) {
        AttributeInstance jumpAttribute = player.getAttribute(Attribute.JUMP_STRENGTH);
        if (jumpAttribute != null) {
            double newJumpStrength = DEFAULT_JUMP_STRENGTH + JUMP_BOOST_MULTIPLIER;
            jumpAttribute.setBaseValue(newJumpStrength);
        }
    }

    public static void resetJumpStrength(Player player) {
        AttributeInstance jumpAttribute = player.getAttribute(Attribute.JUMP_STRENGTH);
        if (jumpAttribute != null) {
            jumpAttribute.setBaseValue(DEFAULT_JUMP_STRENGTH);
        }
    }
}
