package com.enderdevelopment.plugin.listener;

import com.enderdevelopment.plugin.Main;
import com.enderdevelopment.plugin.ProtectionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

public class DeathListener implements Listener {
    private final Main plugin;

    public DeathListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String uuid = player.getUniqueId().toString();
        Iterator<ItemStack> it = event.getDrops().iterator();
        while (it.hasNext()) {
            ItemStack drop = it.next();
            if (ProtectionManager.isProtected(drop)) {
                String owner = ProtectionManager.ownerOf(drop);
                if (owner != null && owner.equals(uuid)) {
                    // Keep the item with its owner: remove from drops, restore to player.
                    it.remove();
                    player.getInventory().addItem(drop);
                }
            }
        }
    }
}
