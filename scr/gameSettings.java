import java.awt.Dimension;
import java.awt.Toolkit;

public class gameSettings {
    // autoW is the user's width display.
    // autoH has value that is 9/16 of the screen width, to maintains a 16:9 aspect
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int autoW = (int) screenSize.getWidth();
    static int autoH = (int) (screenSize.getWidth() * 9 / 16);

    // Constants for game resolution.
    // The aspect ratio should be 16:9.
    // The optimal resolution setting should be:
    // 640 x 360 (16:9)
    // 854 x 480 (16:9)
    // 1280 x 720 (16:9)
    // 1366 x 768 (16:9)
    // NOTE: Input higher resolution will cause display error.
    // DEFAULT AUTO: autoW for WIDTH, autoH for HEIGHT.

    public static final int WIDTH = autoW;
    public static final int HEIGHT = autoH;

    // Number of mines in each Difficulty Mode
    public static final int EASY_MINES = 10;
    public static final int MEDIUM_MINES = 20;
    public static final int HARD_MINES = 30;
    public static final int EXTREME_MINES = 40;

    // Formula to calculate the scaled size:
    // (game resolution / original resolution) * original size.
    public static int AdjustWidth(int x) {
        int newx = (int) ((double) (WIDTH) / 1920 * x);
        return newx;
    }

    public static int AdjustHeight(int y) {
        int newy = (int) ((double) (HEIGHT) / 1080 * y);
        return newy;
    }
}