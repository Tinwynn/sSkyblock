package fr.sayoden.util.timer;

import fr.sayoden.SSkyblock;
import fr.sayoden.essential.SafeTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TimerTeleportation extends Timer{

    private UUID player;
    private boolean playerMove;
    private boolean isInTeleportation;
    private SafeTeleport teleportation;
    private SSkyblock plugin;

    public TimerTeleportation(SSkyblock plugin){
        super(null, 0, 0, plugin);
        this.plugin = plugin;
    }

    public TimerTeleportation(String name, long delay, long start, SSkyblock plugin, UUID player, SafeTeleport teleportation) {
        super(name, delay, start, plugin);
        this.player = player;
        this.teleportation = teleportation;
        List<Boolean> list = new ArrayList<>();
        list.add(false);
        list.add(true);
        plugin.setListPlayersInTeleportation((Map<UUID, List<Boolean>>) plugin.getListPlayersInTeleportation().putIfAbsent(player, list));
        this.isInTeleportation = true;
        teleportation.teleport();
        run();
        cancel();
    }

    public void run(){
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOfflinePlayer(player).getPlayer().sendMessage(getPlugin().createMessage("teleportSucess"));
                isInTeleportation = false;
            }
        }.runTaskLater(super.getPlugin(), teleportation.getTimeLong() * 20);
    }

    public void cancel(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(SSkyblock.getCancelTp().contains(player)){
                    if(isInTeleportation){
                        if(Bukkit.getPlayer(player).isOnline()){
                            isInTeleportation = false;
                            teleportation.getTask().cancel();
                            Bukkit.getOfflinePlayer(player).getPlayer().sendMessage(getPlugin().createMessage("teleportWrong"));
                        }
                    }
                }

            }
        }.runTaskTimer(plugin, teleportation.getTimeLong() * 20, 10L);

    }



}
