package net.achymake.players;

import net.achymake.players.api.*;
import net.achymake.players.commands.*;
import net.achymake.players.files.*;
import net.achymake.players.listeners.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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

public final class Players extends JavaPlugin {
    private static Players instance;
    public static Players getInstance() {
        return instance;
    }
    private static FileConfiguration configuration;
    public static FileConfiguration getConfiguration() {
        return configuration;
    }
    private static Message message;
    public static Message getMessage() {
        return message;
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
    private void start() {
        instance = this;
        configuration = getConfig();
        message = new Message(getLogger());
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getServer().getPluginManager().disablePlugin(this);
            getMessage().sendLog(Level.WARNING, "You have to install 'Vault'");
        } else {
            economyProvider = new EconomyProvider(this);
            getServer().getServicesManager().register(Economy.class, getEconomyProvider(), this, ServicePriority.Normal);
            getMessage().sendLog(Level.INFO, "Hooked to 'Vault'");
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getServer().getPluginManager().disablePlugin(this);
            getMessage().sendLog(Level.WARNING, "You have to install 'PlaceholderAPI'");
        } else {
            new PlaceholderProvider().register();
            getMessage().sendLog(Level.INFO, "Hooked to 'PlaceholderAPI'");
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
        getMessage().sendLog(Level.INFO, "Enabled " + getName() + " " + getServer().getBukkitVersion());
        sendUpdate();
    }
    private void stop() {
        if (!getDatabase().getVanished().isEmpty()) {
            getDatabase().getVanished().clear();
        }
        if (new PlaceholderProvider().isRegistered()) {
            new PlaceholderProvider().unregister();
        }
        getServer().getServicesManager().unregisterAll(this);
        getMessage().sendLog(Level.INFO, "Disabled " + getName() + " " + getServer().getBukkitVersion());
    }
    @Override
    public void onEnable() {
        start();
    }
    @Override
    public void onDisable() {
        stop();
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
                getMessage().sendLog(Level.INFO, "reloaded config.yml");
            } catch (IOException | InvalidConfigurationException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
            saveConfig();
        } else {
            getMessage().sendLog(Level.INFO, "creating config.yml");
            getConfig().options().copyDefaults(true);
            saveConfig();
            getMessage().sendLog(Level.INFO, "created config.yml");
        }
        getJail().reload();
        getKits().reload();
        getMotd().reload();
        getSpawn().reload();
        getWarps().reload();
        getDatabase().resetTabList();
        getDatabase().getCommandCooldown().clear();
    }
    public void reloadPlayerFiles() {
        for (OfflinePlayer offlinePlayer : getServer().getOfflinePlayers()) {
            if (getDatabase().exist(offlinePlayer)) {
                File file = new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                try {
                    config.load(file);
                } catch (IOException | InvalidConfigurationException e) {
                    getMessage().sendLog(Level.WARNING, e.getMessage());
                }
            }
        }
    }
    public void sendUpdate(Player player) {
        if (getConfig().getBoolean("notify-update.enable")) {
            checkLatest((latest) -> {
                if (!getDescription().getVersion().equals(latest)) {
                    getMessage().send(player,"&6" + getName() + " Update:&f " + latest);
                    getMessage().send(player,"&6Current Version: &f" + getDescription().getVersion());
                }
            });
        }
    }
    public void sendUpdate() {
        if (getConfig().getBoolean("notify-update.enable")) {
            checkLatest((latest) -> {
                getMessage().sendLog(Level.INFO, "Checking latest release");
                if (getDescription().getVersion().equals(latest)) {
                    getMessage().sendLog(Level.INFO, "You are using the latest version");
                } else {
                    getMessage().sendLog(Level.INFO, "New Update: " + latest);
                    getMessage().sendLog(Level.INFO, "Current Version: " + getDescription().getVersion());
                }
            });
        }
    }
    public void checkLatest(Consumer<String> consumer) {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
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
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        });
    }
}