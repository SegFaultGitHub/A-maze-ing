package Utils;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Date;
import java.util.Random;
import java.util.Stack;

public class Utils {
    private static Stack<Long> timeStack = new Stack<>();
    private static Random random = new Random();

    public static void setRandomSeed(int seed) {
        random = new Random(seed);
    }

    public static float getRandomFloat() {
        return random.nextFloat();
    }

    /* max excluded */
    public static int getRandomInt(int max) {
        return random.nextInt(max);
    }

    /* min included, max excluded */
    public static int getRandomInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public static void startChrono() {
        timeStack.push(new Date().getTime());
    }

    public static long endChrono() {
        if (!timeStack.empty()) {
            return new Date().getTime() - timeStack.pop();
        }
        return -1;
    }

    private static String foo1(long n, int size) {
        if (n == 0) return stringRepeat("0", size);
        int nSize = (int)(Math.log10(n) + 1);
        return stringRepeat("0", size - nSize) + n;
    }

    public static String endChronoStr() {
        long end = endChrono();
        if (end == -1) return "error";
        Date result = new Date(end);
        return foo1(result.getMinutes(), 2) + ":" + foo1(result.getSeconds(), 2) + ":" + foo1(end % 1000, 3);
    }

    public static JSONObject readJSON(String path) {
        JSONParser jsonParser = new JSONParser();

        try {
            return (JSONObject) jsonParser.parse(new FileReader(path));
        } catch (Exception e) {
            return null;
        }
    }

    public static String stringRepeat(String s, int n) {
        n = Math.max(0, n);
        return new String(new char[n]).replace("\0", s);
    }

    public static String reformatJSON(JSONObject jsonObject, int tab) {
        String result = "{\n";
        boolean first = true;
        for (Object obj : jsonObject.keySet()) {
            if (!first) {
                result += ",\n";
            }
            first = false;
            String key = obj.toString();
            Object value = jsonObject.get(key);
            result += stringRepeat(" ", 4 * (tab + 1)) + "\""+ key + "\" : ";
            if (value.getClass() == JSONObject.class) {
                result += reformatJSON((JSONObject) value, tab + 1);
            } else if (value.getClass() == JSONArray.class) {

            } else if (value.getClass() == String.class) {
                result += "\"" + value.toString() + "\"";
            } else {
                result += value.toString();
            }
        }
        result += "\n" + stringRepeat(" ", tab * 4) + "}";
        return result;
    }

    public static float vector2fLength(Vector2f vector2f) {
        if (vector2f == null)
            return -1;
        return (float) Math.sqrt(vector2f.x * vector2f.x + vector2f.y * vector2f.y);
    }

    public static Texture blurTexture(ConstTexture texture, Vector2f size, int radius) throws TextureCreationException {
        Vector2i sizeInt = new Vector2i((int)size.x, (int)size.y);
        return blurTexture(texture, sizeInt, radius);
    }

    private static void blurCore(Image imageIn, Image imageOut, Vector2i size, int startX, int startY, int endX, int endY, int radius) {
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                int r = 0, g = 0, b = 0, count = 0;
                for (int i = Math.max(0, x - radius); i <= Math.min(size.x - 1, x + radius); i++) {
                    for (int j = Math.max(0, y - radius); j <= Math.min(size.y - 1, y + radius); j++) {
                        if (vector2fLength(new Vector2f(x - i, y - j)) > radius) continue;
                        Color color = imageIn.getPixel(i, j);
                        r += color.r;
                        g += color.g;
                        b += color.b;
                        count++;
                    }
                }
                imageOut.setPixel(x, y, new Color(r / count, g / count, b / count));
            }
        }
    }

    public static Texture blurTexture(ConstTexture texture, Vector2i size, int radius) throws TextureCreationException {
        startChrono();
        Image imageIn = texture.copyToImage();
        System.out.println(size);
        Image imageOut = new Image();
        imageOut.create(size.x, size.y);
        final int[] threadsDone = {0};
        //<editor-fold desc="Threads">
        final int[] array = new int[16];
        array[0] = 0;
        array[1] = 0;
        array[2] = size.x / 2;
        array[3] = size.y / 2;
        Runnable r1 = () -> {
            blurCore(imageIn, imageOut, size, array[0], array[1], array[2], array[3], radius);
            threadsDone[0]++;
        };
        array[4] = 0;
        array[5] = size.y / 2;
        array[6] = size.x / 2;
        array[7] = size.y;
        Runnable r2 = () -> {
            blurCore(imageIn, imageOut, size, array[4], array[5], array[6], array[7], radius);
            threadsDone[0]++;
        };
        array[8] = size.x / 2;
        array[9] = 0;
        array[10] = size.x;
        array[11] = size.y / 2;
        Runnable r3 = () -> {
            blurCore(imageIn, imageOut, size, array[8], array[9], array[10], array[11], radius);
            threadsDone[0]++;
        };
        array[12] = size.x / 2;
        array[13] = size.y / 2;
        array[14] = size.x;
        array[15] = size.y;
        Runnable r4 = () -> {
            blurCore(imageIn, imageOut, size, array[12], array[13], array[14], array[15], radius);
            threadsDone[0]++;
        };
        //</editor-fold>

        r1.run();
        r2.run();
        r3.run();
        r4.run();

        while (threadsDone[0] < 4);

        Texture result = new Texture();
        result.create(size.x, size.y);
        result.loadFromImage(imageOut);
        System.out.println(endChrono());
        return result;
    }

    public static double getDecimalPart(double d) {
        return d - Math.floor(d);
    }
}
