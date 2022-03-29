// @author Pavel Chemarmazovich
// @version 0.1

import org.jsfml.graphics.Sprite;

public class Animation {
	private int currFrame;
	private long delay;
	private long startTime;
	private long elapsed;
	private boolean playedOnce;
	private boolean pause;
	private Sprite[] frames;
	
	Animation(Sprite[] frames, long delay) {
		this.frames = frames;
		this.delay = delay;
		currFrame = 0;
		playedOnce = false;
		pause = false;
		startTime = System.nanoTime();
	}
	
	public void tick() {
		if (pause) return;
		elapsed += (System.nanoTime() - startTime);
		if (elapsed > delay) {
			currFrame++;
			elapsed = 0;
			startTime = System.nanoTime();
		}
		if (currFrame == frames.length) {
			currFrame = 0;
		}
	}
	
	public void replay() {
		currFrame = 0;
	}
	
	public void pause() {
		pause = true;
	}
	
	public void play() {
		pause = false;
	}
	
	public Sprite getFrame() {
		return frames[currFrame];
	}
	
	public Sprite[] getFrames() {
		return frames;
	}
}