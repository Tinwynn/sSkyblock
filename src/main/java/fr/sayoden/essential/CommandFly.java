package fr.sayoden.essential;

import fr.sayoden.SSkyblock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFly implements CommandExecutor {
    private SSkyblock plugin;

    public CommandFly(SSkyblock plugin){
        this.plugin= plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.isOp()){
                if(player.getAllowFlight()){

                    player.setAllowFlight(false);
                    player.sendMessage(plugin.createMessage("flyOff"));
                }else{
                    player.setAllowFlight(true);
                    player.sendMessage(plugin.createMessage("fly"));
                }
                return true;
            }
            player.sendMessage(plugin.createMessage(true));
        }
        return false;
    }
}
