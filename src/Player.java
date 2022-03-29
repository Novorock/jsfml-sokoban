// @author Pavel Chemarmazovich
// @version 0.1

import java.nio.file.Paths;

import java.io.IOException;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.IntRect;

import java.util.ArrayList;

public class Player extends MapObject {
	public boolean isThereBlockDown;
	public boolean isThereBlockRight;
	public boolean isThereBlockUp;
	public boolean isThereBlockLeft;
	
	private AnimationController anim;
	private Block pushedBlock;
	
	Player(TileMap tileMap, ArrayList<Block> blocks) {
		super(tileMap, blocks);
		x = 3 * 24 + tileMap.getTileSize() / 2;
		y = 3 * 24 + tileMap.getTileSize() / 2;
		width = 24;
		height = 24;
		moveSpeed = 0.8f;
		dx = 0;
		dy = 0;
		anim = new AnimationController();
	}
	
	public void loadAnimations(String s) {
		Texture t = new Texture();
		try {
			t.loadFromFile(Paths.get(s));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// LOADING ALL TEXTURE REGIONS OF ANIMATIONS
		Sprite[] movDown = new Sprite[3];
		Sprite[] movUp = new Sprite[3];
		Sprite[] movRight = new Sprite[3];
		Sprite[] movLeft = new Sprite[3];
		for (int i = 0; i < 3; i++) {
			movDown[i] = new Sprite(t);
			movUp[i] = new Sprite(t);
			movRight[i] = new Sprite(t);
			movLeft[i] = new Sprite(t);
			movDown[i].setTextureRect(new IntRect(i * 24, 0, 24, 24));
			movRight[i].setTextureRect(new IntRect(i * 24, 24, 24, 24));
			movUp[i].setTextureRect(new IntRect(i * 24, 48, 24, 24));
			movLeft[i].setTextureRect(new IntRect(i * 24, 72, 24, 24));
		}
		anim.put("movingDown", new Animation(movDown, 1_000_000_000));
		anim.put("movingRight", new Animation(movRight, 1_000_000_000));
		anim.put("movingUp", new Animation(movUp, 1_000_000_000));
		anim.put("movingLeft", new Animation(movLeft, 1_000_000_000));
	}
	
	public void checkBlocks() {
		int col = tileMap.getColTile((int) x); 
		int row = tileMap.getRowTile((int) y); 
		
		for (int i = 0; i < blocks.size(); i++) {
			int bCol = tileMap.getColTile((int) blocks.get(i).getX());
			int bRow = tileMap.getRowTile((int) blocks.get(i).getY());
			if (bCol == col && bRow == row + 1) {
				isThereBlockDown = true;
				blockedDown = blocks.get(i).isBlockedDown();
				break;
			}
			isThereBlockDown = false;
		}
		for (int i = 0; i < blocks.size(); i++) {
			int bCol = tileMap.getColTile((int) blocks.get(i).getX());
			int bRow = tileMap.getRowTile((int) blocks.get(i).getY());
			if (bCol == col + 1 && bRow == row) {
				isThereBlockRight = true;
				blockedRight = blocks.get(i).isBlockedRight();
				break;
			} 
			isThereBlockRight = false;
		}
		for (int i = 0; i < blocks.size(); i++) {
			int bCol = tileMap.getColTile((int) blocks.get(i).getX());
			int bRow = tileMap.getRowTile((int) blocks.get(i).getY());
			if (bCol == col && bRow == row - 1) {
				isThereBlockUp = true;
				blockedUp = blocks.get(i).isBlockedUp();
				break;
			} 
			isThereBlockUp = false;
		}
		for (int i = 0; i < blocks.size(); i++) {
			int bCol = tileMap.getColTile((int) blocks.get(i).getX());
			int bRow = tileMap.getRowTile((int) blocks.get(i).getY());
			if (bCol == col - 1 && bRow == row) {
				isThereBlockLeft = true;
				blockedLeft = blocks.get(i).isBlockedLeft();
				break;
			} 
			isThereBlockLeft = false;
		}
	}
	
	Block getBlock(int col, int row) {
		for (int i = 0; i < blocks.size(); i++) {
			int bCol = tileMap.getColTile((int) blocks.get(i).getX());
			int bRow = tileMap.getRowTile((int) blocks.get(i).getY());
			if (col == bCol && bRow == row)
				return blocks.get(i);
		}
		return null;
	}
	
	public void tick() {
		checkTiles();
		checkBlocks();
		if (!moving) {
			preCol = tileMap.getColTile((int) x);
			preRow = tileMap.getRowTile((int) y);
		}
		
		if (moving) {
			if (down)
				dy = moveSpeed;
			if (right)
				dx = moveSpeed;
			if (up)
				dy = -moveSpeed;
			if (left)
				dx = -moveSpeed;
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
		
		if (moving) {
			int col = tileMap.getColTile((int) x);
			int row = tileMap.getRowTile((int) y);
			
			if (isThereBlockDown && down) {
				getBlock(col, row + 1).down();
				getBlock(col, row + 1).moving();
			}
			if (isThereBlockRight && right) {
				getBlock(col + 1, row).right();
				getBlock(col + 1, row).moving();
			}
			if (isThereBlockUp && up) {
				getBlock(col, row - 1).up();
				getBlock(col, row - 1).moving();
			}
			if (isThereBlockLeft && left) {
				getBlock(col - 1, row).left();
				getBlock(col - 1, row).moving();
			}
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
		
		// ANIMATION LOGIC
		if (down)	anim.set("movingDown");
		if (right)	anim.set("movingRight");
		if (up)		anim.set("movingUp");	
		if (left)	anim.set("movingLeft");	
		
		if (moving) {
			anim.play();
		} else {
			anim.replay();
			anim.pause();
		}
		anim.tick();
		
		// RESET VARIABLES
		down = false; right = false; up = false; left = false;
	}
	
	public void draw(RenderWindow w) {
		Sprite s = anim.getFrame();
		s.setPosition(x - width / 2, y - height / 2);
		w.draw(s);
	}
}