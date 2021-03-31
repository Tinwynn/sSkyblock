package fr.sayoden.player;

import java.util.UUID;

public class Friend {

    private UUID player;
    private String otherPlayer;

    public Friend(UUID player, String otherPlayer) {
        this.player = player;
        this.otherPlayer = otherPlayer;
    }

    public UUID getPlayer() {
        return player;
    }

    public String getOtherPlayer() {
        return otherPlayer;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public void setOtherPlayer(String otherPlayer) {
        this.otherPlayer = otherPlayer;
    }
}
