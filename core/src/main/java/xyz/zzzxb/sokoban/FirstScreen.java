package xyz.zzzxb.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import xyz.zzzxb.sokoban.config.Config;
import xyz.zzzxb.sokoban.game.AbstractScreen;

public class FirstScreen extends AbstractScreen {
    SpriteBatch batch;
    Texture wall;
    Texture box;
    Texture player;
    Array<Vector2> wallArray = new Array<>();
    Array<Vector2> boxArray = new Array<>();
    Vector2 playerXY = new Vector2();
    Direction dir;

    int[][] map = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 3, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 2, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        Pixmap pixmapWall = new Pixmap(Config.BLOCK_SIZE, Config.BLOCK_SIZE, Pixmap.Format.RGBA8888);
        pixmapWall.setColor(Color.BLACK);
        pixmapWall.fillRectangle(0, 0, Config.BLOCK_SIZE, Config.BLOCK_SIZE);
        wall = new Texture(pixmapWall);

        box = new Texture("box.png");
        player = new Texture("player.png");
        dir = Direction.NONE;

        initMap();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        ctrl();

        batch.begin();
        wallArray.forEach(wallXY -> batch.draw(wall, wallXY.x, wallXY.y));
        boxArray.forEach(boxXY -> batch.draw(box, boxXY.x, boxXY.y));
        batch.draw(player, playerXY.x, playerXY.y);
        batch.end();
    }

    public void ctrl() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) Gdx.app.exit();
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) System.out.println("后退");;

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

        boolean move = true;

        if (dir != Direction.NONE && boxArray.contains(cpy, false)) {
            for (Vector2 vector2 : boxArray) {
                if (cpy.epsilonEquals(vector2)) {
                    Vector2 cpy1 = vector2.cpy();
                    switch (dir) {
                        case UP -> cpy1.add(0, Config.BLOCK_SIZE);
                        case DOWN -> cpy1.sub(0, Config.BLOCK_SIZE);
                        case LEFT -> cpy1.sub(Config.BLOCK_SIZE, 0);
                        case RIGHT -> cpy1.add(Config.BLOCK_SIZE, 0);
                    }
                    if(boxArray.contains(cpy1, false) || wallArray.contains(cpy1, false)) {
                        move = false;
                        break;
                    }else {
                        vector2.set(cpy1);
                    }
                }
            }
        }

        if (!wallArray.contains(cpy, false) && move) {
            playerXY.set(cpy);
        } else {
            dir = Direction.NONE;
        }

    }

    public void initMap() {
        for (int i = 0; i < Config.BLOCK_LINE; i++) {
            for (int j = 0; j < Config.BLOCK_LINE; j++) {
                int flag = map[i][j];
                int x = (j) * Config.BLOCK_SIZE;
                int y = (9 - i) * Config.BLOCK_SIZE;
                if (flag == 1) wallArray.add(new Vector2(x, y));
                if (flag == 2) boxArray.add(new Vector2(x, y));
                if (flag == 3) playerXY.set(x, y);
            }
        }
    }

    @Override
    public void dispose() {
        player.dispose();
        box.dispose();
        wall.dispose();
        batch.dispose();
    }
}
