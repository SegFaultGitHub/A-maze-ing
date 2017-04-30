package Main;

import Misc.Maze;
import Textures.Textures;
import Utils.*;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;

public class Main {
    private static RenderWindow window;
    private static RenderWindow windowDebug;
    private static final String NAME = "A-maze-ing";;
    private static int screenWidth, screenHeight;

    private static boolean initialize() {
        Config.initialize();
        screenWidth = (int) Config.SCREEN_WIDTH;
        screenHeight = (int) Config.SCREEN_HEIGHT;
        if (Config.DISPLAY) {
            windowDebug = null;
            if (Config.DEBUG) {
                windowDebug = new RenderWindow();
                windowDebug.create(new VideoMode(512, 256, 32), NAME + " - DEBUG", WindowStyle.RESIZE);
                windowDebug.setFramerateLimit((int)Config.FPS);
            }
            window = new RenderWindow();
            if (Config.FULL_SCREEN) {
                window.create(VideoMode.getDesktopMode(), NAME, WindowStyle.FULLSCREEN);
            } else {
                window.create(new VideoMode(screenWidth, screenHeight, 32), NAME, WindowStyle.RESIZE);
            }
            window.setFramerateLimit((int)Config.FPS);
            Input.initialize();
            Handler.initialize(window.getSize().x, window.getSize().y);
        }
        if (!Textures.initialize()) return false;
        return true;
    }

    private static void update() throws Exception {
        Input.update(window);
        Handler.update();
    }

    private static void draw() {
        Handler.draw(window, windowDebug);
    }

    // Warning: For mac, use java option `-XstartOnFirstThread`
    public static void main(String[] args) throws Exception {
        if (!initialize()) System.exit(1);

        if (!Config.DISPLAY) {
            Maze maze = new Maze(screenWidth / (int) Config.CELL_SIZE, screenHeight / (int)Config.CELL_SIZE);
            for (int i = 0; i < Config.GENERATIONS; i++) {
                maze.generate();
                System.out.print("Generation #" + (i + 1) + ": ");
                while (!maze.continueGeneration());
            }
        } else {
            while (window.isOpen()) {
                update();
                draw();
                window.pollEvent();
            }
        }
    }
}
