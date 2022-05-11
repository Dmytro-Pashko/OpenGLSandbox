#version 330 core
layout (location = 0) in vec3 a_position;
layout (location = 1) in vec2 a_texCoord0;

out vec2 texcoord;

uniform mat4 combined;

void main()
{
    gl_Position = combined * vec4(a_position, 1.0f);
    texcoord = a_texCoord0;
}