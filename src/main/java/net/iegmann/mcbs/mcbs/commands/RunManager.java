package net.iegmann.mcbs.mcbs.commands;

import net.iegmann.mcbs.mcbs.managers.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * クラスRunManagerはGameManagerを起動するコマンド{@code /run}を定義するものです.
 * {@code /run}コマンドによりGameManager#start()が実行されます.
 * 正常に実行されない場合、エラーメッセージを{@link CommandSender}に返します.
 */
public class RunManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("run")){
            if(GameManager.getInstance().start()){
                return true;
            }else{
                commandSender.sendMessage("[MCBS] Error: The game did not start correctly.");
                return false;
            }
        }
        return false;
    }
}
