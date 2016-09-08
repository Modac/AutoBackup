package org.myftp.p_productions.AutoBackup;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class AutoBackupExecutor implements CommandExecutor {
	AutoBackup plugin;
	
	public AutoBackupExecutor(AutoBackup plugin) {
		this.plugin=plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("autobackup")) {
			if( !sender.hasPermission("autobackup.autobackup")){
				sender.sendMessage(ChatColor.RED+"You don't have permission!");
				return true;
			}
			if(args.length<1)
				return false;
			switch(args[0].toLowerCase()){
				case "start":
					return startCommand(sender, cmd, label);
				case "enable":
					return enableCommand(sender, cmd, label);
				case "stop":
					return stopCommand(sender, cmd, label);
				case "now":
					return nowCommand(sender, cmd, label);
			}
		}
		return false;
			
	}
	
	private void rawEnable(){
		FileConfiguration config= plugin.getConfig();
		config.set("enabled", true);
		plugin.saveConfig();
	}
	
	private boolean enableCommand(CommandSender sender, Command cmd, String label) {
		rawEnable();
		Utils.notify(plugin, "Auto backup enabled");
		plugin.backupTask = BackupTask.enable(plugin, 3600*20);
		return true;
	}

	// TODO: permissions
	private boolean startCommand(CommandSender sender, Command cmd, String label) {
		rawEnable();
		Utils.notify(plugin, "Auto backup started");
		plugin.backupTask = BackupTask.start(plugin, 3600*20); 
		
		return true;
	}

	// TODO: permissions
	private boolean stopCommand(CommandSender sender, Command cmd, String label) {
		FileConfiguration config= plugin.getConfig();
		config.set("enabled", false);
		plugin.saveConfig();
		//sender.sendMessage(plugin.prefix+"Auto backup stopped");
		//plugin.getLogger().info("Auto backup stopped");
		Utils.notify(plugin, "Auto backup stopped");
		plugin.backupTask.cancel();
		return true;
	}

	// TODO: permissions
	private boolean nowCommand(CommandSender sender, Command cmd, String label) {
		//sender.sendMessage(plugin.prefix+"Backup started");
		Utils.notify(plugin, "Backup started");
		new BackupTask(plugin).runTask(plugin);
		return true;
	}
}
