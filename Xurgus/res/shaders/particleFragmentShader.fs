#version 400 core

in vec2 textureCoordinates1;
in vec2 textureCoordinates2;
in float blend;

out vec4 out_Color;

uniform sampler2D particleTexture;

void main() {
	vec4 color1 = texture(particleTexture, textureCoordinates1);
	vec4 color2 = texture(particleTexture, textureCoordinates2);

	out_Color = mix(color1, color2, blend);
}