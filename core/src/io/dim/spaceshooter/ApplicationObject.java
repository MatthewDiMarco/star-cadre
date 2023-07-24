package io.dim.spaceshooter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ApplicationObject {

    void update(float deltaTime);
    void render(SpriteBatch batch);
}
