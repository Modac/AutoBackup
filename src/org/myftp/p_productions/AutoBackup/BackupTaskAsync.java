package org.myftp.p_productions.AutoBackup;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.bukkit.scheduler.BukkitRunnable;

public class BackupTaskAsync extends BukkitRunnable{
	BackupTask task;
	
	public BackupTaskAsync(BackupTask task) {
		this.task=task;
	}

	@Override
	public void run() {
		
		
		String command=task.getBackupFile();
 
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			//p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "Cp850"));
			String line = "";			
			while ((line = reader.readLine())!= null) {
				task.output.add(line);
			}
			task.output.add("AutoBackupStop");
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 
	}
	
	
	
}
