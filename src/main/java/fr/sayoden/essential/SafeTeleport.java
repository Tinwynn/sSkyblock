package fr.sayoden.essential;

import fr.sayoden.SSkyblock;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class SafeTeleport {

    private SSkyblock plugin;
    private String homeName;
    private Location location;
    private Player player;
    private long timeToTeleport;

    private BukkitTask task;

    public SafeTeleport(SSkyblock plugin, String homeName, Location location, Player player) {
        this.plugin = plugin;
        this.homeName = homeName;
        this.location = location;
        this.player = player;
        this.timeToTeleport = getTimeLong();
    }

    public long getTimeLong(){
        if(player.hasPermission("vip")) return 2L;
        if(player.isOp()) return 1L;
        return 5L;
    }

    public void teleport(){
        if(location == null) return;
        player.sendMessage(plugin.createMessage("teleport").replace("%timeToTeleport%", String.valueOf(timeToTeleport)));
        this.task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> teleportEntity(location),timeToTeleport * 20);
    }

    /**
     * Teleporte l'entité a la location
     */
    private void teleportEntity(final Location loc) {
        task.cancel();
        Entity entity = player;
        // Return to main thread and teleport the player
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                Vector velocity = entity.getVelocity();
                entity.teleport(loc);
                // Exit spectator mode if in it
                if (entity instanceof Player) {
                    Player player = (Player)entity;
                    if (plugin.getServer().getVersion().contains("1.7")) {
                        player.setGameMode(GameMode.SURVIVAL);
                    } else if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                        player.setGameMode(GameMode.SURVIVAL);
                    }
                } else {
                    entity.setVelocity(velocity);
                }

            }
        });

    }

    public BukkitTask getTask() {
        return task;
    }

}
