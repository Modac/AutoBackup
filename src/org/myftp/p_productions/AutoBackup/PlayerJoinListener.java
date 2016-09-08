package org.myftp.p_productions.AutoBackup;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	
	private AutoBackup plugin;
	
	public PlayerJoinListener(AutoBackup plugin) {
		this.plugin=plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		plugin.backupTask.backedUp = false;
		//Utils.notify(plugin, "World is no longer backed up");
	}

}
