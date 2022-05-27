package edu.cwu.renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL30.*;

public class Shader {

    private int shaderProgramId, vertexID, fragmentID;

    private String vertexSource;
    private String fragmentSource;
    private final String filePath;

    public Shader(String filePath) {
        this.filePath = filePath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            boolean allTypesFound = false;
            int typesFound = 0;

            int splitIndex;
            int eolIndex = 0;

            while(!allTypesFound) {
                if(typesFound == 0) {
                    splitIndex = source.indexOf("#type") + 6;
                }
                else {
                    splitIndex = source.indexOf("#type",eolIndex) + 6;
                }
                eolIndex = source.indexOf("\n", splitIndex);

                String pattern = source.substring(splitIndex,eolIndex).trim();

                if(pattern.equals("vertex") && vertexSource == null) {
                    typesFound++;
                    vertexSource = splitString[1];
                }
                else if(pattern.equals("fragment") && fragmentSource == null) {
                    typesFound++;
                    fragmentSource = splitString[2];
                }
                else {
                    allTypesFound = true; // Either all types are found or an invalid token is presented.
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filePath + "'";
        }

    }

    public boolean compile() {
        // =========================
        // Compile and link shaders
        // =========================

        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass shader source to GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            return false;
            //assert false : "";
        }


        // Compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass shader source to GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            return false;
        }

        return link();
    }

    public boolean link() {

        // Link shader and check for errors
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexID);
        glAttachShader(shaderProgramId, fragmentID);
        glLinkProgram(shaderProgramId);

        // Check for errors in linking
        int success = glGetProgrami(shaderProgramId,GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramId,len));
            return false;
        }

        return true;
    }

    public void bind() {
        glUseProgram(shaderProgramId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void destroy() {
        unbind();
        if(shaderProgramId != 0) glDeleteProgram(shaderProgramId);
    }
}
