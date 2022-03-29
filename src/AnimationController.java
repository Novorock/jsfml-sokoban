// @author Pavel Chemarmazovich
// @version 0.1

import java.util.HashMap;

import org.jsfml.graphics.Sprite;

public class AnimationController {
	private HashMap<String, Animation> animations;
	private String currAnim;
	
	AnimationController() {
		animations = new HashMap<>();
	}
	
	public void tick() {
		animations.get(currAnim).tick();
	}
	
	public void put(String n, Animation a) {
		animations.put(n, a);
		currAnim = n;
	}
	
	public void set(String n) {
		currAnim = n;
	}
	
	public void play() {
		animations.get(currAnim).play();
	}
	
	public void pause() {
		animations.get(currAnim).pause();
	}
	
	public void replay() {
		animations.get(currAnim).replay();
	}
	
	public Sprite getFrame() {
		return animations.get(currAnim).getFrame();
	}
	
	public Sprite[] getFrames() {
		return animations.get(currAnim).getFrames();
	}
}