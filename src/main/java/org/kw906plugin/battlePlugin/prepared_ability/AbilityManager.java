package org.kw906plugin.battlePlugin.prepared_ability;

//방패: 왼손에 들면 무적, 손에 들면 공격불가
//도끼: 피흡
//삼지창: 물에 있을 때 체력증가, 물에 있을 때 재생, 물에서 이속디버프x, 물에서 호흡가능
//주먹: 방어구 사용x, 기본 공격력6, 포화, 특정아이템(네더라이트 주괴 생각중) 사용 시 방어력 증가
//
//활: 이동속도 증가
//쇠뇌: 잃은 체력에 비례해 이동속도 증가
//막대: 누적 피해에 비례해 데미지 증가
//철퇴: 낙뎀감소, 철퇴를 들면 점프강화
//
//괭이: 마을의 영웅, 왼손에 인챈트북 들면 다이아
//낚싯대: 행운 최대치, 왼손에 인챈트북 들면 다이아
//탐험가: 특정아이템을 사용 시 아군 전체 영구 버프
//ex)전달체 사용 시 아군 전체 수중호흡

import org.bukkit.entity.Player;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
import org.kw906plugin.battlePlugin.utils.PlayerImpl;

import java.util.ArrayList;
import java.util.List;

public class AbilityManager {
    private static final ArrayList<BattlePlayer> playerAbilities = new ArrayList<>();

    public static List<BattlePlayer> getPlayers() {
        return playerAbilities;
    }

    public static void addPlayer(BattlePlayer player) {
        playerAbilities.add(player);
    }

    public static void removePlayer(Player player) {
        playerAbilities.removeIf(battlePlayer -> battlePlayer.getPlayer().equals(player));
    }

    public static void cleanup() {
        playerAbilities.clear();
        PlayerImpl.resetPlayerAttributes();
    }

    public static BattlePlayer findPlayer(Player player) {
        for (BattlePlayer battlePlayer : playerAbilities) {
            if (battlePlayer.getPlayer().equals(player)) {
                return battlePlayer;
            }
        }
        return null;
    }

    public static Ability getAbility(Player player) {
        for (BattlePlayer battlePlayer : playerAbilities) {
            if (battlePlayer.getPlayer().equals(player)) {
                return battlePlayer.getAbility();
            }
        }
        return null;
    }

    public static boolean hasAbility(Player player, Class<? extends Ability> abilityClass) {
        Ability ability = getAbility(player);
        return ability != null && ability.getClass().equals(abilityClass);
    }
}
