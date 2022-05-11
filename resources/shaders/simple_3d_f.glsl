#version 330 core

in vec2 texcoord;

uniform sampler2D texture_1;

void main()
{
    gl_FragColor = texture2D(texture_1, texcoord);
}
