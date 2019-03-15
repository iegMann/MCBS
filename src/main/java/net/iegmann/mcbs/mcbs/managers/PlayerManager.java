package net.iegmann.mcbs.mcbs.managers;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * PlayerManagerクラスはプレイヤーの参加・退出といった一連の操作をはじめにゲームに関わるプレイヤーの管理を行うクラスです.
 * 但し、このクラスは「プレイヤーそのものを管理」する目的でありプレイヤーの所属するチームを管理するTeamManagerクラスなどとは性質が異なります.
 * 書いている内容的に「これGameManagerクラスに統合しても良くない？」ってなりましたが、将来のことを考えて(あとGameManagerクラスをあんまり汚したくなくて)念のため分離しました.
 * 参加しているプレイヤーを取得する際は、PlayerManager#getPlayers()を利用してください.
 * また、プレイヤーがゲームに参加しているかどうか判定するときはPlayerManager#isParticipant(Player player)を利用してください.
 * プレイヤーはコマンド{@code /join}を実行することでゲームに参加し、{@code /leave}を実行することでゲームから離脱します.
 * 詳しくはcommandパッケージ下JoinクラスやLeaveクラスを参照してください（執筆時点で未完成）.
 * また、ゲームに参加しているプレイヤーがログアウトした際もゲームから離脱します.
 * この監視処理についてはListenerインタフェースを継承するという特性上処理を分けています.
 * 別途eventsパッケージ下のクラス（これまた執筆時点で未完成）を参照してください.
 *</b>
 * </b>既知のミス・PlayerManager#add,removeメソッド内の条件処理の順番.
 * 眠気に任せて書いたのでちょっと良くない感じになってる.
 * JavaDoc通りの処理にするために直さないといけないが、コンソールへのエラーメッセージの通知がちょいとおかしくなるだけなので緊急事項ではない.
 */
public class PlayerManager {
    private HashMap<UUID,Player> players;
    private boolean allowPlayerJoiningDuringGame;
    PlayerManager(boolean allowPlayerJoiningDuringGame){
        players = new HashMap<>();
        this.allowPlayerJoiningDuringGame = allowPlayerJoiningDuringGame;
    }

    /**
     * プレイヤーがゲームに参加させる際に利用するメソッドです.
     * ゲーム進行中の場合は、TeamManagerクラスにも処理を飛ばします.
     * @param player ゲームに参加させたいプレイヤー
     * @return playerがnullの場合{@code Result.NO_SUCH_PLAYER}、playerが既に参加している場合{@code Result.ALREADY_JOINED}、GameManagerが開始していない場合{@code Result.NOT_STARTED}、ゲームの進行中かつ途中参加が許されていない場合{@code Result.ALREADY_STARTED}、正常に追加した場合（左記以外の場合）{@code Result.SUCCESS}
     */
    public Result add(Player player) {
        if(player==null){
            return Result.NO_SUCH_PLAYER;
        }else if(players.containsKey(player.getUniqueId())){
            return Result.ALREADY_JOINED;
        }else {
            GameManager manager = GameManager.getInstance();
            if(manager.getState() == GameManager.State.IDLE){
                return Result.NOT_STARTED;
            }else if(manager.getState() == GameManager.State.ONGOING){
                if(allowPlayerJoiningDuringGame){
                    players.put(player.getUniqueId(), player);
                    //ここにTeamManagerクラスに参加したプレイヤーを通知する処理！
                    return  Result.SUCCESS;
                }else{
                    return Result.ALREADY_STARTED;
                }
            }else{
                players.put(player.getUniqueId(), player);
                return Result.SUCCESS;
            }
        }
    }

    /**
     * プレイヤーをゲームから離脱させる際に利用するメソッドです.
     * ゲーム進行中にはTeamManagerクラスにも処理を飛ばします.
     * @param player ゲームから離脱させたいプレイヤー
     * @return playerがnullまたはゲームに参加していない場合{@code Result.NO_SUCH_PLAYER}、GameManagerが開始していない場合{@code Result.NOT_STARTED}、正常に離脱した場合（左記以外の場合）{@code Result.SUCCESS}
     */
    public Result remove(Player player){
        if(player==null || players.containsKey(player.getUniqueId())){
            return Result.NO_SUCH_PLAYER;
        }else {
            GameManager manager = GameManager.getInstance();
            if(manager.getState() == GameManager.State.IDLE){
                return Result.NOT_STARTED;
            }else if(manager.getState() == GameManager.State.ONGOING){
                Player removed = players.remove(player.getUniqueId());
                //ここにTeamManagerクラスに離脱したプレイヤーを通知する処理！
                return Result.SUCCESS;
            }else{
                players.remove(player.getUniqueId());
                return Result.SUCCESS;
            }
        }
    }
    public enum Result{
        SUCCESS,NO_SUCH_PLAYER,ALREADY_JOINED,ALREADY_STARTED,NOT_STARTED
    }
}
