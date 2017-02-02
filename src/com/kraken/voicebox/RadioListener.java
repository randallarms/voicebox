package com.kraken.voicebox;

import java.io.File;
import java.io.IOException;

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
      
      public void setBroadcasting(Player player, Boolean isBroadcasting) {
    	  
    	  String UUIDString = player.getUniqueId().toString();
		  players.set(UUIDString + ".radio.allowed", isBroadcasting);
    	  
          try {
  			  players.save(playersFile);
  		  } catch (IOException e1) {
  			  System.out.println("[VB] Could not properly initialize players file; expect possible errors.");
  	  	  }
          
      }
      
      public int getFrequency(Player player) {
    	  
    	  String UUIDString = player.getUniqueId().toString();
    	  return players.getInt(UUIDString + ".radio.frequency");
    	  
      }
      
      public void setFrequency(Player player, Integer frequency) {
    	  
    	  String UUIDString = player.getUniqueId().toString();
		  players.set(UUIDString + ".radio.frequency", frequency);
    	  
          try {
  			  players.save(playersFile);
  		  } catch (IOException e1) {
  			  System.out.println("[VB] Could not properly initialize players file; expect possible errors.");
  	  	  }
    	  
      }
      
      public static String format(String format){
    	    return ChatColor.translateAlternateColorCodes('&', format);
      }
      
}
