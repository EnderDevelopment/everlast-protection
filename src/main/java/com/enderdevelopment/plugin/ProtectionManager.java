package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public final class ProtectionManager {
    private static Main plugin;

    private ProtectionManager() {
    }

    public static void init(Main main) {
        plugin = main;
    }

    public static NamespacedKey essenceOwnerKey() {
        return new NamespacedKey(plugin, "essence_owner");
    }

    public static NamespacedKey tokenOwnerKey() {
        return new NamespacedKey(plugin, "token_owner");
    }

    public static NamespacedKey everlastOwnerKey() {
        return new NamespacedKey(plugin, "everlast_owner");
    }

    public static boolean isEssence(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer()
                .has(essenceOwnerKey(), PersistentDataType.STRING);
    }

    public static boolean isToken(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer()
                .has(tokenOwnerKey(), PersistentDataType.STRING);
    }

    public static boolean isProtected(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer()
                .has(everlastOwnerKey(), PersistentDataType.STRING);
    }

    public static String ownerOf(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (pdc.has(essenceOwnerKey(), PersistentDataType.STRING)) {
            return pdc.get(essenceOwnerKey(), PersistentDataType.STRING);
        }
        if (pdc.has(tokenOwnerKey(), PersistentDataType.STRING)) {
            return pdc.get(tokenOwnerKey(), PersistentDataType.STRING);
        }
        if (pdc.has(everlastOwnerKey(), PersistentDataType.STRING)) {
            return pdc.get(everlastOwnerKey(), PersistentDataType.STRING);
        }
        return null;
    }

    public static ItemStack createEssence(Player player) {
        ItemStack essence = new ItemStack(Material.GLASS_BOTTLE);
        ItemMeta meta = essence.getItemMeta();
        meta.displayName(Component.text("Player Essence")
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false));
        meta.getPersistentDataContainer()
                .set(essenceOwnerKey(), PersistentDataType.STRING, player.getUniqueId().toString());
        Enchantment glow = Registry.ENCHANTMENT.get(NamespacedKey.minecraft("mending"));
        if (glow != null) {
            meta.addEnchant(glow, 1, true);
        }
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        essence.setItemMeta(meta);
        return essence;
    }

    public static ItemStack createToken(UUID owner) {
        ItemStack token = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = token.getItemMeta();
        meta.displayName(Component.text("Everlasting Token")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.ITALIC, false));
        meta.lore(java.util.List.of(
                Component.text("Bound to the player who forged it.").color(NamedTextColor.GRAY)));
        meta.getPersistentDataContainer()
                .set(tokenOwnerKey(), PersistentDataType.STRING, owner.toString());
        Enchantment glow = Registry.ENCHANTMENT.get(NamespacedKey.minecraft("mending"));
        if (glow != null) {
            meta.addEnchant(glow, 1, true);
        }
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        token.setItemMeta(meta);
        return token;
    }

    public static boolean isEligible(ItemStack item) {
        if (item == null) {
            return false;
        }
        Material type = item.getType();
        if (type == Material.SHULKER_BOX || type == Material.AIR || type.isAir()) {
            return false;
        }
        return !isProtected(item) && !isToken(item) && !isEssence(item);
    }

    public static int countProtected(Player player) {
        String uuid = player.getUniqueId().toString();
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType().isAir()) {
                continue;
            }
            if (isProtected(item)) {
                String owner = ownerOf(item);
                if (owner != null && owner.equals(uuid)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static ItemStack protect(Player player, ItemStack item, ItemStack token) {
        String tokenOwner = ownerOf(token);
        if (tokenOwner == null) {
            return null;
        }
        if (!tokenOwner.equals(player.getUniqueId().toString())) {
            return null;
        }
        if (!isEligible(item)) {
            return null;
        }
        if (countProtected(player) >= 3) {
            return null;
        }
        ItemStack result = item.clone();
        ItemMeta meta = result.getItemMeta();
        meta.getPersistentDataContainer()
                .set(everlastOwnerKey(), PersistentDataType.STRING, player.getUniqueId().toString());
        meta.lore(java.util.List.of(
                Component.text("Everlasting - kept on death").color(NamedTextColor.WHITE)));
        result.setItemMeta(meta);
        return result;
    }

    public static void unprotect(Player player, ItemStack item) {
        if (!isProtected(item)) {
            return;
        }
        String owner = ownerOf(item);
        if (owner == null || !owner.equals(player.getUniqueId().toString())) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(everlastOwnerKey());
        item.setItemMeta(meta);
    }
}
