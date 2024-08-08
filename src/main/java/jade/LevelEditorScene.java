package jade;

import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene {
    private String vertexShaderSrc = " #version 330 core\n" +
            "        layout (location=0) in vec3 aPos;\n" +
            "        layout (location=1) in vec4 aColor;\n" +
            "        \n" +
            "        out vec4 fColor;\n" +
            "        \n" +
            "        void main()\n" +
            "        {\n" +
            "        fColor = aColor;\n" +
            "        gl_Position = vec4(aPos, 1.0);\n" +
            "        }\n";

    private String fragmentShaderSrc = " #version 330 core\n" +
            "        in vec4 fColor;\n" +
            "        out vec4 color;\n" +
            "\n" +
            "        void main () {\n" +
            "         color = fColor;\n" +
            "        }\n";

    private int vertexID, fragmentID, shaderProgram;
    private float[] vertexArray = {
            // position              // color
            0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, // Bottom right, red
            -0.5f,  0.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, // Top left, green
            0.5f,  0.5f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f, // Top right, blue
            -0.5f, -0.5f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f, // Bottom left, yellow
    };

    private int[] elementArray = {
            0, 1, 2, // Top right triangle
            0, 3, 1  // Bottom left triangle
    };

    private int vaoID, vboID, eboID;

    public LevelEditorScene() {

       //  System.out.println(testShader);
    }

    @Override
    public void init() {
        // Compile and link shaders
        Shader testShader =  new Shader("asset/shaders/default.glsl");

        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);
        checkCompileErrors(vertexID, "VERTEX");

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);
        checkCompileErrors(fragmentID, "FRAGMENT");

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);
        checkLinkErrors(shaderProgram);

        // Generate and bind VAO, VBO, and EBO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        // Bind shader program
        glUseProgram(shaderProgram);

        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Draw the elements
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glBindVertexArray(0);
        glUseProgram(0);
    }

    private void checkCompileErrors(int shaderID, String type) {
        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR::SHADER_COMPILATION_ERROR of type: " + type);
            System.out.println(glGetShaderInfoLog(shaderID, len));
            assert false : "";
        }
    }

    private void checkLinkErrors(int programID) {
        int success = glGetProgrami(programID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(programID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR::PROGRAM_LINKING_ERROR");
            System.out.println(glGetProgramInfoLog(programID, len));
            assert false : "";
        }
    }
}
