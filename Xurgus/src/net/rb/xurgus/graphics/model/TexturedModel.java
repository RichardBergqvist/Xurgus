package net.rb.xurgus.graphics.model;

import net.rb.xurgus.graphics.texture.ModelTexture;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class TexturedModel {

	private Model model;
	private ModelTexture texture;
	
	public TexturedModel(Model model, ModelTexture texture) {
		this.model = model;
		this.texture = texture;
	}
	
	public Model getModel() {
		return model;
	}
	
	public ModelTexture getTexture() {
		return texture;
	}
}