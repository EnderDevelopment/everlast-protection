package com.enderdevelopment.plugin.listener;

import com.enderdevelopment.plugin.Main;
import com.enderdevelopment.plugin.ProtectionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.AnvilInventory;

public class AnvilListener implements Listener {
    private final Main plugin;

    public AnvilListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack item = inventory.getItem(0);
        ItemStack token = inventory.getItem(1);
        if (item == null || token == null) {
            return;
        }
        if (!ProtectionManager.isToken(token) || !ProtectionManager.isEligible(item)) {
            return;
        }
        if (!(event.getView().getPlayer() instanceof Player player)) {
            return;
        }
        String tokenOwner = ProtectionManager.ownerOf(token);
        if (tokenOwner == null || !tokenOwner.equals(player.getUniqueId().toString())) {
            return;
        }
        if (ProtectionManager.countProtected(player) >= 3) {
            return;
        }
        ItemStack result = ProtectionManager.protect(player, item, token);
        if (result == null) {
            return;
        }
        event.setResult(result);
        inventory.setRepairCost(5);
        player.updateInventory();
    }
}
