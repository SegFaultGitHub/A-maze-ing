package Misc;

import Main.Config;
import Textures.Textures;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

/**
 * Created by SegFault on 29/03/2017.
 */
public class Cell {
    private boolean up, down, left, right;
    private boolean visited;
    private char[] content;
    private Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
        content[0] = up ? '1' : '0';
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
        content[1] = down ? '1' : '0';
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
        content[2] = left ? '1' : '0';
    }

    public void reset() {
        up = false;
        down = false;
        left = false;
        right = false;
        visited = false;
        for (int i = 0; i < 4; i++) {
            content[i] = '0';
        }
        color = Color.BLACK;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
        content[3] = right ? '1' : '0';
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Cell() {
        up = false;
        down = false;
        left = false;
        right = false;
        visited = false;
        content = "0000".toCharArray();
        color = Color.BLACK;
    }

    public void draw(RenderTarget target, int x, int y) {
        Sprite sprite = new Sprite();
        sprite.setPosition(x, y);
        sprite.setColor(color);
        sprite.setScale(new Vector2f(Config.CELL_SIZE / 20f, Config.CELL_SIZE / 20f));
        String contentStr = "";
        for (int i = 0; i < 4; i++) {
            contentStr += content[i];
        }
        switch (contentStr) {
            case "0000":
                break;
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
            case "1000":
                sprite.setTexture(Textures.getImpasse());
                sprite.setRotation(180);
                sprite.setOrigin(Textures.getStraight().getSize().x, Textures.getStraight().getSize().y);
                break;
            case "0100":
                sprite.setTexture(Textures.getImpasse());
                break;
            case "0010":
                sprite.setTexture(Textures.getImpasse());
                sprite.setRotation(90);
                sprite.setOrigin(0, Textures.getStraight().getSize().y);
                break;
            case "0001":
                sprite.setTexture(Textures.getImpasse());
                sprite.setRotation(-90);
                sprite.setOrigin(Textures.getStraight().getSize().x, 0);
                break;
            case "1111":
                sprite.setTexture(Textures.getCross());
                break;
            case "1110":
                sprite.setTexture(Textures.getT());
                sprite.setRotation(90);
                sprite.setOrigin(0, Textures.getStraight().getSize().y);
                break;
            case "1101":
                sprite.setTexture(Textures.getT());
                sprite.setRotation(-90);
                sprite.setOrigin(Textures.getStraight().getSize().x, 0);
                break;
            case "1011":
                sprite.setTexture(Textures.getT());
                sprite.setRotation(180);
                sprite.setOrigin(Textures.getStraight().getSize().x, Textures.getStraight().getSize().y);
                break;
            case "0111":
                sprite.setTexture(Textures.getT());
                break;
            default:
                return;
        }

        target.draw(sprite);
    }
}
