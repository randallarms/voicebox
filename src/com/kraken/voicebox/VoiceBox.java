// ========================================================================
// |VOICEBOX v0.2.1
// | by Kraken | https://www.spigotmc.org/members/kraken_.287802/
// | code inspired by various Bukkit & Spigot devs -- thank you. 
// |
// | Always free & open-source! If this plugin is being sold or re-branded,
// | please let me know on the SpigotMC site, or wherever you can. Thanks!
// | Source code: https://github.com/randallarms/voicebox
// ========================================================================

package com.kraken.voicebox;

import org.bukkit.Bukkit;
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
	    			
	    			if (args.length > 0) {	
	    	    		int blockDistance = 126;
	    	    		Location playerLocation = player.getLocation();
	    	    		
	            		//Concatenate the message
	    	        	int n = 0;
	    	        	String rawMessage = new String();
	    	        	int length = args.length;
	    	    		
	    	        	while (args[n] != null) {
	    	        		
	    	        		if (n >= 1) {
	    		        		rawMessage = rawMessage + " " + args[n];
	    		        		n++;
	    	        	    } else {
	    		        		rawMessage = rawMessage + args[n];
	    		        		n++;
	    	        	    }
	    	        		
	    	        		if (n >= length) {
	    	        			break;
	    	        		}
	    	        		
	    	        	}   
	    			        	
	    	        	for (Player peep : Bukkit.getServer().getOnlinePlayers()) {
	    	        		if (peep.getLocation().distance(playerLocation) <= blockDistance && peep != player) {
	    	        			peep.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + player.getName() + " shouts aloud, " + ChatColor.RESET + "" + ChatColor.DARK_RED + "\"" + rawMessage + "\"");
	    	        		}
	    	        	}	           
	    	            player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You shout aloud, " + ChatColor.RESET + "" + ChatColor.RED + "\"" + rawMessage + "\"");
	    	            
	    	            return true;


	    	        } else {
	            		player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You shout out, monstrously, at the top of your lungs"
	            				+ " to all who can hear you!");
	            		return true;
	            	}
	    			
	    	//Command: re <name> <msg> (Chat type: 'reply')
	    		case "respond":
	    		case "reply":
	    		case "re":
	    			
	    			//Check if user is online and within 26 blocks
	    	        if (args.length > 0) {	
	    	        	Player targetPlayer = Bukkit.getPlayerExact(args[0]);
	    	    		int blockDistance = 26;
	    	    		Location playerLocation = player.getLocation();
	    	    		
	            		//Concatenate the message
	    	        	int n = 1;
	    	        	String rawMessage = new String();
	    	        	int length = args.length;
	    	    		
	    	        	if (length > 1) {
	    		        	while (args[n] != null) {
	    		        		
	    		        		if (n >= 2) {
	    			        		rawMessage = rawMessage + " " + args[n];
	    			        		n++;
	    		        	    } else {
	    			        		rawMessage = rawMessage + args[n];
	    			        		n++;
	    		        	    }
	    		        		
	    		        		if (n >= length) {
	    		        			break;
	    		        		}
	    		        		
	    		        	}
	    	        	}
	    	        	
	    	        	if (targetPlayer != null && length > 1) {	        		
	    		        	
	    		        	if (targetPlayer.getLocation().distance(playerLocation) <= blockDistance) {
	    			        	
	    			        	for (Player peep : Bukkit.getServer().getOnlinePlayers()) {
	    			        		if (peep.getLocation().distance(playerLocation) <= blockDistance && peep != player && peep != targetPlayer) {
	    			        			peep.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + player.getName() + " replies to " + targetPlayer.getName() + ", " + ChatColor.RESET + "" + ChatColor.DARK_GREEN + "\"" + rawMessage + "\"");
	    			        		}
	    			        	}
	    			            targetPlayer.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + player.getName() + " replies to you, " + ChatColor.RESET + "" + ChatColor.GREEN + "\"" + rawMessage + "\"");
	    			            player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + "You reply to " + targetPlayer.getName() + ", " + ChatColor.RESET + "" + ChatColor.DARK_GREEN + "\"" + rawMessage + "\"");
	    			            return true;
	    		        	
	    		        	} else {
	    		        	    player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + "You reply to no one in particular, " + ChatColor.RESET + "" + ChatColor.DARK_GREEN + "\"" + rawMessage + "\"");
	    			        	for (Player peep : Bukkit.getServer().getOnlinePlayers()) {
	    			        		if (peep.getLocation().distance(playerLocation) <= blockDistance && peep != player) {
	    			        			peep.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + player.getName() + " replies to no one in particular, " + ChatColor.RESET + "" + ChatColor.DARK_GREEN + "\"" + rawMessage + "\"");
	    			        		}
	    			        	}
	    		        	    return true;
	    		        	}
	    		        	
	    	        	} else {
	    	        		player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + "Try name, then message.");
	    	        		return true;
	    	        	}
	    	        } else {
	            		player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + "Who are you replying to?");
	            		return true;
	            	}
	        //Command: w <name> <msg> (Chat type: 'whisper')     
	    		case "tell":
	    		case "whisper":
	    		case "w":
	    			
	    			//Check if user is online and within 5 blocks
	    	        if (args.length > 0) {	
	    	        	Player targetPlayer = Bukkit.getPlayerExact(args[0]);
	    	    		int blockDistance = 5;
	    	    		Location playerLocation = player.getLocation();        	
	    	
	    	        	if ( targetPlayer != null && targetPlayer.getLocation().distance(playerLocation) <= blockDistance
	    	        			&& !targetPlayer.equals(player) ) {
	    	        		
	    	        		//Concatenate the message
	    		        	int n = 1;
	    		        	String rawMessage = new String();
	    		        	int length = args.length;
	    		        	
	    		        	try {
	    		        		
	    			        	while (args[n] != null) {
	    			        		
	    			        		if (n >= 2) {
	    				        		rawMessage = rawMessage + " " + args[n];
	    				        		n++;
	    			        	    } else {
	    				        		rawMessage = rawMessage + args[n];
	    				        		n++;
	    			        	    }
	    			        		
	    			        		if (n >= length) {
	    			        			break;
	    			        		}
	    			        		
	    			        	}
	    			        	
	    			            targetPlayer.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + player.getName() + " whispers to you, " + ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "\"" + rawMessage + "\"");
	    			            player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "You whisper to " + player.getName() + ", " + ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "\"" + rawMessage + "\"");
	    			            return true;
	    		        	
	    		        	} catch (ArrayIndexOutOfBoundsException e) {
	    		        	    player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "Your whispers go unheard.");
	    		        	    return true;
	    		        	}
	    		        	
	    	        	} else {
	    	        		player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "You whisper to yourself.");
	    	        		return true;
	    	        	}
	    	        } else {
	            		player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "You whisper to yourself.");
	            		return true;
	            	}
	    	        
	    	}
	    	
    	}
    	
    	player.sendMessage(ChatColor.RED + "Your command was not recognized, or you have insufficient permissions.");
        return true;
        
    }
    
}
