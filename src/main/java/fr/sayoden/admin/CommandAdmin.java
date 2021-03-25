package fr.sayoden.admin;

import fr.sayoden.SSkyblock;
import fr.sayoden.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandAdmin implements CommandExecutor, Listener {

    private SSkyblock plugin;
    private Admins admins;
    private List<UUID> listNeedCode;
    private final Inventory inventory;

    public CommandAdmin(SSkyblock plugin) {
        this.plugin = plugin;
        this.admins = plugin.getAdmins();
        this.listNeedCode = plugin.getListNeedCode();
        this.inventory = Bukkit.createInventory(null, 9, "§8§l| §aAdministration");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player)sender;

            if(admins.playerIsAnAdmin(player.getUniqueId())){
                player.sendMessage(plugin.createMessage("needCode"));
                if(!listNeedCode.contains(player.getUniqueId())) listNeedCode.add(player.getUniqueId());
                return true;
            }else{
                player.sendMessage(plugin.createMessage("notAdmin"));
                return false;
            }

        }
        return false;
    }

    /**
     * Permet de voir si le code d'accès
     * du joueur est bon
     *
     * @param event
     */
    @EventHandler
    public void playerChatEvent(AsyncPlayerChatEvent event){
        if(listNeedCode.contains(event.getPlayer().getUniqueId())){
            listNeedCode.remove(event.getPlayer().getUniqueId());
            if(admins.correctSecurityCode(event.getPlayer().getUniqueId(), event.getMessage())){
                event.getPlayer().sendMessage(plugin.createMessage("goodCode"));
                event.getPlayer().openInventory(buildInventory());
            }else{
                event.getPlayer().sendMessage(plugin.createMessage("wrongCode"));
            }

            event.setCancelled(true);
        }
    }

    /**
     * Construction de l'inventaire de l'administration
     *
     */
    public Inventory buildInventory(){
        List<UUID> playersCached = plugin.getCache().getOnlineCachePlayers();
        int nb = playersCached.size();
        for(int i = 0; i < nb; i++){
            ItemStack item = new ItemBuilder(Material.PLAYER_HEAD, 1,(byte)2).toItemStack();
            SkullMeta skull = (SkullMeta) item.getItemMeta();
            skull.setOwningPlayer(Bukkit.getOfflinePlayer(playersCached.get(i)));
            skull.setDisplayName("§a> §l" + Bukkit.getOfflinePlayer(playersCached.get(i)).getName());
            item.setItemMeta(skull);
            inventory.addItem(item);
        }
        return inventory;
    }
}
