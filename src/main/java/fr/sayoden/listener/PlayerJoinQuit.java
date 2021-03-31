package fr.sayoden.listener;

import fr.sayoden.SSkyblock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuit implements Listener {

    private SSkyblock plugin;

    public PlayerJoinQuit(SSkyblock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event){
        plugin.getCache().addPlayer(event.getPlayer().getUniqueId());
        Player player = event.getPlayer();
        if(!player.hasPlayedBefore()){
            plugin.addInscrit();
            event.setJoinMessage(plugin.createMessage("newPlayer").replace("%player%", player.getName()).replace("%nbPlayer%", String.valueOf(plugin.getInscrit())));
        }else{
            event.setJoinMessage(plugin.createMessage("playerJoin").replace("%player%", player.getName()));
        }
        if(player.isOp()){
            player.setAllowFlight(true);
            player.sendMessage(plugin.createMessage("permFly"));
        }
    }

    @EventHandler
    public void playerLeaveEvent(PlayerQuitEvent event){
        plugin.getCache().removeOnlinePlayer(event.getPlayer().getUniqueId());
        event.setQuitMessage(plugin.createMessage("playerLeave").replace("%player%", event.getPlayer().getName()));
    }
}
