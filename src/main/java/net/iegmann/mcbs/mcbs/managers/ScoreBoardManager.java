package net.iegmann.mcbs.mcbs.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

/**
 * このクラスは、スコアボードの生成、削除、更新を担うクラスです。
 * フィールドによってスコアボードを管理しているので、インスタンス化して用いてください。
 *
 * @author WiZ
 */
public class ScoreBoardManager {

    /**
     * デフォルトコンストラクタ
     * パラメーターはありません。
     */
    public ScoreBoardManager() {

    }

    ScoreboardManager manager = Bukkit.getScoreboardManager();


    List<BoardInfo> boardInfos = new ArrayList<BoardInfo>();
    //一対一対応
    Map<Player,Scoreboard> scoreboardsOfPlayers = new HashMap<Player, Scoreboard>();

    /**
     * 指定したプレイヤーのこのクラスがが生成しているスコアボードを表示します。
     * 今のところうまく機能しませんが、updateScoreによって、表示することができます。
     * @param player
     */
    public void showTo(Player player) {
        if(this.scoreboardsOfPlayers.containsKey(player)) {
            player.setScoreboard(this.scoreboardsOfPlayers.get(player));
        } else {
            Bukkit.getLogger().warning("そのプレイヤーのスコアボードが存在しないので、表示することができません");
        }
    }

    /**
     * 指定したプレイヤーのスコアボードを非表示にします。
     * MainScoreboardが設定されている場合、そちらを表示します。
     * @param player
     */
    public void hideFrom(Player player) {
        player.setScoreboard(manager.getMainScoreboard());
    }

    /**
     * このメソッドは、指定したスコアを更新し、プレイヤーの表示に反映させます。
     * 更新のたびに、最新のBoardInfoを変換して、スコアボードに反映させる仕組みです。
     *
     * @param boardInfo : 更新したいスコアを持つBoardInfo
     * @param scorename : 更新したいスコアの名前
     * @param newVal : 更新したいスコアの更新後の値
     */
    public void updateScore(BoardInfo boardInfo, String scorename, int newVal) {
        if(this.boardInfos.contains(boardInfo)) {
            Player player = boardInfo.getPlayer();
            Scoreboard scoreboard = scoreboardsOfPlayers.get(player);
            Objective objective = scoreboard.getObjective(player.getDisplayName());
            if(objective != null) {
                BoardScore boardScore = boardInfo.getScore(scorename);
                if(boardScore != null) {
                    objective.unregister();
                    boardScore.setValue(newVal);
                    registerNewObjectiveFromBoardInfo(boardInfo,scoreboard);
                } else {
                    Bukkit.getLogger().warning("メソッド: updateScore において、存在しないスコア名を指定されました。");
                }
            } else {
                Bukkit.getLogger().warning("ObjectiveがNullです。");
            }
            player.setScoreboard(scoreboard);
        } else {

            Bukkit.getLogger().warning("そのプレイヤーのBoardInfoが存在しないので、スコアボードを更新することができません");
        }

    }

    /**
     * このクラス内で使うためのプライベートメソッドです。
     * BoardInfoからObjectiveを作り出します。
     * register系のメソッドは、1回のみ呼び出すことを想定しています。
     *
     * @param boardInfo
     * @param scoreboard
     */
    private void registerNewObjectiveFromBoardInfo(BoardInfo boardInfo,Scoreboard scoreboard) {
        Objective o = scoreboard.registerNewObjective(boardInfo.getPlayer().getDisplayName(),"dummy",boardInfo.title);
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (BoardScore score : boardInfo.scores) {
            Score s = o.getScore(score.name + " : " + score.getValue());
            s.setScore(score.priority);
        }
    }

    /**
     * 指定したプレイヤーのBoardInfoを、
     * メソッドのFinal変数による固定された形で生成し、返します。
     * register系Mメソッドは、フィールドのリストに情報を追加するため、一回のみの呼び出しを想定しています。
     * @param player
     * @return : このメソッドによる固定的なBoardInfo
     */
    public void registerBoardInfo(Player player) {
        final String title = "Title";
        final List<BoardScore> scores =  new ArrayList<BoardScore>(Arrays.asList(new BoardScore("Kills",0,3),new BoardScore("Coins",0,2)));
        BoardInfo ret = new BoardInfo(player,title,scores);
        if(findBoardInfo(player) == null) {
            this.boardInfos.add(ret);
            Bukkit.getLogger().info(player.getName() + "のBoardInfoを作成しました。");
        } else {
            Bukkit.getLogger().info("registerBoardInfo: そのプレイヤーのBoardInfoはすでに登録されています。");
        }
    }

    // ↑↓対のメソッド

    /**
     * BoardInfoから、スコアボードを生成し、このクラスのフィールドのHashMapに登録します。
     * プレイヤーのスコアボードを更新、表示するためには、まずこのメソッドを呼び出す必要があります。
     * @param boardInfo
     */
    public void registerScoreBoard(BoardInfo boardInfo) {
        if(this.scoreboardsOfPlayers.containsKey(boardInfo.getPlayer())) {
            Bukkit.getLogger().warning("そのプレイヤーのMCBSが管理するスコアボードはすでに登録されています。");
        } else if(boardInfo != null) {
            Scoreboard ret = manager.getNewScoreboard();
            registerNewObjectiveFromBoardInfo(boardInfo,ret);
            scoreboardsOfPlayers.put(boardInfo.getPlayer(), ret);
        } else {
            Bukkit.getLogger().info("Method: registerScoreboard　において、NullのboardInfoが渡されました.");
        }
    }

    /**
     * 指定したプレイヤーのスコアボードを取得します。
     * @param player
     * @return
     */
    public Scoreboard getScoreBoard(Player player) {
        return this.scoreboardsOfPlayers.get(player);
    }

    /**
     * 指定したプレイヤーのスコアボードの情報を持つBoardInfoを取得します。
     * @param player
     * @return
     */
    public BoardInfo findBoardInfo(Player player) {
        BoardInfo ret = null;
        for(BoardInfo boardInfo : this.boardInfos) {
            if(boardInfo.getPlayer().getName() == player.getName())
                ret = boardInfo;
        }
        if(ret != null) {
            return ret;
        } else {
            Bukkit.getLogger().warning("メソッド findBoardInfo(Player player) が Nullを返しています。");
            Bukkit.getLogger().info("BoardInfosのサイズ:" + boardInfos.size() );
            return null;
        }
    }

    /**
     * 指定したプレイヤーのスコアボードを削除します。
     * 同時に、スコアボードを非表示にします。
     * もう一度登録するには、 registerScoreboardメソッドを呼んでください。
     * @param player
     */
    public boolean removeScoreboard(Player player) {
        hideFrom(player);
        if(player != null) {
            this.scoreboardsOfPlayers.remove(player);
            this.boardInfos.remove(findBoardInfo(player));
            return true;
        } else {
            Bukkit.getLogger().info("removeScoreBoardにて、存在しないプレイやーが指定されています。");
            return false;
        }
    }



    /**
     * スコアボードの表示の形式を規定するためのクラスです。
     * これを変換してスコアボードに表示することになります。
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

        private BoardScore getScore(String name) {
            for (BoardScore score : this.scores) {
                if (name.equalsIgnoreCase(score.name)) {
                    return score;
                }
            }
            return null;
        }
        public void removeScore(String scoreName) {
            scores.remove(getScore(scoreName));
        }
        public void addScore(BoardScore boardScore) {
            scores.add(boardScore);
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
         *スコアボードの項目の情報を持つクラス
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
        public void
        setValue(int newValue) {
            this.val = newValue;
        }
    }

}
