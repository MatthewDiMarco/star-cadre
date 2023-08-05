package io.dim.spaceshooter.helper;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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
        new Vector2(0.1f, 0.8f),
        new Vector2(0.5f, 0.8f),
        new Vector2(0.5f, 0.7f),
        new Vector2(0.1f, 0.7f),
        new Vector2(0.1f, 0.6f),
        new Vector2(0.5f, 0.6f),
        new Vector2(0.5f, 0.5f),
        new Vector2(0.1f, 0.5f),
        new Vector2(0.1f, 0.4f),
        new Vector2(0.5f, 0.4f),
        new Vector2(0.5f, 0.3f),
        new Vector2(0.1f, 0.3f),
        new Vector2(0.1f, 0.2f),
        new Vector2(0.5f, 0.2f),
        new Vector2(0.5f, 0.1f),
        new Vector2(0.1f, 0.1f),
    };

    public static final Vector2[] PATH_BLUEPRINT_DRAGON = {
        new Vector2(0.10f, 0.9f),
        new Vector2(0.45f, 0.8f),
        new Vector2(0.10f, 0.7f),
        new Vector2(0.45f, 0.6f),
        new Vector2(0.10f, 0.5f),
        new Vector2(0.45f, 0.4f),
        new Vector2(0.10f, 0.3f),
        new Vector2(0.45f, 0.2f),
        new Vector2(0.30f, 0.1f),
        new Vector2(0.15f, 0.2f),
        new Vector2(0.30f, 0.3f),
        new Vector2(0.45f, 0.2f),
        new Vector2(0.45f, -1.0f),
    };


    public static final Vector2[] PATH_BLUEPRINT_TANK = {
        new Vector2(0.1f, 0.9f),
        new Vector2(0.9f, 0.9f),
    };

    public Array<Vector2[]> paths;

    public PathAtlas(Rectangle pathBoundary) {
        this.paths = new Array<>();
        this.paths.add(MathUtils.calcControlPointsFromBlueprint(
            PATH_BLUEPRINT_SNAKE, pathBoundary.width, pathBoundary.height));
        this.paths.add(MathUtils.calcControlPointsFromBlueprint(
            PATH_BLUEPRINT_INVADER, pathBoundary.width, pathBoundary.height));
        this.paths.add(MathUtils.calcControlPointsFromBlueprint(
            PATH_BLUEPRINT_DRAGON, pathBoundary.width, pathBoundary.height));
        this.paths.add(MathUtils.calcControlPointsFromBlueprint(
            PATH_BLUEPRINT_TANK, pathBoundary.width, pathBoundary.height));
    }
}
