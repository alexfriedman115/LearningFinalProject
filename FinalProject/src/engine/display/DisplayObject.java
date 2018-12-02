package engine.display;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.geom.Area;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;


import javax.imageio.ImageIO;

/**
 * A very basic display object for a java based gaming engine
 *
 * */
public class DisplayObject {

	/* All DisplayObject have a unique id */
	private String id;
	private DisplayObject parent;
	private Point position;
	private Point pivotPoint;
	private int rotation;
	private boolean visible;
	private float alpha;
	private float oldAlpha;
	private double scaleX;
	private double scaleY;
	private Shape hitBox;
	private boolean hasPhysics;

	public boolean isVisible() {
		return visible;
	}

	public float getAlpha() {
		return alpha;
	}

	public float getOldAlpha() {
		return oldAlpha;
	}

	public double getScaleX() {
		return scaleX;
	}

	public double getScaleY() {
		return scaleY;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public void setOldAlpha(float oldAlpha) {
		this.oldAlpha = oldAlpha;
	}

	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
		updateHitbox();
	}

	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
		updateHitbox();
	}


	/* The image that is displayed by this object */
	private BufferedImage displayImage;

	/**
	 * Constructors: can pass in the id OR the id and image's file path and
	 * position OR the id and a buffered image and position
	 */
	public DisplayObject(String id) {
		this.setId(id);
		this.setPivotPoint(new Point(0,0));
		this.setPosition(new Point(0,0));
		this.setRotation(0);
		this.setVisible(true);
		this.setAlpha(1.0f);
		this.setOldAlpha(0.0f);
		this.setScaleX(1.0);
		this.setScaleY(1.0);
		this.parent = null;
		if (displayImage != null)
			this.setHitBox(new Rectangle(this.position.x, this.position.y, displayImage.getWidth(), displayImage.getHeight()));
		else
			this.setHitBox(new Rectangle(this.position.x, this.position.y, 0, 0));
	}

	public DisplayObject(String id, String fileName) {
		this.setId(id);
		this.setImage(fileName);
		this.setPivotPoint(new Point(0,0));
		this.setPosition(new Point(0,0));
		this.setRotation(0);
		this.setVisible(true);
		this.setAlpha(1.0f);
		this.setOldAlpha(0.0f);
		this.setScaleX(1.0);
		this.setScaleY(1.0);
		this.parent = null;
		this.setHitBox(new Rectangle(this.position.x, this.position.y, displayImage.getWidth(), displayImage.getHeight()));
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}


	/**
	 * Returns the unscaled width and height of this display object
	 * */
	public int getUnscaledWidth() {
		if(displayImage == null) return 0;
		return displayImage.getWidth();
	}

	public int getUnscaledHeight() {
		if(displayImage == null) return 0;
		return displayImage.getHeight();
	}

	public BufferedImage getDisplayImage() {
		return this.displayImage;
	}

	protected void setImage(String imageName) {
		if (imageName == null) {
			return;
		}

		displayImage = readImage(imageName);
		if (displayImage == null) {
			System.err.println("[DisplayObject.setImage] ERROR: " + imageName + " does not exist!");
		}
	}


	/**
	 * Helper function that simply reads an image from the given image name
	 * (looks in resources\\) and returns the bufferedimage for that filename
	 * */
	public BufferedImage readImage(String imageName) {
		BufferedImage image = null;
		try {
			String file = ("resources" + File.separator + imageName);
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:readImage] Could not read image " + imageName);
			e.printStackTrace();
		}
		return image;
	}

	public void setImage(BufferedImage image) {
		if(image == null) return;
		displayImage = image;
	}

	public void updateHitbox(){
		if (this.displayImage != null){
			this.hitBox = new Rectangle(position.x, position.y, (int)(displayImage.getWidth()*getScaleX()), (int)(displayImage.getHeight()*getScaleY()));
			AffineTransform tx = new AffineTransform();
			tx.rotate(Math.toRadians(this.getRotation()), this.position.x+this.pivotPoint.x, this.position.y+this.pivotPoint.y);
			Shape newShape = tx.createTransformedShape(this.hitBox);
			this.hitBox = newShape;
		}
	}


	/**
	 * Invoked on every frame before drawing. Used to update this display
	 * objects state before the draw occurs. Should be overridden if necessary
	 * to update objects appropriately.
	 * */
	protected void update(ArrayList<Integer> pressedKeys, ArrayList<Integer> pressedMouse) {

	}

	/**
	 * Draws this image. This should be overloaded if a display object should
	 * draw to the screen differently. This method is automatically invoked on
	 * every frame.
	 * */
	public void draw(Graphics g) {

		if (displayImage != null) {

			/*
			 * Get the graphics and apply this objects transformations
			 * (rotation, etc.)
			 */
			Graphics2D g2d = (Graphics2D) g;
			applyTransformations(g2d);

			/* Actually draw the image, perform the pivot point translation here */
			g2d.drawImage(displayImage, 0, 0,
					(int) (getUnscaledWidth()),
					(int) (getUnscaledHeight()), null);

			/*
			 * undo the transformations so this doesn't affect other display
			 * objects
			 */
			reverseTransformations(g2d);
		}
	}

	/**
	 * Applies transformations for this display object to the given graphics
	 * object
	 * */
	protected void applyTransformations(Graphics2D g2d) {
		g2d.translate(this.position.x, this.position.y);
		g2d.rotate(Math.toRadians(this.getRotation()), this.pivotPoint.x, this.pivotPoint.y);
		g2d.scale(this.scaleX, this.scaleY);
		float curAlpha;
		this.oldAlpha = curAlpha = ((AlphaComposite)g2d.getComposite()).getAlpha();
		g2d.setComposite(AlphaComposite.getInstance(3, curAlpha * this.alpha));
	}

	/**
	 * Reverses transformations for this display object to the given graphics
	 * object
	 * */
	protected void reverseTransformations(Graphics2D g2d) {
		//g2d.rotate(Math.toRadians(this.getRotation()) * -1, this.pivotPoint.x, this.pivotPoint.y);
		g2d.setComposite(AlphaComposite.getInstance(3, this.oldAlpha));
		g2d.scale(1 / this.scaleX, 1 / this.scaleY);
		g2d.rotate(Math.toRadians(this.getRotation()) * -1, this.pivotPoint.x, this.pivotPoint.y);
		g2d.translate(this.position.x * -1, this.position.y * -1);

	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
		updateHitbox();
	}

	public Point getPivotPoint() {
		return pivotPoint;
	}

	public void setPivotPoint(Point pivotPoint) {
		this.pivotPoint = pivotPoint;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
		updateHitbox();
	}


	public DisplayObject getParent() {
		return parent;
	}

	public void setParent(DisplayObject parent) {
		this.parent = parent;
	}
	
	public Point localToGlobal(Point p)
	{
		Point containerLoc = new Point(this.getPosition().x + this.getParent().getPosition().x,
				this.getPosition().y + this.getParent().getPosition().y);
		return containerLoc;
	}
	
	public Point globalToLocal(Point p)
	{
		Point containerLoc = new Point(this.getPosition().x - this.getParent().getPosition().x,
				this.getPosition().y - this.getParent().getPosition().y);
		return containerLoc;
	}

	public Shape getHitBox() {
		return hitBox;
	}

	public void setHitBox(Shape hitBox) {
		this.hitBox = hitBox;
	}
	
	public boolean collidesWith(DisplayObject other)
	{
	    
		Area areaA = new Area(this.getHitBox());
		areaA.intersect(new Area(other.getHitBox()));
		return !areaA.isEmpty();
	}

	public boolean hasPhysics() {
		return hasPhysics;
	}

	public void setHasPhysics(boolean hasPhysics) {
		this.hasPhysics = hasPhysics;
	}

}
