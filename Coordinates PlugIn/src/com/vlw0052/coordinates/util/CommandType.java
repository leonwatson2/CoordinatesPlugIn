package com.vlw0052.coordinates.util;

public enum CommandType {
	ADD,
	REMOVE,
	UPDATE,
	SEARCH,
	DISTANCE,
	LIST;
	
	public static CommandType getCommandType(String typeName){
		switch(typeName.toLowerCase()){
			case "add":
				return ADD;
			case "remove":
				return REMOVE;
			case "update":
				return UPDATE;
			case "search":
				return SEARCH;
			case "distance":
				return DISTANCE;
			case "list":
				return LIST;
		}
		return null;
	}

}
