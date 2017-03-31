package Main;

import Utils.Utils;
import org.json.simple.JSONObject;

public class Config {
    public static boolean FULL_SCREEN = false;
    public static boolean HD = true;
    public static boolean DEBUG = false;
    public static boolean SAVE = true;
    public static boolean DISPLAY = false;
    public static long GENERATIONS = 0;
    public static float CELL_SIZE = 0;
    public static long SCREEN_WIDTH = 0;
    public static long SCREEN_HEIGHT = 0;
    public static boolean SOLVE = false;

    public static void initialize() {
        JSONObject jsonObject = Utils.readJSON("config/config.json");
        if (jsonObject == null) return;
        HD = (long) jsonObject.get("HD") != 0;
        FULL_SCREEN = (long) jsonObject.get("FULL_SCREEN") != 0;
        DEBUG = (long) jsonObject.get("DEBUG") != 0;
        DISPLAY = (long) jsonObject.get("DISPLAY") != 0;
        SOLVE = (long) jsonObject.get("SOLVE") != 0;
        SAVE = (long) jsonObject.get("SAVE") != 0;
        GENERATIONS = (long) jsonObject.get("GENERATIONS");
        CELL_SIZE = ((Number)jsonObject.get("CELL_SIZE")).floatValue();
        SCREEN_WIDTH = (long) jsonObject.get("SCREEN_WIDTH");
        SCREEN_HEIGHT = (long) jsonObject.get("SCREEN_HEIGHT");
    }
}
