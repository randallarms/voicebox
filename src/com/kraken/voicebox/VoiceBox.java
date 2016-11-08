// ========================================================================
// |VOICEBOX v0.3
// | by Kraken | https://www.spigotmc.org/members/kraken_.287802/
// | code inspired by various Bukkit & Spigot devs -- thank you. 
// |
// | Always free & open-source! If this plugin is being sold or re-branded,
// | please let me know on the SpigotMC site, or wherever you can. Thanks!
// | Source code: https://github.com/randallarms/voicebox
// ========================================================================

package com.kraken.voicebox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VoiceBox extends JavaPlugin {
	
	ArrayList<String> shhh = new ArrayList<String>(); 
    File censorFile = new File("plugins/VoiceBox", "censor.yml");
    FileConfiguration censor = YamlConfiguration.loadConfiguration(censorFile);

    @Override
    public void onEnable() {
    	
        getLogger().info("VoiceBox has been enabled.");
        
      //Initialize the censor with a dummy value
        censor.set("yarbles", true);
        
        try {
			censor.save(censorFile);
		} catch (IOException e1) {
			System.out.println("Could not properly initialize censor file; expect possible censor errors.");
		}
    
    	for ( String key : censor.getKeys(false) ) {
    		shhh.add( key.toLowerCase() );
        }
        
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
	        			boolean isCensored = false;
	        			
	        			//Check the censor
	        			for ( String badWord : shhh ) {
	        				
	        				if ( rawMessage.toLowerCase().contains(badWord) ) {
	        					player.sendMessage(ChatColor.RED + "You are forbidden from speaking banned phrases.");
	        					isCensored = true;
	        					break;
	        				}
	        				
	        			}
	        				
	        			if (!isCensored) {
	        				if (peepDistance <= blockDistance && peep != player) {
		        				peep.sendMessage(ChatColor.DARK_GREEN + messageBulk + ChatColor.RESET + ChatColor.DARK_GREEN +  "\"" + rawMessage + "\"");
		                    } else if (peepDistance <= blockDistance && peep == player) {
		                    	peep.sendMessage(ChatColor.GREEN + messageBulk + ChatColor.RESET + ChatColor.GREEN +  "\"" + rawMessage + "\"");
		                    }
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
    		
    		ChatRelay chat = new ChatRelay(player, args, shhh);
    		
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
	    			
	    	//Command: censor <add/remove> <phrase>
	    		case "censor":
	    			
	    			String arg;
	    			
		    		if ( player.isOp() && args.length > 1) {
		    			
		    			switch (args[0]) {
		    			
		    				case "add":
		    					arg = args[1].toLowerCase();
		    					if ( !shhh.contains( arg ) ) {
		    						shhh.add( arg );
		    						censor.set(arg, true);
		    				        try {
		    							censor.save(censorFile);
		    						} catch (IOException e1) {
		    							System.out.println("Could not properly initialize censor file; expect possible censor errors.");
		    						}
		    						player.sendMessage(ChatColor.GREEN + "Phrase added to the censor.");
		    						return true;	
		    					}
		    					
		    				case "remove":
		    					arg = args[1].toLowerCase();
		    					if ( shhh.contains( arg ) ) {
		    						shhh.remove( arg );
		    						censor.set(arg, null);
		    				        try {
		    							censor.save(censorFile);
		    						} catch (IOException e1) {
		    							System.out.println("Could not properly initialize censor file; expect possible censor errors.");
		    						}
		    						player.sendMessage(ChatColor.GREEN + "Phrase removed from the censor.");
		    					} else {
		    						player.sendMessage(ChatColor.RED + "Phrase not found in the censor.");
		    					}
		    					return true;
		    			
		    			}
		    			
		    		} else if ( args.length <= 1 ) {
		    			
		    			player.sendMessage(ChatColor.GRAY + "Unrecognized format, use \"/censor <add/remove> <phrase>\"");	
						return true;
						
		    		}
	    	        
	    	}
	    	
    	}
    	
    	player.sendMessage(ChatColor.RED + "Your command was not recognized, or you have insufficient permissions.");
        return true;
        
    }
    
}
