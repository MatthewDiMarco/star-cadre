package io.dim.spaceshooter.helper;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Paths {

    public enum PathIndex { SNAKE, INVADER, TANK };

    public static final Vector2[] PATH_BLUEPRINT_SNAKE = {
        new Vector2(0.05f, 0.90f),
        new Vector2(0.45f, 0.75f),
        new Vector2(0.05f, 0.50f),
        new Vector2(0.45f, 0.25f),
        new Vector2(0.05f, 0.05f),
        new Vector2(0.05f, -1.0f),
    };

    public static final Vector2[] PATH_BLUEPRINT_INVADER = {
        new Vector2(0.05f, 0.90f),
        new Vector2(0.05f, 0.85f),
        new Vector2(0.45f, 0.85f),
        new Vector2(0.45f, 0.80f),
        new Vector2(0.05f, 0.80f),
        new Vector2(0.05f, 0.75f),
        new Vector2(0.45f, 0.75f),
        new Vector2(0.45f, 0.70f),
        new Vector2(0.05f, 0.70f),
        new Vector2(0.05f, 0.65f),
        new Vector2(0.45f, 0.65f),
        new Vector2(0.45f, 0.60f),
        new Vector2(0.05f, 0.60f),
        new Vector2(0.05f, 0.55f),
        new Vector2(0.45f, 0.55f),
        new Vector2(0.45f, 0.50f),
        new Vector2(0.05f, 0.50f),
        new Vector2(0.05f, 0.45f),
        new Vector2(0.45f, 0.45f),
        new Vector2(0.45f, 0.40f),
        new Vector2(0.05f, 0.40f),
        new Vector2(0.05f, 0.35f),
        new Vector2(0.45f, 0.35f),
        new Vector2(0.45f, 0.30f),
        new Vector2(0.05f, 0.30f),
        new Vector2(0.05f, 0.25f),
        new Vector2(0.45f, 0.25f),
        new Vector2(0.45f, -1.0f),
    };


    public static final Vector2[] PATH_BLUEPRINT_TANK = {
        new Vector2(0.05f, 0.9f),
        new Vector2(0.45f, 0.9f),
    };

    public Vector2[][] array;

    public Paths(Rectangle pathBoundary) {
        this.array = new Vector2[][]{
            MathUtils.calcControlPointsFromBlueprint(
                PATH_BLUEPRINT_SNAKE, pathBoundary.width, pathBoundary.height),
            MathUtils.calcControlPointsFromBlueprint(
                PATH_BLUEPRINT_INVADER, pathBoundary.width, pathBoundary.height),
            MathUtils.calcControlPointsFromBlueprint(
                PATH_BLUEPRINT_TANK, pathBoundary.width, pathBoundary.height),
        };
    }
}
