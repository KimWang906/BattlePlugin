package org.kw906plugin.battlePlugin.player;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.prepared_ability.AbilityManager;

public class BattlePlayer {
    private Player player;
    private Ability ability;
    private int teamIndex;

    public BattlePlayer(Player player, Ability ability, int teamIndex) {
        this.player = player;
        this.ability = ability;
        this.teamIndex = teamIndex;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getTeamIndex() {
        return teamIndex;
    }

    public void setTeamIndex(int teamIndex) {
        this.teamIndex = teamIndex;
    }
}
