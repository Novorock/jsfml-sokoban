// @author Pavel Chemarmazovich
// @version 0.1

import org.jsfml.graphics.Sprite;

public class Tile {
	private Sprite img;
	private boolean blocked;
	
	public Tile(Sprite img, boolean blocked) {
		this.img = img;
		this.blocked = blocked;
	}
	
	public boolean isBlocked() {
		return blocked;
	}
	
	public Sprite getImg() {
		return img;
	}
}