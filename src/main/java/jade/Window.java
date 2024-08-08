package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private static Window window = null;
    public float r,g,b,a ;
    private boolean fadeToBlack =false  ;
    private static Scene currentScene ;
    public Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        r=1;
        g=1 ;
        b=1 ;
        a=1 ;

    }

    public static void changeScene (int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene() ;
                 currentScene.init() ;
                break;
            case 1 :
                currentScene = new LevelScene() ;
                currentScene.init() ;

                break;
            default:
                assert false : "unknow scene ' " + newScene + "'" ;
                break;
        }


    }
    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        // Set up input callbacks
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's OpenGL context
        GL.createCapabilities();
        Window.changeScene(0);
    }

    private void loop() {
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        float beginTime = Time.getTime();
        float endTime ;
        float  dt = -1.0f ;
        while (!glfwWindowShouldClose(glfwWindow)) {
                // Poll for window events. The key callback will only be
                // invoked during this call.
                glfwPollEvents();

                // Set the clear color
                glClearColor(r, g,b, a);
                glClear(GL_COLOR_BUFFER_BIT ); // clear the framebuffer

                if (dt>=0) {
                currentScene.update(dt);
                }


                glfwSwapBuffers(glfwWindow); // swap the color buffers
             endTime = Time.getTime();
              dt = endTime - beginTime;
             beginTime = endTime;
        }

    }

    public static void main(String[] args) {
        Window.get().run();
    }
}
