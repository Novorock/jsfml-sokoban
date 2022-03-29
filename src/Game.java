// @author Pavel Chemarmazovich
// @version 0.1

import org.jsfml.window.VideoMode;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Color;

import java.util.ArrayList;

public class Game implements Runnable {	
	public static int width = 288;
	public static int height = 288;
	
	private RenderWindow window;
	private TmxLoader tmxLoader;
	private TileMap tileMap;
	private Player p;
	private ArrayList<Block> blocks;
	
	Game() {
		window = new RenderWindow(new VideoMode(width, height), "Sokoban");
		initialize();
	}
	
	public void initialize() {
		tmxLoader = new TmxLoader("assets/map.tmx");
		tileMap = new TileMap("gfx/icons1.png");
		tmxLoader.createTileMap(tileMap);
		blocks = new ArrayList<>();
		tmxLoader.createBlocks(blocks);
		p = new Player(tileMap, blocks);
		p.loadAnimations("gfx/icons2.png");
		tmxLoader.setPlayerLocation(p);
	}
	
	public void run() {
		long curr;
		long dt = 0;
		long last = System.nanoTime();
		long target = 1_000_000_000 / 60;
		
		while (window.isOpen()) {
			for (Event event : window.pollEvents()) {
				if (event.type == Event.Type.CLOSED) 
					window.close();
				// HANDLING KEY EVENTS
				if (event.type == Event.Type.KEY_PRESSED) {
					if (!p.isMoving()) {
						if (event.asKeyEvent().key == Keyboard.Key.RIGHT) {
							p.right();
							p.moving();
						}
						if (event.asKeyEvent().key == Keyboard.Key.LEFT) {
							p.left();
							p.moving();
						}
						if (event.asKeyEvent().key == Keyboard.Key.DOWN) {
							p.down();
							p.moving();
						}
						if (event.asKeyEvent().key == Keyboard.Key.UP) {
							p.up();
							p.moving();
						}
					}
				}
			}	
			curr = System.nanoTime();
			dt = (curr - last) / target;
			while (dt >= 1) {
				tick();
				last = curr;
				dt--;
			}
			// FILL WINDOW WITH BLACK COLOR
			window.clear(Color.BLACK);
			draw();
			// DISPLAY ALL WHAT WAS DRAWN
			window.display();
		}
	}
	
	void tick() {
		p.tick();
		// UPDATING BLOCKS
		for (int i = 0; i < blocks.size(); i++) {
			blocks.get(i).tick();
		}
		
		// CHECK BLOCKS FOR REACHING TARGET
		for (int i = 0; i < blocks.size(); i++) {
			if (!blocks.get(i).isMoving()) {
				int t = blocks.get(i).getType();
				int col = tileMap.getColTile((int) blocks.get(i).getX());
				int row = tileMap.getRowTile((int) blocks.get(i).getY());
				
				if (t == Block.BROWN) {
					if (tileMap.getTile(col, row) == 2) {
						blocks.remove(blocks.get(i));
						tileMap.change(col, row, 3);
					}
				}
				if (t == Block.RED) {
					if (tileMap.getTile(col, row) == 10) {
						blocks.remove(blocks.get(i));
						tileMap.change(col, row, 11);
					}
				}
				if (t == Block.BLUE) {
					if (tileMap.getTile(col, row) == 18) {
						blocks.remove(blocks.get(i));
						tileMap.change(col, row, 19);
					}
				}
			}
		}
	}
	
	void draw() {
		tileMap.draw(window);
		p.draw(window);
		for (int i = 0; i < blocks.size(); i++) {
			blocks.get(i).draw(window);
		}
	}
	
	public static void main(String[] args) {
		new Game().run();
	}
}