package com.vlw0052.coordinates.util;
/*
 * CoordinatesStore class holds all the coordinates
 * in the current session of the game.
 * Saves those coordinates and reads those coordinates
 * from the file(FileName).
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.vlw0052.coordinates.commands.CoordinatesCommand;

public class CoordinatesStore {
	
	public static String FileName = "saved_coordinates.cr";
	public static String visibleSeparator = "-";
	private File storageFile;
	private ArrayList<Coordinate> values;
	// TODO:Undo
	
	public CoordinatesStore(File file){
		this.storageFile = file;
		this.values = new ArrayList<Coordinate>();
		
		if(!this.storageFile.exists()){
			try {
				this.storageFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.load();
	}
	/*
	 * Reads from the coordinates file and loads the values into the store.
	 */
	public void load(){
		try {
			DataInputStream input = new DataInputStream(new FileInputStream(this.storageFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			String line;
			Coordinate loadedC;
			while((line = reader.readLine()) != null){
				if(!this.contains(line)){
					loadedC = Coordinate.convertIntoCoordinate(line);
					if(loadedC != null)
					this.values.add(loadedC);
				}
			}
			
			reader.close();
			input.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * Writes to file the coordinates currently in the store.
	 */
	public void save(){
		
		try {
			FileWriter stream = new FileWriter(this.storageFile);
			BufferedWriter out = new BufferedWriter(stream);
			for(Coordinate val: this.values){
				out.write(val.toString());
				out.newLine();
			}
			out.close();
			stream.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Checks for coordinate with the name of the value
	 * passed is in the stored Coordinates if so returns true
	 * otherwise returns false.
	 */
	public boolean contains(String value){
		for(Coordinate c : this.values){
			
			if(c.getName().replaceAll(" ","")
					.equalsIgnoreCase(value.replaceAll(" ", ""))){
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(Coordinate search){
		for(Coordinate c : this.values){
			if(c.getName().equalsIgnoreCase(search.getName())){
				return true;
			}
		}
		return false;
	}
	
	public int indexOf(String value){
		for(int i=0; i < this.values.size(); i++){
			if(this.values.get(i).getName().equalsIgnoreCase(value.replaceAll(" ", ""))){
				return i;
			}
		}
		return -1;
	}
	
	public int indexOf(Coordinate search){
		for(int i=0; i < this.values.size(); i++){
			if(this.values.get(i).getName().equalsIgnoreCase(search.getName())){
				return i;
			}
		}
		return -1;
	}
	
	public String add(String value){
		Coordinate newCoordinates = Coordinate.convertIntoCoordinate(value);
		if(newCoordinates != null){
			if(!this.contains(newCoordinates)){
				this.values.add(newCoordinates);
				return newCoordinates.toString(visibleSeparator) + ": Added";
			}
			return "Location Already Exist";
		}
		return value + " : is invalid.";
	}
	
	public String update(String value){
		Coordinate newCoordinates = Coordinate.convertIntoCoordinate(value);
		if(newCoordinates != null){
			if(this.contains(newCoordinates)){
				this.values.set(this.indexOf(newCoordinates), newCoordinates);
				return newCoordinates.toString(visibleSeparator) + ": Updated.";
			}
			return newCoordinates.getName() + " location does not exist.";
		}
		return value + " : is invalid. try /sc update location_name x y z";
	}
	
	public String remove(String value){
		if(this.contains(value)){
			for(Coordinate c : this.values){
				if(c.getName().equalsIgnoreCase(value)){		
					this.values.remove(c);
					return c.toString(visibleSeparator) + " deleted.";
				}
			}	
		}
		return "Location does not exist.";
	}
	
	public String remove(String[] values){
		String allRemoved = "";
		for(String name : values){
			allRemoved += this.remove(name) + "\n";
		}
		return allRemoved;
	}
	
	public String distanceFrom(String[] args, Player player){
		if(args.length == 0){
			return null;
		}
		String locationName = args[0];
		int index = this.indexOf(locationName);
		if(index > -1){
			return this.getDistanceFromPlayer(this.values.get(index), player);
		}else{
			return String.format(ChatColor.RED+"'%s' does not exist.", locationName);
		}
	}
	
	private String getDistanceFromPlayer(Coordinate coord, Player player){
		Location playerLocation = player.getLocation();
		Location placeLocation = coord.getLocation();
		
		
		double xDif = placeLocation.getX() - playerLocation.getX();
		double yDif = placeLocation.getY() - playerLocation.getY();
		double zDif = placeLocation.getZ() - playerLocation.getZ();
		String xDirection = xDif < 0 ? "West(-X) ": "East(+X)";
		String yDirection = yDif < 0 ? "Down(-Y) ": "Up(+Y)";
		String zDirection = zDif > 0 ? "South(+Z) ": "North(-Z)";
		
		return String.format("The location '%s' is \n"
								+ ChatColor.RED + "%s %.2f"
								+ ChatColor.AQUA + ", "
								+ ChatColor.GREEN + "%s %.2f"
								+ ChatColor.AQUA + ", and "
								+ ChatColor.BLUE + "%s %.2f" 
								+ ChatColor.AQUA + "\nto %s.",
								coord.getName(), 
								xDirection, Math.abs(xDif), 
								yDirection, Math.abs(yDif), 
								zDirection, Math.abs(zDif),
								CoordinatesCommand.locationToString(placeLocation));
	}
	
	public String search(String search){
		int index = this.indexOf(search);
		if(index != -1){
			return this.values.get(index).toString("-");
		}
		
		return search + " location could not be found.";
	}
	
	public String search(String[] searches){
		String all = "";
		for(String search: searches){
			all += this.search(search) + "\n";
		}
		return all;
	}
	
	public ArrayList<Coordinate> searchByPlayer(String playerName){
		ArrayList<Coordinate> allCoordinates = new ArrayList<Coordinate>();
		for(Coordinate c : this.values()){
			if(c.getPlayerName().equalsIgnoreCase(playerName))
			allCoordinates.add(c);
		}
		
		return allCoordinates;
	}
	
	public ArrayList<Coordinate> values(){
		return this.values;
	}
}
