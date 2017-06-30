#version 400 core

in vec3 textureCoordinates;

out vec4 out_Color;

uniform samplerCube cubeMap;
uniform samplerCube cubeMap2;
uniform float blendFactor;
uniform vec3 fogColor;

const float lowerLimit = 0.0;
const float upperLimit = 50.0;

void main() {
	vec4 texture1 = texture(cubeMap, textureCoordinates);
	vec4 texture2 = texture(cubeMap2, textureCoordinates);
	vec4 finalColor = mix(texture1, texture2, blendFactor);

	float factor = (textureCoordinates.y - lowerLimit) / (upperLimit - lowerLimit);
	factor = clamp(factor, 0.0, 1.0);
	out_Color = mix(vec4(fogColor, 1.0), finalColor, factor);
}