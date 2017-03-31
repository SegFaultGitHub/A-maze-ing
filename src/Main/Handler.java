package Main;

import Misc.Cell;
import Misc.Maze;
import Utils.Input;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

import java.util.ArrayList;
import java.util.Date;

class Handler {
    private enum GameState {
        inGame
    }

    private static GameState gameState;
    private static Vector2f screenSize;
    private static ArrayList<Long> frames;
    private static ArrayList<Integer> previousFPS;

    private static Maze maze;

    private static int currentFPS;

    static void initialize(int screenWidth, int screenHeight) {
        gameState = GameState.inGame;
        screenSize = new Vector2f(screenWidth, screenHeight);
        frames = new ArrayList<>();
        previousFPS = new ArrayList<>();

        maze = new Maze(screenWidth / (int)Config.CELL_SIZE, screenHeight / (int)Config.CELL_SIZE);
    }

    static void update() throws Exception {
        if (Input.isKeyPressed(Keyboard.Key.ESCAPE)) System.exit(0);

        switch (gameState) {
            case inGame:
                updateInGame();
                break;
            default:
                break;
        }

        //<editor-fold desc="Debug">
        if (Config.DEBUG) {
            long now = new Date().getTime();
            frames.add(now);
            while (now - frames.get(0) > 1000) {
                frames.remove(0);
            }
            currentFPS = frames.size();
            previousFPS.add(currentFPS);
            if (previousFPS.size() > 512) {
                previousFPS.remove(0);
            }
        }
        //</editor-fold>
    }

    private static void updateInGame() throws Exception {
        maze.continueGeneration();
    }

    static void draw(RenderWindow window, RenderWindow windowDebug) {
        window.clear(Color.BLACK);
        switch (gameState) {
            case inGame:
                drawInGame(window);
                break;
            default:
                break;
        }

        window.display();

        //<editor-fold desc="Debug">
        if (Config.DEBUG && windowDebug != null) {
            windowDebug.clear(Color.WHITE);
            windowDebug.display();
        }
        //</editor-fold>
    }

    private static void drawInGame(RenderTarget target) {
        maze.draw(target);
    }
}

