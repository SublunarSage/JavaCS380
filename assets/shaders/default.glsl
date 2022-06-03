#type vertex
#version 330 core
layout (location=0) in vec2 aPos;
layout (location=1) in vec4 aColor;

out vec4 fColor;

void main() {
    fColor = aColor;
    gl_Position = vec4(aPos, 0, 1.0f);
}

#type fragment
#version 330 core

in vec4 fColor;

out vec4 color;
uniform vec4 oColor;

void main() {
    color = vec4(fColor.xy,oColor.x,fColor.w);

}