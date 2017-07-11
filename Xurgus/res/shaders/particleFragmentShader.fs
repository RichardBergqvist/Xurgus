#version 400 core

in vec2 textureCoordinatesX;
in vec2 textureCoordinatesY;
in float blend;

out vec4 out_Color;

uniform sampler2D particleTexture;

void main() {
	vec4 colorX = texture(particleTexture, textureCoordinatesX);
	vec4 colorY = texture(particleTexture, textureCoordinatesY);

	out_Color = mix(colorX, colorY, blend);
}