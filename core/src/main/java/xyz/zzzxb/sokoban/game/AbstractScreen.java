package xyz.zzzxb.sokoban.game;

import com.badlogic.gdx.Screen;

/**
 * zzzxb
 * 2024/8/1
 */
public abstract class AbstractScreen implements Screen {

    @Override
    public abstract void show();

    @Override
    public abstract void render(float delta);

    @Override
    public void hide(){};

    @Override
    public void dispose() {}


    @Override
    public void pause() {
    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void resume() {
    }

}
