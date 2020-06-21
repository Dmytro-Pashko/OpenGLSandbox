#version 330 core

in vec3 a_position;

uniform mat4 cameraCombinedMatrix;
uniform vec4 gridColor;

varying vec4 v_color;

void main()
{
    gl_Position = cameraCombinedMatrix * vec4(a_position, 1.0);
    v_color = gridColor;
}