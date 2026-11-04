package com.enderdevelopment.plugin.listener;

import com.enderdevelopment.plugin.Main;
import com.enderdevelopment.plugin.ProtectionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CraftListener implements Listener {
    private final Main plugin;

    public CraftListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof org.bukkit.entity.Player player)) {
            return;
        }
        Recipe recipe = event.getRecipe();
        if (recipe == null || !(recipe instanceof org.bukkit.inventory.ShapedRecipe)) {
            return;
        }

        List<ItemStack> matrix = new ArrayList<>();
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item != null && !item.getType().isAir()) {
                matrix.add(item.clone());
            }
        }
        if (matrix.size() != 9) {
            return;
        }

        int totems = 0;
        int stars = 0;
        int essences = 0;
        String essenceOwner = null;
        for (ItemStack item : matrix) {
            if (item.getType() == Material.TOTEM_OF_UNDYING) {
                totems++;
            } else if (item.getType() == Material.NETHER_STAR) {
                stars++;
            } else if (ProtectionManager.isEssence(item)) {
                essences++;
                String owner = ProtectionManager.ownerOf(item);
                if (owner == null) {
                    return;
                }
                if (essenceOwner == null) {
                    essenceOwner = owner;
                } else if (!essenceOwner.equals(owner)) {
                    player.sendMessage(Component.text("All three player essences must belong to the same player.")
                            .color(NamedTextColor.RED));
                    return;
                }
            } else {
                return;
            }
        }

        if (totems == 4 && stars == 2 && essences == 3 && essenceOwner != null) {
            // Only the owner of the essences may forge the token from them.
            if (!essenceOwner.equals(player.getUniqueId().toString())) {
                player.sendMessage(Component.text("Only the player who the essences belong to can forge this token.")
                        .color(NamedTextColor.RED));
                return;
            }
            UUID ownerUuid = UUID.fromString(essenceOwner);
            event.setCancelled(true);
            event.getInventory().setResult(ProtectionManager.createToken(ownerUuid));
            player.sendMessage(Component.text("You forged an Everlasting Token.")
                    .color(NamedTextColor.LIGHT_PURPLE));
        }
    }
}
