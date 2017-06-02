package com.vlw0052.coordinates.commands;


import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vlw0052.coordinates.CoordinatePlugin;
import com.vlw0052.coordinates.util.Arguments;
import com.vlw0052.coordinates.util.CommandType;
import com.vlw0052.coordinates.util.Coordinate;

/**
 * {@code CoordinatesCommand} is a {@link CommandExecutor}
 * for the {@link CoordinatePlugin} that overrides the onCommand method
 * to do commands of the coordinate plugin.
 * 
 * @author Leon Watson
 * 
 * @since Jun 1, 2017
 */
public class CoordinatesCommand implements CommandExecutor {
	
	private CoordinatePlugin coordinatePlugin;
	private static final double MAXSHOWN = 7;
	
	public CoordinatesCommand(CoordinatePlugin cP) {
		
		this.coordinatePlugin = cP;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
			
			if(args.length == 0 ){
				if(sender instanceof Player){
					
					Player player = (Player) sender;
					Location l = player.getLocation();
					player.sendMessage("You are at : " );
					player.sendMessage(locationToString(l));
					return true;
				}else {
					return false;
				}
			}


			CommandType command = this.coordinatePlugin.customCommands.getCommandType(args[0]);
			
			switch(command){
				case LIST:
					return this.printSavedLocations(args, sender);
				case ADD:
					return this.addLocation(args, sender);
					
				case REMOVE:
					return this.removeLocation(args, sender);
					
				case UPDATE:
					return this.updateLocation(args, sender);
					
				case DISTANCE:
					return this.distanceFromLocation(args, sender);
				case SEARCH:
					return this.searchLocation(args, sender);
				default:
					
					return false;
			}
		
		
	}
	
	private boolean searchLocation(String[] args, CommandSender player){
		
		String response = this.coordinatePlugin.getStore().search(Arrays.copyOfRange(args, 1, args.length));
		sendMessageAndSave(response, player);
		return true;
	}
	
	private boolean updateLocation(String[] args, CommandSender player) {
		if(args.length != 5 && args.length != 3)
			return false;
		// TODO: send error message how to use
		String value;
		if(args.length == 3){
			if((args[2].equalsIgnoreCase("here") || args[2].equalsIgnoreCase("koko")) && player instanceof Player){
				Location pLoc = ((Player) player).getLocation();
				value = String.format("%s  "
										+ Coordinate.characterSeparator 
										+" %.2f, %.2f, %.2f  "
										+ Coordinate.characterSeparator +" %s", 
										args[1], 
										pLoc.getX(), 
										pLoc.getY(), 
										pLoc.getZ(),
										((Player)player).getDisplayName());
			}else{
				player.sendMessage("No you can't do that.");
				return false;
				
			}
		}else{
			value = getCoordinateString(args, player);		
		}
		
		
		String response = this.coordinatePlugin.getStore().update(value);
		sendMessageAndSave(ChatColor.DARK_AQUA + response, player);
		
		
		return true;
	}

	private String getCoordinateString(String[] args, CommandSender player) {
		String value;
		if(player instanceof Player)
			value = String.format("%s  "
								+ Coordinate.characterSeparator 
								+" %s, %s, %s  "
								+ Coordinate.characterSeparator +" %s", 
								args[Arguments.NAME.val()], 
								args[Arguments.X.val()], 
								args[Arguments.Y.val()], 
								args[Arguments.Z.val()],((Player)player).getDisplayName());
		else
			value = String.format("%s "+ Coordinate.characterSeparator +" %s, %s, %s", 
					args[Arguments.NAME.val()], 
					args[Arguments.X.val()], 
					args[Arguments.Y.val()], 
					args[Arguments.Z.val()]);
		return value;
	}

	private boolean addLocation(String[] args, CommandSender player){
		if(args.length != 5 && args.length != 3)
			return false;
		// TODO: send error message how to use
		String value;
		if(args.length == 3){
			if((args[2].equalsIgnoreCase("here") || args[2].equalsIgnoreCase("koko")) && player instanceof Player ){
				Location pLoc = ((Player)player).getLocation();
				value = String.format("%s  "+ Coordinate.characterSeparator 
											+" %.2f, %.2f, %.2f  "
											+ Coordinate.characterSeparator +" %s", 
										args[1], 
										pLoc.getX(), 
										pLoc.getY(), 
										pLoc.getZ(),
										((Player)player).getDisplayName());
			}else{
				player.sendMessage("No you can't do that.");
				return false;
				// TODO: send error message how to use
			}
		}else{
			value = getCoordinateString(args, player);
		}
		
		String response = this.coordinatePlugin.getStore().add(value);
		sendMessageAndSave(ChatColor.DARK_AQUA + response, player);
		
		return true;
		
	}
	
	private boolean removeLocation(String[] args, CommandSender player){

		String response = this.coordinatePlugin.getStore().remove(Arrays.copyOfRange(args, 1, args.length));
		sendMessageAndSave(ChatColor.YELLOW + response, player);
		
		return true;
		
	}
	
	private boolean distanceFromLocation(String[] args, CommandSender player){
		if(!(player instanceof Player)){
			player.sendMessage("You can't you distance as an admin.");
			return false;
		}
		String response = this.coordinatePlugin.getStore()
							.distanceFrom(Arrays.copyOfRange(args, 1, args.length), (Player)player);
		if(response == null)
			return false;
		player.sendMessage(ChatColor.AQUA + response);
		return true;
	}
	
	private void sendMessageAndSave(String message, CommandSender player){
		player.sendMessage(message);
		this.coordinatePlugin.getStore().save();
	}
	
	public boolean printSavedLocations(String[] args, CommandSender p){
		if(args.length > 3)
			return false;
		int sizeOfList = this.coordinatePlugin.getStore().values().size(); 

		ArrayList <Coordinate> allCoordinates = new ArrayList<Coordinate>();
		ArrayList <Coordinate> storeValues = this.coordinatePlugin.getStore().values();
		int pageNumber = 0;
		String playerName = "";
		
		//get values based on page number and/or username
		if(args.length > 1){
			try{
				//when just page number
				pageNumber = Integer.parseInt(args[Arguments.PLAYERNAME.val()]) - 1 ;
				allCoordinates = storeValues;
				
			}catch(Exception e){
				//when searching by player name
				playerName = args[Arguments.PLAYERNAME.val()];
				
				allCoordinates = this.coordinatePlugin.getStore().searchByPlayer(playerName);
				sizeOfList = allCoordinates.size();
				
				//get page number of list for user
				if(args.length == 3){
					pageNumber = Integer.parseInt(args[Arguments.PAGENUMBER.val()]) - 1;
				}
				
			}
			
		}else{
			// get the first part of list
			for (int i = 0; i < MAXSHOWN && i < storeValues.size(); i++) {
				allCoordinates.add(storeValues.get(i));
			}
			
		}
		int totalPages = (int)Math.ceil(sizeOfList/MAXSHOWN);
		
		if(pageNumber > totalPages)
			pageNumber = totalPages - 1;
		
		if(pageNumber < 0)
			pageNumber = 0;
		
		String message = "";
		if(allCoordinates.size() == 0){
			if(sizeOfList == 0)
				message = "[Save Coordinates] No locations saved";
			else 
				message = "[Save Coordinates] No locations with that criteria";
		}else{
		
			message += createHeader(sizeOfList, pageNumber, playerName);
			message += ChatColor.RESET +""+ ChatColor.AQUA;
			for(int i = pageNumber * (int)MAXSHOWN; i < sizeOfList && i < (pageNumber*MAXSHOWN + MAXSHOWN); i++){
				
				message += allCoordinates.get(i).toString("-") + "\n";
			
			}
			message += getFooter();
		}
		
		
		p.sendMessage(ChatColor.AQUA + message);
		
		return true;
	}
	
	private String createHeader(int sizeOfData, int pageNumber, String playerName){
		String message = "";
		int totalPages = (int)Math.ceil(sizeOfData / MAXSHOWN);
		//header
		if(sizeOfData > MAXSHOWN){
			if(pageNumber == 0){
				message += String.format("\n------Saved Locations%s(1, %d)-------\n",
								playerName == ""?" ":" for "+ playerName, 
										totalPages);
				message += ChatColor.GRAY + "" + ChatColor.ITALIC + "Use /sc list "+ playerName +" [n] to get page n of locations.\n";
			}
			else
				message += String.format("\n------Saved Locations%s(%d,%d)-------\n",
										playerName == ""?" ":" for "+ playerName, 
										pageNumber + 1, 
										totalPages);
			
		}else
			message += ChatColor.AQUA + "\n---------Saved Locations---------\n";
		return message;
	}
	
	private String getFooter(){
		return "--------------------------------";
	}
	
	
	public static String locationToString(Location l){
		
		return String.format("XYZ: %.2f / %.2f / %.2f", l.getX(), l.getY(), l.getZ());
	}
}
