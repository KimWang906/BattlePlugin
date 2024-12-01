package org.kw906plugin.battlePlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kw906plugin.battlePlugin.player.TeamManager;
import org.kw906plugin.battlePlugin.prepared_ability.AbilityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController implements CommandExecutor, TabCompleter {
    ArrayList<String> commands = new ArrayList<>();

    public MainController() {
        commands.addAll(Arrays.asList("start", "stop", "init", "register", "unregister", "status", "config", "list"));

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어가 커맨드를 실행해야 합니다.");
            return true;
        }

        if (!sender.isOp()) {
            sender.sendMessage("이 명령어를 실행할 권한이 없습니다.");
            return true;
        }

        String subCommand = "";
        if (args.length == 0) {
            sender.sendMessage("사용법: battle [options] [args]");
            sender.sendMessage("init : 게임 초기화를 진행합니다.");
            sender.sendMessage("start: 게임을 시작합니다.");
            sender.sendMessage("stop: 게임을 종료합니다.");
            sender.sendMessage("config [name]: 게임 설정을 변경합니다.");
            sender.sendMessage("register [player name] [ability name] [team index]: 플레이어를 추가 및 능력을 부여합니다.");
            sender.sendMessage("unregister [player name]: 플레이어를 제외시키며 능력이 회수됩니다.");
            sender.sendMessage("status: 게임 상태를 확인합니다.");
            sender.sendMessage("list: 등록된 플레이어 목록을 표시합니다.");
            return true;
        } else {
            subCommand = args[0];
        }

        switch (subCommand) {
            case "start":
                Sequence.start();
                break;
            case "stop":
                Sequence.stop();
                break;
            case "init":
                Sequence.init();
                break;
            case "register":
                if (args.length < 4) {
                    sender.sendMessage("사용법: register [player name] [ability name] [team index]");
                    return true;
                }
                String regPlayerName = args[1];
                String ability = args[2];
                int teamIndex = Integer.parseInt(args[3]);
                Sequence.register(regPlayerName, ability, teamIndex);
                break;
            case "unregister":
                if (args.length < 2) {
                    sender.sendMessage("사용법: unregister [player name]");
                    return true;
                }
                String unregPlayerName = args[1];
                Sequence.unregister(unregPlayerName);
                break;
            case "status":
                sender.sendMessage("게임 상태를 확인 중...");
                break;
            case "config":
                String[] conf_args = Arrays.copyOfRange(args, 1, args.length);
                Sequence.config(conf_args);
                break;
            case "list":
                Sequence.printPlayerData();
                break;
            default:
                sender.sendMessage("알 수 없는 명령어입니다.");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(commands);
        } else if ((args[0].equals("register") || args[0].equals("unregister")) && args.length == 2) {
            completions.addAll(getOnlinePlayerNames());
        } else if (args[0].equals("register") && args.length == 3) {
            completions.addAll(AbilityManager.getAllAbilityNames());
        } else if (args[0].equals("register") && args.length == 4) {
            completions.addAll(TeamManager.getTeamNumberStrings());
        }

        // 입력된 값에 따라 필터링
        return filterCompletions(completions, args[args.length - 1]);
    }

    /**
     * 현재 온라인 플레이어 이름을 가져옵니다.
     */
    private List<String> getOnlinePlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }

    /**
     * 입력값에 따라 자동완성 목록을 필터링합니다.
     */
    private List<String> filterCompletions(List<String> completions, String currentInput) {
        List<String> filtered = new ArrayList<>();
        for (String completion : completions) {
            if (completion.toLowerCase().startsWith(currentInput.toLowerCase())) {
                filtered.add(completion);
            }
        }
        return filtered;
    }
}
