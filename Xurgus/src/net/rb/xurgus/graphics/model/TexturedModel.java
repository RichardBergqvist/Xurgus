package net.rb.xurgus.graphics.model;

import net.rb.xurgus.graphics.texture.ModelTexture;

/**
 * 
 * @since In-Development 0.1
 * @author Richard Bergqvist
 * @category Graphics
 *
 */
public class TexturedModel {

	/** The model which will be textured **/
	private RawModel model;
	/** The texture the model uses **/
	private ModelTexture texture;
	
	/**
	 *
	 * Creates a new textured model.
	 * @since In-Development 0.1
	 * @param model The model which will be texture
	 * @param texture The texture the model uses
	 * 
	 */
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.model = model;
		this.texture = texture;
	}

	public RawModel getModel() {
		return model;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}