package com.vlw0052.coordinates.commands;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.vlw0052.coordinates.CoordinatePlugin;
import com.vlw0052.coordinates.util.Coordinate;

/**
 * CommandComplete class Overrides the onTabComplete to 
 * add values from the store into the possible tab completions.
 * 
 * @author Leon Watson
 * 
 * @since Jun 1, 2017
 */
public class CommandComplete implements TabCompleter {
	
	CoordinatePlugin plugin;
	
	public CommandComplete(CoordinatePlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		List<String> values = new ArrayList<String>();
		
		if(args.length == 1){
			
			String[] commands = {
					"add", "remove","update","distance","list","search"
			};
			for(String c: commands){
				if(c.startsWith(args[0]))
				values.add(c);
			}
		}
		String action = args[0];
		if(args.length == 2){
			switch(action){
				case "list":
				case "add" :
					return null;
				default:
					values.addAll(getStoredNames(args[1]));
				break;
				
					
			}
		}else if(args.length > 2){
			switch(action){
				case "add":
				case "list":
				case "update":
					return null;
				case "search":
				case "remove":
					values.addAll(getStoredNames(args[args.length - 1]));
					break;
				default: 
					return null;
			}
		}
		
		return values;
	}
	
	/**
	 * Gets the current names of all the coordinates in the store that start
	 * with the prefix passed in.
	 * @param prefix the starting value of the location typed.
	 * @return a list of strings that with the {@code prefix}.
	 */
	private List<String> getStoredNames(String prefix){

		List<Coordinate> coords = this.plugin.getStore().values();
		List<String> values = new ArrayList<String>();
		for(Coordinate c: coords)
			if(c.getName().startsWith(prefix))
				values.add(c.getName());
		return values;
	}
}
