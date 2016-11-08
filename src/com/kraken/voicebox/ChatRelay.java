package com.kraken.voicebox;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ChatRelay {
	
	Player player;
	String[] args;
	
	public ChatRelay(Player player, String[] args) {
		
		this.player = player;
		this.args = args;
		
	}
	
	public boolean whisper() {
		
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
	
	public boolean reply() {
		
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
		
	}

    public boolean yell() {
    	
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
    	
    }
    
}
