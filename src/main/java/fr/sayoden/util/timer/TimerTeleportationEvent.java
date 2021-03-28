package fr.sayoden.util.timer;

import fr.sayoden.SSkyblock;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TimerTeleportationEvent implements Listener {

    private SSkyblock plugin;

    public TimerTeleportationEvent(SSkyblock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        plugin.getServer().getLogger().info("Event good");
        if(plugin.getListPlayersInTeleportation().containsKey(event.getPlayer().getUniqueId())){
            List<Boolean> list = plugin.getListPlayersInTeleportation().get(event.getPlayer().getUniqueId());

            boolean playerMove = list.get(0);
            boolean isInTeleportation = list.get(1);


            if(!playerMove && isInTeleportation){
                Location from = event.getFrom();
                if(from.getZ() != event.getTo().getZ() && from.getX() != event.getTo().getX()){
                    List<Boolean> newList = new ArrayList<>();
                    newList.add(true);
                    newList.add(true);
                    plugin.getListPlayersInTeleportation().replace(event.getPlayer().getUniqueId(), list, newList);


                    List<UUID> cancelList = plugin.getCancelTp();
                    if(!cancelList.contains(event.getPlayer().getUniqueId())){
                        cancelList.add(event.getPlayer().getUniqueId());
                        plugin.setCancelTp(cancelList);
                    }
                }
            }
        }


    }


}
