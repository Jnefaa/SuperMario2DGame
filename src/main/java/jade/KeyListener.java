package jade;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;
    private boolean[] keyPressed = new boolean[GLFW_KEY_LAST + 1];

    private KeyListener() {
    }

    private static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key < 0 || key > GLFW_KEY_LAST) {
            return;
        }

        System.out.println("Key callback: key=" + key + ", action=" + action);

        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
            System.out.println("Key pressed: " + key);
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
            System.out.println("Key released: " + key);
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode <= GLFW_KEY_LAST && keyCode >= 0) {
            return get().keyPressed[keyCode];
        }
        return false;
    }
}
