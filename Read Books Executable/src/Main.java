import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import Anvil.NBTTagCompound;

import java.awt.*;


public class Main {

	static ArrayList<Integer> bookHashes = new ArrayList<Integer>();
	
	public static void main(String[] args) throws IOException
	{
		readBooksAnvil();
		
		/**
		 * GUI TO-DO
		 * Read saves folder, dropdown menu for each world
		 * books + signs in one app
		 * sign filter option. Lifts, [private], empty etc.
		 * Book author / title filter
		 * progress bar
		 * SCAN ITEM FRAME AND FURNACES
		 * ender chests
		 * shulker chests
		 * item frames
		 * 
		 */
	}
	
	public void displayGUI()
	{
		File  minecraftDir = new File((String)(System.getenv("APPDATA") +  "\\.minecraft\\saves"));
		
		Choice test = new Choice();
		
		for (int i = 0; i < minecraftDir.listFiles().length; i++)
		{
			if (minecraftDir.listFiles()[i].isDirectory())
			{
				File worldFolder = minecraftDir.listFiles()[i];
				Path leveldat = Paths.get(worldFolder.getAbsolutePath() + "\\level.dat");
						
				
				if (Files.exists(leveldat))
					test.add(worldFolder.getName());
			}
		}
		
		Frame frame = new Frame();
		frame.setSize(new Dimension(500,400));
		frame.setLayout(new FlowLayout());
		frame.setTitle("Test");
		frame.setResizable(false);
		frame.setVisible(true);
		
		Button btn = new Button("Output Signs");
		btn.setLocation(1, 10);
		btn.setSize(50,50);
		
		Button btn2 = new Button("Output books");
		btn.setLocation(1, 10);
		btn.setSize(50,50);
		
		frame.add(btn);
		frame.add(btn2);
		frame.add(test);
	}
	
	public static void readBooksAnvil() throws IOException
	{
		File folder = new File("C:\\Users\\Matt\\AppData\\Roaming\\.minecraft\\saves\\nov2016survival\\region");
		File[] listOfFiles = folder.listFiles();
		File output = new File("bookOutput.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		
		for (int f = 0; f < listOfFiles.length; f++)
		{
			for (int x = 0; x < 32; x++)
			{
				for (int z = 0; z < 32; z++)
				{
					DataInputStream dataInputStream  = MCR.RegionFileCache.getChunkInputStream(listOfFiles[f], x, z); 
					//Stops a null pointer exception when there is no chunk in the .mcr
					if (dataInputStream == null) 
						continue;
					Anvil. NBTTagCompound nbttagcompund = new Anvil.NBTTagCompound();
				    nbttagcompund = Anvil.CompressedStreamTools.read(dataInputStream);
				   
				    Anvil.NBTTagCompound nbttagcompund2 = new Anvil.NBTTagCompound();
				    nbttagcompund2 = nbttagcompund.getCompoundTag("Level");
				       
				    Anvil.NBTTagList nbttaglist = nbttagcompund2.getTagList("TileEntities", 10);
	
				    for (int i = 0; i < nbttaglist.tagCount(); i ++)
				    {
				    	Anvil.NBTTagCompound entity = (Anvil.NBTTagCompound) nbttaglist.getCompoundTagAt(i);
				    	{
				    		if (entity.hasKey("id")) //If it is an item
				    		{
				    			Anvil.NBTTagList chestItems = entity.getTagList("Items", 10);
				    			for (int n = 0; n < chestItems.tagCount(); n++)
				    			{
				    				Anvil.NBTTagCompound item = chestItems.getCompoundTagAt(n);
				    				if (item.getString("id").equals("minecraft:written_book") || item.getShort("id") == 387)
				    				{
				    					Anvil.NBTTagCompound tag = item.getCompoundTag("tag");
				    					Anvil.NBTTagList pages = tag.getTagList("pages", 8);
				    					
				    					if (pages.tagCount() == 0)
				    						continue;
				    					if (bookHashes.contains(pages.hashCode()))
				    						continue;
				    					else
				    						bookHashes.add(pages.hashCode());
				    					
				    					String author = tag.getString("author");
				    					String title = tag.getString("title");
				    					/*if (title.equals("Global Market") || title.equals("Grief") || title.equals("Muttsworld Rules") || title.equals("Chat Help") || title.equals("Grief Rules")
				    							|| title.equals("Muttsworld Trade") || title.equals("Diplomat Perks") || title.equals("Muttsworld Chat") || title.equals("Voting Guide") || title.equals("MuttsWorld Rules")
				    							|| title.equals("Pay it Forward"))
				    							continue;*/
				    					writer.newLine();
				    					writer.write("------------------------------------Chunk [" + x + ", " + z + "]\t(" +  entity.getInteger("x") + " " + entity.getInteger("y") + " " + entity.getInteger("z") + ") \t " + listOfFiles[f].getName() + "------------------------------------" );
				    					writer.newLine();
				    					writer.write("\tTitle: " + title);
				    					writer.newLine();
				    					writer.write("\tAuthor: " + author );
				    					writer.newLine();
				    					writer.write("\tType: Written");
				    					writer.newLine();
				    					writer.newLine();
				    					
				    					for (int pc = 0; pc < pages.tagCount(); pc++)
				    					{
				    						JSONObject pageJSON = null;
				    						String pageText = pages.getStringTagAt(pc);
				    						
				    						if (pageText.startsWith("{")) //If valid JSON
				    						{
				    							try { pageJSON = new JSONObject(pageText); }
							    				catch(JSONException e) { pageJSON = null; }
				    						}
				    						
				    						writer.write("Page " + pc + ": ");
				    						
				    						//IF VALID JSON
				    						if (pageJSON != null)
				    						{
				    							if (pageJSON.has("extra"))
							    				{
				    								
								    				for (int h = 0; h < pageJSON.getJSONArray("extra").length(); h++)
								    				{
								    					if (pageJSON.getJSONArray("extra").get(h) instanceof String)
								    						writer.write(pageJSON.getJSONArray("extra").get(0).toString());
								    					else
								    					{
								    						JSONObject temp = (JSONObject) pageJSON.getJSONArray("extra").get(h);
								    						writer.write(temp.get("text").toString());
								    					}				    			
								    				}
							    				}
							    				else if (pageJSON.has("text"))
							    				{
							    					writer.write(pageJSON.getString("text"));
							    				}
				    						}
				    						else
				    						{
					    						writer.write(pages.getStringTagAt(pc));
				    						}
				    						
				    						writer.newLine();
				    					}
				    				}
				    				if (item.getString("id").equals("minecraft:writable_book") || item.getShort("id") == 386)
				    				{
				    					Anvil.NBTTagCompound tag = item.getCompoundTag("tag");
				    					Anvil.NBTTagList pages = tag.getTagList("pages", 8);
				    					if (pages.tagCount() == 0)
				    						continue;
				    					writer.newLine();
				    					writer.write("----------------------------------Chunk [" + x + ", " + z + "]\t(" +  entity.getInteger("x") + " " + entity.getInteger("y") + " " + entity.getInteger("z") + ") \t " + listOfFiles[f].getName() + "----------------------------------" + "");
				    					writer.newLine();
				    						writer.write("\tType: Writable");
				    						writer.newLine();
				    						writer.newLine();
				    						for (int pc = 0; pc < pages.tagCount(); pc++)
					    					{
				    							writer.write("Page " + pc + ": " + pages.getStringTagAt(pc));
				    							writer.newLine();
					    					}
				    				}
				    			}
				    		}
				    	}
				    }
				}
			}	
		}
		writer.close();
	}
	
}
