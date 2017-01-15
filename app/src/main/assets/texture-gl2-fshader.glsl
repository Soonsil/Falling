precision mediump float;

uniform vec3 uLight, uLight2, uColor;
uniform sampler2D uTextureUnit; // texture

varying vec3 vNormal;
varying vec3 vPosition;
varying vec2 vTextureCoord;

void main() {
  vec4 color = texture2D(uTextureUnit, vTextureCoord);
  vec3 transparent = vec3(0.0, 0.0, 1.0);
  if(color.r == transparent.r &&
   color.g == transparent.g &&
   color.b == transparent.b){
   color.a = 0.0;
   }
  gl_FragColor = color;
}
