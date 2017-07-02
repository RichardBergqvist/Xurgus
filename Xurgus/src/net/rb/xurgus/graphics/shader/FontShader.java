package net.rb.xurgus.graphics.shader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "fontVertexShader";
    private static final String FRAGMENT_FILE = "fontFragmentShader";
	
    private int location_color;
    private int location_translation;
    
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoordinates");
	}

	@Override
	protected void getAllUniformLocations() {
		location_color = getUniformLocation("color");
		location_translation = getUniformLocation("translation");
	}
	
	public void loadColor(Vector3f color) {
		loadVector(location_color, color);
	}
	
	public void loadTranslation(Vector2f translation) {
		loadVector(location_translation, translation);
	}
}