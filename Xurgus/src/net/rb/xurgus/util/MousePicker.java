package net.rb.xurgus.util;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.rb.xurgus.entity.Camera;
import net.rb.xurgus.world.terrain.Terrain;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class MousePicker {

	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;
	
	private Vector3f currentRay = new Vector3f();
	
	private Camera camera;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	
	private Terrain terrain;
	private Vector3f currentTerrainPoint;

	public MousePicker(Camera camera, Matrix4f projectionMatrix, Terrain terrain) {
		this.camera = camera;
		this.viewMatrix = Maths.createViewMatrix(camera);
		this.projectionMatrix = projectionMatrix;
		this.terrain = terrain;
	}
	
	public void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay))
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		else
			currentTerrainPoint = null;
	}
	
	private Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoordinates = getNormalizedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoordinates = new Vector4f(normalizedCoordinates.x, normalizedCoordinates.y, -1F, 1F);
		Vector4f eyeCoordinates = toEyeCoordinates(clipCoordinates);
		Vector3f worldRay = toWorldCoordinates(eyeCoordinates);
		return worldRay;
	}
	
	private Vector3f toWorldCoordinates(Vector4f eyeCoordinates) {
		Matrix4f invertedViewMatrix = Matrix4f.invert(viewMatrix, null);
		Vector4f worldRay = Matrix4f.transform(invertedViewMatrix, eyeCoordinates, null);
		Vector3f mouseRay = new Vector3f(worldRay.x, worldRay.y, worldRay.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vector4f toEyeCoordinates(Vector4f clipCoordinates) {
		Matrix4f invertedProjectionMatrix = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoordinates = Matrix4f.transform(invertedProjectionMatrix, clipCoordinates, null);
		return new Vector4f(eyeCoordinates.x, eyeCoordinates.y, -1F, 0F);
	}
	
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2F);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());
			if (terrain != null)
				return endPoint;
			else
				return null;
		}
		
		if (intersectionInRange(start, half, ray))
			return binarySearch(count + 1, start, half, ray);
		else
			return binarySearch(count + 1, half, finish, ray);
	}
	
	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderground(startPoint) && isUnderground(endPoint))
			return true;
		else
			return false;
	}
	
	private boolean isUnderground(Vector3f testPoint) {
		Terrain terrain = getTerrain(testPoint.getX(), testPoint.getZ());
		float height = 0;
		if (terrain != null)
			height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
		if (testPoint.y < height)
			return true;
		else
			return false;
	}
	
	private Vector2f getNormalizedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2F * mouseX) / Display.getWidth() - 1F;
		float y = (2F * mouseY) / Display.getHeight() - 1F;
		return new Vector2f(x, y);
	}
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f cameraPosition = camera.getPosition();
		Vector3f start = new Vector3f(cameraPosition.x, cameraPosition.y, cameraPosition.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	
	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public Terrain getTerrain(float worldX, float worldZ) {
		/**
		 * IF MULTIPLE TERRAINS
		 * int x = worldX / Terrain.SIZE;
		 * int z = worldZ / Terrain.SIZE;
		 * return terrains[x][z];
		 */
		
		return terrain;
	}
	
	public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}
}