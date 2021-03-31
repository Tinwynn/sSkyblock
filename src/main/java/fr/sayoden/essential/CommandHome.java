package fr.sayoden.essential;

import fr.sayoden.SSkyblock;
import fr.sayoden.player.Home;
import fr.sayoden.player.PlayerCache;
import fr.sayoden.player.Players;
import fr.sayoden.util.timer.Timer;
import fr.sayoden.util.timer.TimerTeleportation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CommandHome implements CommandExecutor {

    private final SSkyblock plugin;
    private final PlayerCache cache;

    public CommandHome(SSkyblock plugin) {
        this.plugin = plugin;
        this.cache = plugin.getCache();
    }

    /**
     * Methode qui permet d'effectuer
     * la commande /home
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player)sender;
            Players playerObject = cache.getPlayerObject(player.getUniqueId());

            if(cmd.getName().equalsIgnoreCase("home") || cmd.getName().equalsIgnoreCase("homes")){
                switch (args.length){
                    case 0:
                        if(playerObject.getHomeList().size() == 0){
                            player.sendMessage(plugin.createMessage("noHome"));
                            return false;
                        }else{
                            StringBuilder homes = new StringBuilder(plugin.createMessage("homeList"));
                            for(Home home : playerObject.getHomeList()){
                                homes.append(" ").append(home.getName()).append(",");
                            }

                            player.sendMessage(homes.toString());
                        }
                        break;
                    case 1:
                        if(playerObject.getHomeList().size() > 0){
                            if(playerObject.findHomeWithName(args[0]) == null){
                                player.sendMessage(plugin.createMessage("homeNonReconnu"));
                            }else{
                                Home home = playerObject.findHomeWithName(args[0]);
                                SafeTeleport safeTp = new SafeTeleport(plugin, home.getName(), home.getLocation(), player);

                                new TimerTeleportation(UUID.randomUUID().toString(), safeTp.getTimeLong(), safeTp.getTimeLong(), plugin, playerObject.getUuid() ,safeTp);
                                //safeTp.teleport();
                            }

                        }

                }

            }else if(cmd.getName().equalsIgnoreCase("sethome")){
                List<Home> homes = playerObject.getHomeList();
                switch (args.length){
                    case 0:
                        player.sendMessage(plugin.createMessage("sethomeNoArg"));
                        return false;
                    case 1:
                        int nbHomes = playerObject.getHomeList().size();
                        for(Home home : homes){
                            if(home.getName().equals(args[0])){
                                homes.remove(home);
                                home.setLocation(player.getLocation());
                                homes.add(home);
                                playerObject.setHomeList(homes);
                                player.sendMessage(plugin.createMessage("replaceHome"));
                                return true;
                            }
                        }

                        if(!player.isOp()){
                            if(player.hasPermission("vip")){
                                if(nbHomes + 1 > 3){
                                    player.sendMessage(plugin.createMessage("noHaveManyHomes"));
                                    return false;
                                }
                            }else if(player.hasPermission("vip++")){
                                if(nbHomes + 1 > 4){
                                    player.sendMessage(plugin.createMessage("noHaveManyHomes"));
                                    return false;
                                }
                            }else{
                                if(nbHomes + 1 > 2){
                                    player.sendMessage(plugin.createMessage("noHaveManyHomes"));
                                    return false;

                                }
                            }
                        }

                }
                Home home = new Home(player.getLocation(),args[0]);
                deleteHome(homes, args[0], playerObject, player.getUniqueId());
                homes.add(home);
                playerObject.setHomeList(homes);
                player.sendMessage(plugin.createMessage("addHome").replace("%home%", home.getName()));
                return true;
            }else if(cmd.getName().equalsIgnoreCase("delhome")){
                switch (args.length){
                    case 0:
                        player.sendMessage(plugin.createMessage("delhomeNoArg"));
                        return false;
                    case 1:
                        if(playerObject.getHomeList().size() > 0){
                            if(playerObject.findHomeWithName(args[0]) == null){
                                player.sendMessage(plugin.createMessage("homeNonReconnu"));
                                return false;
                            }else{
                                String result = deleteHome(playerObject.getHomeList(), args[0], playerObject, player.getUniqueId());
                                if(!result.equals("")){
                                    player.sendMessage(plugin.createMessage("deleteHome").replace("%home%", result));
                                    return true;
                                }
                                player.sendMessage(plugin.createMessage("homeNonReconnu"));
                                return false;
                            }
                        }else{
                            player.sendMessage(plugin.createMessage("noHome"));
                            return false;
                        }
                }
            }
        }
        return false;
    }

    /**
     * Méthode permettant de supprimer un home
     * d'un joueur avec son nom, sa liste de homes
     * et le playerObject
     *
     */
    public String deleteHome(List<Home> playerHomes, String name, Players playerObject, UUID uuid){
        for(Home home : playerHomes){
            if(home.getName().equals(name)){
                List<Home> homes = playerObject.getHomeList();
                homes.remove(new Home(home.getLocation(), home.getName()));

                cache.getPlayerObject(uuid).setHomeList(homes);
                return home.getName();
            }
        }
        return "";
    }


}
