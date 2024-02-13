package org.achymake.players.data;

import org.achymake.players.Players;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public record Kits(Players plugin) {
    private File getDataFolder() {
        return plugin.getDataFolder();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public boolean exist() {
        return getFile().exists();
    }
    public File getFile() {
        return new File(getDataFolder(),"kits.yml");
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public List<String> getKits() {
        return new ArrayList<>(getConfig().getKeys(false));
    }
    public boolean hasPrice(String kitName) {
        return getConfig().isDouble(kitName + ".cost");
    }
    public double cost(String kitName) {
        return getConfig().getDouble(kitName + ".cost");
    }
    public List<ItemStack> getKit(String kitName) {
        List<ItemStack> giveItems = new ArrayList<>();
        for (String items : getConfig().getConfigurationSection(kitName + ".materials").getKeys(false)) {
            String materialString = getConfig().getString(kitName + ".materials." + items + ".type");
            Material material = Material.valueOf(materialString);
            int amount = getConfig().getInt(kitName + ".materials." + items + ".amount");
            ItemStack item = new ItemStack(material, amount);
            ItemMeta itemMeta = item.getItemMeta();
            if (getConfig().getKeys(true).contains(kitName + ".materials." + items + ".name")) {
                String displayName = getConfig().getString(kitName + ".materials." + items + ".name");
                itemMeta.setDisplayName(getMessage().addColor(displayName));
            }
            if (getConfig().getKeys(true).contains(kitName + ".materials." + items + ".lore")) {
                List<String> lore = new ArrayList<>();
                for (String listedLore : getConfig().getStringList(kitName + ".materials." + items + ".lore")) {
                    lore.add(getMessage().addColor(listedLore));
                }
                itemMeta.setLore(lore);
            }
            if (getConfig().getKeys(true).contains(kitName+".materials." + items + ".enchantments")) {
                for (String enchantList : getConfig().getConfigurationSection(kitName + ".materials." + items + ".enchantments").getKeys(false)) {
                    String enchantmentString = getConfig().getString(kitName + ".materials." + items + ".enchantments." + enchantList + ".type");
                    Enchantment enchantment = Enchantment.getByName(enchantmentString);
                    int enchantmentLevel = getConfig().getInt(kitName + ".materials." + items + ".enchantments." + enchantList + ".amount");
                    itemMeta.addEnchant(enchantment, enchantmentLevel, true);
                }
            }
            item.setItemMeta(itemMeta);
            giveItems.add(item);
        }
        return giveItems;
    }
    public void giveKit(Player player, String kitName) {
        for (ItemStack itemStack : getKit(kitName)) {
            if (Arrays.asList(player.getInventory().getStorageContents()).contains(null)) {
                player.getInventory().addItem(itemStack);
            } else {
                player.getWorld().dropItem(player.getLocation(), itemStack);
            }
        }
    }
    public void reload() {
        File file = getFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (exist()) {
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            List<String> lore = new ArrayList<>();
            lore.add("&9from");
            lore.add("&7-&6 Starter");
            config.addDefault("starter.cooldown", 3600);
            config.addDefault("starter.cost", 75.00);
            config.addDefault("starter.materials.sword.type", "STONE_SWORD");
            config.addDefault("starter.materials.sword.amount", 1);
            config.addDefault("starter.materials.sword.name", "&6Stone Sword");
            config.addDefault("starter.materials.sword.lore", lore);
            config.addDefault("starter.materials.sword.enchantments.unbreaking.type", "DURABILITY");
            config.addDefault("starter.materials.sword.enchantments.unbreaking.amount", 1);
            config.addDefault("starter.materials.pickaxe.type", "STONE_PICKAXE");
            config.addDefault("starter.materials.pickaxe.amount", 1);
            config.addDefault("starter.materials.pickaxe.name", "&6Stone Pickaxe");
            config.addDefault("starter.materials.pickaxe.lore", lore);
            config.addDefault("starter.materials.pickaxe.enchantments.unbreaking.type", "DURABILITY");
            config.addDefault("starter.materials.pickaxe.enchantments.unbreaking.amount", 1);
            config.addDefault("starter.materials.axe.type", "STONE_AXE");
            config.addDefault("starter.materials.axe.amount", 1);
            config.addDefault("starter.materials.axe.name", "&6Stone Axe");
            config.addDefault("starter.materials.axe.lore", lore);
            config.addDefault("starter.materials.axe.enchantments.unbreaking.type", "DURABILITY");
            config.addDefault("starter.materials.axe.enchantments.unbreaking.amount", 1);
            config.addDefault("starter.materials.shovel.type", "STONE_SHOVEL");
            config.addDefault("starter.materials.shovel.amount", 1);
            config.addDefault("starter.materials.shovel.name", "&6Stone Shovel");
            config.addDefault("starter.materials.shovel.lore", lore);
            config.addDefault("starter.materials.shovel.enchantments.unbreaking.type", "DURABILITY");
            config.addDefault("starter.materials.shovel.enchantments.unbreaking.amount", 1);
            config.addDefault("starter.materials.food.type", "COOKED_BEEF");
            config.addDefault("starter.materials.food.amount", 16);
            config.addDefault("food.cooldown", 1800);
            config.addDefault("food.materials.food.type", "COOKED_BEEF");
            config.addDefault("food.materials.food.amount", 16);
            config.options().copyDefaults(true);
            try {
                config.save(file);
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
}