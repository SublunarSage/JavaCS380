#type vertex
#version 330 core

layout (location = 0) in vec2 pos;
out vec4 fColor;

uniform int uSampleRate;

uniform float uOffsetX = 0;
uniform float uScaleX = 1;

void main() {
    gl_Position = vec4((pos.x + uOffsetX) * uScaleX, pos.y, 1, 1);
    fColor = vec4(pos.xy/2 + .5, 1, 1);
}

#type fragment
#version 330 core

in vec4 fColor;
out vec4 color;

void main() {
   color = fColor;
}