#version 400 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;
in vec3 tangent;

out vec2 pass_textureCoordinates;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform vec3 lightPosition[4];
uniform float numberOfRows;
uniform vec2 offset;
uniform vec4 plane;

const float density = 0;
const float gradient = 5.0;

void main() {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

	gl_ClipDistance[0] = dot(worldPosition, plane);

	mat4 modelViewMatrix = viewMatrix * transformationMatrix;
	vec4 positionRelativeToCamera = modelViewMatrix * vec4(position, 1.0);

	gl_Position = projectionMatrix * positionRelativeToCamera;

	pass_textureCoordinates = (textureCoordinates / numberOfRows) + offset;

	vec3 surfaceNormal = (modelViewMatrix * vec4(normal, 0.0)).xyz;

	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize((modelViewMatrix * vec4(tangent, 0.0)).xyz);
	vec3 bitang = normalize(cross(norm, tang));
  
	mat3 toTangentSpace = mat3(
	tang.x, bitang.x, norm.x,
	tang.y, bitang.y, norm.y,
	tang.z, bitang.z, norm.z);

	for (int i = 0; i < 4; i++) {
		toLightVector[i] = toTangentSpace * (lightPosition[i] - positionRelativeToCamera.xyz);
	}

	toCameraVector = toTangentSpace * (-positionRelativeToCamera.xyz);

	float distance = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}