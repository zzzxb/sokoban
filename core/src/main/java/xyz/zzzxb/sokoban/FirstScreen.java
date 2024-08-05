package xyz.zzzxb.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import xyz.zzzxb.sokoban.config.Config;
import xyz.zzzxb.sokoban.game.AbstractScreen;

public class FirstScreen extends AbstractScreen {
    SpriteBatch batch;
    Texture wall;
    Texture box;
    Texture player;
    Texture exit;

    Array<Vector2> wallArray = new Array<>();
    Array<Vector2> boxArray = new Array<>();
    Array<Vector2> exitArray = new Array<>();
    Vector2 playerXY = new Vector2();
    Direction dir;
    int level = 0;

    JsonReader jsonReader;
    JsonValue mapJson;

    enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        wall = new Texture("wall.png");
        box = new Texture("box.png");
        player = new Texture("player.png");
        exit = new Texture("exit.png");

        jsonReader = new JsonReader();
        mapJson = jsonReader.parse(Gdx.files.internal("map.json"));

        dir = Direction.NONE;
        initMap();
    }

    public void initGame() {
        wallArray.clear();
        boxArray.clear();
        exitArray.clear();
        initMap();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        ctrl();

        batch.begin();
        wallArray.forEach(wallXY -> batch.draw(wall, wallXY.x, wallXY.y, Config.BLOCK_SIZE, Config.BLOCK_SIZE));
        exitArray.forEach(exitXY -> batch.draw(exit, exitXY.x, exitXY.y, Config.BLOCK_SIZE, Config.BLOCK_SIZE));
        boxArray.forEach(boxXY -> batch.draw(box, boxXY.x, boxXY.y, Config.BLOCK_SIZE, Config.BLOCK_SIZE));
        batch.draw(player, playerXY.x, playerXY.y, Config.BLOCK_SIZE, Config.BLOCK_SIZE);
        batch.end();

        win();
    }

    public void ctrl() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) Gdx.app.exit();
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) initGame();

        Vector2 cpy = playerXY.cpy();
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            dir = Direction.UP;
            cpy.add(0, Config.BLOCK_SIZE);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            dir = Direction.DOWN;
            cpy.sub(0, Config.BLOCK_SIZE);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            dir = Direction.LEFT;
            cpy.sub(Config.BLOCK_SIZE, 0);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            dir = Direction.RIGHT;
            cpy.add(Config.BLOCK_SIZE, 0);
        }

        moveBox(cpy);
    }

    public void moveBox(Vector2 cpy) {
        boolean move = true;

        // 如果玩家坐标不与未移动前的箱子坐标重合，说明玩家没有推动箱子
        if (dir != Direction.NONE && boxArray.contains(cpy, false)) {
            for (Vector2 vector2 : boxArray) {
                if (cpy.epsilonEquals(vector2)) {
                    Vector2 cpy1 = vector2.cpy();
                    // 记录玩家移动方向，让箱子随玩家移动方向移动。
                    switch (dir) {
                        case UP -> cpy1.add(0, Config.BLOCK_SIZE);
                        case DOWN -> cpy1.sub(0, Config.BLOCK_SIZE);
                        case LEFT -> cpy1.sub(Config.BLOCK_SIZE, 0);
                        case RIGHT -> cpy1.add(Config.BLOCK_SIZE, 0);
                    }
                    // 如果箱子前方有碰撞物(箱子、墙)，箱子和玩家就不能移动
                    if(boxArray.contains(cpy1, false) || wallArray.contains(cpy1, false)) {
                        move = false;
                        break;
                    }else {
                        vector2.set(cpy1);
                    }
                }
            }
        }

        // 玩家坐标不与墙重叠，并且箱子可推动, 玩家方可进行移动
        if (!wallArray.contains(cpy, false) && move) {
            playerXY.set(cpy);
        } else {
            dir = Direction.NONE;
        }
    }

    public void win() {
        if(boxArray.containsAll(exitArray, false)) {
            Gdx.app.log("胜利判定", "你胜利了");
            level = level == 8 ? 0 : level + 1;
            initGame();
        }
    }

    public void initMap() {
        int[][] map = new Json().readValue(int[][].class, mapJson.get("level"+ level));

        for (int i = 0; i < Config.BLOCK_LINE; i++) {
            for (int j = 0; j < Config.BLOCK_LINE; j++) {
                int flag = map[i][j];
                int x = (j) * Config.BLOCK_SIZE;
                int y = (9 - i) * Config.BLOCK_SIZE;
                if (flag == 1) wallArray.add(new Vector2(x, y));
                if (flag == 2) boxArray.add(new Vector2(x, y));
                if (flag == 3) playerXY.set(x, y);
                if (flag == 4) exitArray.add(new Vector2(x, y));
            }
        }
    }

    @Override
    public void dispose() {
        exit.dispose();
        player.dispose();
        box.dispose();
        wall.dispose();
        batch.dispose();
    }
}
