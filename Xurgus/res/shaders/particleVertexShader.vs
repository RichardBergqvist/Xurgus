#version 400 core

in vec2 position;

out vec2 textureCoordinatesX;
out vec2 textureCoordinatesY;
out float blend;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

uniform vec2 textureOffsetX;
uniform vec2 textureOffsetY;
uniform vec2 textureCoordinateInfo;

void main() {
	vec2 textureCoordinates = position + vec2(0.5, 0.5);
	textureCoordinates.y = 1.0 - textureCoordinates.y;
	textureCoordinates /= textureCoordinateInfo.x;
	textureCoordinatesX = textureCoordinates + textureOffsetX;
	textureCoordinatesY = textureCoordinates + textureOffsetY;
	blend = textureCoordinateInfo.y;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}