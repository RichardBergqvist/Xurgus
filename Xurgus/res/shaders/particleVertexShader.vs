#version 400 core

in vec2 position;

in mat4 modelViewMatrix;
in vec4 textureOffsets;
in float blendFactor;

out vec2 textureCoordinates1;
out vec2 textureCoordinates2;
out float blend;

uniform mat4 projectionMatrix;

uniform float numberOfRows;

void main() {
	vec2 textureCoordinates = position + vec2(0.5, 0.5);
	textureCoordinates.y = 1.0 - textureCoordinates.y;
	textureCoordinates /= numberOfRows;
	textureCoordinates1 = textureCoordinates + textureOffsets.xy;
	textureCoordinates2 = textureCoordinates + textureOffsets.zw;
	blend = blendFactor;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}