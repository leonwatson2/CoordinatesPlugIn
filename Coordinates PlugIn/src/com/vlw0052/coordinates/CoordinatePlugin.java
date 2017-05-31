package com.vlw0052.coordinates;
/*	
 * Main file for Java plugin which overrides the onEnable and onDisable
 * functions.
 * 
 *  This plugin allows players to save coordinates for later use.
 *  As well as get the direction of the location from their current location.
 */
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.vlw0052.coordinates.commands.CommandComplete;
import com.vlw0052.coordinates.commands.CoordinatesCommand;
import com.vlw0052.coordinates.util.Coordinate;
import com.vlw0052.coordinates.util.CoordinatesStore;

public class CoordinatePlugin extends JavaPlugin {
	
	protected CoordinatesStore savedCoordinates;
	protected FileConfiguration coordinatesConfig;
	static String CoordinatesFileName = "coordinates.yml"; 
	
	@Override
	public void onEnable(){
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = Logger.getLogger("Cooridinates");
		
		registerConfig();
		registerCommands();
		registerDataFile();
		registerEvents();
		logger.info(pdfFile.getName() + " is working.");
		// TODO: Testing the use of config files.
		@SuppressWarnings("unchecked")
		List<String> t = (List<String>) getCoordinatesFile().get("commands.add");
		FileConfiguration c = getCoordinatesFile();
		for(String command : t){
			logger.info(command);
				
		}
		
		saveCoordinatesFile();
	}
	
	@Override
	public void onDisable(){
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = Logger.getLogger("Coordinates");
		
		logger.info(pdfFile.getName() + " is disabled.");
	}
	
	private void saveCoordinatesFile(){
		try {
			getCoordinatesFile().save(createFile(CoordinatesFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void registerConfig(){
		coordinatesConfig = new YamlConfiguration();
		try{			
			coordinatesConfig.load(createFile(CoordinatesFileName));
		}catch(Exception c){
			System.out.println("Something went wrong when loading the coordinates file!");
		}
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
	}
	
	public FileConfiguration getCoordinatesFile(){
		return this.coordinatesConfig;
	}
	
	public void registerEvents(){
		
	}
	
	public CoordinatesStore getStore(){
		return this.savedCoordinates;
	}
	
	public void registerCommands(){
		
		getCommand("sc").setExecutor( new CoordinatesCommand(this));
		getCommand("sc").setTabCompleter(new CommandComplete(this));
	}
	
	public void registerDataFile(){
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		
		(new File(pluginFolder)).mkdirs();
		
		File coordinatesFile = new File(pluginFolder + File.separator + CoordinatesStore.FileName);
		this.savedCoordinates = new CoordinatesStore(coordinatesFile);
	
		
	}
	
	private File createFile(String fileName) {
	    try {
	        if (!getDataFolder().exists()) {
	            getDataFolder().mkdirs();
	        }
	        
	        File file = new File(getDataFolder(), fileName);
	        if (!file.exists()) {
	            getLogger().info(String.format("%s not found, creating!", fileName));
	            saveResource(fileName, false);
	        } else {
	            getLogger().info(String.format("%s found, loading!", fileName));
	        }
	        
	        return file;
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	public void printLocations(){
		Logger logger = Logger.getLogger("Coordinates");
		for(Coordinate c: this.savedCoordinates.values()){
			logger.info(c.toString());
		}
	}
	
}
