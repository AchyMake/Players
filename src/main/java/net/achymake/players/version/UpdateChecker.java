package net.achymake.players.version;

import net.achymake.players.Players;
import net.achymake.players.files.Message;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;

public class UpdateChecker {
    private final Players players;
    private final int resourceId;
    public UpdateChecker(Players players, int resourceId) {
        this.players = players;
        this.resourceId = resourceId;
    }
    private final Message message = Players.getMessage();
    public void getVersion(Consumer<String> consumer) {
        players.getServer().getScheduler().runTaskAsynchronously(players, () -> {
            try {
                InputStream inputStream = (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId)).openStream();
                Scanner scanner = new Scanner(inputStream);
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                    scanner.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                message.sendLog(Level.WARNING, e.getMessage());
            }
        });
    }
    public void getUpdate() {
        if (players.getConfig().getBoolean("notify-update.enable")) {
            (new UpdateChecker(players, resourceId)).getVersion((latest) -> {
                if (players.getDescription().getVersion().equals(latest)) {
                    message.sendLog(Level.INFO, "You are using the latest version");
                } else {
                    message.sendLog(Level.INFO, "New Update: " + latest);
                    message.sendLog(Level.INFO, "Current Version: " + players.getDescription().getVersion());
                }
            });
        }
    }
    public void sendMessage(Player player) {
        if (players.getConfig().getBoolean("notify-update.enable")) {
            (new UpdateChecker(players, resourceId)).getVersion((latest) -> {
                if (!players.getDescription().getVersion().equals(latest)) {
                    message.send(player,"&6" + players.getName() + " Update:&f " + latest);
                    message.send(player,"&6Current Version: &f" + players.getDescription().getVersion());
                }
            });
        }
    }
}
