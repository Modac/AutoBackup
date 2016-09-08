package org.myftp.p_productions.AutoBackup;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class BackupTask extends BukkitRunnable {
	ConcurrentLinkedQueue<String> output;
	String cmd;
	AutoBackup plugin;
	AtomicBoolean finished;
	
	boolean backedUp = false;
	
	public BackupTask(AutoBackup plugin) {
		this.plugin=plugin;
		output = new ConcurrentLinkedQueue<>();
		cmd = plugin.getConfig().getString("script", "backup.bat");
		finished=new AtomicBoolean(true);
	}
	
	@Override
	public void run() {
		
		if(!finished.compareAndSet(true, false)){
			//plugin.getLogger().log(Level.INFO, "Skipped auto backup because last one is not finished");
			//plugin.getServer().broadcast(plugin.prefix+"Skipped auto backup because last one is not finished", "autobackup.msg");
			Utils.notify(plugin, "Skipped auto backup because last one is not finished");
			return;
		}
		if(backedUp){
			Utils.notify(plugin, "Skipped auto backup because world is already backed up");
			finished.set(true);
			return;
		}
		
		//plugin.getLogger().log(Level.INFO, "Starting backup");
		//plugin.getServer().broadcast(plugin.prefix+"Starting backup", "autobackup.msg");
		if(plugin.getServer().getOnlinePlayers().size()==0)
			backedUp = true;
		else
			backedUp = false;
		
		Utils.notify(plugin, "Starting backup");
		List<World> worlds=plugin.getServer().getWorlds();
		worlds.forEach(world->world.setAutoSave(false));
		
		new BackupTaskAsync(this).runTaskAsynchronously(plugin);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				String msg = output.poll();
				if(msg==null)
					return;
				if(msg=="AutoBackupStop"){
					finished.set(true);
					//plugin.getLogger().log(Level.INFO, "Backup finished");
					//plugin.getServer().broadcast(plugin.prefix+"Backup finished", "autobackup.msg");
					Utils.notify(plugin, "Backup finished");
					this.cancel();
					return;
				}
				//plugin.getLogger().log(Level.INFO, msg);
				//plugin.getServer().broadcast(plugin.prefix+msg, "autobackup.msg");
				Utils.notify(plugin, msg);
			}
		}.runTaskTimer(plugin, 0, 1);
		
		worlds.forEach(world->world.setAutoSave(true));
		
	}

	synchronized public String getBackupFile() {
		return cmd;
	}
	
	public static BackupTask start(AutoBackup plugin, long period){
		BackupTask backupTask = new BackupTask(plugin);
		backupTask.runTaskTimer(plugin, 20, period);
		return backupTask;
	}
	
	public static BackupTask enable(AutoBackup plugin, long period){
		BackupTask backupTask = new BackupTask(plugin);
		backupTask.runTaskTimer(plugin, period, period);
		return backupTask;
	}
	
}
