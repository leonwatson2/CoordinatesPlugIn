package com.vlw0052.coordinates.util;

import org.bukkit.Location;

public class Coordinate {
	
	private String name;
	private Location location; 
	private String player;
	public static String characterSeparator = "=";
	
	public Coordinate(String name, Location l) {
		this.setName(name);
		this.setLocation(l);
		this.player = "";
	}
	public Coordinate(String name, Location l, String player) {
		this.setName(name);
		this.setLocation(l);
		this.player = player.replaceAll(" ", "");
	}
	
	
	
	/*
	 * Converts the stringCoordinate value into a Coordinate object and return that object.
	 * The format for that string is:
	 * locationName ^ X, Y, Z ^ playerName
	 * 
	 */
	public static Coordinate convertIntoCoordinate(String stringCoordinate){
		
		String parts[] = stringCoordinate.split(Coordinate.characterSeparator);
		if(parts.length < 2)
			return null;
		String coordinates[] = parts[1].split(",");
		
		if(isValidStringCoordinate(stringCoordinate)){
			
			String name;
			Double x,y,z;
			x = Double.parseDouble(coordinates[0]);
			y = Double.parseDouble(coordinates[1]);
			z = Double.parseDouble(coordinates[2]);
			
			Location l = new Location(null, x, y, z);
			name = parts[0].replaceAll(" ", "");
			if(parts.length == 3){
				String playerName = parts[2];
				return new Coordinate(name, l, playerName);
			}
			return new Coordinate(name, l);
		}
		System.out.println("InValid string:"+stringCoordinate);
		return null;
	}
	
	/*
	 * Checks if the string passed in is in the correct format.
	 */
	public static boolean isValidStringCoordinate(String stringCoord){
		String parts[] = stringCoord.split(Coordinate.characterSeparator);
		
		if(parts.length != 2 && parts.length != 3){
			return false;
		}
		String coordinates[] = parts[1].split(",");
		if(coordinates.length != 3){
			return false;
		}
		
		try{
			for(String axis: coordinates){
				Double.parseDouble(axis);
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	public String toString(){
		// name - x,y,z
		String coordinate = isPlayerMade() ? 
				String.format("%s "+ characterSeparator +" %.2f, %.2f, %.2f "+ characterSeparator +" %s", 
						this.name, 
						this.location.getX(),
						this.location.getY(),
						this.location.getZ(),
						this.player
						) 	
				: 
					String.format("%s  "+ characterSeparator +" %.2f, %.2f, %.2f", 
							this.name, 
							this.location.getX(),
							this.location.getY(),
							this.location.getZ()
							); 
				return coordinate;
	}
	
	public String toString(String characterSep){
		// name - x,y,z
		String coordinate = isPlayerMade() ? 
					String.format("%s "+ characterSep +" %.2f, %.2f, %.2f "+ characterSep +" %s", 
						this.name, 
						this.location.getX(),
						this.location.getY(),
						this.location.getZ(),
						this.player
						) 	
					: 
					String.format("%s  "+ characterSep +" %.2f, %.2f, %.2f", 
						this.name, 
						this.location.getX(),
						this.location.getY(),
						this.location.getZ()
						); 
		return coordinate;
	}
	
	public Location getLocation() {
		return location;
	}
	
	/*
	 * Returns true if the Coordinate has a player name assigned to it
	 * otherwise false
	 */
	public boolean isPlayerMade(){
		if(this.player == "")
			return false;
		return true;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public String getPlayerName(){
		return this.player;
	}
}
