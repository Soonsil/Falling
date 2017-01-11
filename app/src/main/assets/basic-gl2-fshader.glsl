precision mediump float;

uniform vec3 uLight, uLight2, uColor;

varying vec3 vNormal;
varying vec3 vPosition;

void main() {
  gl_FragColor = vec4(uColor, 1.0);
}
