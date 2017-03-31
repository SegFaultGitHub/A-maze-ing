package Textures;

import org.jsfml.graphics.Texture;

import java.nio.file.Paths;

/**
 * Created by SegFault on 29/03/2017.
 */
public class Textures {
    private static boolean smooth = true;
    private static Texture corner, straight, impasse, cross, t;

    public static boolean initialize() {
        corner = new Texture();
        try {
            corner.loadFromFile(Paths.get("graphics/corner.png"));
            corner.setSmooth(smooth);
        } catch (Exception e) {
            System.err.println("Cannot load texture corner.");
            return false;
        }

        straight = new Texture();
        try {
            straight.loadFromFile(Paths.get("graphics/straight.png"));
            straight.setSmooth(smooth);
        } catch (Exception e) {
            System.err.println("Cannot load texture straight.");
            return false;
        }

        impasse = new Texture();
        try {
            impasse.loadFromFile(Paths.get("graphics/impasse.png"));
            impasse.setSmooth(smooth);
        } catch (Exception e) {
            System.err.println("Cannot load texture impasse.");
            return false;
        }

        cross = new Texture();
        try {
            cross.loadFromFile(Paths.get("graphics/cross.png"));
            cross.setSmooth(smooth);
        } catch (Exception e) {
            System.err.println("Cannot load texture cross.");
            return false;
        }

        t = new Texture();
        try {
            t.loadFromFile(Paths.get("graphics/t.png"));
            t.setSmooth(smooth);
        } catch (Exception e) {
            System.err.println("Cannot load texture t.");
            return false;
        }

        return true;
    }

    public static Texture getCorner() {
        return corner;
    }

    public static Texture getStraight() {
        return straight;
    }

    public static Texture getImpasse() {
        return impasse;
    }

    public static Texture getCross() {
        return cross;
    }

    public static Texture getT() {
        return t;
    }
}
