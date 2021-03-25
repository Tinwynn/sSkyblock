package fr.sayoden.essential;

import fr.sayoden.SSkyblock;
import fr.sayoden.player.PlayerCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMoney implements CommandExecutor {

    private SSkyblock plugin;
    private PlayerCache cache;

    public CommandMoney(SSkyblock plugin) {
        this.plugin = plugin;
        this.cache = plugin.getCache();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player)sender;
            double money = cache.getPlayerMoney(player.getUniqueId());

            switch (args.length){
                case 0:
                    player.sendMessage(plugin.createMessage("moneyShow").replace("%money%", "" + money));
                    return true;

            }
        }
        return false;
    }


}
