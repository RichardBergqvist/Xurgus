#version 400 core

in vec2 pass_textureCoordinates;

out vec4 out_Color;

uniform vec3 color;
uniform sampler2D fontAtlas;

const float width = 0.5;
const float edge = 0;

const float borderWidth = 0;
const float borderEdge = 0.1;

const vec2 offset = vec2(0, 0);

const vec3 outlineColor = vec3(1, 0, 0);

void main() {
	float distance = 1.0 - texture(fontAtlas, pass_textureCoordinates).a;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);

	float distance2 = 1.0 - texture(fontAtlas, pass_textureCoordinates + offset).a;
	float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);

	float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha);

	out_Color = vec4(overallColor, overallAlpha);
}