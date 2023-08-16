package io.dim.spaceshooter.helper;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class MathUtils {

    public static Random random = new Random();

    public static Vector2[] calcControlPointsFromBlueprint(
        Vector2[] blueprint, float worldWidth, float worldHeight) {
        Vector2[] controlPoints = new Vector2[blueprint.length];
        for (int ii = 0; ii < blueprint.length; ii++) {
            controlPoints[ii] = new Vector2(blueprint[ii]);
            controlPoints[ii].x *= worldWidth;
            controlPoints[ii].y *= worldHeight;
        }
        return controlPoints;
    }

    /**
     * Calculates a path using a spline function given control points.
     * @param spline The spline function for generating the points.
     * @param fidelity The total number of points to be generated;
     *                 the amount of detail or curvature to the path.
     * @return The path as an array of vectors.
     */
    public static Vector2[] calcPathFromSpline(Path spline, int fidelity) {
        Vector2[] path = new Vector2[fidelity];
        for (int ii = 0; ii < fidelity; ii++) {
            path[ii] = new Vector2();
            spline.valueAt(path[ii], ((float)ii/(float)fidelity));
        }
        return path;
    }
}
