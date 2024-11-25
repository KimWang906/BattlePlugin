package org.kw906plugin.battlePlugin.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.kw906plugin.battlePlugin.BattlePlugin;
import org.kw906plugin.battlePlugin.SendMessage;
import org.kw906plugin.battlePlugin.commands.Sequence;

import java.time.Duration;
import java.util.HashMap;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;

public class TeamManager {
    private static final HashMap<Integer, Integer> teamScore = new HashMap<>();
    private static final long MAX_SCORE = config.maxScore;
    private static int taskId = -1; // taskId를 저장할 변수

    public static void showScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective(
                "s", "s1", Component.text("Team Score").color(NamedTextColor.AQUA));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // 주기적인 작업을 시작하고, taskId를 저장
        taskId = BattlePlugin.getPlugin(BattlePlugin.class).getServer().getScheduler().scheduleSyncRepeatingTask(
                BattlePlugin.getPlugin(BattlePlugin.class), () -> {
                    // 점수 업데이트
                    for (int i = 0; i < 4; i++) {
                        int currentTeamScore = TeamManager.getTeamScore().get(i);
                        Score score = objective.getScore("Team " + i + ": " + currentTeamScore);
                        score.setScore(currentTeamScore);
                    }

                    // 스코어보드를 플레이어에게 할당
                    Player scoreboardPlayer = Bukkit.getOfflinePlayer("scoreboardPlayer").getPlayer();
                    if (scoreboardPlayer != null) {
                        scoreboardPlayer.setScoreboard(scoreboard);
                    }

                    // 종료 조건을 체크
                    if (shouldStopTask()) {
                        stopScoreboardTask();  // 조건이 맞으면 작업 종료
                    }
                }, 0L, 20L); // 20L = 1초마다 실행
    }

    // 종료 조건을 체크하는 메서드 (예시: 특정 팀 점수가 50점 이상이면 종료)
    private static boolean shouldStopTask() {
        for (int i = 0; i < 4; i++) {
            int currentTeamScore = TeamManager.getTeamScore().get(i);
            if (currentTeamScore >= 50) {
                return true; // 예시: 특정 팀 점수가 50점 이상이면 종료
            }
        }
        return false; // 종료 조건에 해당하지 않으면 false
    }

    public static void stopScoreboardTask() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    public static HashMap<Integer, Integer> getTeamScore() {
        return teamScore;
    }

    public static void setTeamScore(int team, int score) {
        teamScore.put(team, score);
        checkMaxScore(team);
    }

    public static void addTeamScore(int team, int score) {
        teamScore.put(team, teamScore.get(team) + score);
        checkMaxScore(team);
    }

    public static void subTeamScore(int team, int score) {
        teamScore.put(team, teamScore.get(team) - score);
        checkMaxScore(team);
    }

    public static void resetTeamScore() {
        if (teamScore.isEmpty())
        {
            teamScore.put(1, 0);
            teamScore.put(2, 0);
            teamScore.put(3, 0);
        }
        teamScore.put(1, 0);
        teamScore.put(2, 0);
        teamScore.put(3, 0);
    }

    private static void checkMaxScore(int team) {
        int score = teamScore.get(team);
        if (score >= MAX_SCORE) {
            handleMaxScoreEvent(team);
        }
    }

    private static void handleMaxScoreEvent(int team) {
        SendMessage.broadcastTitle(
                Component.text("팀 " + team + "이 " + MAX_SCORE + "로 우승하였습니다!").color(NamedTextColor.GREEN),
                Title.Times.times(Duration.ofSeconds(3), Duration.ofSeconds(10), Duration.ofSeconds(3))
        );
        Sequence.stop();
    }
}
