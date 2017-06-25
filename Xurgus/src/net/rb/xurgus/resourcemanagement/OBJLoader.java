package net.rb.xurgus.resourcemanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.model.Model;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class OBJLoader {

	private static final String RESOURCE_LOCATION = "res/models/";
	
	public static Model loadModel(String name, ResourceLoader loader) {
		FileReader fr = null;
		File modelFile = new File(RESOURCE_LOCATION + name + ".obj");
		
		try {
			fr = new FileReader(modelFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Could not find obj model file: " + name + ".obj");
			System.exit(-1);
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] vertexArray = null;
		float[] textureArray = null;
		float[] normalArray = null;
		int[] indexArray = null;
		
		try {
			while (true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}
				else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}
				else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}
				else if (line.startsWith("f ")) {
					textureArray = new float[vertices.size() * 2];
					normalArray = new float[vertices.size() * 3];
					break;
				}
			}
			
			while (line != null) {
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, textures, normals, indices, textureArray, normalArray);
				processVertex(vertex2, textures, normals, indices, textureArray, normalArray);
				processVertex(vertex3, textures, normals, indices, textureArray, normalArray);
				line = reader.readLine();
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not read obj model file: " + name + ".obj");
			System.exit(-1);
		}
		
		vertexArray = new float[vertices.size() * 3];
		indexArray = new int[indices.size()];
		
		int vertexPointer = 0;
		
		for (Vector3f vertex : vertices) {
			vertexArray[vertexPointer++] = vertex.x;
			vertexArray[vertexPointer++] = vertex.y;
			vertexArray[vertexPointer++] = vertex.z;
		}
		
		for (int i = 0; i < indices.size(); i++)
			indexArray[i] = indices.get(i);
		
		return loader.loadToVAO(vertexArray, textureArray, normalArray, indexArray);
	}
	
	private static void processVertex(String[] vertexData, List<Vector2f> textures, List<Vector3f> normals, List<Integer> indices, float[] textureArray, float[] normalArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		
		Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTexture.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;
		
		Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalArray[currentVertexPointer * 3] = currentNormal.x;
		normalArray[currentVertexPointer * 3 + 1] = currentNormal.y;
		normalArray[currentVertexPointer * 3 + 2] = currentNormal.z;
	}
}