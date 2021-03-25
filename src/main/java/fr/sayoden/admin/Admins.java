package fr.sayoden.admin;

import fr.sayoden.SSkyblock;
import fr.sayoden.util.Utils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Admins {

    private SSkyblock plugin;
    private Map<String, String> admins;

    public Admins(SSkyblock plugin) {
        this.plugin = plugin;
        this.admins = registerAdmins();
    }

    /**
     * Permet de register tous les admins du fichier
     * config.yml
     *
     * @return - Map String, String
     */
    public Map<String, String> registerAdmins(){
        plugin.getLogger().info("Recuperation des admins");
        Map<String, String> map = new HashMap<>();
        YamlConfiguration file = Utils.loadYamlFile("config.yml");

        if(file.contains("admins")){
            for(String name : file.getConfigurationSection("admins").getValues(false).keySet()){
                try{
                    map.put(name, file.getString("admins." + name));
                }catch (Exception e){
                    plugin.getLogger().warning("Erreur lors de l'importation des admins");
                }
            }
        }
        return map;
    }

    /**
     * Permet de voir si un joueur
     * est enregistrer dans la liste
     * des administrateurs
     */
    public boolean playerIsAnAdmin(UUID playerUUID){
        return admins.containsKey(playerUUID.toString());
    }

    /**
     * Permet de voir si le joueur
     * a rentré le bon code d'auth
     *
     * @param playerUUID
     * @param code
     * @return
     */
    public boolean correctSecurityCode(UUID playerUUID, String code){
        if(playerIsAnAdmin(playerUUID)){
            return admins.get(playerUUID.toString()).equals(code);
        }else return false;
    }
}
