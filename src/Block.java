// @author Pavel Chemarmazovich
// @version 0.1

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;

import java.util.ArrayList;

public class Block extends MapObject {
	public static final int BROWN = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	
	private Sprite img;
	private int type;
	
	Block(TileMap tileMap, ArrayList<Block> blocks, int i, float x, float y) {
		super(tileMap, blocks);
		super.x = x;
		super.y = y;
		super.width = tileMap.getTileSize();
		super.height = tileMap.getTileSize();
		this.blocks = blocks;
		type = i;
		moveSpeed = 0.8f;
		create(i);
	}
	
	public void create(int i) {
		switch (i) {
			case BROWN:
				img = tileMap.getTileImg(1);
			break;
			
			case RED:
				img = tileMap.getTileImg(9);
			break;
			
			case BLUE:
				img = tileMap.getTileImg(17);
			break;
		}
	}
	
	public void checkBlocks() {
		int col = tileMap.getColTile((int) x);
		int row = tileMap.getRowTile((int) y);
		
		for (int i = 0; i < blocks.size(); i++) {
			int bCol = tileMap.getColTile((int) blocks.get(i).getX());
			int bRow = tileMap.getColTile((int) blocks.get(i).getY());
			if (bCol == col && bRow == row + 1) {
				blockedDown = true;
				break;
			}
		}
		for (int i = 0; i < blocks.size(); i++) {
			int bCol = tileMap.getColTile((int) blocks.get(i).getX());
			int bRow = tileMap.getColTile((int) blocks.get(i).getY());
			if (bCol == col + 1 && bRow == row) {
				blockedRight = true;
				break;
			}
		}
		for (int i = 0; i < blocks.size(); i++) {
			int bCol = tileMap.getColTile((int) blocks.get(i).getX());
			int bRow = tileMap.getColTile((int) blocks.get(i).getY());
			if (bCol == col && bRow == row - 1) {
				blockedUp = true;
				break;
			}
		}
		for (int i = 0; i < blocks.size(); i++) {
			int bCol = tileMap.getColTile((int) blocks.get(i).getX());
			int bRow = tileMap.getColTile((int) blocks.get(i).getY());
			if (bCol == col - 1 && bRow == row) {
				blockedLeft = true;
				break;
			}
		}
	}
	
	public void tick() {
		checkTiles();
		checkBlocks();
		
		if (!moving) {
			preCol = tileMap.getColTile((int) x);
			preRow = tileMap.getRowTile((int) y);
		}
		
		if (moving) {
			if (down) dy = moveSpeed;
			if (right) dx= moveSpeed;
			if (up) dy = -moveSpeed;
			if (left) dx = -moveSpeed;
		}
		
		if (down && blockedDown) {
			moving = false;
			dy = 0;
		}
		if (right && blockedRight) {
			moving = false;
			dx = 0;
		}
		if (up && blockedUp) {
			moving = false;
			dy = 0;
		}
		if (left && blockedLeft) {
			moving = false;
			dx = 0;
		}
		
		x += dx;
		
		if (dx > 0) {
			if (x >= (preCol + 1) * tileMap.getTileSize() + tileMap.getTileSize() / 2) {
				x = (preCol + 1) * tileMap.getTileSize() + tileMap.getTileSize() / 2;
				dx = 0;
				moving = false;
			}
		}
		if (dx < 0) {
			if (x <= (preCol - 1) * tileMap.getTileSize() + tileMap.getTileSize() / 2) {
				x = (preCol - 1) * tileMap.getTileSize() + tileMap.getTileSize() / 2;
				dx = 0;
				moving = false;
			}
		}
		
		y += dy;
		
		if (dy > 0) {
			if (y >= (preRow + 1) * tileMap.getTileSize() + tileMap.getTileSize() / 2) {
				y = (preRow + 1) * tileMap.getTileSize() + tileMap.getTileSize() / 2;
				dy = 0;
				moving = false;
			}
		}
		if (dy < 0) {
			if (y <= (preRow - 1) * tileMap.getTileSize() + tileMap.getTileSize() / 2) {
				y = (preRow - 1) * tileMap.getTileSize() + tileMap.getTileSize() / 2;
				dy = 0;
				moving = false;
			}
		}
		
		// RESET VARIABLES
		down = false; right = false; up = false; left = false;
	}
	
	public int getType() {return type;}
	
	public void draw(RenderWindow w) {
		img.setPosition(x - width / 2, y - height / 2);
		w.draw(img);
	}
}