#version 330 core
layout (location = 0) in vec3 a_position;
layout (location = 1) in vec3 a_normal;

out vec3 normal;
out vec3 position;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    gl_Position = projection * view *  model * vec4(a_position, 1.0f);
    position = vec3(model * vec4(a_position, 1.0f));
    normal = mat3(transpose(inverse(model))) * a_normal;
}