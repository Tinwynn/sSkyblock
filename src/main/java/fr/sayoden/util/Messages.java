package fr.sayoden.util;

import fr.sayoden.SSkyblock;
import fr.sayoden.player.Home;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Messages {

    private SSkyblock plugin;
    private final Map<String, String> messages;

    public Messages(SSkyblock plugin) {
        this.plugin = plugin;
        this.messages = registerMessages();
    }

    /**
     * Permet de register tous les messages du fichier
     * messages.yml
     *
     * @return - Map String, String
     */
    public Map<String, String> registerMessages(){
        plugin.getLogger().info("Enregistrement des messages depuis le fichier messages.");
        Map<String, String> map = new HashMap<>();
        YamlConfiguration file = Utils.loadYamlFile("messages.yml");

        if(file.contains("messages")){
            for(String name : file.getConfigurationSection("messages").getValues(false).keySet()){
                try{
                    plugin.getLogger().info("debug 1: " + name);
                    map.put(name, file.getString("messages." + name));
                }catch (Exception e){
                    plugin.getLogger().warning("Erreur lors de l'importation des messages");
                }
            }
        }

        return map;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public String getPrefix(){
        return messages.get("prefix");
    }
}
