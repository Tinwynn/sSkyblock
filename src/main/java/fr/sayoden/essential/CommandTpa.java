package fr.sayoden.essential;

import fr.sayoden.SSkyblock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpa implements CommandExecutor {
    private SSkyblock plugin;

    public CommandTpa(SSkyblock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;


        }
        return false;
    }
}
