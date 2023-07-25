package io.dim.spaceshooter.util;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;

public class MathUtil {

    /**
     * Calculates a path using a spline function given some generic points.
     * @param points The main points, in order, from which to generate the path.
     * @param fidelity The total number of points to be generated;
     *                 the amount of detail or curvature to the path.
     * @return The path as an array of vectors.
     */
    public static Vector2[] calcSplinePath(
        Vector2[] points,
        float xOffset,
        float yOffset,
        int fidelity) {
        Vector2[] path = new Vector2[fidelity];
        CatmullRomSpline<Vector2> spline = new CatmullRomSpline<>(points, true);
        for (int ii = 0; ii < fidelity; ii++) {
            path[ii] = new Vector2();
            spline.valueAt(path[ii], ((float)ii/(float)fidelity));
            path[ii].x += xOffset;
            path[ii].y += yOffset;
        }
        return path;
    }
}
