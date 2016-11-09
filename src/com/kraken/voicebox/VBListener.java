package com.kraken.voicebox;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VBListener implements Listener {
	
	private VoiceBox plugin;
	
      public VBListener(VoiceBox plugin) {
    	  
    	  plugin.getServer().getPluginManager().registerEvents(this, plugin);
    	  this.plugin = plugin;
    	  
      }
      
      @EventHandler
      public void onPlayerJoin(PlayerJoinEvent e) {
    	  
          e.setJoinMessage( format( plugin.getMotd() ) );
    	  
      }
      
      public static String format(String format){
    	    return ChatColor.translateAlternateColorCodes('&', format);
      }
      
}
