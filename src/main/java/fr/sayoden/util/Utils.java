package fr.sayoden.util;

import fr.sayoden.SSkyblock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Utils {

    private static SSkyblock plugin = SSkyblock.getPlugin();
    private static boolean midSave = false;
    private static boolean midLoad = false;
    private static final long TIMEOUT = 3000;
    private static Queue<PendingItem> saveQueue = new ConcurrentLinkedQueue<>();
    private static BukkitTask queueSaver;

    /**
     * Load a YAML file
     *
     * @param file - name of the file
     * @return - return YamlConfiguration
     */
    public static YamlConfiguration loadYamlFile(String file){
        File dataFolder = plugin.getDataFolder();
        File yamlFile = new File(dataFolder, file);

        YamlConfiguration config = null;

        if(yamlFile.exists()){
            midLoad = true;
            long infiniteLoop = System.currentTimeMillis();
            while(midSave && System.currentTimeMillis() < infiniteLoop +  TIMEOUT) {};
            try{
                config = new YamlConfiguration();
                config.load(yamlFile);
            }catch (Exception e){
                e.printStackTrace();
            }
            midLoad = false;
        }else{
            // Create the missing file
            config = new YamlConfiguration();
            if(!file.startsWith("players")){
                plugin.getLogger().info("Le fichier " + file + " n'a pas été trouvé. Création en cours...");
            }
            try{
                if(plugin.getResource(file) != null){
                    plugin.saveResource(file, false);
                    config = new YamlConfiguration();
                    config.load(yamlFile);
                }else{
                    config.save(yamlFile);
                }
            }catch(Exception e){
                plugin.getLogger().info("Impossible de créer le fichier " + file + " !");
            }
        }

        return config;
    }

    public static void saveYamlFile(YamlConfiguration yamlFile, String fileLocation){
        if(queueSaver == null){
            queueSaver = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                if(!plugin.isEnabled()){
                    queueSaver.cancel();
                }else if(!midLoad && !midSave && !saveQueue.isEmpty()){
                    PendingItem item = saveQueue.poll();
                    if(item != null){
                        midSave = true;
                        try {
                            Files.copy(item.getSource(), item.getDest(), StandardCopyOption.REPLACE_EXISTING);
                            Files.delete(item.getSource());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        midSave = false;
                    }
                }
            }, 0L, 1L);
        }

        save(yamlFile, fileLocation);
    }

    /**
     * Permet de sauvegarder un fichier Yaml
     *
     * @param yamlFile
     * @param fileLocation
     */
    private static void save(YamlConfiguration yamlFile, String fileLocation) {
        File dataFolder = plugin.getDataFolder();
        File file = new File(dataFolder, fileLocation);
        try{
            File tmpFile = File.createTempFile("yaml", null, dataFolder);
            tmpFile.deleteOnExit();
            yamlFile.save(tmpFile);
            if(tmpFile.exists()){
                Files.copy(tmpFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.delete(tmpFile.toPath());
            }
        }catch(Exception e){
            plugin.getLogger().severe(() -> "Impossible de sauvegarder le fichier: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Permet de faire executer une commande
     * a un joueur
     *
     * @param player - The player
     * @param command - The command
     */
    public static void runCommand(final Player player, final String command){
        if(plugin.getServer().isPrimaryThread()){
            player.performCommand(command);
        }else{
            plugin.getServer().getScheduler().runTask(plugin, () -> player.performCommand(command));
        }
    }

    /**
     * Converts a serialized location to a Location. Returns null if string is
     * empty
     *
     * @param s
     *            - serialized location in format "world:x:y:z"
     * @return Location
     */
    static public Location getLocationString(final String s) {
        if (s == null || s.trim() == "") {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 4) {
            final World w = Bukkit.getServer().getWorld(parts[0]);
            if (w == null) {
                return null;
            }
            final int x = Integer.parseInt(parts[1]);
            final int y = Integer.parseInt(parts[2]);
            final int z = Integer.parseInt(parts[3]);
            return new Location(w, x, y, z);
        } else if (parts.length == 6) {
            final World w = Bukkit.getServer().getWorld(parts[0]);
            if (w == null) {
                return null;
            }
            final int x = Integer.parseInt(parts[1]);
            final int y = Integer.parseInt(parts[2]);
            final int z = Integer.parseInt(parts[3]);
            final float yaw = Float.intBitsToFloat(Integer.parseInt(parts[4]));
            final float pitch = Float.intBitsToFloat(Integer.parseInt(parts[5]));
            return new Location(w, x, y, z, yaw, pitch);
        }
        return null;
    }

    /**
     * Converts a location to a simple string representation
     * If location is null, returns empty string
     *
     * @param location
     * @return String of location
     */
    static public String getStringLocation(final Location location) {
        if (location == null || location.getWorld() == null) {
            return "";
        }
        return location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + Float.floatToIntBits(location.getYaw()) + ":" + Float.floatToIntBits(location.getPitch());
    }
}
