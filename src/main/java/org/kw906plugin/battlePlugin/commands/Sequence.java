package org.kw906plugin.battlePlugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.kw906plugin.battlePlugin.Ability;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.Status;
import org.kw906plugin.battlePlugin.events.NoPvPEvent;
import org.kw906plugin.battlePlugin.player.BattlePlayer;
import org.kw906plugin.battlePlugin.player.TeamManager;
import org.kw906plugin.battlePlugin.prepared_ability.*;
import org.kw906plugin.battlePlugin.utils.PlayerImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.kw906plugin.battlePlugin.BattlePlugin.config;
import static org.kw906plugin.battlePlugin.BattlePlugin.recipeKeys;
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
            Configure.WorldConfig worldConfig = config.getWorldConfig();
            Configure.WorldBorderConfig worldBorderConfig = config.getWorldBorderConfig();
            SendMessage.broadcastMessage(Component.text("시퀀스 - 플레이어 초기화 중.."));
            setFullCondition();
            TeamManager.initScoreboard();
            PlayerImpl.resetPlayerAbility();
            SendMessage.broadcastMessage(Component.text("허용 아이템 설정 중.."));
            for (Player target : getOnlinePlayers()) {
                target.discoverRecipes(recipeKeys);
            }

            SendMessage.broadcastMessage(Component.text("시퀀스 - 오버월드 월드보더 설정 중..")
                                                  .color(NamedTextColor.GRAY));
            setWorldBorder(
                    worldConfig.getOverworld(),
                    worldBorderConfig.getOverworldWorldBorderSize()
            );

            SendMessage.broadcastMessage(Component.text("시퀀스 - 네더 월드보더 설정 중..")
                                                  .color(NamedTextColor.GRAY));
            setWorldBorder(
                    worldConfig.getNether(),
                    worldBorderConfig.getNetherWorldBorderSize()
            );

            SendMessage.broadcastMessage(Component.text("시퀀스 - 엔드 월드보더 설정 중..")
                                                  .color(NamedTextColor.GRAY));
            setWorldBorder(
                    worldConfig.getTheEnd(),
                    worldBorderConfig.getEndWorldBorderSize()
            );

            SendMessage.broadcastMessage(Component.text("시퀀스 - 스코어보드 초기화 중..")
                                                  .color(NamedTextColor.GRAY));
            TeamManager.initScoreboard();
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
        Status.setStatus(Status.GAME_SETUP);
        SendMessage.sendMessageOP(Component.text("시퀀스 - 셋업 진행중")
                                           .color(NamedTextColor.GRAY)
        );
        SendMessage.broadcastMessage(Component.text("시퀀스 - 팀 초기화 중.."));
        TeamManager.showScoreboard();
        SendMessage.broadcastMessage(Component.text("시퀀스 - 타이머 설정 중.."));
        NoPvPEvent.startPvPTimer();
        SendMessage.broadcastMessage(Component.text("벤 아이템 목록 설정 중.."));
        AbilityManager.setDefaultBanItems();
        SendMessage.broadcastMessage(Component.text("벤 아이템 설정 중.."));
        AbilityManager.customLimitItems();
        SendMessage.broadcastMessage(Component.text("게임 규칙 설정 중.."));
        ArrayList<World> worlds = new ArrayList<>();
        worlds.add(config.getWorldConfig().getOverworld());
        worlds.add(config.getWorldConfig().getNether());
        worlds.add(config.getWorldConfig().getTheEnd());
        for (World world : worlds) {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.GLOBAL_SOUND_EVENTS, false);
            world.setGameRule(GameRule.DO_LIMITED_CRAFTING, false);
            world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 50);
        }
        for (BattlePlayer battlePlayer : AbilityManager.getPlayers()) {
            AbilityManager.limitItems(battlePlayer);
        }
    }

    public static void register(String name, String ability, int teamIndex) {
        if (Status.getStatus().equals(Status.RUNNING) ||
                Status.getStatus().equals(Status.GAME_SETUP) ||
                Status.getStatus().equals(Status.COUNT_DOWN))
        {
            SendMessage.sendMessageOP(Component.text("게임이 이미 시작되어 등록이 불가능합니다.").color(NamedTextColor.RED));
        } else if (!Status.getStatus().equals(Status.INITIALIZED)) {
            SendMessage.sendMessageOP(Component.text("게임이 초기화 되지 않았습니다!").color(NamedTextColor.RED));
        }

        SendMessage.broadcastMessage(Component.text("시퀀스 - 플레이어 추가 중..")
                                              .color(NamedTextColor.GRAY));

        SendMessage.logConsole("getOnlinePlayers " + getOnlinePlayers());
        getOnlinePlayers().stream()
                          .filter(player -> player.getName().equals(name))
                          .findFirst()
                          .ifPresentOrElse(player -> {
                              Ability playerAbility = switch (ability) {
                                  case "axe" -> new AxeAbility(player);
                                  case "shield" -> new ShieldAbility(player);
                                  case "trident" -> new TridentAbility(player);
                                  case "arrow" -> new ArrowAbility(player);
                                  case "crossbow" -> new CrossbowAbility(player);
                                  case "explorer" -> new ExplorerAbility(player, teamIndex);
                                  case "fishingrod" -> new FishingRodAbility(player);
                                  case "fist" -> new FistAbility(player);
                                  case "lighter" -> new LighterAbility(player);
                                  case "mace" -> new MaceAbility(player);
                                  case "wizard" -> new WizardAbility(player);
                                  case "stick" -> new StickAbility(player);
                                  default -> throw new IllegalStateException("Unexpected value: " + ability);
                              };

                              BattlePlayer battlePlayer = new BattlePlayer(player, playerAbility, teamIndex);
                              SendMessage.broadcastMessage(Component.text(
                                      "시퀀스 - " + player.getName() + "을 대기 목록에 추가하였습니다."));
                              AbilityManager.addPlayer(battlePlayer);
                          }, () -> {
                              SendMessage.sendMessageOP(Component.text("플레이어를 찾을 수 없습니다.").color(NamedTextColor.RED));
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
            for (Field field : config.getClass().getFields()) {
                try {
                    Object value = field.get(config);
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

        Optional<Field> configOptional = Arrays.stream(config.getClass().getFields())
                                               .filter(f -> f.getName().equals(args[0])).findAny();
        if (configOptional.isPresent()) {
            try {
                Field targetConfig = configOptional.get();
                String configType = targetConfig.getType().getSimpleName();
                String beforeValue = targetConfig.get(config).toString();
                boolean success = false;

                // 설정이 클래스일 경우 내부 필드 접근
                Object configObject = targetConfig.get(config);
                if (configObject != null && !configObject.getClass().isPrimitive() && !(configObject instanceof String)) {
                    success = updateClassConfig(configObject, args[1]);
                } else {
                    // 기본 타입 처리
                    switch (configType) {
                        case "String":
                            targetConfig.set(config, args[1]);
                            success = true;
                            break;
                        case "int":
                            try {
                                targetConfig.set(config, Integer.parseInt(args[1]));
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
                    config.saveAllConfigs();
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
        NoPvPEvent.stopPvPTimer();
        TeamManager.forcedStop();
        TeamManager.clearScoreboard();
        AbilityManager.cleanup();
        Status.setStatus(Status.STOPPED);
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
