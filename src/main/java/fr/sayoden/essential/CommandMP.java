package fr.sayoden.essential;

import fr.sayoden.SSkyblock;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class CommandMP implements CommandExecutor {
    private SSkyblock plugin;
    public CommandMP(SSkyblock plugin){ this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length > 1){
                if(Bukkit.getPlayer(args[0]).isOnline()){
                    Player receiver = Bukkit.getPlayer(args[0]);

                    String messageBase = "";

                    for(int i = 1; i < args.length; i++){
                        messageBase.concat(args[i] + " ");
                    }
                    player.sendMessage(plugin.createMessage("msgTo").replace("%receiver%",player.getName()).replace("%message%",messageBase));
                    receiver.sendMessage(plugin.createMessage("msgFrom").replace("%sender%",player.getName()).replace("%message%",messageBase));
                }else{
                    player.sendMessage(plugin.createMessage("playerNotConnected"));
                }

            }else{
                player.sendMessage(plugin.createMessage("msgArgs"));
            }
        }
        return false;
    }
}
