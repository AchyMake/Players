package net.achymake.players;

import net.achymake.players.api.*;
import net.achymake.players.commands.*;
import net.achymake.players.files.*;
import net.achymake.players.listeners.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Players extends JavaPlugin {
    private static Players instance;
    public static Players getInstance() {
        return instance;
    }
    private static FileConfiguration configuration;
    public static FileConfiguration getConfiguration() {
        return configuration;
    }
    private static Logger logger;
    public static void sendLog(Level level, String message) {
        logger.log(level, message);
    }
    private static Database database;
    public static Database getDatabase() {
        return database;
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
    private static Motd motd;
    public static Motd getMotd() {
        return motd;
    }
    private static EconomyProvider economyProvider;
    public static EconomyProvider getEconomyProvider() {
        return economyProvider;
    }
    @Override
    public void onEnable() {
        instance = this;
        configuration = getConfig();
        logger = getLogger();
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getServer().getPluginManager().disablePlugin(this);
            sendLog(Level.WARNING, "You have to install 'Vault'");
        } else {
            economyProvider = new EconomyProvider(this);
            getServer().getServicesManager().register(Economy.class, getEconomyProvider(), this, ServicePriority.Normal);
            sendLog(Level.INFO, "Hooked to 'Vault'");
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getServer().getPluginManager().disablePlugin(this);
            sendLog(Level.WARNING, "You have to install 'PlaceholderAPI'");
        } else {
            new PlaceholderProvider().register();
            sendLog(Level.INFO, "Hooked to 'PlaceholderAPI'");
        }
        database = new Database(getDataFolder());
        jail = new Jail(getDataFolder());
        kits = new Kits(getDataFolder());
        motd = new Motd(getDataFolder());
        spawn = new Spawn(getDataFolder());
        warps = new Warps(getDataFolder());
        reload();
        commands();
        events();
        sendLog(Level.INFO, "Enabled " + getName() + " " + getServer().getBukkitVersion());
        getUpdate();
    }
    @Override
    public void onDisable() {
        if (!getDatabase().getVanished().isEmpty()) {
            getDatabase().getVanished().clear();
        }
        if (new PlaceholderProvider().isRegistered()) {
            new PlaceholderProvider().unregister();
        }
        getServer().getServicesManager().unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);
        sendLog(Level.INFO, "Disabled " + getName() + " " + getServer().getBukkitVersion());
    }
    private void commands() {
        getCommand("announcement").setExecutor(new AnnouncementCommand());
        getCommand("back").setExecutor(new BackCommand());
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("color").setExecutor(new ColorCommand());
        getCommand("coordinates").setExecutor(new CoordinatesCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand());
        getCommand("delwarp").setExecutor(new DelWarpCommand());
        getCommand("eco").setExecutor(new EcoCommand());
        getCommand("enchant").setExecutor(new EnchantCommand());
        getCommand("enderchest").setExecutor(new EnderChestCommand());
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
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
        getCommand("motd").setExecutor(new MotdCommand());
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
        new DamageEntity(this);
        new DamageEntityWithArrow(this);
        new DamageEntityWithSnowball(this);
        new DamageEntityWithSpectralArrow(this);
        new DamageEntityWithThrownPotion(this);
        new DamageEntityWithTrident(this);
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
        new PlayerMount(this);
        new PlayerMove(this);
        new PlayerQuit(this);
        new PlayerRespawn(this);
        new PlayerShearEntity(this);
        new PlayerSpawnLocation(this);
        new PlayerTeleport(this);
        new PrepareAnvil(this);
        new SignChange(this);
    }
    public void reload() {
        File file = new File(getDataFolder(), "config.yml");
        if (file.exists()) {
            try {
                getConfig().load(file);
                sendLog(Level.INFO, "loaded config.yml");
            } catch (IOException | InvalidConfigurationException e) {
                sendLog(Level.WARNING, e.getMessage());
            }
            saveConfig();
        } else {
            getConfig().options().copyDefaults(true);
            saveConfig();
            sendLog(Level.INFO, "created config.yml");
        }
        getJail().reload();
        getKits().reload();
        getMotd().reload();
        getSpawn().reload();
        getWarps().reload();
        getDatabase().resetTabList();
        getDatabase().getCommandCooldown().clear();
        getDatabase().reload(getServer().getOfflinePlayers());
    }
    public void getUpdate(Player player) {
        if (notifyUpdate()) {
            getLatest((latest) -> {
                if (!getDescription().getVersion().equals(latest)) {
                    send(player,"&6" + getName() + " Update:&f " + latest);
                    send(player,"&6Current Version: &f" + getDescription().getVersion());
                }
            });
        }
    }
    public void getUpdate() {
        if (notifyUpdate()) {
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
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
    public void getLatest(Consumer<String> consumer) {
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
    private boolean notifyUpdate() {
        return getConfig().getBoolean("notify-update.enable");
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