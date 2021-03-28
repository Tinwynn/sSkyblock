package fr.sayoden;

import fr.sayoden.admin.Admins;
import fr.sayoden.admin.CommandAdmin;
import fr.sayoden.essential.CommandHome;
import fr.sayoden.essential.CommandMoney;
import fr.sayoden.player.PlayerCache;
import fr.sayoden.util.Messages;
import fr.sayoden.util.timer.TimerTeleportation;
import fr.sayoden.util.timer.TimerTeleportationEvent;
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
    private static Map<UUID, List<Boolean>> listPlayersInTeleportation = new HashMap<>();
    private static List<UUID> cancelTp = new ArrayList<>();



    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("sSkyblock vient de s'allumer correctement");

        getServer().getPluginManager().registerEvents(new ListenerTest(), this);
        cache = new PlayerCache(this);
        messages = new Messages(this);
        admins = new Admins(this);

        getServer().getPluginManager().registerEvents(new CommandAdmin(this), this);
        getServer().getPluginManager().registerEvents(new TimerTeleportationEvent(this), this);

        registerCommands();
        super.onEnable();
    }

    @Override
    public void onDisable() {
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

    }

    /**
     * Permet de créer un message dans le tchat
     * avec le prefix et le message
     *
     * @return - String
     */
    public String createMessage(String name){
        try{
            return messages.getPrefix() + " " + messages.getMessages().get(name);
        }catch (Exception e){
            getLogger().warning("Impossible de récuperer le contenu du message: " + name);

        }
        return messages.getPrefix() + "§cERREUR MESSAGE";
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

    public Map<UUID, List<Boolean>> getListPlayersInTeleportation() {
        return listPlayersInTeleportation;
    }

    public void setListPlayersInTeleportation(Map<UUID, List<Boolean>> listPlayersInTeleportation) {
        SSkyblock.listPlayersInTeleportation = listPlayersInTeleportation;
    }

    public static List<UUID> getCancelTp() {
        return cancelTp;
    }

    public static void setCancelTp(List<UUID> cancelTp) {
        SSkyblock.cancelTp = cancelTp;
    }
}
