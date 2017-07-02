package net.rb.xurgus.graphics.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rb.xurgus.graphics.font.FontType;
import net.rb.xurgus.graphics.font.GuiText;
import net.rb.xurgus.graphics.font.TextModelData;
import net.rb.xurgus.graphics.shader.FontShader;
import net.rb.xurgus.resourcemanagement.ResourceLoader;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class TextRenderer {

	private static ResourceLoader loader;
	private static Map<FontType, List<GuiText>> texts = new HashMap<FontType, List<GuiText>>();
	private static FontShader shader = new FontShader();
	private static FontRenderer renderer;
	
	public static void init (ResourceLoader theLoader) {
		loader = theLoader;
		renderer = new FontRenderer(shader);
	}
	
	public static void render() {
		renderer.render(texts);
	}
	
	public static void load(GuiText text) {
		FontType font = text.getFont();
		TextModelData data = font.loadText(text);
		int vaoID = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoordinates());
		text.setModelInfo(vaoID, data.getVertexCount());
		
		List<GuiText> batch = texts.get(font);
		if (batch == null) {
			batch = new ArrayList<GuiText>();
			texts.put(font, batch);
		}
		
		batch.add(text);
	}
	
	public static void remove(GuiText text) {
		List<GuiText> batch = texts.get(text.getFont());
		batch.remove(text);
		if (batch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}
	
	public static void clean() {
		shader.clean();
	}
}