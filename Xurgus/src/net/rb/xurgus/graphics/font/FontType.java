package net.rb.xurgus.graphics.font;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class FontType {

	private int textureAtlas;
	private TextModelCreator loader;
	
	public FontType(int textureAtlas, String font) {
		this.textureAtlas = textureAtlas;
		this.loader = new TextModelCreator(font);
	}
	
	public TextModelData loadText(GuiText text) {
		return loader.createTextModel(text);
	} 
	
	public int getTextureAtlas() {
		return textureAtlas;
	}
}