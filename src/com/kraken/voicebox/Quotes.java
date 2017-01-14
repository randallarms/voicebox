package com.kraken.voicebox;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Quotes {
	
	File qFile = new File("plugins/VoiceBox", "quotes.yml");
	FileConfiguration quotes = YamlConfiguration.loadConfiguration(qFile);
	
	public Quotes(VoiceBox plugin) {
		
		if ( !qFile.exists() ) {
			quotes.set("1" + ".quote", "The general who loses a battle makes but few calculations beforehand.");
			quotes.set("1" + ".author", "Sun Tzu");
			quotes.set("2" + ".quote", "Great minds discuss ideas; average minds discuss events; small minds discuss people.");
			quotes.set("2" + ".author", "Eleanor Roosevelt");
			quotes.set("3" + ".quote", "It is better to be hated for what you are than to be loved for what you are not.");
			quotes.set("3" + ".author", "Andre Gide");
			quotes.set("4" + ".quote", "All that is gold does not glitter, Not all those who wander are lost...");
			quotes.set("4" + ".author", "JRR Tolkein");
			quotes.set("5" + ".quote", "...The old that is strong does not wither, Deep roots are not reached by the frost.");
			quotes.set("5" + ".author", "JRR Tolkein");
			try {
				quotes.save(qFile);
			} catch (IOException e) {
				// No need to fuss!
			}
		}
		
	}
	
	int i = 0;
	
	public void sendQuote(Player player) {
		
		for ( @SuppressWarnings("unused") String key : quotes.getKeys(false) ) {
			i++;
		}
		
		int n = new Random().nextInt(i) + 1;
		
		player.sendMessage( ChatColor.DARK_GRAY + "[" + n + "] " + ChatColor.GRAY + "\"" + quotes.get(n + ".quote") + "\" --" + ChatColor.GREEN + "" + quotes.get(n + ".author") );
		
	}
	
	public void addQuote(String name, String[] args) {
		
		i = 1;
		
		for ( @SuppressWarnings("unused") String key : quotes.getKeys(false) ) {
			i++;
		}
		
		//Concatenate the message
    	int n = 2;
    	String quote = new String();
    	int length = args.length;
    	
    	try {
    		
        	while (args[n] != null) {
        		
        		if (n >= 2) {
        			quote = quote + " " + args[n];
	        		n++;
        	    } else {
        	    	quote = quote + args[n];
	        		n++;
        	    }
        		
        		if (n >= length) {
        			break;
        		}
        		
        	}
        	
    	} catch (ArrayIndexOutOfBoundsException e) {
    	    // No need to fuss!
    	}
		
		quotes.set(i + ".quote", quote);
		quotes.set(i + ".author", name);
		try {
			quotes.save(qFile);
		} catch (IOException e) {
			// No need to fuss!
		}
		
	}
	
	public void delQuote(String name, int n) {
			
			quotes.set(n + ".quote", "");
			quotes.set(n + ".author", "");
			quotes.set(Integer.toString(n), "");
			
			try {
				quotes.save(qFile);
			} catch (IOException e) {
				// No need to fuss!
			}
			
		}
	    
	}
