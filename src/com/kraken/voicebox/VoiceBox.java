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
        
    	//Initialize new variable of type "Player" named "player" which "=" the "(Player)" named "sender"
    	Player player = (Player) sender;
    	
    	//Command: vb
        if (cmd.getName().equalsIgnoreCase("vb") && sender instanceof Player) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "[VB]" + ChatColor.GRAY + " | VoiceBox | Immersive chat plugin.");
            return true;
        } //End of vb command
        
        //Command: bc (Chat type: 'broadcast/radio')
            // "/bc 4 Message"
            // broadcasts "Message" to all listeners who have a radio and are tuned to channel 4
        //End of broadcast/radio command
        
        //Command: y <msg> (Chat type: 'yell')
        if (cmd.getName().equalsIgnoreCase("y") && sender instanceof Player) {
        	
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
        	
        } //End of yell command
        
        //Command: re <name> <msg> (Chat type: 'reply')
        if (cmd.getName().equalsIgnoreCase("re") && sender instanceof Player) {
        	
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
        	
        } //End of reply command
        
    	//Command: w <name> <msg> (Chat type: 'whisper')
        if (cmd.getName().equalsIgnoreCase("w") && sender instanceof Player) {
	        	
        	//Check if user is online and within 5 blocks
	        if (args.length > 0) {	
	        	Player targetPlayer = Bukkit.getPlayerExact(args[0]);
	    		int blockDistance = 5;
	    		Location playerLocation = player.getLocation();        	
	
	        	if (targetPlayer != null && targetPlayer.getLocation().distance(playerLocation) <= blockDistance) {
	        		
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
        	
        } //End of whisper command
    	
        return false;
    }
    
}
