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
    private BukkitTask task;

    private double lastPosX, lastPosZ;

    public TimerTeleportation(String name, long delay, long start, SSkyblock plugin, UUID player, SafeTeleport teleportation) {
        super(name, delay, start, plugin);
        this.player = player;
        this.teleportation = teleportation;
        this.isInTeleportation = true;
        this.playerMove = false;
        this.lastPosX = Bukkit.getPlayer(player).getLocation().getX();
        this.lastPosZ = Bukkit.getPlayer(player).getLocation().getZ();
        teleportation.teleport();
        checkIfCancel();
    }

    public void run(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!playerMove){
                    task.cancel();
                    Bukkit.getOfflinePlayer(player).getPlayer().sendMessage(getPlugin().createMessage("teleportSucess"));
                }
                isInTeleportation = false;
            }
        }.runTaskLater(super.getPlugin(), teleportation.getTimeLong() * 20);
    }

    public void checkIfCancel(){
        run();
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(super.getPlugin(), () -> {
            getPlugin().getLogger().info("IL SE LANCE KAPPA KAPPA");
            if(isInTeleportation && Bukkit.getPlayer(player).isOnline()){
                Location loc = Bukkit.getPlayer(player).getLocation();
                if(loc.getX() != lastPosX || loc.getZ() != lastPosZ){
                    getPlugin().getLogger().info("IL SE LANCE ^^");
                    playerMove = true;
                    isInTeleportation = false;
                    teleportation.getTask().cancel();
                    task.cancel();
                    Bukkit.getOfflinePlayer(player).getPlayer().sendMessage(getPlugin().createMessage("teleportCancel"));
                }

            }
        }, 0, 5L);
    }
}
