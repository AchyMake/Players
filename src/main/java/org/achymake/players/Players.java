package org.achymake.players;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.achymake.players.api.EconomyProvider;
import org.achymake.players.api.PlaceholderProvider;
import org.achymake.players.commands.*;
import org.achymake.players.files.*;
import org.achymake.players.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Players extends JavaPlugin {
    private static Players instance;
    public static Players getInstance() {
        return instance;
    }
    private static Logger logger;
    public static void sendLog(Level level, String message) {
        logger.log(level, message);
    }
    private static Server host;
    public static Server getHost() {
        return host;
    }
    private static FileConfiguration fileConfiguration;
    public static FileConfiguration getConfiguration() {
        return fileConfiguration;
    }
    private static File folder;
    public static File getFolder() {
        return folder;
    }
    private static Database database;
    public static Database getDatabase() {
        return database;
    }
    private static EconomyProvider economyProvider;
    public static EconomyProvider getEconomyProvider() {
        return economyProvider;
    }
    private static Jail jail;
    public static Jail getJail() {
        return jail;
    }
    private static Kits kits;
    public static Kits getKits() {
        return kits;
    }
    private static Spawn spawn;
    public static Spawn getSpawn() {
        return spawn;
    }
    private static Warps warps;
    public static Warps getWarps() {
        return warps;
    }
    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        folder = getDataFolder();
        fileConfiguration = getConfig();
        host = getServer();
        if (getHost().getPluginManager().getPlugin("Vault") != null) {
            database = new Database();
            economyProvider = new EconomyProvider(this);
            getHost().getServicesManager().register(Economy.class, getEconomyProvider(), this, ServicePriority.Normal);
            sendLog(Level.INFO, "Hooked to 'Vault'");
        } else {
            sendLog(Level.WARNING, "You have to install 'Vault'");
            getHost().getPluginManager().disablePlugin(this);
        }
        if (getHost().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getHost().getPluginManager().disablePlugin(this);
            sendLog(Level.WARNING, "You have to install 'PlaceholderAPI'");
        } else {
            new PlaceholderProvider().register();
            sendLog(Level.INFO, "Hooked to 'PlaceholderAPI'");
        }
        jail = new Jail();
        kits = new Kits();
        spawn = new Spawn();
        warps = new Warps();
        reload();
        commands();
        events();
        sendLog(Level.INFO, "Enabled " + getName() + " " + getDescription().getVersion());
        getUpdate();
    }
    @Override
    public void onDisable() {
        sendLog(Level.INFO, "Disabled " + getName() + " " + getDescription().getVersion());
        getHost().getScheduler().cancelTasks(this);
    }
    private void commands() {
        getCommand("announcement").setExecutor(new AnnouncementCommand());
        getCommand("back").setExecutor(new BackCommand());
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("color").setExecutor(new ColorCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand());
        getCommand("delwarp").setExecutor(new DelWarpCommand());
        getCommand("eco").setExecutor(new EcoCommand());
        getCommand("enchant").setExecutor(new EnchantCommand());
        getCommand("enderchest").setExecutor(new EnderChestCommand());
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("gamemode").setExecutor(new GameModeCommand());
        getCommand("gma").setExecutor(new GMACommand());
        getCommand("gmc").setExecutor(new GMCCommand());
        getCommand("gms").setExecutor(new GMSCommand());
        getCommand("gmsp").setExecutor(new GMSPCommand());
        getCommand("hat").setExecutor(new HatCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("help").setExecutor(new HelpCommand());
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("homes").setExecutor(new HomesCommand());
        getCommand("inventory").setExecutor(new InventoryCommand());
        getCommand("jail").setExecutor(new JailCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("motd").setExecutor(new MOTDCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("nickname").setExecutor(new NicknameCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("players").setExecutor(new PlayersCommand());
        getCommand("pvp").setExecutor(new PVPCommand());
        getCommand("repair").setExecutor(new RepairCommand());
        getCommand("respond").setExecutor(new RespondCommand());
        getCommand("rtp").setExecutor(new RTPCommand());
        getCommand("rules").setExecutor(new RulesCommand());
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("setjail").setExecutor(new SetJailCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("skull").setExecutor(new SkullCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("tpaccept").setExecutor(new TPAcceptCommand());
        getCommand("tpa").setExecutor(new TPACommand());
        getCommand("tpcancel").setExecutor(new TPCancelCommand());
        getCommand("tp").setExecutor(new TPCommand());
        getCommand("tpdeny").setExecutor(new TPDenyCommand());
        getCommand("tphere").setExecutor(new TPHereCommand());
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("whisper").setExecutor(new WhisperCommand());
        getCommand("workbench").setExecutor(new WorkbenchCommand());
    }
    private void events() {
        new AsyncPlayerChat(this);
        new BlockBreak(this);
        new BlockFertilize(this);
        new BlockPlace(this);
        new EntityDamageByEntity(this);
        new EntityMount(this);
        new PlayerBucketEmpty(this);
        new PlayerBucketEntity(this);
        new PlayerBucketFill(this);
        new PlayerCommandPreprocess(this);
        new PlayerDeath(this);
        new PlayerHarvestBlock(this);
        new PlayerInteractPhysical(this);
        new PlayerJoin(this);
        new PlayerLeashEntity(this);
        new PlayerLogin(this);
        new PlayerMoveWhileFrozen(this);
        new PlayerMoveWhileVanished(this);
        new PlayerQuit(this);
        new PlayerRespawn(this);
        new PlayerShearEntity(this);
        new PlayerSpawnLocation(this);
        new PlayerTeleport(this);
        new PrepareAnvil(this);
        new SignChange(this);
    }
    public static void getUpdate(Player player) {
        if (notifyUpdate()) {
            getLatest((latest) -> {
                if (!getInstance().getDescription().getVersion().equals(latest)) {
                    send(player,"&6" + getInstance().getName() + " Update:&f " + latest);
                    send(player,"&6Current Version: &f" + getInstance().getDescription().getVersion());
                }
            });
        }
    }
    public void getUpdate() {
        if (notifyUpdate()) {
            getHost().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    getLatest((latest) -> {
                        sendLog(Level.INFO, "Checking latest release");
                        if (getDescription().getVersion().equals(latest)) {
                            sendLog(Level.INFO, "You are using the latest version");
                        } else {
                            sendLog(Level.INFO, "New Update: " + latest);
                            sendLog(Level.INFO, "Current Version: " + getDescription().getVersion());
                        }
                    });
                }
            });
        }
    }
    public static void getLatest(Consumer<String> consumer) {
        try {
            InputStream inputStream = (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + 110266)).openStream();
            Scanner scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
                scanner.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            sendLog(Level.WARNING, e.getMessage());
        }
    }
    private static boolean notifyUpdate() {
        return getConfiguration().getBoolean("notify-update");
    }
    public static void reload() {
        File file = new File(getFolder(), "config.yml");
        if (file.exists()) {
            try {
                getConfiguration().load(file);
                getConfiguration().save(file);
                sendLog(Level.INFO, "loaded config.yml");
            } catch (IOException | InvalidConfigurationException e) {
                sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            getConfiguration().options().copyDefaults(true);
            try {
                getConfiguration().save(file);
                sendLog(Level.INFO, "created config.yml");
            } catch (IOException e) {
                sendLog(Level.WARNING, e.getMessage());
            }
        }
        getJail().reload();
        getKits().reload();
        getSpawn().reload();
        getWarps().reload();
        getDatabase().reload(getHost().getOfflinePlayers());
        getDatabase().resetTabList();
    }
    public static void send(ConsoleCommandSender sender, String message) {
        sender.sendMessage(message);
    }
    public static void send(Player player, String message) {
        player.sendMessage(addColor(message));
    }
    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(addColor(message)));
    }
    public static String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
