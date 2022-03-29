// @author Pavel Chemarmazovich
// @version 0.1

import java.util.ArrayList;

public abstract class MapObject {
	protected float x;
	protected float y;
	protected float width;
	protected float height;
	protected float moveSpeed;
	protected float dx;
	protected float dy;
	
	protected int preCol, currCol;
	protected int preRow, currRow;
	
	protected boolean moving;
	protected boolean down;
	protected boolean right;
	protected boolean up;
	protected boolean left;
	protected boolean blockedDown;
	protected boolean blockedRight;
	protected boolean blockedUp;
	protected boolean blockedLeft;
	
	protected TileMap tileMap;
	protected ArrayList<Block> blocks;
	
	MapObject(TileMap tileMap, ArrayList<Block> blocks) {
		this.tileMap = tileMap;
		this.blocks = blocks;
		moving = false;
		down = false;
		right = false;
		up = false;
		left = false;
		blockedDown = false;
		blockedRight = false;
		blockedUp = false;
		blockedLeft = false;
	}
	
	public void checkTiles() {
		int currCol = tileMap.getColTile((int) x);
		int currRow = tileMap.getRowTile((int) y);
		blockedDown = tileMap.isBlocked(currCol, currRow + 1);
		blockedRight = tileMap.isBlocked(currCol + 1, currRow);
		blockedUp = tileMap.isBlocked(currCol, currRow - 1);
		blockedLeft = tileMap.isBlocked(currCol - 1, currRow);
	}
	
	public abstract void tick();
	
	public void setX(int x) {this.x = x;}
	
	public void setY(int y) {this.y = y;}
	
	public void down() {down = true;}
	
	public void right() {right = true;}
	
	public void up() {up = true;}
	
	public void left() {left = true;}
	
	public void moving() {moving = true;}
	
	public float getX() {return x;}
	
	public float getY() {return y;}
	
	public boolean isMoving() {return moving;}
	
	public boolean isDown() {return down;}
	
	public boolean isRight() {return right;}
	
	public boolean isUp() {return up;}
	
	public boolean isLeft() {return left;}
	
	public boolean isBlockedDown() {return blockedDown;}
	
	public boolean isBlockedRight() {return blockedRight;}
	
	public boolean isBlockedUp() {return blockedUp;}
	
	public boolean isBlockedLeft() {return blockedLeft;}
}