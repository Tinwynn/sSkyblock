package fr.sayoden.util.timer;

import fr.sayoden.SSkyblock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class Timer implements Listener {

    private String name;
    private UUID idTimer;
    private long delay, start;
    private SSkyblock plugin;

    public Timer(String name, long delay, long start, SSkyblock plugin) {
        this.name = name;
        this.idTimer = new UUID(5,5);
        this.delay = delay;
        this.start = start;
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    public UUID getIdTimer() {
        return idTimer;
    }

    public long getDelay() {
        return delay;
    }

    public long getStart() {
        return start;
    }

    public SSkyblock getPlugin(){
        return plugin;
    }
}
