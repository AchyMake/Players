package org.achymake.players.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.achymake.players.Players;
import org.achymake.players.data.Economy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

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
        return "43";
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
        } else {
            if (params.equals("name")) {
                return Players.getInstance().getUserdata().getConfig(player).getString("name");
            }
            if (params.equals("display-name")) {
                return Players.getInstance().getUserdata().getConfig(player).getString("display-name");
            }
            if (params.equals("vanished")) {
                return String.valueOf(Players.getInstance().getVanished().contains(player));
            }
            if (params.equals("online_players")) {
                return String.valueOf(player.getServer().getOnlinePlayers().size() - Players.getInstance().getVanished().size());
            }
            if (params.equals("account")) {
                Economy economy = Players.getInstance().getEconomy();
                return economy.currency() + economy.format(economy.get(player));
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}
