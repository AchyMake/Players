package net.achymake.players.api;

import net.achymake.players.Players;
import net.achymake.players.files.Database;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class EconomyProvider implements Economy {
    private final Players plugin;
    public EconomyProvider(Players plugin) {
        this.plugin = plugin;
    }
    public boolean isEnabled() {
        return plugin.isEnabled();
    }
    public String getName() {
        return plugin.getName();
    }
    public boolean hasBankSupport() {
        return false;
    }
    public int fractionalDigits() {
        return -1;
    }
    public String format(double amount) {
        return new DecimalFormat(plugin.getConfig().getString("economy.format")).format(amount);
    }
    public String currencyNamePlural() {
        return currencyNameSingular();
    }
    public String currencyNameSingular() {
        return plugin.getConfig().getString("economy.currency");
    }
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        Database database = new Database(plugin.getDataFolder());
        return database.exist(offlinePlayer);
    }
    public boolean hasAccount(String playerName) {
        Database database = new Database(plugin.getDataFolder());
        return database.exist(plugin.getServer().getOfflinePlayer(playerName));
    }
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }
    public double getBalance(OfflinePlayer offlinePlayer) {
        Database database = new Database(plugin.getDataFolder());
        return database.getEconomy(offlinePlayer);
    }
    public double getBalance(String playerName) {
        Database database = new Database(plugin.getDataFolder());
        return database.getEconomy(plugin.getServer().getOfflinePlayer(playerName));
    }
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        Database database = new Database(plugin.getDataFolder());
        return database.getEconomy(offlinePlayer) >= amount;
    }
    public boolean has(String playerName, double amount) {
        Database database = new Database(plugin.getDataFolder());
        return database.getEconomy(plugin.getServer().getOfflinePlayer(playerName)) >= amount;
    }
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        if (offlinePlayer == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player cannot be null!");
        } else if (amount < 0.0) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds!");
        } else {
            Database database = new Database(plugin.getDataFolder());
            database.removeEconomy(offlinePlayer, amount);
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
        }
    }
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        if (playerName == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player name cannot be null!");
        } else if (amount < 0.0) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds!");
        } else {
            Database database = new Database(plugin.getDataFolder());
            database.removeEconomy(plugin.getServer().getOfflinePlayer(playerName), amount);
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
        }
    }
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        if (offlinePlayer == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player can not be null.");
        } else if (amount < 0.0) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        } else {
            Database database = new Database(plugin.getDataFolder());
            database.addEconomy(offlinePlayer, amount);
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
        }
    }
    public EconomyResponse depositPlayer(String playerName, double amount) {
        if (playerName == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player name can not be null.");
        } else if (amount < 0.0) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        } else {
            Database database = new Database(plugin.getDataFolder());
            database.addEconomy(plugin.getServer().getOfflinePlayer(playerName), amount);
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
        }
    }
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        Database database = new Database(plugin.getDataFolder());
        database.setup(offlinePlayer);
        return true;
    }
    public boolean createPlayerAccount(String playerName) {
        Database database = new Database(plugin.getDataFolder());
        database.setup(plugin.getServer().getOfflinePlayer(playerName));
        return true;
    }
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public List<String> getBanks() {
        return Collections.emptyList();
    }
}
