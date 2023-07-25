package io.dim.spaceshooter.util;

import com.badlogic.gdx.math.BSpline;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;

public class MathUtil {

    public static Vector2[] calcBSplinePath( // TODO need offset for control points (marching patterns)
        Vector2[] controlPoints, int fidelity) {
        //CatmullRomSpline<Vector2> spline = new CatmullRomSpline<>(controlPoints, true);
        BSpline<Vector2> spline = new BSpline<>(controlPoints, 3, true);
        return calcPathFromSpline(spline, fidelity);
    }

    public static Vector2[] calcCatmullRomPath(
        Vector2[] controlPoints, int fidelity) {
        CatmullRomSpline<Vector2> spline = new CatmullRomSpline<>(controlPoints, true);
        return calcPathFromSpline(spline, fidelity);
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
