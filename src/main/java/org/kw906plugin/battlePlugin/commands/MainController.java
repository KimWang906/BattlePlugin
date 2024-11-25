package org.kw906plugin.battlePlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MainController implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어가 커맨드를 실행해야 합니다.");
            return true;
        }

        if (!sender.isOp()) {
            sender.sendMessage("이 명령어를 실행할 권한이 없습니다.");
        }

        // args not empty
        String subCommand = "";
        if (args.length == 0) {
            sender.sendMessage("사용법: battle [options] [args]");
            sender.sendMessage("init : 게임 초기화를 진행합니다.");
            sender.sendMessage("start: 게임을 시작합니다.");
            sender.sendMessage("stop: 게임을 종료합니다.");
            sender.sendMessage("config [name]: 게임 설정을 변경합니다.");
            sender.sendMessage("register [player name] [ability name] [team index]: 플레이어를 추가 및 능력을 부여합니다.");
            sender.sendMessage("unregister [player name]: 플레이어를 제외시며 능력이 회수됩니다.");
        }else {
            subCommand = args[0];
        }

        String playerName = args[1];
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
                String ability = args[2];
                int teamIndex = Integer.parseInt(args[3]);
                Sequence.register(playerName, ability, teamIndex);
            case "unregister":
                Sequence.unregister(playerName);
                break;
            case "status":
                sender.sendMessage("Status Game Command");
                break;
            case "config":
                String[] conf_args = Arrays.copyOfRange(args, 1, args.length);
                Sequence.config(conf_args);
                break;
            case "list":
                Sequence.printPlayerData();
            default:
                break;
        }

        return true;
    }
}
