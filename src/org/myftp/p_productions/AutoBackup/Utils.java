package org.myftp.p_productions.AutoBackup;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Server;

public class Utils {

	public static int broadcastToPlayers(Server server, String msg, String perm){
		AtomicInteger count = new AtomicInteger(0);
		server.getOnlinePlayers().forEach(player->{if(player.hasPermission(perm)) player.sendMessage(msg); count.incrementAndGet();});
		return count.get();
	}
	
	public static int broadcastToPlayers(String msg, String perm){
		return broadcastToPlayers(Bukkit.getServer(), msg, perm);
	}
	
	
	// Very specific for my needs.
	public static void notify(AutoBackup plugin, String msg){
		notify(plugin, msg, Level.INFO);
	}
	
	public static void notify(AutoBackup plugin, String msg, Level level){
		plugin.getLogger().log(level, msg);
		broadcastToPlayers(plugin.getServer(), plugin.prefix + msg, "autobackup.msg");
	}
}
