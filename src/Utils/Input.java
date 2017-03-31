package Utils;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;

import java.util.HashMap;

public class Input {
    private static HashMap<Keyboard.Key, Boolean> previousKeys = new HashMap<>();
    private static HashMap<Keyboard.Key, Boolean> currentKeys = new HashMap<>();
    private static Vector2i currentMousePosition;
    private static Vector2i previousMousePosition;
    private static boolean currentMousePressed;
    private static boolean previousMousePressed;

    public static Vector2i getCurrentMousePosition() {
        return currentMousePosition;
    }

    public static void initialize() {
        currentMousePressed = false;
        previousMousePressed = false;
        currentMousePosition = new Vector2i(0, 0);
        previousMousePosition = new Vector2i(0, 0);
        Keyboard.Key[] keys = Keyboard.Key.values();
        for (Keyboard.Key key : keys) {
            previousKeys.put(key, false);
            currentKeys.put(key, false);
        }
    }

    public static void update(RenderWindow window) {
        previousMousePressed = currentMousePressed;
        currentMousePressed = Mouse.isButtonPressed(Mouse.Button.LEFT);
        previousMousePosition = currentMousePosition;
        currentMousePosition = Mouse.getPosition(window);
        Keyboard.Key[] keys = Keyboard.Key.values();
        for (int i = 1; i < keys.length; i++) {
            Keyboard.Key key = keys[i];
            previousKeys.put(key, currentKeys.get(key));
            currentKeys.put(key, Keyboard.isKeyPressed(key));
        }
    }

    public static boolean isKeyPressed(Keyboard.Key key) {
        return currentKeys.get(key);
    }

    public static boolean isKeyPressedOnce(Keyboard.Key key) {
        return currentKeys.get(key) && !previousKeys.get(key);
    }

    public static boolean isMouseInRect(int x, int y, int w, int h) {
        return currentMousePosition.x >= x && currentMousePosition.y >= y &&
                currentMousePosition.x < x + w && currentMousePosition.y < y + h;
    }

    public static boolean isMousePressed() {
        return currentMousePressed;
    }

    public static boolean isMousePressedOnce() {
        return currentMousePressed && !previousMousePressed;
    }

    public static boolean isMouseReleased() {
        return !currentMousePressed && previousMousePressed;
    }
}

