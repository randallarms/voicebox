// ========================================================================
// |VOICEBOX v0.4.1.1
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

import org.bukkit.Bukkit;
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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VoiceBox extends JavaPlugin {
	
	public ArrayList<String> shhh = new ArrayList<String>(); 
    private File censorFile = new File("plugins/VoiceBox", "censor.yml");
    private FileConfiguration censor = YamlConfiguration.loadConfiguration(censorFile);
	String motd;
	Boolean joinMsgEnabled;

    @Override
    public void onEnable() {
    	
        getLogger().info("VoiceBox has been enabled.");
    	PluginManager pm = getServer().getPluginManager();
    	VBListener listener = new VBListener(this);
		pm.registerEvents(listener, this);
		
		if (getConfig().getString("motd") == null) {
			getConfig().set("motd", "Welcome to the server!");
			saveConfig();
		}
		
		this.motd = getConfig().getString("motd");
		
		if ( getConfig().get("joinMsgEnabled") == null ) {
			getConfig().set("joinMsgEnabled", true);
			saveConfig();
		}
		
		this.joinMsgEnabled = getConfig().getBoolean("joinMsgEnabled");
		
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
	    			
	    			if ( player.isOp() && args.length > 1) {
	    				
	    				switch ( args[0].toLowerCase() ) {
	    				
	    					case "motd":
	    						String message = args[1];
	    						for (int i = 2; i < args.length; i++) {
	    							message = message + " " + args[i];
	    						}
	    						getConfig().set( "motd", message );
    							saveConfig();
	    						this.motd = message;
	    						player.sendMessage(ChatColor.LIGHT_PURPLE + "[VB]" + ChatColor.GREEN + " | MotD successfully set to: \"" + message + "\"");
	    						return true;
	    				
	    				}
	    				
	    			} else if (args.length == 0) {
	    			
	    				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName().toString() + " times 15 80 15");
    					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName().toString() 
    							+ " title {\"text\":\"VoiceBox\",\"color\":\"light_purple\",\"bold\":\"true\"}");
    					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName().toString() 
    							+ " subtitle {\"text\":\"Light custom chat plugin by kraken_\",\"color\":\"gold\"}");  
    					
		                return true;
		                
	    			} else {
	    				
	    				player.sendMessage(ChatColor.RED + "Your command was not recognized, or you have insufficient permissions.");
	    		        return true;
	    				
	    			}
	                
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
		    		
		    	//Command: quote
	    			case "quote":
	    			case "quotes":
	    				
	    				switch (args.length) {
	    					
		    				case 0:
		    					new Quotes(this).sendQuote(player);
		    					return true;
		    					
		    				case 1:	
		    					player.sendMessage(ChatColor.GRAY + "Unrecognized format, use \"/quote\"");	
								return true;
							
							default:
								new Quotes(this).addQuote(args[0], args);
								return true;
	    					
	    				}
	    				
	    		//Command: joinMsg <on/off>
		    		case "joinMsg":
		    		case "joinmsg":
		    		case "joinMessage":
		    		case "joinmessage":
		    			
			    		if ( player.isOp() && args.length == 1) {
			    			
			    			switch (args[0]) {
			    			
			    				case "on":
			    				case "true":
			    				case "enable":
			    				case "enabled":
			    					getConfig().set("joinMessageEnabled", true);
			    					saveConfig();
			    					this.joinMsgEnabled = true;
			    					
			    				    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName().toString() + " times 15 26 5");
			    					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName().toString() 
			    							+ " title {\"text\":\"Join Message\",\"color\":\"light_purple\"}");
			    					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName().toString() 
			    							+ " subtitle {\"text\":\"Enabled\",\"color\":\"green\"}"); 
			    					
			    					return true;
			    					
			    				case "off":
			    				case "false":
			    				case "disable":
			    				case "disabled":
			    					getConfig().set("joinMessageEnabled", false);
			    					saveConfig();
			    					this.joinMsgEnabled = false;
			    					
			    				    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName().toString() + " times 15 26 5");
			    					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName().toString() 
			    							+ " title {\"text\":\"Join Message\",\"color\":\"light_purple\"}");
			    					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName().toString() 
			    							+ " subtitle {\"text\":\"Disabled\",\"color\":\"red\"}");  
			    					
			    					return true;
			    			
			    			}
			    			
			    		} else {
			    			
			    			player.sendMessage(ChatColor.GRAY + "Unrecognized format, use \"/joinMsg <on/off>\"");	
							return true;
							
			    		}
	    	        
	    	}
	    	
    	}
    	
    	player.sendMessage(ChatColor.RED + "Your command was not recognized, or you have insufficient permissions.");
        return true;
        
    }
    
    public String getMotd() {
  	  
  	    return motd;
  	  
    }
    
    public Boolean joinMsgEnabled() {
  	    return joinMsgEnabled;
    }
    
}
