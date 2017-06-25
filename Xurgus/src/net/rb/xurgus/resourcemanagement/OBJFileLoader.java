package net.rb.xurgus.resourcemanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.rb.xurgus.model.ModelData;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class OBJFileLoader {

	private static final String RESOURCE_LOCATION = "res/models/";
	
	public static ModelData loadOBJ(String name) {
		FileReader fr = null;
		File file = new File(RESOURCE_LOCATION + name + ".obj");
		
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Could not find obj model file: " + name + ".obj");
			System.exit(-1);
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		try {
			while (true) {
				line = reader.readLine();
				
				if (line.startsWith("v ")) {
					String[] currentLine = line.split(" ");
					Vector3f vertex = new Vector3f((float) Float.valueOf(currentLine[1]), (float) Float.valueOf(currentLine[2]), (float) Float.valueOf(currentLine[3]));
					Vertex newVertex = new Vertex(vertex, vertices.size());
					vertices.add(newVertex);
				}
				else if (line.startsWith("vt ")) {
					String[] currentLine = line.split(" ");
					Vector2f texture = new Vector2f((float) Float.valueOf(currentLine[1]), (float) Float.valueOf(currentLine[2]));
					textures.add(texture);
				}
				else if (line.startsWith("vn ")) {
					String[] currentLine = line.split(" ");
					Vector3f normal = new Vector3f((float) Float.valueOf(currentLine[1]), (float) Float.valueOf(currentLine[2]), (float) Float.valueOf(currentLine[3]));
					normals.add(normal);
				}
				else if (line.startsWith("f "))
					break;
			}
			
			while (line != null && line.startsWith("f ")) {
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				processVertex(vertex1, vertices, indices);
				processVertex(vertex2, vertices, indices);
				processVertex(vertex3, vertices, indices);
				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not read obj model file: " + name + ".obj");
			System.exit(-1);
		}
		
		removeUnusedVertices(vertices);
		float[] vertexArray = new float[vertices.size() * 3];
		float[] textureArray = new float[vertices.size() * 2];
		float[] normalArray = new float[vertices.size() * 3];
		int[] indexArray = convertIndicesListToArray(indices);
		float furthest = convertDataToArrays(vertices, textures, normals, vertexArray, textureArray, normalArray);
	
		ModelData data = new ModelData(vertexArray, textureArray, normalArray, indexArray, furthest);
		return data;
	}
	
	private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
		int index = Integer.parseInt(vertex[0]) - 1;
		Vertex currentVertex = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		int normalIndex = Integer.parseInt(vertex[2]) - 1;
		
		if (!currentVertex.isSet()) {
			currentVertex.setTextureIndex(textureIndex);
			currentVertex.setNormalIndex(normalIndex);
			indices.add(index);
		} else
			dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, vertices, indices);
	}
	
	private static float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures, List<Vector3f> normals, float[] vertexArray, float[] textureArray, float[] normalArray) {
		float furthestPoint = 0;
		
		for (int i = 0; i < vertices.size(); i++) {
			Vertex currentVertex = vertices.get(i);
			
			if (currentVertex.getLength() > furthestPoint)
				furthestPoint = currentVertex.getLength();
			
			Vector3f position = currentVertex.getPosition();
			Vector2f textureCoordinate = textures.get(currentVertex.getTextureIndex());
			Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
			vertexArray[i * 3] = position.x;
			vertexArray[i * 3 + 1] = position.y;
			vertexArray[i * 3 + 2] = position.z;
			textureArray[i * 2] = textureCoordinate.x;
			textureArray[i * 2 + 1] = 1 - textureCoordinate.y;
			normalArray[i * 3] = normalVector.x;
			normalArray[i * 3 + 1] = normalVector.y;
			normalArray[i * 3 + 2] = normalVector.z;
		}
		
		return furthestPoint;
	}
	
	private static int[] convertIndicesListToArray(List<Integer> indices) {
		int[] indexArray = new int[indices.size()];
		
		for (int i = 0; i < indexArray.length; i++)
			indexArray[i] = indices.get(i);
		
		return indexArray;
	}
	
	private static void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex, int newNormalIndex, List<Vertex> vertices, List<Integer> indices) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex))
			indices.add(previousVertex.getIndex());
		else {
			Vertex anotherVertex = previousVertex.getDuplicateVertex();
		
			if (anotherVertex != null)
				dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex, vertices, indices);
			else {
				Vertex duplicateVertex = new Vertex(previousVertex.getPosition(), vertices.size());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
			}
		}
	}
	
	private static void removeUnusedVertices(List<Vertex> vertices) {
		for (Vertex vertex : vertices) {
			if (!vertex.isSet()) {
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}
}