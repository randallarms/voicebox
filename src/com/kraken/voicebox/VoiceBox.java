// ========================================================================
// |VOICEBOX v0.6.2
// | by Kraken | https://www.spigotmc.org/members/kraken_.287802/
// | code inspired by various Bukkit & Spigot devs -- thank you.
// | code quality tested by *fatpigsarefat* -- THANK YOU!
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
    
    private File playersFile = new File("plugins/VoiceBox", "players.yml");
    private FileConfiguration players = YamlConfiguration.loadConfiguration(playersFile);
    
    RadioListener radios;
    
	String motd;
	Boolean joinMsgEnabled;

    @Override
    public void onEnable() {
    	
        getLogger().info("[VB] VoiceBox has been enabled.");
    	PluginManager pm = getServer().getPluginManager();
		
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
			System.out.println("[VB] Could not properly initialize censor file; expect possible censor errors.");
		}
    
    	for ( String key : censor.getKeys(false) ) {
    		shhh.add( key.toLowerCase() );
        }
    	
      //Initialize the players file
        try {
			players.save(playersFile);
		} catch (IOException e1) {
			System.out.println("[VB] Could not properly save players file; expect possible errors.");
		}
        
      //Initialize radios
    	radios = new RadioListener(this, players, playersFile);
    	pm.registerEvents(radios, this);
    	
      //Initialize main listener
    	VBListener listener = new VBListener(this, radios);
		pm.registerEvents(listener, this);
    	
      //Chat type: 'say' (no command), 'radio' (when broadcasting)
        getServer().getPluginManager().registerEvents(new Listener() {
        	
        	@EventHandler
        	public void playerChat(AsyncPlayerChatEvent e) {
        		
        		Player player = e.getPlayer();
        		
        		if ( radios.isBroadcasting(player) && radios.onAir(player) ) {
        			
        			String[] args = e.getMessage().split(" ");
        			
        			ChatRelay chat = new ChatRelay(player, args, shhh);
        			chat.broadcast(radios);
        			
        			e.getRecipients().clear();
        			
        		} else {
        		
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
        		
        	}
        	
        }, this);
        
    }
    
    @Override
    public void onDisable() {
        getLogger().info("[VB] VoiceBox has been disabled.");
    }
    
    //VoiceBox commands
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
    	String command = cmd.getName();
    	
    	if (sender instanceof Player) {
    		
        	Player player = (Player) sender;
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
    							+ " subtitle {\"text\":\"Light custom chat plugin by kraken_ (v0.6.2)\",\"color\":\"gold\"}");  
    					
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
	         
	        //Command: grumble (Chat type: 'grumble')  
	    		case "grumble":
	    			
	    			return chat.grumble();
	    	
	    	//Command: radio
	    		case "radio":
	    			
	    			//Personal radio control
	    			if ( args.length == 1 ) {
	    			
	    				switch ( args[0].toLowerCase() ) {
	    					case "enable":
	    					case "on":
	    					case "true":
	    						
	    						if ( radios.isBroadcasting(player) ) {
	    							radios.setOnAir(player, true);
	    							savePlayersFile();
	    							player.sendMessage(ChatColor.GREEN + "Your radio has been turned on.");
	    						} else {
	    							player.sendMessage(ChatColor.RED + "You do not have radio access.");
	    						}
	    						return true;
	    						
	    					case "disable":
	    					case "off":
	    					case "false":
	    						
	    						if ( radios.isBroadcasting(player) ) {
	    							radios.setOnAir(player, false);
	    							savePlayersFile();
	    							player.sendMessage(ChatColor.GREEN + "Your radio has been turned off.");
	    						} else {
	    							player.sendMessage(ChatColor.RED + "You do not have radio access.");
	    						}
	    						return true;
	    						
	    					default:
	    						
	    						player.sendMessage(ChatColor.RED + "Try entering \"/radio <on/off>\".");
	    						return true;
    						
	    				}
	    			
	    			//Others' radio control
	    			} else if ( player.isOp() && args.length == 2) {
	    				
	    				switch ( args[0].toLowerCase() ) {
	    					case "enable":
	    					case "on":
	    					case "true":
	    						
	    						if ( Bukkit.getPlayerExact( args[1] ) == null ) {
    								player.sendMessage(ChatColor.RED + "Player not found!");
    	    						return true;
								}
	    						
	    						Player target = (Player) Bukkit.getPlayerExact( args[1] );
    							String UUIDString = target.getUniqueId().toString();
	    						
    							players.set(UUIDString + ".radio.allowed", true);
    							players.set(UUIDString + ".info.name", target.getName());
								savePlayersFile();
								radios.setBroadcasting(UUIDString, true);
								
								player.sendMessage(ChatColor.GREEN + "Radio access granted.");
								return true;
	    						
	    					case "disable":
	    					case "off":
	    					case "false":
	    						
	    						if ( Bukkit.getPlayerExact( args[1] ) == null ) {
    								player.sendMessage(ChatColor.RED + "Player not found!");
    	    						return true;
								}
	    						
	    						Player targetB = (Player) Bukkit.getPlayerExact( args[1] );
    							String UUIDStringB = targetB.getUniqueId().toString();
	    						
    							players.set(UUIDStringB + ".radio.allowed", false);
    							players.set(UUIDStringB + ".info.name", targetB.getName());
								savePlayersFile();
								radios.setBroadcasting(UUIDStringB, false);
								
								player.sendMessage(ChatColor.GREEN + "Radio access removed.");
								return true;
	    						
	    					default:
	    						
	    						player.sendMessage(ChatColor.RED + "Try entering \"/radio <on/off> <playerName>\".");
	    						return true;
	    						
	    				}
	    				
	    			}
	    				
	    			player.sendMessage(ChatColor.RED + "Your command was not recognized, or you have insufficient permissions.");
	    			return true;
	    			
	        //Command: ch 
	    		case "freq":
	    		case "frequency":
	    		case "channel":
	    		case "ch":
	    		case "tune":
	    			
	    			Integer frequency;
	    			
	    		    try { 
	    		        frequency = Integer.parseInt(args[0]); 
	    		    } catch(NumberFormatException e) { 
	    		    	player.sendMessage(ChatColor.RED + "Channel must be an integer number between 1 and 9999.");
	    		        return true; 
	    		    } catch(ArrayIndexOutOfBoundsException e) {
	    		    	player.sendMessage(ChatColor.RED + "Try \"/ch <#>\".");
	    		        return true;
	    		    }
	    		    
	    		    if ( frequency <= 0 || frequency > 9999 ) {
	    		    	player.sendMessage(ChatColor.RED + "Channel must be an integer number between 1 and 9999 (inclusive).");
	    		        return true; 
	    		    }
	    			
	    			if ( args.length == 1 ) {
	    				if ( radios.isBroadcasting(player) ) {
		    				player.sendMessage(ChatColor.GREEN + "You are now set to channel " + frequency + ".");
		    				radios.setFrequency(player, frequency);
		    				savePlayersFile();
	    				} else {
	    					player.sendMessage(ChatColor.RED + "You do not have radio access.");
	    				}
	    			} else {
	    				player.sendMessage(ChatColor.RED + "Too many arguments; try \"/ch <#>\".");
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
		    					new Quotes(this).sendRandomQuote(player);
		    					return true;
		    					
		    				case 1:	
		    					
		    					File qFile = new File("plugins/VoiceBox", "quotes.yml");
		    					FileConfiguration quotes = YamlConfiguration.loadConfiguration(qFile);
		    					
		    					if ( quotes.getString(args[0] + ".quote") != null ) {
		    						int n = Integer.valueOf(args[0]);
		    						new Quotes(this).sendQuote(player, n);
		    						return true;
		    					} else {
		    						player.sendMessage(ChatColor.GRAY + "Unrecognized format or quote not found.");	
		    						return true;
	    						}
							
							default:
								switch (args[0]) {
									case "add":
										new Quotes(this).addQuote(player, args[1], args);
										return true;
										
									case "del":
									case "delete":
										new Quotes(this).delQuote(player, args[1]);
										return true;
								}
	    					
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
			    					getConfig().set("joinMsgEnabled", true);
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
			    					getConfig().set("joinMsgEnabled", false);
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
	    	
    	} else {
    		
	    	switch (command) {
	    	
	    	//Command: vb
	    		case "voicebox":
	    		case "vb":		
	    			
	    			if ( args.length > 1) {
	    				
	    				switch ( args[0].toLowerCase() ) {
	    				
	    					case "motd":
	    						String message = args[1];
	    						for (int i = 2; i < args.length; i++) {
	    							message = message + " " + args[i];
	    						}
	    						getConfig().set( "motd", message );
    							saveConfig();
	    						this.motd = message;
	    						System.out.println(ChatColor.LIGHT_PURPLE + "[VB]" + ChatColor.GREEN + " | MotD successfully set to: \"" + message + "\"");
	    						return true;
	    				
	    				}
	    				
	    			} else if (args.length == 0) {
	    			
	    				System.out.println(ChatColor.LIGHT_PURPLE + "[VOICEBOX] Light custom chat plugin by kraken_ (v0.6.2)");
		                return true;
		                
	    			} else {
	    				
	    				System.out.println(ChatColor.RED + "Your command was not recognized, or you have insufficient permissions.");
	    		        return true;
	    				
	    			}
	    	
	    	//Command: radio
	    		case "radio":
	    			
	    			//Personal radio control (disabled on console command)
	    			if ( args.length == 1 ) {
	    				
						System.out.println(ChatColor.RED + "Try entering \"/radio <on/off> <name>\".");
						return true;
	    			
	    			//Others' radio control
	    			} else if ( args.length == 2) {
	    				
	    				switch ( args[0].toLowerCase() ) {
	    					case "enable":
	    					case "on":
	    					case "true":
	    						
	    						if ( Bukkit.getPlayerExact( args[1] ) == null ) {
	    							System.out.println(ChatColor.RED + "Player not found!");
    	    						return true;
								}
	    						
	    						Player target = (Player) Bukkit.getPlayerExact( args[1] );
    							String UUIDString = target.getUniqueId().toString();
	    						
    							players.set(UUIDString + ".radio.allowed", true);
    							players.set(UUIDString + ".info.name", target.getName());
								savePlayersFile();
								radios.setBroadcasting(UUIDString, true);
								
								System.out.println(ChatColor.GREEN + "Radio access granted.");
								return true;
	    						
	    					case "disable":
	    					case "off":
	    					case "false":
	    						
	    						if ( Bukkit.getPlayerExact( args[1] ) == null ) {
	    							System.out.println(ChatColor.RED + "Player not found!");
    	    						return true;
								}
	    						
	    						Player targetB = (Player) Bukkit.getPlayerExact( args[1] );
    							String UUIDStringB = targetB.getUniqueId().toString();
	    						
    							players.set(UUIDStringB + ".radio.allowed", false);
    							players.set(UUIDStringB + ".info.name", targetB.getName());
								savePlayersFile();
								radios.setBroadcasting(UUIDStringB, false);
								
								System.out.println(ChatColor.GREEN + "Radio access removed.");
								return true;
	    						
	    					default:
	    						
	    						System.out.println(ChatColor.RED + "Try entering \"/radio <on/off> <playerName>\".");
	    						return true;
	    						
	    				}
	    				
	    			}
	    				
	    			System.out.println(ChatColor.RED + "Your command was not recognized, or you have insufficient permissions.");
	    			return true;
	    			
	    	//Command: censor <add/remove> <phrase>
	    		case "censor":
	    			
	    			String arg;
	    			
		    		if ( args.length > 1) {
		    			
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
		    				        System.out.println(ChatColor.GREEN + "Phrase added to the censor.");
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
		    				        System.out.println(ChatColor.GREEN + "Phrase removed from the censor.");
		    					} else {
		    						System.out.println(ChatColor.RED + "Phrase not found in the censor.");
		    					}
		    					return true;
		    			
		    			}
		    			
		    		} else if ( args.length <= 1 ) {
		    			
		    			System.out.println(ChatColor.RED + "Unrecognized format, use \"/censor <add/remove> <phrase>\"");	
						return true;
						
		    		}
	    				
	    		//Command: joinMsg <on/off>
		    		case "joinMsg":
		    		case "joinmsg":
		    		case "joinMessage":
		    		case "joinmessage":
		    			
			    		if ( args.length == 1) {
			    			
			    			switch (args[0]) {
			    			
			    				case "on":
			    				case "true":
			    				case "enable":
			    				case "enabled":
			    					getConfig().set("joinMsgEnabled", true);
			    					saveConfig();
			    					this.joinMsgEnabled = true;
			    					
			    					System.out.println(ChatColor.LIGHT_PURPLE + "[VB]" + ChatColor.GREEN + " | Join message is now enabled.");
			    					return true;
			    					
			    				case "off":
			    				case "false":
			    				case "disable":
			    				case "disabled":
			    					getConfig().set("joinMsgEnabled", false);
			    					saveConfig();
			    					this.joinMsgEnabled = false;
			    					
			    					System.out.println(ChatColor.LIGHT_PURPLE + "[VB]" + ChatColor.GREEN 
			    											+ " | Join message is now " + ChatColor.RED 
			    											+ "disabled" + ChatColor.GREEN + ".");
			    					return true;
			    			
			    			}
			    			
			    		} else {
			    			
			    			System.out.println(ChatColor.RED + "Unrecognized format, use \"/joinMsg <on/off>\"");	
							return true;
							
			    		}
	    	        
	    	}
    		
    		System.out.println("[VB] Your command was not recognized.");
    		return true;
    		
    	}
    	
    	System.out.println("[VB] Commands can only be executed from the console or by players.");
    	return true;
    	
    }
    
    public void savePlayersFile() {
    	
        try {
			players.save(playersFile);
		} catch (IOException e1) {
			System.out.println("[VB] Could not properly save players file; expect possible errors.");
		}
    	
    }
    
    public String getMotd() {
  	  
  	    return motd;
  	  
    }
    
    public Boolean joinMsgEnabled() {
  	    return joinMsgEnabled;
    }
    
}
