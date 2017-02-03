package com.kraken.voicebox;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class RadioListener implements Listener {
	
	FileConfiguration players;
	File playersFile;
	
      public RadioListener(VoiceBox plugin, FileConfiguration players, File playersFile) {
    	  
    	  this.players = players;
    	  this.playersFile = playersFile;
    	  
      }
      
      public boolean onAir(Player player) {
    	  
    	  String UUIDString = player.getUniqueId().toString();
    	  return players.getBoolean(UUIDString + ".radio.broadcasting");
    	  
      }
      
      public void setOnAir(Player player, Boolean isOnAir) {
    	  
    	  String UUIDString = player.getUniqueId().toString();
    	  players.set(UUIDString + ".radio.broadcasting", isOnAir);
    	  
      }
      
      public boolean isBroadcasting(Player player) {
    	  
    	  String UUIDString = player.getUniqueId().toString();
    	  return players.getBoolean(UUIDString + ".radio.allowed");
    	  
      }
      
      public void setBroadcasting(String UUIDString, Boolean value) {
    	  
		  players.set(UUIDString + ".radio.allowed", value);
          
      }
      
      public int getFrequency(Player player) {
    	  
    	  String UUIDString = player.getUniqueId().toString();
    	  return players.getInt(UUIDString + ".radio.frequency");
    	  
      }
      
      public void setFrequency(Player player, Integer frequency) {
    	  
    	  String UUIDString = player.getUniqueId().toString();
		  players.set(UUIDString + ".radio.frequency", frequency);
    	  
      }
      
      public static String format(String format){
    	    return ChatColor.translateAlternateColorCodes('&', format);
      }
      
}
