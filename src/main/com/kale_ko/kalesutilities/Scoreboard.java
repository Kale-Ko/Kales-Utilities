package com.kale_ko.kalesutilities;

import com.kale_ko.api.spigot.TextStyler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

public class Scoreboard {
    public static void updateScoreBoard(Player player, Main plugin) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective(player.getName(), "dummy");

        objective.setDisplayName(TextStyler.noPrefix(plugin.config.getString("scoreboard.title"), plugin.config));

        int index = 0;
        for (String line : plugin.config.getStringList("scoreboard.values")) {
            Score newline = objective.getScore(TextStyler.noPrefix(TextStyler.replacePlaceholders(line, player, plugin.playerData), plugin.config));
            newline.setScore(plugin.config.getStringList("scoreboard.values").size() - index);
            index++;

            if (index == plugin.config.getStringList("scoreboard.values").size()) {
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                player.setScoreboard(board);
            }
        }
    }
}