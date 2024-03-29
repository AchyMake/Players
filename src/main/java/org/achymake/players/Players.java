package org.achymake.players;

import org.achymake.players.api.*;
import org.achymake.players.commands.*;
import org.achymake.players.data.*;
import org.achymake.players.listeners.*;
import org.achymake.players.net.UpdateChecker;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public final class Players extends JavaPlugin {
    public static Players instance;
    public static Userdata userdata;
    public static Economy economy;
    public static Jail jail;
    public static Kits kits;
    public static Spawn spawn;
    public static Warps warps;
    public static Worth worth;
    public static Message message;
    public final List<Player> vanished = new ArrayList<>();
    public final HashMap<String, Long> commandCooldown = new HashMap<>();
    public final HashMap<String, Long> kitCooldown = new HashMap<>();
    public static UpdateChecker updateChecker;
    @Override
    public void onEnable() {
        instance = this;
        message = new Message(this);
        userdata = new Userdata(this);
        economy = new Economy(this);
        jail = new Jail(this);
        kits = new Kits(this);
        spawn = new Spawn(this);
        warps = new Warps(this);
        worth = new Worth(this);
        updateChecker = new UpdateChecker(this);
        reload();
        getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new VaultEconomyProvider(this), this, ServicePriority.Normal);
        new PlaceholderProvider().register();
        registerCommands();
        registerEvents();
        getMessage().sendLog(Level.INFO, "Enabled " + getDescription().getName() + " " + getDescription().getVersion());
        getUpdateChecker().sendUpdate();
    }
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getVanished().clear();
        getMessage().sendLog(Level.INFO, "Disabled " + getDescription().getName() + " " + getDescription().getVersion());
    }
    private void registerCommands() {
        getCommand("announcement").setExecutor(new AnnouncementCommand(this));
        getCommand("back").setExecutor(new BackCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("color").setExecutor(new ColorCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand(this));
        getCommand("delwarp").setExecutor(new DelWarpCommand(this));
        getCommand("eco").setExecutor(new EcoCommand(this));
        getCommand("enchant").setExecutor(new EnchantCommand(this));
        getCommand("enderchest").setExecutor(new EnderChestCommand(this));
        getCommand("feed").setExecutor(new FeedCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("flyspeed").setExecutor(new FlySpeedCommand(this));
        getCommand("freeze").setExecutor(new FreezeCommand(this));
        getCommand("gamemode").setExecutor(new GameModeCommand(this));
        getCommand("gma").setExecutor(new GMACommand(this));
        getCommand("gmc").setExecutor(new GMCCommand(this));
        getCommand("gms").setExecutor(new GMSCommand(this));
        getCommand("gmsp").setExecutor(new GMSPCommand(this));
        getCommand("hat").setExecutor(new HatCommand(this));
        getCommand("heal").setExecutor(new HealCommand(this));
        getCommand("help").setExecutor(new HelpCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("homes").setExecutor(new HomesCommand(this));
        getCommand("information").setExecutor(new InformationCommand(this));
        getCommand("inventory").setExecutor(new InventoryCommand(this));
        getCommand("jail").setExecutor(new JailCommand(this));
        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("motd").setExecutor(new MOTDCommand(this));
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("nickname").setExecutor(new NicknameCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("players").setExecutor(new PlayersCommand(this));
        getCommand("pvp").setExecutor(new PVPCommand(this));
        getCommand("repair").setExecutor(new RepairCommand(this));
        getCommand("respond").setExecutor(new RespondCommand(this));
        getCommand("rtp").setExecutor(new RTPCommand(this));
        getCommand("rules").setExecutor(new RulesCommand(this));
        getCommand("sell").setExecutor(new SellCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("setjail").setExecutor(new SetJailCommand(this));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        getCommand("setworth").setExecutor(new SetWorthCommand(this));
        getCommand("skull").setExecutor(new SkullCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("store").setExecutor(new StoreCommand(this));
        getCommand("tpaccept").setExecutor(new TPAcceptCommand(this));
        getCommand("tpa").setExecutor(new TPACommand(this));
        getCommand("tpcancel").setExecutor(new TPCancelCommand(this));
        getCommand("tp").setExecutor(new TPCommand(this));
        getCommand("tpdeny").setExecutor(new TPDenyCommand(this));
        getCommand("tphere").setExecutor(new TPHereCommand(this));
        getCommand("unban").setExecutor(new UnBanCommand(this));
        getCommand("uuid").setExecutor(new UUIDCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("walkspeed").setExecutor(new WalkSpeedCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("whisper").setExecutor(new WhisperCommand(this));
        getCommand("workbench").setExecutor(new WorkbenchCommand(this));
        getCommand("worth").setExecutor(new WorthCommand(this));
    }
    private void registerEvents() {
        getManager().registerEvents(new AsyncPlayerChat(this), this);
        getManager().registerEvents(new BlockBreak(this), this);
        getManager().registerEvents(new BlockFertilize(this), this);
        getManager().registerEvents(new BlockPlace(this), this);
        getManager().registerEvents(new BlockReceiveGame(this), this);
        getManager().registerEvents(new EntityDamageByEntity(this), this);
        getManager().registerEvents(new PlayerBucketEmpty(this), this);
        getManager().registerEvents(new PlayerBucketEntity(this), this);
        getManager().registerEvents(new PlayerBucketFill(this), this);
        getManager().registerEvents(new PlayerCommandPreprocess(this), this);
        getManager().registerEvents(new PlayerDeath(this), this);
        getManager().registerEvents(new PlayerHarvestBlock(this), this);
        getManager().registerEvents(new PlayerInteractPhysical(this), this);
        getManager().registerEvents(new PlayerJoin(this), this);
        getManager().registerEvents(new PlayerLeashEntity(this), this);
        getManager().registerEvents(new PlayerLogin(this), this);
        getManager().registerEvents(new PlayerMount(this), this);
        getManager().registerEvents(new PlayerMove(this), this);
        getManager().registerEvents(new PlayerQuit(this), this);
        getManager().registerEvents(new PlayerRespawn(this), this);
        getManager().registerEvents(new PlayerShearEntity(this), this);
        getManager().registerEvents(new PlayerSpawnLocation(this), this);
        getManager().registerEvents(new PlayerTeleport(this), this);
        getManager().registerEvents(new PrepareAnvil(this), this);
        getManager().registerEvents(new SignChange(this), this);
    }
    public void reload() {
        File file = new File(getDataFolder(), "config.yml");
        if (file.exists()) {
            try {
                getConfig().load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            getConfig().options().copyDefaults(true);
            try {
                getConfig().save(file);
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        }
        getJail().reload();
        getKits().reload();
        getSpawn().reload();
        getWarps().reload();
        getWorth().reload();
        getUserdata().reload(getServer().getOfflinePlayers());
        getUserdata().resetTabList();
    }
    public PluginManager getManager() {
        return getServer().getPluginManager();
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    public HashMap<String, Long> getCommandCooldown() {
        return commandCooldown;
    }
    public HashMap<String, Long> getKitCooldown() {
        return kitCooldown;
    }
    public List<Player> getVanished() {
        return vanished;
    }
    public Worth getWorth() {
        return worth;
    }
    public Warps getWarps() {
        return warps;
    }
    public Spawn getSpawn() {
        return spawn;
    }
    public Kits getKits() {
        return kits;
    }
    public Jail getJail() {
        return jail;
    }
    public Economy getEconomy() {
        return economy;
    }
    public Userdata getUserdata() {
        return userdata;
    }
    public Message getMessage() {
        return message;
    }
    public static Players getInstance() {
        return instance;
    }
}