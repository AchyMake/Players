package org.achymake.players.data;

import org.achymake.players.Players;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;

public record Economy(Players plugin) {
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    public double get(OfflinePlayer offlinePlayer) {
        return getUserdata().getConfig(offlinePlayer).getDouble("account");
    }
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return get(offlinePlayer) >= amount;
    }
    public void add(OfflinePlayer offlinePlayer, double amount) {
        getUserdata().setDouble(offlinePlayer, "account", amount + get(offlinePlayer));
    }
    public void remove(OfflinePlayer offlinePlayer, double amount) {
        getUserdata().setDouble(offlinePlayer, "account", get(offlinePlayer) - amount);
    }
    public void set(OfflinePlayer offlinePlayer, double value) {
        getUserdata().setDouble(offlinePlayer, "account", value);
    }
    public void reset(OfflinePlayer offlinePlayer) {
        getUserdata().setDouble(offlinePlayer, "account", getConfig().getDouble("economy.starting-balance"));
    }
    public String format(double amount) {
        return new DecimalFormat(getConfig().getString("economy.format")).format(amount);
    }
    public String currency() {
        return getConfig().getString("economy.currency");
    }
}
