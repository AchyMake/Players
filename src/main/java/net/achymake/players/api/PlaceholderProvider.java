package net.achymake.players.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.achymake.players.Players;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderProvider extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "players";
    }
    @Override
    public String getAuthor() {
        return "AchyMake";
    }
    @Override
    public String getVersion() {
        return "1.10.0";
    }
    @Override
    public boolean canRegister() {
        return true;
    }
    @Override
    public boolean register() {
        return super.register();
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        if (params.equals("name")) {
            return Players.getDatabase().getConfig(player).getString("display-name");
        }
        if (params.equals("vanished")) {
            return String.valueOf(Players.getDatabase().isVanished(player));
        }
        if (params.equals("online_players")) {
            return String.valueOf(player.getServer().getOnlinePlayers().size() - Players.getDatabase().getVanished().size());
        }
        if (params.equals("account")) {
            return Players.getEconomyProvider().format(Players.getDatabase().getEconomy(player));
        }
        return super.onPlaceholderRequest(player, params);
    }
}