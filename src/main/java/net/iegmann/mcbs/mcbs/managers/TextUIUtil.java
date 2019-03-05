package net.iegmann.mcbs.mcbs.managers;
import net.iegmann.mcbs.mcbs.managers.TeamManager.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class TextUIUtil {
    /**
     * 指定したチームにメッセージを送信します。
     * @param team
     * @param message
     */
    public static void sendMessageToTeam(Team team, String message) {
        for(Player player : team.getMembers()) {
            player.sendMessage(message);
        }
    }

    /**
     * 指定したチームに色付きのメッセージを送信します。
     * @param team
     * @param message
     * @param color
     */
    public static void sendMessageToTeam(Team team, String message, ChatColor color) {
        for(Player player : team.getMembers()) {
            player.sendMessage(color + message);
        }
    }

    /**
     * 指定したチームに色付きのタイトルメッセージを表示します。
     * @param team
     * @param message
     * @param color
     */
    public static void sendTitleToTeam(Team team, String message, ChatColor color) {
        for(Player player : team.getMembers()) {
                player.sendTitle(message,"",1,2,1);
        }
    }

    /**
     * いずれかのチームに所属しているプレイヤー全員にタイトルメッセージを表示します。
     * @param teamManager
     * @param message
     * @param color
     */
    public static void sendTitleToPlayers(TeamManager teamManager, String message, ChatColor color) {
            for(Player player : teamManager.getPlayers()) {

                player.sendTitle(message,"",1,2,1);

        }
    }

    /**
     * 秒数のカウントに適したタイトルメッセージをいずれかのチームに所属しているプレイヤー全員に表示します。
     * @param teamManager
     * @param number
     * @param color
     */
    public static void sendTitleToPlayersForCount(TeamManager teamManager, int number, ChatColor color) {

            for(Player player : teamManager.getPlayers()) {

                player.sendTitle(Integer.toString(number),"",0,1,0);

        }
    }

    }
