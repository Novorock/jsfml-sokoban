// @author Pavel Chemarmazovich
// @version 0.1

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

public class TmxLoader {
	private Data data;
	private ArrayList<ObjectGroup> groups;
	private TileMap tileMap;
	
	TmxLoader(String s) {
		groups = new ArrayList<>();
		loadFromTmx(s);
	}
	
	public void loadFromTmx(String s) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(s));
			
			NodeList layer = doc.getElementsByTagName("layer");
			NamedNodeMap layerAttr = layer.item(0).getAttributes();
			data = new Data(
					Integer.parseInt(layerAttr.getNamedItem("width").getNodeValue()),
					Integer.parseInt(layerAttr.getNamedItem("height").getNodeValue())
				);
			// PARSING TILES
			NodeList tiles = doc.getElementsByTagName("tile");
			for (int i = 0; i < tiles.getLength(); i++) {
				Node tile =	tiles.item(i);
				NamedNodeMap att = tile.getAttributes();
				data.set(i, Integer.parseInt(att.getNamedItem("gid").getNodeValue()));
			}
			// PARSING OBJECTS
			NodeList objGroups = doc.getElementsByTagName("objectgroup");	
			for (int i = 0; i < objGroups.getLength(); i++) {
				Node el = objGroups.item(i);
				NamedNodeMap a = el.getAttributes();
				ObjectGroup group = new ObjectGroup(a.getNamedItem("name").getNodeValue());
				//System.out.print("New group was found: name - " + a.getNamedItem("name").getNodeValue() + "\n");
				NodeList objs = el.getChildNodes();
				for (int j = 0; j < objs.getLength(); j++) {
					Node obj = objs.item(j);
					if (obj.getNodeType() == Node.ELEMENT_NODE) {
						NamedNodeMap att = obj.getAttributes();
						//System.out.print("New object was found: name - " + att.getNamedItem("name").getNodeValue() + "\n");
						group.add(new Object(
							att.getNamedItem("name").getNodeValue(),
							(int) Double.parseDouble(att.getNamedItem("x").getNodeValue()),
							(int) Double.parseDouble(att.getNamedItem("y").getNodeValue())
						));	
					}
				}
				groups.add(group);
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
		tileMap.create(data.getWidth(), data.getHeight(), data.get());
	}
	
	public void setPlayerLocation(Player p) {
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).name.equals("player_position")) {
				int x = (int) (groups.get(i).get(0).getX() / tileMap.getTileSize()) * tileMap.getTileSize();
				int y = (int) (groups.get(i).get(0).getY() / tileMap.getTileSize()) * tileMap.getTileSize();
				p.setX(x + tileMap.getTileSize() / 2);
				p.setY(y - tileMap.getTileSize() / 2);
			}
		}
	}
	
	public void createBlocks(ArrayList<Block> blocks) {
		for (int i = 0; i < groups.size(); i++) {
			ObjectGroup group = groups.get(i);
			if (group.name.equals("blocks")) {
				for (int j = 0; j < group.size(); j++) {
					Object block = group.get(j);
					int t = 0;
					switch (block.getName().intern()) {
						case "brown":
							t = Block.BROWN;
						break;
						
						case "red":
							t = Block.RED;
						break;
						
						case "blue":
							t = Block.BLUE;
						break;
					}
					int x = (int) (block.getX() / tileMap.getTileSize()) * tileMap.getTileSize();
					int y = (int) (block.getY() / tileMap.getTileSize()) * tileMap.getTileSize();
					blocks.add(new Block(tileMap, blocks, t, x + tileMap.getTileSize() / 2, y + tileMap.getTileSize() / 2));
				}
			}
		}
	}
	
	class Data {
		int width, height;
		int[] tiles;
		
		Data(int width, int height) {
			this.width = width;
			this.height = height;
			tiles = new int[width * height];
		}
		
		void set(int i, int v) {
			tiles[i] = v;
		}
		
		int[] get() {
			return tiles;
		}
		
		int getWidth() {
			return width;
		}
		
		int getHeight() {
			return height;
		}
		
		// THIS METHOD IS FOR DEBUGING
		void printMap() {
			int counter = 0;
			for (int i = 0; i < tiles.length; i++) {
				System.out.print(tiles[i] + " "); 
				counter++;
				if (counter == 12) {
					System.out.print("\n");
					counter = 0;
				}
			}
		}
	}
	
	class Object {
		private String name;
		private int x, y;
		
		Object(String name, int x, int y) {
			this.name = name;
			this.x = x;
			this.y = y;
		}
		
		String getName() {
			return name;
		}
		
		int getX() {
			return x;
		}
		
		int getY() {
			return y;
		}
	}
	
	class ObjectGroup {
		String name;
		private ArrayList<Object> objs;
		
		ObjectGroup(String name) {
			this.name = name;
			objs = new ArrayList<>();
		}
		
		void add(Object o) {
			objs.add(o);
		}
		
		Object get(int i) {
			return objs.get(i);
		}
		
		int size() {
			return objs.size();
		}
	}
}