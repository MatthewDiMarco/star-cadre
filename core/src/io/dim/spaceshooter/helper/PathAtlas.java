package io.dim.spaceshooter.helper;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.dim.spaceshooter.factory.EntityFactory.EnemyType;

public class PathAtlas {
    public static final Vector2[] PATH_BLUEPRINT_SNAKE = {
        new Vector2(-0.2f, 1.1f),
        new Vector2(0.9f, 0.5f),
        new Vector2(0.1f, 0.05f),
        new Vector2(1.1f, 0.05f),
        new Vector2(1.1f, -1.0f),
    };

    public static final Vector2[] PATH_BLUEPRINT_INVADER = {
        new Vector2(0.1f, 0.9f),
        new Vector2(0.4f, 0.5f),
        new Vector2(0.1f, 0.5f),
        new Vector2(0.4f, 0.9f),
    };

    public Array<Vector2[]> paths;

    public PathAtlas(Rectangle pathBoundary) {
        this.paths = new Array<>();
        this.paths.add(MathUtils.calcControlPointsFromBlueprint(
            PATH_BLUEPRINT_SNAKE, pathBoundary.width, pathBoundary.height));
        this.paths.add(MathUtils.calcControlPointsFromBlueprint(
            PATH_BLUEPRINT_INVADER, pathBoundary.width, pathBoundary.height));
    }
}
