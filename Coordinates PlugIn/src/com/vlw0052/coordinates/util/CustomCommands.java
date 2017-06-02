package com.vlw0052.coordinates.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
/**
 * CustomCommands holds a map of {@link CustomCommand}s.
 * 
 * It provides a way to get all the {@code aliases} of all
 * the {@link CustomCommand}s and converting a string to a {@code CustomCommand}
 * in the Map.
 * 
 * @author Leon Watson
 * @since 2017
 *
 */
public class CustomCommands {
	
	private Map<CommandType, CustomCommand> commands;
	
	public CustomCommands() {
		this.commands = new HashMap<CommandType, CustomCommand>();
	}
	
	public void getAddAliases(){
	
	}
	
	/**
	 * insets the command into the {@code map} of the CustomCommands object
	 * @param key is the CommandType 
	 * @param command is the {@link CustomCommand} to be inserted
	 */
	public void insertCommand(CommandType key, CustomCommand command){
		this.commands.put(key, command);
	}
	
	/**
	 * Searches for the command in the custom commands.
	 * Return the type of command of the value passed in.
	 * If the command is invalid it returns null
	 * @param command is the string value 
	 * @return return an enum value from {@code CommandType}
	 * 
	 */
	public CommandType getCommandType(String command){
		Iterator<Entry<CommandType, CustomCommand>> iter = commands.entrySet().iterator();
		while(iter != null && iter.hasNext()){
			Map.Entry<CommandType, CustomCommand> entry = iter.next();
			if(entry.getValue().hasAlias(command)){
				
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	/**
	 * Gets all the main names of the {@code CustomCommand}s in the commands.
	 * 
	 * @return List<String> of all the main names of the commands.
	 */
	public List<String> getMainNames(){
		List<String> all = new ArrayList<String>();
		for(CommandType c: commands.keySet()){
			all.add(c.name().toLowerCase());
		}
		return all;
	}
	
	/**
	 * Gets all aliases from the {@code commands} in the Map.
	 * 
	 * @return List<String> of all the aliases of the commands.
	 * 
	 */
	public List<String> getAllAliases(){
		List<String> all = new ArrayList<String>();
		for(CustomCommand c : commands.values()){
			all.addAll(c.aliases);
		}
		return all;
	}

	
}
