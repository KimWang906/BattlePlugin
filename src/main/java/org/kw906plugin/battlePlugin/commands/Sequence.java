package org.kw906plugin.battlePlugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.Status;
import org.kw906plugin.battlePlugin.events.NoPvPEvent;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
import org.kw906plugin.battlePlugin.player.TeamManager;
import org.kw906plugin.battlePlugin.prepared_ability.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.kw906plugin.battlePlugin.utils.PlayerImpl.setFullCondition;
import static org.kw906plugin.battlePlugin.utils.UpdateWorldBorder.setWorldBorder;

public class Sequence {
    private static final int gameTime = 0;

    public static void init() {
        if (Status.getStatus().equals(Status.INITIALIZED) || Status.getStatus().equals(Status.RUNNING)) {
            SendMessage.sendMessageOP(Component.text("이미 게임이 초기화 되어있습니다!")
                                               .color(NamedTextColor.RED));
            return;
        }

        SendMessage.broadcastMessage(Component.text("시퀀스 - 게임 초기화 진행")
                                              .color(NamedTextColor.GRAY)
        );

        try {
            Configure.WorldConfig worldConfig = BattlePlugin.config.getWorldConfig();
            Configure.WorldBorderConfig worldBorderConfig = BattlePlugin.config.getWorldBorderConfig();
            SendMessage.broadcastMessage(Component.text("시퀀스 - 플레이어 초기화 중.."));
            setFullCondition();

            SendMessage.broadcastMessage(Component.text("시퀀스 - 오버월드 월드보더 설정 중..")
                                                  .color(NamedTextColor.GRAY));
            setWorldBorder(worldConfig.getOverworld(),
                           worldBorderConfig.getOverworldWorldBorderSize());

            SendMessage.broadcastMessage(Component.text("시퀀스 - 네더 월드보더 설정 중..")
                                                  .color(NamedTextColor.GRAY));
            setWorldBorder(worldConfig.getNether(),
                           worldBorderConfig.getNetherWorldBorderSize());

            SendMessage.broadcastMessage(Component.text("시퀀스 - 엔드 월드보더 설정 중..")
                                                  .color(NamedTextColor.GRAY));
            setWorldBorder(worldConfig.getTheEnd(),
                           worldBorderConfig.getEndWorldBorderSize());
        } catch (NullPointerException e) {
            SendMessage.broadcastMessage(Component.text("게임 초기화 도중 오류가 발생하였습니다.")
                                                  .color(NamedTextColor.RED));
            SendMessage.broadcastMessage(Component.text("오류 내용: " + e.getMessage())
                                                  .color(NamedTextColor.RED));
        }

        Status.setStatus(Status.INITIALIZED);
        SendMessage.broadcastMessage(Component.text("시퀀스 - 게임 초기화가 완료되었습니다.")
                                              .color(NamedTextColor.GRAY));
    }

    public static void start() {
        SendMessage.broadcastMessage(Component.text("시퀀스 - 게임이 시작됩니다!")
                                              .color(NamedTextColor.BLUE));
        setup();
        SendMessage.broadcastMessage(Component.text("시퀀스 - 게임이 시작되었습니다!")
                                              .color(NamedTextColor.BLUE));
    }

    public static void setup() {
        SendMessage.sendMessageOP(Component.text("시퀀스 - 셋업 진행중")
                                           .color(NamedTextColor.GRAY)
        );
        SendMessage.broadcastMessage(Component.text("시퀀스 - 팀 초기화 중.."));
        TeamManager.resetTeamScore();
        TeamManager.showScoreboard();
        SendMessage.broadcastMessage(Component.text("시퀀스 - 타이머 설정 중.."));
        NoPvPEvent.startPvPTimer();
    }

    public static void register(String name, String ability, int teamIndex) {
        SendMessage.broadcastMessage(Component.text("시퀀스 - 플레이어 추가 중..")
                                              .color(NamedTextColor.GRAY));

        Ability playerAbility = switch (ability) {
            case "axe" -> new AxeAbility();
            case "shield" -> new ShieldAbility();
            case "trident" -> new TridentAbility();
            case "arrow" -> new ArrowAbility();
            default -> throw new IllegalStateException("Unexpected value: " + ability);
        };

        getOnlinePlayers().stream()
                          .filter(player -> player.getName().equals(name))
                          .findFirst()
                          .ifPresent(player -> {
                              BattlePlayer battlePlayer = new BattlePlayer(player, playerAbility, teamIndex);
                              AbilityManager.addPlayer(battlePlayer);
                              SendMessage.broadcastMessage(Component.text(
                                      "시퀀스 - " + player.getName() + "을 대기 목록에 추가하였습니다."));
                          });
    }

    public static void unregister(String name) {
        SendMessage.broadcastMessage(Component.text("시퀀스 - 플레이어 등록 취소 중..")
                                              .color(NamedTextColor.GRAY));
        getOnlinePlayers().stream()
                          .filter(player -> player.getName().equals(name))
                          .findFirst()
                          .ifPresent(player -> {
                              AbilityManager.removePlayer(player);
                              SendMessage.broadcastMessage(Component.text(
                                      "시퀀스 - " + player.getName() + "을 대기 목록에서 삭제하였습니다."));
                          });
    }

    public static void config(String[] args) {
        if (args.length == 0) {
            SendMessage.sendMessageOP(Component.text("설정 목록:"));
            for (Field field : BattlePlugin.config.getClass().getFields()) {
                try {
                    Object value = field.get(BattlePlugin.config);
                    // 클래스 타입인 경우, 필드 내의 필드를 출력
                    if (value != null && !value.getClass().isPrimitive() && !(value instanceof String)) {
                        SendMessage.sendMessageOP(Component.text(field.getName() + ": ")
                                                           .color(NamedTextColor.AQUA)
                                                           .append(Component.text("클래스 타입: " + value.getClass().getSimpleName()).color(NamedTextColor.GRAY)));
                    } else {
                        SendMessage.sendMessageOP(
                                Component.text(field.getName() + ": ")
                                         .color(NamedTextColor.AQUA)
                                         .append(Component.text(value.toString())
                                                          .color(NamedTextColor.GRAY)));
                    }
                } catch (IllegalAccessException e) {
                    getLogger().log(Level.WARNING, e.getMessage());
                }
            }
            return;
        }

        Optional<Field> configOptional = Arrays.stream(BattlePlugin.config.getClass().getFields())
                                               .filter(f -> f.getName().equals(args[0])).findAny();
        if (configOptional.isPresent()) {
            try {
                Field targetConfig = configOptional.get();
                String configType = targetConfig.getType().getSimpleName();
                String beforeValue = targetConfig.get(BattlePlugin.config).toString();
                boolean success = false;

                // 설정이 클래스일 경우 내부 필드 접근
                Object configObject = targetConfig.get(BattlePlugin.config);
                if (configObject != null && !configObject.getClass().isPrimitive() && !(configObject instanceof String)) {
                    success = updateClassConfig(configObject, args[1]);
                } else {
                    // 기본 타입 처리
                    switch (configType) {
                        case "String":
                            targetConfig.set(BattlePlugin.config, args[1]);
                            success = true;
                            break;
                        case "int":
                            try {
                                targetConfig.set(BattlePlugin.config, Integer.parseInt(args[1]));
                                success = true;
                            } catch (NumberFormatException e) {
                                SendMessage.sendMessageOP(Component.text("잘못된 입력 값으로 인해 변경에 실패하였습니다.\n" +
                                                                                 "요청된 값: " + args[1])
                                                                   .color(NamedTextColor.RED));
                            }
                            break;
                        default:
                            SendMessage.sendMessageOP(Component.text("구현되지 않은 요청입니다. (요청된 타입: " + configType + ")")
                                                               .color(NamedTextColor.DARK_GRAY));
                            break;
                    }
                }

                if (success) {
                    SendMessage.sendMessageOP(Component.text("설정이 성공적으로 변경되었습니다!"));
                    SendMessage.sendMessageOP(Component.text("변경 내용: (" + beforeValue + "→" + args[1] + ")"));
                    BattlePlugin.config.saveAllConfigs();
                }
            } catch (IllegalAccessException e) {
                SendMessage.sendMessageOP(Component.text("설정을 변경하던 중 오류가 발생하였습니다.\n" +
                                                                 "자세한 내용은 서버 로그를 참고하세요.")
                                                   .color(NamedTextColor.RED));
                getLogger().log(Level.WARNING, e.getMessage());
            }
        }
    }

    // 클래스 타입 설정을 갱신하는 메소드
    private static boolean updateClassConfig(Object configObject, String newValue) {
        boolean success = false;
        // 해당 객체의 모든 필드를 재귀적으로 수정
        for (Field field : configObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);  // private 필드에도 접근 가능하도록 설정
            try {
                String fieldType = field.getType().getSimpleName();
                if (fieldType.equals("String")) {
                    field.set(configObject, newValue);
                    success = true;
                } else if (fieldType.equals("int")) {
                    try {
                        field.set(configObject, Integer.parseInt(newValue));
                        success = true;
                    } catch (NumberFormatException e) {
                        SendMessage.sendMessageOP(Component.text("잘못된 입력 값으로 인해 변경에 실패하였습니다.\n" +
                                                                         "요청된 값: " + newValue)
                                                           .color(NamedTextColor.RED));
                        success = false;
                    }
                } else {
                    SendMessage.sendMessageOP(Component.text("지원되지 않는 타입입니다: " + fieldType)
                                                       .color(NamedTextColor.RED));
                    success = false;
                }
            } catch (IllegalAccessException e) {
                SendMessage.sendMessageOP(Component.text("필드 접근 중 오류가 발생하였습니다.")
                                                   .color(NamedTextColor.RED));
                getLogger().log(Level.WARNING, e.getMessage());
            }
        }
        return success;
    }

    public static void stop() {
        AbilityManager.cleanup();
        NoPvPEvent.stopPvPTimer();
        TeamManager.stopScoreboardTask();
        SendMessage.broadcastMessage(Component.text("시퀀스 - 게임이 관리자에 의해 종료되었습니다!")
                                              .color(NamedTextColor.BLUE));
    }

    public static void printPlayerData() {
        for (BattlePlayer battlePlayer : AbilityManager.getPlayers()) {
            SendMessage.sendMessageOP(Component.text("이름: " + battlePlayer.getPlayer().getName()));
            SendMessage.sendMessageOP(Component.text("능력: " + battlePlayer.getAbility().getName()));
        }
    }
}
