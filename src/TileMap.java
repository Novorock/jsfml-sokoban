// @author Pavel Chemarmazovich
// @version 0.1

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;

import java.nio.file.Paths;
import java.io.IOException;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.IntRect;

public class TileMap {
	private int tileSize;
	private int width;
	private int height;
	private int[] map;
	private Tile[] tiles;
	
	public TileMap(String s) {
		tileSize = 24;
		loadTiles(s);
		tiles[0] = new Tile(null, false);
	}
	
	public void loadTiles(String s) {
		Texture t = new Texture();
		try {
			t.loadFromFile(Paths.get(s));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int col = t.getSize().x / tileSize;
		int row = t.getSize().y / tileSize;
		tiles = new Tile[col * row];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col - 1; j++) {
				Sprite img = new Sprite(t);
				img.setTextureRect(new IntRect(j * tileSize, i * tileSize, tileSize, tileSize));
				// CASING BLOCKS WHICH ARE BLOCKED
				boolean blocked = false;
				switch (j + i * col + 1) {
					case 4:
						blocked = true;
					break;
				}
				tiles[j + i * col + 1] = new Tile(img, blocked);
			}
		}
	}
	
	public void draw(RenderWindow w) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Sprite s = tiles[map[j + i * width]].getImg();
				if (s == null)
					continue;
				s.setPosition(j * tileSize, i * tileSize);
				w.draw(s);
			}
		}
	}
	
	public void create(int width, int height, int[] map) {
		this.map = map;
		this.width = width;
		this.height = height;
	}
	
	public boolean isBlocked(int x, int y) {
		return tiles[map[x + y * width]].isBlocked();
	}
	
	public int getColTile(int x) {
		return x / tileSize;
	}
	
	public int getRowTile(int y) {
		return y / tileSize;
	}
	
	public int getTileSize() {
		return tileSize;
	}
	
	public int getTile(int x, int y) {
		return map[x + y * width];
	}
	
	public Sprite getTileImg(int v) {
		return tiles[v].getImg();
	}
	
	public void change(int x, int y, int v) {
		map[x + y * width] = v;
	}
}