package fr.sayoden;

import fr.sayoden.admin.Admins;
import fr.sayoden.admin.CommandAdmin;
import fr.sayoden.essential.CommandFly;
import fr.sayoden.essential.CommandHome;
import fr.sayoden.essential.CommandMoney;
import fr.sayoden.listener.PlayerJoinQuit;
import fr.sayoden.player.PlayerCache;
import fr.sayoden.util.Messages;
import fr.sayoden.util.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class SSkyblock extends JavaPlugin {

    private final String CONFIG_FILE = "config.yml";
    private final String MESSAGE_FILE = "messages.yml";

    private static SSkyblock instance;
    private static PlayerCache cache;
    private static Messages messages;
    private static Admins admins;

    private static List<UUID> listNeedCode = new ArrayList<>();

    private static int nbInscrit;



    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("sSkyblock vient de s'allumer correctement");
        cache = new PlayerCache(this);
        messages = new Messages(this);
        admins = new Admins(this);
        setInscrit();

        getServer().getPluginManager().registerEvents(new PlayerJoinQuit(this), this);
        getServer().getPluginManager().registerEvents(new CommandAdmin(this), this);


        registerCommands();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        saveInscrit();
        getLogger().info("sSkyblock vient de s'éteindre correctement");
        super.onDisable();
    }

    /**
     * Permet d'enregistrer les commandes
     */
    public void registerCommands(){
        getCommand("sethome").setExecutor(new CommandHome(this));
        getCommand("home").setExecutor(new CommandHome(this));
        getCommand("delhome").setExecutor(new CommandHome(this));
        getCommand("money").setExecutor(new CommandMoney(this));
        getCommand("niceblockadmin").setExecutor(new CommandAdmin(this));
        getCommand("fly").setExecutor(new CommandFly(this));

    }

    /**
     * Permet de créer un message dans le tchat
     * avec le prefix et le message
     *
     * @return - String
     */
    public String createMessage(String name){
        try{
            if(name.equals("newPlayer") ||
            name.equals("playerJoin") ||
            name.equals("playerLeave")){
                return messages.getMessages().get(name);
            }
            return messages.getPrefix() + " " + messages.getMessages().get(name);
        }catch (Exception e){
            getLogger().warning("Impossible de récuperer le contenu du message: " + name);

        }
        return messages.getPrefix() + "§cERREUR MESSAGE";
    }

    public String createMessage(boolean isPermDeny){
        return "§cVous n'avez pas la permission de faire cette commande.";
    }

    public static SSkyblock getPlugin(){
        return instance;
    }
    public PlayerCache getCache(){
        return cache;
    }
    public Admins getAdmins() { return admins; }

    public void setListNeedCode(List<UUID> listNeedCode) {
        SSkyblock.listNeedCode = listNeedCode;
    }

    public List<UUID> getListNeedCode() {
        return listNeedCode;
    }

    public int getInscrit(){
        return nbInscrit;
    }

    public void setInscrit(){
        YamlConfiguration file = Utils.loadYamlFile("config.yml");

        nbInscrit = file.getInt("nbInscrit");
    }

    public void addInscrit(){
        nbInscrit++;
    }

    public void saveInscrit(){
        YamlConfiguration file = Utils.loadYamlFile("config.yml");

        if(file.contains("nbInscrit")){
            file.set("nbInscrit", nbInscrit);
            Utils.saveYamlFile(file, "config.yml");
        }
    }
}
