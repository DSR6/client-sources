#version 120
               
            uniform sampler2D textureIn, textureToCheck;
            uniform vec2 texelSize, direction;
            uniform vec3 color1, color2, color3, color4, color5;
            uniform bool avoidTexture;
            uniform float exposure, radius, time;
            uniform float weights[64];
               
            #define offset direction * texelSize
               
            void main() {
                if (direction.y == 1 && avoidTexture) {
                    if (texture2D(textureToCheck, gl_TexCoord[0].st).a != 0.0) discard;
                }
               
                float innerAlpha = texture2D(textureIn, gl_TexCoord[0].st).a * weights[0];
               
                for (float r = 1.0; r <= radius; r ++) {
                    innerAlpha += texture2D(textureIn, gl_TexCoord[0].st + offset * r).a * weights[int(r)];
                    innerAlpha += texture2D(textureIn, gl_TexCoord[0].st - offset * r).a * weights[int(r)];
                }
               
                float r = length(gl_TexCoord[0].st - vec2(0.5));
                float theta = atan((gl_TexCoord[0].st.y - 0.5) / (gl_TexCoord[0].st.x - 0.5)) + step(gl_TexCoord[0].st.x, 0.5) * 3.1415926535897932384626433832795;
               
                vec3 color1 = color1;
                vec3 color2 = color2;
                vec3 color3 = color3;
                vec3 color4 = color4;
                vec3 color5 = color5;
               
                vec3 color;
                float t = mod(theta + time, 2.0 * 3.1415926535897932384626433832795) / (2.0 * 3.1415926535897932384626433832795);
                if (t < 0.2) {
                    color = mix(color1, color2, smoothstep(0.0, 0.2, t));
                } else if (t < 0.4) {
                    color = mix(color2, color3, smoothstep(0.2, 0.4, t));
                } else if (t < 0.6) {
                    color = mix(color3, color4, smoothstep(0.4, 0.6, t));
                } else if (t < 0.8) {
                    color = mix(color4, color5, smoothstep(0.6, 0.8, t));
                } else {
                    color = mix(color5, color1, smoothstep(0.8, 1.0, t));
                }
               
                gl_FragColor = vec4(color, mix(innerAlpha, 1.0 - exp(-innerAlpha * exposure), step(0.0, direction.y)));
            }