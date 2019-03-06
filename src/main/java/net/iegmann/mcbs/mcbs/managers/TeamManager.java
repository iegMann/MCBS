package net.iegmann.mcbs.mcbs.managers;

import net.iegmann.mcbs.mcbs.MCBS;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TeamManager {
    /**
     * このクラスはチームの作成、解散や解散などの操作を行います。
     * チームのリストはこのクラスが持っています。
     * TeamManager#getTeams() でチームのリストを取得してください。
     * このクラスのインスタンスは、1ゲームに一つのみとします。
     * @author Wiz
     */
    private List<Team> teams = new ArrayList<Team>();

    /**
     *
     * @param teamsCount: チームの数
     * @param players : プレイヤーのリスト
     * @return : チームのリスト
     */
    public List<Team> createTeamsRandomly(int teamsCount, List<Player> players) {
        List<Team> ret = new ArrayList<Team>();
        Collections.shuffle(players);
        //空のチームを作る処理,最大6チームまで想定
        for(int i = 0; i < teamsCount; i++) {
            if(i == 0)
                ret.add(new Team(ChatColor.BLUE,"MarsForce"));
            if(i == 1)
                ret.add(new Team(ChatColor.RED, "TeamMinerva"));
            if(i == 2)
                ret.add(new Team(ChatColor.GREEN,"Chronos"));
            if(i == 3)
                ret.add(new Team(ChatColor.YELLOW, "Hikari"));
            if(i == 4)
                ret.add(new Team(ChatColor.BLACK, "TeamGrimoire"));
            if(i == 5)
                ret.add(new Team(ChatColor.WHITE, "WhiteNoNakamatachi"));
        }
        for(int i = 0; i < players.size(); i++) {
            ret.get(i % teamsCount).Join(players.get(i));
        }
        return ret;
    }

    /**
     * チームを作成します
     * プレイヤーのリスト、チームの名前、チームカラーを指定してください。
     * @param players
     * @param name
     * @param color
     * @return
     */
    public Team createTeam(List<Player> players,String name, ChatColor color) {
        Team ret = new Team(players,color,name);
        this.teams.add(ret);
        return ret;
    }

    /**
     * 最もメンバーの少ないチームのいずれかにプレイヤーを参加させます。
     * @param player
     */
    public void SmoothJoin(Player player) {
        Team smallest;
        if(teams.size() < 1) {
            Bukkit.getLogger().info("まずはチームを結成してください。");
        } else {

        smallest = this.teams.get(0);
        for(Team team : this.teams) {
            if(team.getMembers().size() < smallest.getMembers().size()) {
                smallest = team;
            }
        }
        smallest.Join(player);
        }
    }
    /**
     * 指定したチームを解散させます。
     * @param team
     */
    public void DissolveTeam(Team team) {
        for(Player p :team.players) {
            team.Quit(p);
        }
        this.teams.remove(team);
    }

    /**
     * すべてのチームを解散させます。
     */
    public void DissolveAllTeam() {
        for (Team team : teams) {
            for (Player p : team.getMembers()) {
                team.Quit(p);
            }
        }
        this.teams = new ArrayList<Team>();
    }

    /**
     *
     * @return すべてのチームのリストを返します。
     */
    public List<Team> getTeams() {
        return this.teams;
    }

    /**
     * @return いずれかのチームに所属しているすべてのプレイヤー
     */
    public List<Player> getPlayers() {
        List<Player> ret = new ArrayList<Player>();
        for(Team team : this.teams) {
            for(Player player : team.getMembers()) {
                ret.add(player);
            }
        }
        return ret;
    }

    /**
     * 自前のチームクラス
     * チームのメンバーの情報と名前、色を持つ。
     */
    public class Team {
        private List<Player> players = new ArrayList<Player>();
        private ChatColor teamcolor;
        private String name;

        /**
         * デフォルトコンストラクタ
         * @param players : プレイヤーのリスト
         * @param color : チームの色
         * @param name : チームの名前
         */
        private Team(List<Player> players, ChatColor color, String name) {
            this.players = players;
            this.teamcolor = color;
            this.name = name;
        }

        /**
         * コンストラクタ2
         * プレイヤーがいないチームのインスタンスを作成します。
         * @param color チームの色
         * @param name　チームの名前
         */
        private Team(ChatColor color, String name) {
            this.teamcolor = color;
            this.name = name;
        }

        /**
         * プレイヤーをチームに追加します。
         * @param player
         */
        public void Join(Player player) {
            this.players.add(player);
            player.setMetadata("TeamBelongsTo", new FixedMetadataValue(MCBS.getInstance(), this.name));
        }

        /**
         * プレイヤーをチームから脱退させます。
         * @param player
         */
        public void Quit(Player player) {
            this.players.remove(player);
            player.removeMetadata("TeamBelongsTo", MCBS.getInstance());
        }

        /**
         *
         * @return  チームの名前
         */
        public String getName() {
            return this.name;
        }

        /**
         *
         * @return チームの色
         */
        public ChatColor getColor() {
            return this.teamcolor;
        }

        /**
         *
         * @return チームに所属するプレイヤーのリスト
         */
        public List<Player> getMembers() {
            return this.players;
        }
    }

}