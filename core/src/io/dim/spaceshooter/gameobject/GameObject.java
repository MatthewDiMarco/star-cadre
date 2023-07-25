package io.dim.spaceshooter.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public interface GameObject {

    void onStep(GameHandler gameHandler, float deltaTime);
    void onDraw(SpriteBatch batch);
}
