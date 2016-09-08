package org.myftp.p_productions.AutoBackup;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoBackup extends JavaPlugin{
	BackupTask backupTask;
	String prefix = ChatColor.GREEN+"["+getName()+"] "+ChatColor.RESET;
	
	@Override
	public void onEnable() {
		
		
		this.getCommand("autobackup").setExecutor(new AutoBackupExecutor(this));
		
		
		if(getConfig().getBoolean("enabled", false)){
			backupTask = BackupTask.start(this, 3600*20); 
		}
		
		this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
		
		
		// getLogger().info("AutoBackup activated");
		Utils.notify(this, "AutoBackup activated");
		
	}
	
	@Override
	public void onDisable() {
		Utils.notify(this, "AutoBackup deactivated"); 
	}
}
