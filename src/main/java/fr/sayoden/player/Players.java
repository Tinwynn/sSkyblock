package fr.sayoden.player;

import fr.sayoden.SSkyblock;
import fr.sayoden.util.Utils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Permet d'avoir accès aux
 * différentes informations
 * du joueur
 */
public class Players {

    private final SSkyblock plugin;

    private YamlConfiguration playerInfo;
    private String playerName;
    private boolean hasIsland;
    private String islandLocation;
    private List<Home> homeList;
    private double money;
    private List<Friend> friendList;
    private UUID uuid;
    private int deaths;
    private int kills;

    /**
     *
     * @param plugin - SSkyBlock
     * @param uuid - player's uuid
     * @throws java.io.IOException - if uuid's player is null
     */
    public Players(final SSkyblock plugin, final UUID uuid) throws IOException {
        this.plugin = plugin;
        if(uuid == null) throw new IOException("player's uuid is null !");
        this.uuid = uuid;
        this.hasIsland = false;
        this.islandLocation = "";
        this.homeList = new ArrayList<>();
        this.money = 500;
        this.friendList = new ArrayList<>();
        this.playerName = "";
        this.deaths = 0;
        this.kills = 0;

        load(uuid);
    }

    /**
     * Récupere le joueur dans le systeme de fichier
     * Si le joueur n'existe pas, il sera créé
     *
     * @param uuid - player's uuid
     */
    public void load(UUID uuid){
        playerInfo = Utils.loadYamlFile("players/" + uuid.toString() + ".yml");

        this.playerName = playerInfo.getString("playerName", "");
        if(playerName.isEmpty()){
            Player player = plugin.getServer().getPlayer(uuid);

            //True = Entity is NPC
            if(player != null && player.hasMetadata("NPC")){
                playerName = player.getUniqueId().toString();
            }else{
                playerName = uuid.toString();
                try{
                    OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
                    if(offlinePlayer != null && offlinePlayer.getName() !=null) playerName = offlinePlayer.getName();
                }catch(Exception e){
                    plugin.getLogger().severe("Impossible de trouver le pseudo de l'uuid: "+uuid.toString());
                    playerName = uuid.toString();
                }
            }
        }

        this.hasIsland = playerInfo.getBoolean("hasIsland", false);
        this.islandLocation = playerInfo.getString("islandLocation", "");

        if(playerInfo.contains("homeLocations")){
            for(String name : playerInfo.getConfigurationSection("homeLocations").getValues(false).keySet()){
                try{
                    Location loc = Utils.getLocationString(playerInfo.getString("homeLocations." + name));
                    this.homeList.add(new Home(loc, name));
                }catch (Exception e){
                    plugin.getLogger().warning("Erreur lors de l'importation des homes de: " + playerName);
                }
            }
        }

        this.deaths = playerInfo.getInt("death", 0);
        this.kills = playerInfo.getInt("kills", 0);
        this.money = playerInfo.getDouble("money", 500);

    }

    /**
     * Permet de sauvegarder un joueur
     */
    public void save(){
        plugin.getLogger().info("Sauvegarde du joueur: " + playerName);

        playerInfo.set("playerName", playerName);
        playerInfo.set("hasIsland", hasIsland);
        playerInfo.set("islandLocation", islandLocation);
        playerInfo.set("homeLocations", null);
        for(Home home : homeList){
            playerInfo.set("homeLocations." + home.getName(), Utils.getStringLocation(home.getLocation()));
        }
        playerInfo.set("deaths", deaths);
        playerInfo.set("kills", kills);
        playerInfo.set("money", money);


        Utils.saveYamlFile(playerInfo, "players/" + uuid.toString() + ".yml");
    }

    public Home findHomeWithName(String name){
        for(Home home : homeList){
            if(home.getName().equals(name)) return home;
        }

        return null;
    }

    public void setHasIsland(boolean hasIsland) {
        this.hasIsland = hasIsland;
    }

    public void setHomeList(List<Home> homeList) {
        this.homeList = homeList;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public String getPlayerName() {
        return playerName;
    }

    public YamlConfiguration getPlayerInfo() {
        return playerInfo;
    }

    public boolean isHasIsland() {
        return hasIsland;
    }

    public String getIslandLocation() {
        return islandLocation;
    }

    public List<Home> getHomeList() {
        return homeList;
    }

    public double getMoney() {
        return money;
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }
}
