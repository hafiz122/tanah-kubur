package me.syntaxable.minecraft.DeathChests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class DeathChestListener implements Listener
{
	private final List<Material> chestable_blocks;
	private final DeathChestPlugin plugin;

	public DeathChestListener(DeathChestPlugin P, List<Material> cb) {
		plugin=P;
		chestable_blocks=cb;
	}

	public DeathChestListener(DeathChestPlugin P) {
		plugin = P;
		chestable_blocks = new ArrayList<Material>();
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		Location deathLocation = player.getLocation();
		int numDrops = event.getDrops().size();
		Inventory inventory;

		if ( plugin.msgLocation() ) {
			player.sendMessage(ChatColor.GREEN + "You died at " +
			       deathLocation.getBlockX() + ",  " +
			       deathLocation.getBlockY() + ",  " +
			       deathLocation.getBlockZ());
		}

		if (numDrops > 0 ) {
			inventory = plugin.getServer().createInventory(null,
			                                (((numDrops + 8) / 9) * 9) );
			for (ItemStack item : event.getDrops()) {
				inventory.addItem(item);
			}

			if (inventory.contains(Material.CHEST)) {
				plugin.getServer().getScheduler().runTaskLater((Plugin)plugin,
						(Runnable) new DeathChestTask(player, inventory,
							deathLocation, chestable_blocks, plugin.debug()),
						plugin.delay());

				event.getDrops().clear();
			} else {
				
				player.sendMessage(ChatColor.GREEN + "You did not have any chests, so your items are on the ground.");
				player.sendMessage(ChatColor.GREEN + "Hurry up and retrieve them!");
				return;
			}
		}
	}
}
