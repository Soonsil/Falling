precision mediump float;

uniform vec3 uLight, uLight2, uColor;
uniform sampler2D uTextureUnit; // texture

varying vec3 vNormal;
varying vec3 vPosition;
varying vec2 vTextureCoord;

void main() {
  vec4 color = texture2D(uTextureUnit, vTextureCoord);
  gl_FragColor = color;
}
