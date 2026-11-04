package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EverlastCommand implements CommandExecutor {
    private final Main plugin;

    public EverlastCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.").color(NamedTextColor.RED));
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(Component.text("Usage: /everlast <unprotect|info>").color(NamedTextColor.GRAY));
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "unprotect" -> {
                ItemStack held = player.getInventory().getItemInMainHand();
                if (!ProtectionManager.isProtected(held)) {
                    player.sendMessage(Component.text("The item in your hand is not protected.")
                            .color(NamedTextColor.RED));
                    return true;
                }
                String owner = ProtectionManager.ownerOf(held);
                if (owner == null || !owner.equals(player.getUniqueId().toString())) {
                    player.sendMessage(Component.text("You can only unprotect an item you own.")
                            .color(NamedTextColor.RED));
                    return true;
                }
                ProtectionManager.unprotect(player, held);
                player.sendMessage(Component.text("This item is no longer protected.")
                        .color(NamedTextColor.GREEN));
            }
            case "info" -> {
                int count = ProtectionManager.countProtected(player);
                player.sendMessage(Component.text("Euphrasie: You have protected " + count + " of 3 items.")
                        .color(NamedTextColor.AQUA));
            }
            default -> player.sendMessage(Component.text("Usage: /everlast <unprotect|info>").color(NamedTextColor.GRAY));
        }
        return true;
    }
}
