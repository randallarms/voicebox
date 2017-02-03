package com.kraken.voicebox;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VBListener implements Listener {
	
	private VoiceBox plugin;
	
	RadioListener radios;
	
    private File playersFile = new File("plugins/VoiceBox", "players.yml");
    private FileConfiguration players = YamlConfiguration.loadConfiguration(playersFile);
	
      public VBListener(VoiceBox plugin, RadioListener radios) {
    	  
    	  this.plugin = plugin;
    	  this.radios = radios;
    	  
      }
      
      @EventHandler
      public void onPlayerJoin(PlayerJoinEvent e) {
    	  
    	  Player player = (Player) e.getPlayer();
    	  String UUIDString = player.getUniqueId().toString();
    	  
    	  if ( !players.getKeys(false).contains(UUIDString) ) {
    		  
    		  players.set(UUIDString + ".info.name", player.getName());
    		  players.set(UUIDString + ".radio.allowed", false);
    		  players.set(UUIDString + ".radio.broadcasting", false);
    		  players.set(UUIDString + ".radio.frequency", 1);
    		  
    	  }
    	  
    	  if ( !player.getName().equals( players.getString( UUIDString + ".info.name") ) ) {
    		  
    		  players.set(UUIDString + ".info.name", player.getName());
    		  player.sendMessage("Name added");
    		  
    	  }
    	  
    	  try {
		  		players.save(playersFile);
			} catch (IOException e1) {
				System.out.println("[VB] Could not fully initialize player's radio info; expect possible errors.");
			}
    	  
    	  if ( !plugin.joinMsgEnabled() ) {
    		  
    		  e.setJoinMessage("");
    		  
    	  } else {
    	  
    		  e.getPlayer().sendMessage( format( plugin.getMotd() ) );
          
    	  }
    	  
      }
      
      public static String format(String format){
    	    return ChatColor.translateAlternateColorCodes('&', format);
      }
      
}
