package net.iegmann.mcbs.mcbs.commands;

import net.iegmann.mcbs.mcbs.managers.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * クラスStartはゲームに参加するプレイヤーの募集後にゲームを開始する際のコマンド{@code /start}を定義するものです.
 * 参加人数が足りている場合は{@link net.iegmann.mcbs.mcbs.managers.GameManager}内のフラグ{@code state}が{@code State.QUEUEING}から{@code State.PREPARING}へ移行します.
 * 参加人数が足りない場合はゲームは開始されません（フラグが移行しない）.
 * </b>
 * </b>注：書きかけです
 */
public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("start")){
            GameManager gameManager = GameManager.getInstance();
            if(gameManager.getState() == GameManager.State.QUEUEING){
                //書きかけ.
            }
        }
        return false;
    }
}
