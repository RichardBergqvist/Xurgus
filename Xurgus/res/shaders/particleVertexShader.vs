#version 400 core

in vec2 position;

out vec2 textureCoordinates1;
out vec2 textureCoordinates2;
out float blend;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

uniform vec2 textureOffset1;
uniform vec2 textureOffset2;
uniform vec2 textureCoordinateInfo;

void main() {
	vec2 textureCoordinates = position + vec2(0.5, 0.5);
	textureCoordinates.y = 1.0 - textureCoordinates.y;
	textureCoordinates /= textureCoordinateInfo.x;
	textureCoordinates1 = textureCoordinates + textureOffset1;
	textureCoordinates2 = textureCoordinates + textureOffset2;
	blend = textureCoordinateInfo.y;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}