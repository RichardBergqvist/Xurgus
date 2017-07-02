package net.rb.xurgus.graphics.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;
import java.util.Map;

import net.rb.xurgus.graphics.font.FontType;
import net.rb.xurgus.graphics.font.GuiText;
import net.rb.xurgus.graphics.shader.FontShader;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class FontRenderer {

	private FontShader shader;
	
	public FontRenderer(FontShader shader) {
		this.shader = shader;
	}
	
	public void render(Map<FontType, List<GuiText>> texts) {
		bind();
		
		for (FontType font : texts.keySet()) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, font.getTextureAtlas());
			for (GuiText text : texts.get(font)) {
				renderText(text);
			}
		}
		
		unbind();
	}
	
	private void renderText(GuiText text) {
		glBindVertexArray(text.getModel());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		shader.loadColor(text.getColor());
		shader.loadTranslation(text.getPosition());
		glDrawArrays(GL_TRIANGLES, 0, text.getVertexCount());
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	private void bind() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		shader.start();
	}
	
	private void unbind() {
		shader.stop();
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
	}
}