package fr.sayoden;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenerTest implements Listener {

    private SSkyblock plugin = SSkyblock.getPlugin();

    public ListenerTest() {
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        plugin.getCache().addPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event){
        plugin.getCache().removeOnlinePlayer(event.getPlayer().getUniqueId());
    }


}
