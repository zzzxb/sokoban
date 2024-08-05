package xyz.zzzxb.sokoban.config;

import com.badlogic.gdx.Application;

/**
 * zzzxb
 * 2024/7/31
 */
public class Config {
    public final static String TITLE = "sokoban";
    public final static int BLOCK_SIZE = 40;
    public final static int BLOCK_LINE = 10;
    public final static int WALL_SIZE = BLOCK_SIZE * BLOCK_LINE;
    public final static int CENTER = WALL_SIZE / 2;
    public final static int[] WINDOW_SIZE = new int[]{WALL_SIZE, WALL_SIZE};
    public final static int LOGGER_LEVEL = Application.LOG_DEBUG;

}
