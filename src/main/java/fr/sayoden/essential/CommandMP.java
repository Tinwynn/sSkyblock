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
                try {
                    if(plugin.getCache().getUUIDWithName(args[0])){
                        Player receiver = Bukkit.getPlayer(args[0]);

                        String sm = "";
                        for (int i = 1; i < args.length; i++){
                            String arg = (args[i] + " ");
                            sm = (sm + arg);
                        }

                        player.sendMessage(plugin.createMessage("msgTo").replace("%player%",player.getName()).replace("%message%",sm).replace("%receiver%", receiver.getName()));
                        receiver.sendMessage(plugin.createMessage("msgFrom").replace("%receiver%",receiver.getName()).replace("%message%",sm).replace("%player%", player.getName()));
                    }
                } catch (Exception e) {
                    player.sendMessage(plugin.createMessage("playerNotConnected"));
                    e.printStackTrace();
                }

            }else{
                player.sendMessage(plugin.createMessage("msgArgs"));
            }
        }
        return false;
    }
}
