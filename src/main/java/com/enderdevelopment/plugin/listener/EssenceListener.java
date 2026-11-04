package com.enderdevelopment.plugin.listener;

import com.enderdevelopment.plugin.Main;
import com.enderdevelopment.plugin.ProtectionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EssenceListener implements Listener {
    private final Main plugin;

    public EssenceListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        // The player must be looking straight down.
        if (player.getLocation().getPitch() < 70) {
            return;
        }
        ItemStack hand = event.getItem();
        if (hand == null || hand.getType() != Material.GLASS_BOTTLE || hand.getAmount() < 1) {
            return;
        }
        if (player.getLevel() < 5) {
            player.sendMessage(Component.text("You need at least 5 XP levels to forge a player essence.")
                    .color(NamedTextColor.RED));
            return;
        }
        event.setCancelled(true);

        player.setLevel(player.getLevel() - 5);
        hand.setAmount(hand.getAmount() - 1);
        if (hand.getAmount() == 0) {
            player.getInventory().setItemInMainHand(null);
        }

        ItemStack essence = ProtectionManager.createEssence(player);
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), essence);
        } else {
            player.getInventory().addItem(essence);
        }
        player.sendMessage(Component.text("You forged a shimmering white player essence, bound to you.")
                .color(NamedTextColor.WHITE));
    }
}
