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
import org.kw906plugin.battlePlugin.prepared_ability.AbilityManager;

import java.time.Duration;
import java.util.HashMap;

import static org.kw906plugin.battlePlugin.BattlePlugin.config;

public class TeamManager {
    private static final HashMap<Integer, Integer> teamScore = new HashMap<>();
    private static final long MAX_SCORE = config.maxScore;
    private static int taskId = -1; // taskId를 저장할 변수
    private static boolean forcedStop = false;
    private static Objective objective;
    private static Scoreboard scoreboard;

    public static void initScoreboard() {
        teamScore.put(1, 0);
        teamScore.put(2, 0);
        teamScore.put(3, 0);
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        objective = scoreboard.registerNewObjective(
                "teamScore", Criteria.create("test"), Component.text("Team Score").color(NamedTextColor.AQUA));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void showScoreboard() {
        taskId = BattlePlugin.getPlugin(BattlePlugin.class).getServer().getScheduler().scheduleSyncRepeatingTask(
                BattlePlugin.getPlugin(BattlePlugin.class), () -> {
                    // 팀 점수 갱신
                    for (int i = 1; i <= 3; i++) { // 팀 번호 1, 2, 3에 대해
                        int currentTeamScore = TeamManager.getTeamScore().getOrDefault(i, 0);
                        Score score = objective.getScore("Team " + i);
                        score.setScore(currentTeamScore);
                    }

                    for (BattlePlayer battlePlayer : AbilityManager.getPlayers()) {
                        Player player = battlePlayer.getPlayer();
                        player.setScoreboard(scoreboard);
                    }

                    if (shouldStopTask() || isForcedStop()) {
                        stopScoreboardTask();
                    }
                }, 0L, 20L); // 20L = 1초마다 실행
    }

    public static void clearScoreboard() {
        for (BattlePlayer battlePlayer : AbilityManager.getPlayers()) {
            Player player = battlePlayer.getPlayer();
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    private static void stopScoreboardTask() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    public static boolean shouldStopTask() {
        // 종료 조건 예: 모든 팀이 특정 점수를 달성했을 때
        for (int score : teamScore.values()) {
            if (score >= MAX_SCORE) return true;
        }
        return false;
    }

    public static void forcedStop() {
        forcedStop = true;
    }

    public static boolean isForcedStop() {
        return forcedStop;
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
