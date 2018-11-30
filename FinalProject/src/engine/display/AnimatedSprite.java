package engine.display;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import engine.util.GameClock;

public class AnimatedSprite extends Sprite {
	private boolean playing;
	private String fileName;
	private ArrayList<BufferedImage> frames;
	private ArrayList<Animation> animations;
	private int currentFrame;
	private int startFrame;
	private int endFrame;
	private int stopFrame;
	private boolean isJumping;
	private static final int DEFAULT_ANIMATION_SPEED = 1;
	private int animationSpeed;
	private GameClock gameClock;
	private int facing; // 1=right, -1=left
	private int jumpTimer = 0;

	public AnimatedSprite(String ID, String fileName, Point position) {
		super(ID);
		this.fileName = fileName;
		this.frames = new ArrayList<BufferedImage>();
		this.animations = new ArrayList<Animation>();
		this.currentFrame = 0;
		this.startFrame = 0;
		this.endFrame = 0;
		this.setStopFrame(0);
		this.animationSpeed = 1000;
		this.playing = false;
		this.isJumping = false;
		this.gameClock = new GameClock();
		this.facing = 1;
	}

	// Implement a method to populate the ArrayList frames with the images you
	// will iterate through.
	// Refer to the lab slides for tips to do this.
	public void populate() throws IOException {
		BufferedImage right1 = ImageIO.read(new File("resources/mario_right_1.png"));
		BufferedImage right2 = ImageIO.read(new File("resources/mario_right_2.png"));
		BufferedImage left1 = ImageIO.read(new File("resources/mario_left_1.png"));
		BufferedImage left2 = ImageIO.read(new File("resources/mario_left_2.png"));
		BufferedImage jumpLeft = ImageIO.read(new File("resources/mario_jumping_left.png"));
		BufferedImage jumpRight = ImageIO.read(new File("resources/mario_jumping_right.png"));
		frames.add(right1);
		frames.add(right2);
		frames.add(left1);
		frames.add(left2);
		frames.add(jumpRight);
		frames.add(right1);
		frames.add(jumpLeft);
		frames.add(left1);
	}

	@Override
	public void draw(Graphics g) {
		if (frames.size() != 0)
			super.setImage(this.frames.get(currentFrame));

		super.draw(g);

	}

	public void initGameClock() {
		if (this.gameClock == null)
			this.gameClock = new GameClock();
	}

	public Animation getAnimation(String id) {
		for (int i = 0; i < this.animations.size(); i++)
			if (animations.get(i).getId().equals(id))
				return animations.get(i);
		return null;
	}

	public void animate(Animation animation) {
		this.setStartFrame(animation.getStartFrame());
		this.setEndFrame(animation.getEndFrame());
	}

	public void animate(String id) {
		Animation anim = getAnimation(id);
		this.setStartFrame(anim.getStartFrame());
		this.setEndFrame(anim.getEndFrame());
	}

	public void animate(int start, int end) {
		this.setStartFrame(start);
		this.setEndFrame(end);
	}

	public void stopAnimation(int frame) {
		this.setStopFrame(frame);
	}

	public void stopAnimation() {
		stopAnimation(this.startFrame);
	}

	public int getFacing() {
		return facing;
	}

	public void setFacing(int facing) {
		this.facing = facing;
	}

	public int getJumpTimer() {
		return jumpTimer;
	}

	public void setJumpTimer(int jumpTimer) {
		this.jumpTimer = jumpTimer;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean jumping) {
		isJumping = jumping;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public int getStartFrame() {
		return startFrame;
	}

	public void setStartFrame(int startFrame) {
		this.startFrame = startFrame;
	}

	public int getEndFrame() {
		return endFrame;
	}

	public void setEndFrame(int endFrame) {
		this.endFrame = endFrame;
	}

	public int getAnimationSpeed() {
		return animationSpeed;
	}

	public void setAnimationSpeed(int animationSpeed) {
		this.animationSpeed = animationSpeed;
	}

	public GameClock getGameClock() {
		return gameClock;
	}

	public void setGameClock(GameClock gameClock) {
		this.gameClock = gameClock;
	}

	public int getStopFrame() {
		return stopFrame;
	}

	public void setStopFrame(int stopFrame) {
		this.stopFrame = stopFrame;
	}

	public static int getDefaultAnimationSpeed() {
		return DEFAULT_ANIMATION_SPEED;
	}
}
