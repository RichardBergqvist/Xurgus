package net.rb.xurgus.graphics.model;

import net.rb.xurgus.graphics.texture.ModelTexture;

/**
 * 
 * @author Richard
 *
 */
public class TexturedModel {

	private RawModel model;
	private ModelTexture texture;
	
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.model = model;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return model;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}