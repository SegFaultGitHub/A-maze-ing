package Misc;

import Main.Config;
import Textures.Textures;
import Utils.Utils;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Created by SegFault on 29/03/2017.
 */
public class Maze {
    private int seed;

    private int width, height;
    private Stack<Vector2i> path;

    private Cell[][] maze;
    private ArrayList<Color> colors;

    private Vector2i current;
    private Sprite sprite;
    private Vector2f vect;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;

        maze = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                maze[i][j] = new Cell();
            }
        }

        colors = new ArrayList<>();
        path = new Stack<>();
        vect = new Vector2f(0, 0);
        sprite = new Sprite();
        sprite.setScale(new Vector2f(Config.CELL_SIZE / 20f, Config.CELL_SIZE / 20f));
        sprite.setColor(Color.WHITE);

        generate();
    }

    public boolean continueGeneration() throws Exception {
        if (!path.isEmpty()) {
            current = path.peek();
            Vector2i next = pickCell(current);
            float ratio = (float) path.size() / (float) (width * height) * 10;
            ratio *= colors.size() - 1;
            Color end = colors.get(((int) ratio + 1) % colors.size());
            Color start = colors.get(((int) ratio) % colors.size());
            ratio = (float) Utils.getDecimalPart(ratio);
            int r = (int) ((float) (end.r - start.r) * ratio) + start.r;
            int g = (int) ((float) (end.g - start.g) * ratio) + start.g;
            int b = (int) ((float) (end.b - start.b) * ratio) + start.b;
            maze[current.x][current.y].setColor(r, g, b);
            if (next == null) {
                path.pop();
                return continueGeneration();
            } else {
                maze[next.x][next.y].setVisited(true);
                path.push(next);
                if (next.x == current.x - 1) {
                    maze[current.x][current.y].setLeft();
                    maze[next.x][next.y].setRight();
                } else if (next.x == current.x + 1) {
                    maze[current.x][current.y].setRight();
                    maze[next.x][next.y].setLeft();
                } else if (next.y == current.y - 1) {
                    maze[current.x][current.y].setUp();
                    maze[next.x][next.y].setDown();
                } else if (next.y == current.y + 1) {
                    maze[current.x][current.y].setDown();
                    maze[next.x][next.y].setUp();
                }
            }
        } else {
            if (Config.SAVE) {
                RenderTexture texture = new RenderTexture();
                texture.create(width * (int) Config.CELL_SIZE, height * (int) Config.CELL_SIZE);
                texture.clear(Color.BLACK);
                draw(texture);
                texture.display();
                texture.getTexture().copyToImage().saveToFile(Paths.get("mazes/seed-" + seed + ".png"));
            }
            if (Config.SOLVE) {
                solve();
            }
            System.out.println(Utils.endChronoStr());
            if (Config.DISPLAY) {
                generate();
            }
            return true;
        }
        return false;
    }

    public void generate() {
        Utils.startChrono();
        seed = Math.abs(new Random().nextInt());
        Utils.setRandomSeed(seed);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                maze[i][j].reset();
            }
        }
        maze[0][0].setUp();
        maze[width - 1][height - 1].setDown();
        path.clear();
        int i, j;
        do {
            i = Utils.getRandomInt(width);
            j = Utils.getRandomInt(height);
        } while (maze[0][0].isVisited());
        path.push(new Vector2i(i, j));
        maze[i][j].setVisited(true);
        int colorNumber = Utils.getRandomInt(2, 5);
        colors.clear();
        for (int k = 0; k < colorNumber; k++) {
            colors.add(new Color(Utils.getRandomInt(256), Utils.getRandomInt(256), Utils.getRandomInt(256)));
        }
    }

    private Vector2i pickCell(Vector2i position) {
        int i = position.x;
        int j = position.y;
        ArrayList<Vector2i> choices = new ArrayList<>();
        Vector2i toAdd[] = {
                new Vector2i(i - 1, j),
                new Vector2i(i + 1, j),
                new Vector2i(i, j - 1),
                new Vector2i(i, j + 1),
        };
        for (Vector2i v : toAdd) {
            if (v.x >= 0 && v.x < width && v.y >= 0 && v.y < height) {
                if (!maze[v.x][v.y].isVisited()) choices.add(v);
            }
        }
        if (choices.isEmpty()) return null;
        else return choices.get(Utils.getRandomInt(choices.size()));
    }

    private Vector2i chooseWay(Vector2i position) {
        int i = position.x;
        int j = position.y;
        Vector2i toAdd[] = {
                new Vector2i(i - 1, j),
                new Vector2i(i + 1, j),
                new Vector2i(i, j - 1),
                new Vector2i(i, j + 1),
        };
        for (Vector2i v : toAdd) {
            if (v.x >= 0 && v.x < width && v.y >= 0 && v.y < height) {
                if (maze[v.x][v.y].isVisited()) {
                    if (v.x == i - 1 && !maze[i][j].isLeft()) continue;
                    if (v.x == i + 1 && !maze[i][j].isRight()) continue;
                    if (v.y == j - 1 && !maze[i][j].isUp()) continue;
                    if (v.y == j + 1 && !maze[i][j].isDown()) continue;
                    return v;
                }
            }
        }
        return null;
    }

    private void solve() throws TextureCreationException, IOException {
        path.clear();
        RenderTexture texture = new RenderTexture();
        texture.create(width * (int) Config.CELL_SIZE, height * (int) Config.CELL_SIZE);
        texture.clear(Color.BLACK);
        draw(texture);

        maze[0][0].setVisited(false);

        path.push(new Vector2i(0, 0));

        while (current.x != width - 1 || current.y != height - 1) {
            current = path.peek();
            Vector2i next = chooseWay(current);
            if (next == null) {
                path.pop();
            } else {
                maze[next.x][next.y].setVisited(false);
                path.push(next);
            }
        }

        path.pop();
        drawSolution(texture);
        texture.display();
        texture.getTexture().copyToImage().saveToFile(Paths.get("mazes/seed-" + seed + "-solved.png"));
    }

    public void draw(RenderTarget target) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                vect = new Vector2f(i * (int) Config.CELL_SIZE, j * (int) Config.CELL_SIZE);
                maze[i][j].draw(target, sprite, vect);
            }
        }
    }

    private void foo(Vector2i cur, Vector2i vector, char[] direction) {
        if (vector.x == cur.x - 1) {
            direction[2] = '1';
        } else if (vector.x == cur.x + 1) {
            direction[3] = '1';
        } else if (vector.y == cur.y - 1) {
            direction[0] = '1';
        } else if (vector.y == cur.y + 1) {
            direction[1] = '1';
        }
    }

    private void drawSolution(RenderTarget target) {
        Object[] pathArray = path.toArray();

        for (int i = 0; i < pathArray.length; i++) {
            char[] direction = { '0', '0', '0' ,'0' };

            Vector2i current = (Vector2i)pathArray[i];
            if (i == 0) {
                direction[0] = '1';
            } else {
                Vector2i previous = (Vector2i)pathArray[i - 1];
                foo(current, previous, direction);
            }
            if (i == pathArray.length - 1) {
                direction[1] = '1';
            } else {
                Vector2i next = (Vector2i)pathArray[i + 1];
                foo(current, next, direction);
            }

            Sprite sprite = new Sprite();
            sprite.setColor(new Color(0, 0, 0, 192));
            sprite.setPosition(current.x * Config.CELL_SIZE, current.y * Config.CELL_SIZE);
            sprite.setScale(new Vector2f(Config.CELL_SIZE / 20f, Config.CELL_SIZE / 20f));

            String directionStr = "";
            for (int k = 0; k < 4; k++) {
                directionStr += direction[k];
            }
            switch (directionStr) {
                case "1100":
                    sprite.setTexture(Textures.getStraight());
                    break;
                case "0011":
                    sprite.setTexture(Textures.getStraight());
                    sprite.setRotation(90);
                    sprite.setOrigin(0, Textures.getStraight().getSize().y);
                    break;
                case "1010":
                    sprite.setTexture(Textures.getCorner());
                    sprite.setRotation(180);
                    sprite.setOrigin(Textures.getStraight().getSize().x, Textures.getStraight().getSize().y);
                    break;
                case "0110":
                    sprite.setTexture(Textures.getCorner());
                    sprite.setRotation(90);
                    sprite.setOrigin(0, Textures.getStraight().getSize().y);
                    break;
                case "1001":
                    sprite.setTexture(Textures.getCorner());
                    sprite.setRotation(-90);
                    sprite.setOrigin(Textures.getStraight().getSize().x, 0);
                    break;
                case "0101":
                    sprite.setTexture(Textures.getCorner());
                    break;
                default:
                    return;
            }

            target.draw(sprite);
        }
    }
}