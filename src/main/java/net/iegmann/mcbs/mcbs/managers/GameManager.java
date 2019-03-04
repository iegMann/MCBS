package net.iegmann.mcbs.mcbs.managers;

import net.iegmann.mcbs.mcbs.MCBS;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * GameManagerクラスはゲームの参加者募集段階から終了までを管理します.
 * Singletonパターンによりただ一つ生成されたGameManagerインスタンスはSpigotサーバーの起動中は常にプールされています.
 * GameManagerインスタンスを取得する際は{@code GameManager#getInstance()}を利用してください.
 * GameManagerクラスの役割は別に存在するManagerクラス同士を橋渡しすることです.
 * GameManagerクラスに直接的な処理は記述せず、なるべく別のManagerクラスに処理を委譲するようにしてください.
 * @author iegMann
 */
public class GameManager {
    private static GameManager gameManager;

    /**
     * インスタンスの初期化をします.
     * Singletonパターンの為、インスタンスを取得する際は{@code GameManager#getInstance}を利用してください.
     */
    private GameManager(){
        state = State.IDLE;
        plugin = MCBS.getInstance();
    }

    /**
     * {@code GameManager#getInstance()}メソッドでプールされているGameManagerインスタンスを取得することができます.
     * インスタンスが存在しない場合は新しく生成した後、そのインスタンスを返します.
     * 但し、新しく生成されるタイミングは基本的にプラグインの有効時、すなわち{@code MCBS#onEnable()}の直後です.
     * @return {@code GameManager}のプールされた唯一のインスタンス
     */
    public static GameManager getInstance(){
        if(gameManager == null){
            gameManager = new GameManager();
        }
        return gameManager;
    }

    private State state;
    private JavaPlugin plugin;

    /**
     * {@code GameManager#start()}でゲームの募集から終了までの一連の処理を開始します.
     * ゲームは1つのサーバーにつき1つのみ行われる（多重に管理できない）ことに留意してください.
     * @return ゲームを正常に開始した場合は{@code true}、その他（既に開始している等）の場合{@code false}
     */
    public boolean start(){
        try {
            if (state != State.IDLE) {
                return false;
            }
            state = State.QUEUEING;
            queueing();
            return true;
        }catch(Exception e){
            plugin.getLogger().warning(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * このメソッドにゲーム開始前の待機中における処理を記述します.
     * 外部からコマンドでゲーム開始の命令がされると、{@code state}（GameManageの状態）が{@code State.QUEUEING}から{@code State.PREPARING}に移行します.
     * 当該コマンドについては{@link MCBS}を参照のこと.
     * このメソッドでは、{@code state}が移行したかどうかを20ticksおきに監視し、移行が検知された段階で{@code GameManager#preparing()}を呼び出します.
     */
    private void queueing(){
        BukkitRunnable runner = new BukkitRunnable() {
            @Override
            public void run() {
                if(state ==State.PREPARING){
                    preparing();
                    this.cancel();
                    return;
                }
            }
        };
        runner.runTaskTimer(plugin,0,20);
    }

    /**
     * このメソッドではゲーム開始にあたっての準備を行います.
     * 以下の順にゲーム開始の準備をします.
     * <br>TeamManagerインスタンスを生成、参加者のリストを渡す.
     * <br>configから試合の設定を読み込む.
     * <br>ゲームを開始する、つまり{@code GameManager#onGoing()}を呼び出す.
     */
    private void preparing(){
        /*
        ここにゲーム開始準備の処理を記述
         */
        state = State.ONGOING;
        onGoing();
    }

    /**
     * このメソッドにゲーム中の処理を記述します.
     * ゲームの終了条件が満たされているかどうかを20ticksごとに検知します.
     * 終了条件が満たされたか、時間切れした際に{@code GameManager#result()}を呼び出します.
     */
    private void onGoing(){
        BukkitRunnable runner = new BukkitRunnable() {
            @Override
            public void run() {
                /*
                ここに監視処理を記述（監視処理専用のクラスを作ったほうが色々応用できそうだが...#にわとりメモ）
                 */
            }
        };
        runner.runTaskTimer(plugin,0,20);
    }

    /**
     * このメソッドにゲーム終了時の処理を記述します.
     * 勝敗の通知や報酬などの処理はここに記述してください.
     * 全ての処理が完了した段階で、{@code state}は{@code State.IDLE}に移行、ゲームの実行を待機する初めの状態に戻ります.
     */
    private void result(){
        /*
        ここにゲーム終了時の処理を記述
         */
        state = State.IDLE;
    }

    /**
     * ゲームの進行状況を示す列挙型です.
     */
    protected enum State{
        /**
         * GameManagerが実行されていない状態を示します.
         */
        IDLE,
        /**
         * ゲームの募集中の段階を示します.
         */
        QUEUEING,
        /**
         * ゲームの開始前の設定をしている段階を示します.
         */
        PREPARING,
        /**
         * ゲームが進行中であることを示します.
         */
        ONGOING
    }
}
