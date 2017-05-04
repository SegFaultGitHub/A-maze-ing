package Misc;

import Textures.Textures;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

/**
 * Created by SegFault on 29/03/2017.
 */
public class Cell {
    private boolean visited;
    private byte content;
    private Color color;

    public void setColor(int r, int g, int b) {
        this.color = null;
        this.color = new Color(r, g, b);
    }

    public boolean isUp() {
        return (content & 0b1000) != 0;
    }

    public void setUp() {
        content |= 0b1000;
    }

    public boolean isDown() {
        return (content & 0b0100) != 0;
    }

    public void setDown() {
        content |= 0b0100;
    }

    public boolean isLeft() {
        return (content & 0b0010) != 0;
    }

    public void setLeft() {
        content |= 0b0010;
    }

    public boolean isRight() {
        return (content & 0b0001) != 0;
    }

    public void setRight() {
        content |= 0b0001;
    }

    public void reset() {
        visited = false;
        content = 0b0000;
        color = Color.BLACK;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Cell() {
        reset();
    }

    public void draw(RenderTarget target, Sprite sprite, Vector2f pos) {
        sprite.setPosition(pos);
        sprite.setColor(color);
        switch (content) {
            case 0b0000:
                return;
            case 0b1100:
                sprite.setTexture(Textures.getStraight());
                sprite.setRotation(0);
                sprite.setOrigin(0, 0);
                break;
            case 0b0011:
                sprite.setTexture(Textures.getStraight());
                sprite.setRotation(90);
                sprite.setOrigin(0, Textures.getStraight().getSize().y);
                break;
            case 0b1010:
                sprite.setTexture(Textures.getCorner());
                sprite.setRotation(180);
                sprite.setOrigin(Textures.getStraight().getSize().x, Textures.getStraight().getSize().y);
                break;
            case 0b0110:
                sprite.setTexture(Textures.getCorner());
                sprite.setRotation(90);
                sprite.setOrigin(0, Textures.getStraight().getSize().y);
                break;
            case 0b1001:
                sprite.setTexture(Textures.getCorner());
                sprite.setRotation(-90);
                sprite.setOrigin(Textures.getStraight().getSize().x, 0);
                break;
            case 0b0101:
                sprite.setTexture(Textures.getCorner());
                sprite.setRotation(0);
                sprite.setOrigin(0, 0);
                break;
            case 0b1000:
                sprite.setTexture(Textures.getImpasse());
                sprite.setRotation(180);
                sprite.setOrigin(Textures.getStraight().getSize().x, Textures.getStraight().getSize().y);
                break;
            case 0b0100:
                sprite.setTexture(Textures.getImpasse());
                sprite.setRotation(0);
                sprite.setOrigin(0, 0);
                break;
            case 0b0010:
                sprite.setTexture(Textures.getImpasse());
                sprite.setRotation(90);
                sprite.setOrigin(0, Textures.getStraight().getSize().y);
                break;
            case 0b0001:
                sprite.setTexture(Textures.getImpasse());
                sprite.setRotation(-90);
                sprite.setOrigin(Textures.getStraight().getSize().x, 0);
                break;
            case 0b1111:
                sprite.setTexture(Textures.getCross());
                sprite.setRotation(0);
                sprite.setOrigin(0, 0);
                break;
            case 0b1110:
                sprite.setTexture(Textures.getT());
                sprite.setRotation(90);
                sprite.setOrigin(0, Textures.getStraight().getSize().y);
                break;
            case 0b1101:
                sprite.setTexture(Textures.getT());
                sprite.setRotation(-90);
                sprite.setOrigin(Textures.getStraight().getSize().x, 0);
                break;
            case 0b1011:
                sprite.setTexture(Textures.getT());
                sprite.setRotation(180);
                sprite.setOrigin(Textures.getStraight().getSize().x, Textures.getStraight().getSize().y);
                break;
            case 0b0111:
                sprite.setTexture(Textures.getT());
                sprite.setRotation(0);
                sprite.setOrigin(0, 0);
                break;
            default:
                return;
        }

        target.draw(sprite);
    }
}
