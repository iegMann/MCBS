package net.iegmann.mcbs.mcbs.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

/**
 * スコアボードの管理を行います。
 * プレイヤーにスコアボードを表示するには、
 * showScoreBoard(BoardInfo boardinfo) を用いてください。
 * @author WiZ
 */
public class ScoreBoardUtil {

    private TeamManager tm;
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getNewScoreboard();
   // Player p;
   // BoardInfo boardInfoExample = new BoardInfo(p,"a",new ArrayList<BoardScore>(Arrays.asList( new BoardScore("Kills", 1,3),new BoardScore("Coins", 1000,2), new BoardScore("Steps", 123,1))));
    /**
     * デフォルトコンストラクタ
     * TeamManagerから、ゲームに参加するプレイヤー、チームを取得します。
     * @param teamManager
     */
    public ScoreBoardUtil(TeamManager teamManager) {
        this.tm = teamManager;
    }

    /**
     * 現在のBoardInfoの状況から、スコアボードを
     * @param boardInfo
     */
    public void showScoreBoard(BoardInfo boardInfo) {
        Scoreboard sb = writeScoreBoard(boardInfo);
        boardInfo.player.setScoreboard(sb);
    }

    /**
     * そのプレイヤーのBoardInfo情報を生成する。
     * @param player
     * @return
     */
    public BoardInfo makeBoardInfo(Player player) {
        //下記は例である。
        return new BoardInfo(player, "Title",new ArrayList<BoardScore>(Arrays.asList( new BoardScore("Kills", 1,3),new BoardScore("Coins", 1000,2), new BoardScore("Steps", 123,1))));
    }

    /**
     * 現在のBoardInfoの状況から、スコアボードに変換する
     * @param boardInfo
     * @return
     */
    private Scoreboard writeScoreBoard(BoardInfo boardInfo) {
        Objective o = boardInfo.scoreboard.registerNewObjective(boardInfo.player.getDisplayName(),"dummy",boardInfo.title);
                o.setDisplaySlot(DisplaySlot.SIDEBAR);
                for(BoardScore bs : boardInfo.scores) {
                    Score s = o.getScore(bs.name + ":" + Integer.toString(bs.val));
                    s.setScore(bs.priority);
                }
                return boardInfo.scoreboard;
    }

    /**
     * ボードInfoのスコアを更新する
     * @param boardInfo
     * @param scorename
     * @param newValue
     */
    public void updateScore(BoardInfo boardInfo,String scorename, int newValue) {
        boardInfo.findScoreByName(scorename).setValue(newValue);
        boardInfo.getPlayer().setScoreboard(writeScoreBoard(boardInfo));
    }
    /**
     * スコアボードの表示の形式を規定するためのクラス。
     * これを変換してスコアボードに表示することになる。
     */
    public class BoardInfo {
        private Player player;
        private String title;
        private List<BoardScore> scores;
        private Scoreboard scoreboard;

        /**
         * @param title       スコアボードのタイトル
         * @param boardScores スコアボードの項目
         */
        public BoardInfo(Player player, String title, List<BoardScore> boardScores) {
            this.player = player;
            this.title = title;
            this.scores = boardScores;
            this.scoreboard = manager.getNewScoreboard();
        }

        public Player getPlayer() {
            return player;
        }

        private BoardScore findScoreByName(String name) {
            for (BoardScore score : this.scores) {
                if (name.equalsIgnoreCase(score.name)) {
                    return score;
                }
            }
            return null;
        }
    }

    /**
     * スコアボードに表示する項目の情報
     */
    public class BoardScore{
        private String name;
        private int initval;
        private int val;
        private int priority;

        /**
         *
         * @param name 項目の名前
         * @param initval 初期値
         * @param priority 優先度。スコアボード上の並び順に影響する。
         */
        public BoardScore(String name, int initval, int priority) {
            this.name = name;
            this.initval = initval;
            this.val = initval;
            this.priority = priority;
        }
        public int getValue() {
            return this.val;
        }
        public void setValue(int newValue) {
            this.val = newValue;
        }
    }

}
