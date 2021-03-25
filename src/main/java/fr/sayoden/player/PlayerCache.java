package fr.sayoden.player;

import fr.sayoden.SSkyblock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

/**
 * Permet d'effectuer différentes actions
 * sur le joueur
 * Permet d'enregistrer des joueurs dans
 * le cache du serveur
 * @author Les étoiles vagabondes
 */
public class PlayerCache {

    private static SSkyblock plugin;
    private Map<UUID, Players> playersCache = new HashMap<>();

    public PlayerCache(final SSkyblock plugin) {
        this.plugin = plugin;
        for(Player p : plugin.getServer().getOnlinePlayers()){
            if(p.getUniqueId() != null){
                try{
                    final Players playerInfos = new Players(plugin, p.getUniqueId());
                    playerInfos.save();
                    playersCache.put(p.getUniqueId(), playerInfos);

                    plugin.getLogger().info("Ajout d'un nouveau joueur dans le playerCache: " + p.getUniqueId());
                }catch(Exception e){
                    plugin.getLogger().severe("Une erreur est survenue");
                }
            }
        }
    }

    /**
     *
     * @return list of all online cached player
     */
    public List<UUID> getOnlineCachePlayers(){
        List<UUID> list = plugin.getServer().getOnlinePlayers().stream()
                .filter(p -> playersCache.containsKey(p.getUniqueId()))
                .map(Entity::getUniqueId)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(list);
    }

    /*
    * Methods du cache
     */

    /**
     * Ajout d'un joueur dans le cache
     *
     * @param playerUUID - player's UUID
     */
    public void addPlayer(final UUID playerUUID){
        if(!playersCache.containsKey(playerUUID)){
            try{
                final Players players = new Players(plugin, playerUUID);
                playersCache.put(playerUUID, players);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Suppression du joueur et enregistrement
     * de ses informations
     *
     * @param playerUUID
     */
    public void removeOnlinePlayer(final UUID playerUUID){
        if(playersCache.containsKey(playerUUID)){
            Players p = playersCache.get(playerUUID);
            plugin.getLogger().info(p.getPlayerName());
            playersCache.get(playerUUID).save();
            playersCache.remove(playerUUID);

            plugin.getLogger().info("Suppression du joueur: " + playerUUID);
        }
    }

    /**
     * Suppression de tous les joueurs
     */
    public void removeAllPlayers(){
        Map<UUID, Players> map = ImmutableMap.copyOf(playersCache);

        map.keySet().forEach(player -> map.get(player).save());
        playersCache.clear();

        plugin.getLogger().info("Suppression de tous les joueurs et sauvegarde.");
    }

    /**
     * Permet de récuperer l'object players pour
     * pouvoir get la money par exemple etc...
     *
     * @param playerUUID - player's UUID
     * @return - Players object
     */
    public Players getPlayerObject(UUID playerUUID){
        //Juste pour la sécurité
        addPlayer(playerUUID);

        return playersCache.get(playerUUID);
    }

    /**
     * Permet de changer la money du joueur
     *
     * @param playerUUID
     * @param amount
     */
    public void changeMoneyPlayer(UUID playerUUID, double amount){
        Players player = playersCache.get(playerUUID);

        if(amount > 0){
            player.setMoney(player.getMoney() + amount);
        }else{
            if(playerHasMoney(playerUUID, amount)) player.setMoney(player.getMoney() - amount);
        }

    }

    /**
     * Permet de vérifier si le
     * joueur a vraiment la somme
     * d'argent
     *
     * @param playerUUID
     * @param amount
     * @return
     */
    public boolean playerHasMoney(UUID playerUUID, double amount){
        return playersCache.get(playerUUID).getMoney() - amount >= 0;
    }

    /**
     * Permet de retourner un double
     * avec la money du joueur
     *
     * @param playerUUID
     * @return
     */
    public double getPlayerMoney(UUID playerUUID){
        return playersCache.get(playerUUID).getMoney();
    }


}
