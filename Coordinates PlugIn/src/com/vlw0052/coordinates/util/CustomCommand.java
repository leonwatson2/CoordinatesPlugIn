package com.vlw0052.coordinates.util;

import java.util.List;

public class CustomCommand {
	
	public List<String> aliases;
	public CommandType type;
	
	public CustomCommand(String typ, List<String> a){
		this.aliases = a;
		this.type = CommandType.getCommandType(typ);
		
	}
	
	
	/**
	 * Checks if the value is an alias of the commands
	 * returns true if so, otherwise returns false.
	 * @param value is the string that is searched for in the 
	 * aliases
	 * @return {@code true} if the value is found {@code false} 
	 * if the value is not found.
	 * 
	 */
	public boolean hasAlias(String value){
		for(String temp: aliases){
			if(temp.equalsIgnoreCase(value))
				return true;
		}
		
		return false;
	}
	
}
