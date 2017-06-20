package net.rb.xurgus.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import net.rb.xurgus.graphics.model.RawModel;

/**
 * 
 * @author Richard
 *
 */
public class RenderManager {

	public void prepare() {
		glClear(GL_COLOR_BUFFER_BIT);
		glClearColor(1, 0, 0, 1);
	}
	
	public void render(RawModel model) {
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(0);
		glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
}