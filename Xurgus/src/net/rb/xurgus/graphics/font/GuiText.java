package net.rb.xurgus.graphics.font;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.graphics.rendering.TextRenderer;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class GuiText {

	private String text;
	private float fontSize;
	
	private int modelVAO;
	private int vertexCount;
	private Vector3f color = new Vector3f(0, 0, 0);
	
	private FontType font;

	private Vector2f position;
	private float maxLineLength;
	private int numberOfLines;
	
	private boolean centerText = false;
	
	public GuiText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centerText) {
		this.text = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.maxLineLength = maxLineLength;
		this.centerText = centerText;
		TextRenderer.load(this);
	}
	
	public void remove() {
		TextRenderer.remove(this);
	}
	
	public String getText() {
		return text;
	}
	
	public float getFontSize() {
		return fontSize;
	}
	
	public void setModelInfo(int modelVAO, int vertexCount) {
		this.modelVAO = modelVAO;
		this.vertexCount = vertexCount;
	}
	
	public int getModel() {
		return modelVAO;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public void setColor(float r, float g, float b) {
		color.set(r, g, b);
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public FontType getFont() {
		return font;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public float getMaxLineLength() {
		return maxLineLength;
	}
	
	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}
	
	public int getNumberOfLines() {
		return numberOfLines;
	}
	
	public boolean isCentered() {
		return centerText;
	}
}
