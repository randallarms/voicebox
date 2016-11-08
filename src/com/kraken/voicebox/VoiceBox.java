// ========================================================================
// |VOICEBOX v0.2.2
// | by Kraken | https://www.spigotmc.org/members/kraken_.287802/
// | code inspired by various Bukkit & Spigot devs -- thank you. 
// |
// | Always free & open-source! If this plugin is being sold or re-branded,
// | please let me know on the SpigotMC site, or wherever you can. Thanks!
// | Source code: https://github.com/randallarms/voicebox
// ========================================================================

package com.kraken.voicebox;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VoiceBox extends JavaPlugin {

    @Override
    public void onEnable() {
    	
        getLogger().info("VoiceBox has been enabled.");
        
        //Chat type: 'say' (no command)
        getServer().getPluginManager().registerEvents(new Listener() {
        	
        	@EventHandler
        	public void playerChat(AsyncPlayerChatEvent e) {
        		
        		Player player = e.getPlayer();
        		
        		//Preparing the message
        		String rawMessage = e.getMessage();
        		String messageBulk = ("" + ChatColor.ITALIC + e.getPlayer().getName() + " says, ");
        			
	        		//Max distance from sender to receiver
	        		int blockDistance = 26;
	        		
	        		Location playerLocation = e.getPlayer().getLocation();
	        		
	        		for (Player peep : e.getRecipients()) {
	        			
	        			double peepDistance = peep.getLocation().distance(playerLocation);
	        			
	        			if (peepDistance <= blockDistance && peep != player) {
	        				peep.sendMessage(ChatColor.DARK_GREEN + messageBulk + ChatColor.RESET + ChatColor.DARK_GREEN +  "\"" + rawMessage + "\"");
	                    } else if (peepDistance <= blockDistance && peep == player) {
	                    	peep.sendMessage(ChatColor.GREEN + messageBulk + ChatColor.RESET + ChatColor.GREEN +  "\"" + rawMessage + "\"");
	                    }
	        			
	                }
        		
        		e.getRecipients().clear();
        		
        	}
        	
        }, this);
        
    }
    
    @Override
    public void onDisable() {
        getLogger().info("VoiceBox has been disabled.");
    }
    
    //VoiceBox commands
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
    	String command = cmd.getName();
    	Player player = (Player) sender;
    	
    	if (sender instanceof Player) {
    		
    		ChatRelay chat = new ChatRelay(player, args);
    		
	    	switch (command) {
	    	
	    	//Command: vb
	    		case "voicebox":
	    		case "vb":		
	    			
	    			player.sendMessage(ChatColor.LIGHT_PURPLE + "[VB]" + ChatColor.GRAY + " | VoiceBox | Immersive chat plugin.");
	                return true;
	                
	        //Command: clearChat        
	    		case "clearChat":
	    		case "clearchat":
	    			
	    			for (int i = 0; i < 20; i++) {
	            		player.sendMessage(" ");
	            	}
	                return true;
	                
	        //Command: y <msg> (Chat type: 'yell')  
	    		case "shout":
	    		case "yell":
	    		case "y":
	    			
	    			return chat.yell();
	    			
	    	//Command: re <name> <msg> (Chat type: 'reply')
	    		case "respond":
	    		case "reply":
	    		case "re":
	    			
	    			return chat.reply();
	    			
	        //Command: w <name> <msg> (Chat type: 'whisper')     
	    		case "tell":
	    		case "whisper":
	    		case "w":
	    			
	    			return chat.whisper();
	    	        
	    	}
	    	
    	}
    	
    	player.sendMessage(ChatColor.RED + "Your command was not recognized, or you have insufficient permissions.");
        return true;
        
    }
    
}
