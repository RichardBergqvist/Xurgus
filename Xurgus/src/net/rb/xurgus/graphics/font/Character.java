package net.rb.xurgus.graphics.font;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class Character {

	private int id;
	private double xTextureCoordinate;
	private double yTextureCoordinate;
	private double maxXTextureCoordinate;
	private double maxYTextureCoordinate;
	private double xOffset;
	private double yOffset;
	private double xSize;
	private double ySize;
	private double xAdvance;
	
	public Character(int id, double xTextureCoordinate, double yTextureCoordinate, double xTextureSize, double yTextureSize, double xOffset, double yOffset, double xSize, double ySize, double xAdvance) {
		this.id = id;
		this.xTextureCoordinate = xTextureCoordinate;
		this.yTextureCoordinate = yTextureCoordinate;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xAdvance = xAdvance;
		this.maxXTextureCoordinate = xTextureSize + xTextureCoordinate;
		this.maxYTextureCoordinate = yTextureSize + yTextureCoordinate;
	}
	
	protected int getId() {
        return id;
    }
 
    protected double getXTextureCoordinate() {
        return xTextureCoordinate;
    }
 
    protected double getYTextureCoordinate() {
        return yTextureCoordinate;
    }
 
    protected double getMaxXTextureCoordinate() {
        return maxXTextureCoordinate;
    }
 
    protected double getMaxYTextureCoordinate() {
        return maxYTextureCoordinate;
    }
 
    protected double getXOffset() {
        return xOffset;
    }
 
    protected double getYOffset() {
        return yOffset;
    }
 
    protected double getXSize() {
        return xSize;
    }
 
    protected double getYSize() {
        return ySize;
    }
 
    protected double getXAdvance() {
        return xAdvance;
    }
}