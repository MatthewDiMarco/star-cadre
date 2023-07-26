package io.dim.spaceshooter.util;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;

public class MathUtil {

    public static Vector2[] calcCatmullRomPath(
        Vector2[] controlPoints, int fidelity) {
        CatmullRomSpline<Vector2> spline = new CatmullRomSpline<>(controlPoints, true);
        return calcPathFromSpline(spline, fidelity);
    }

    /**
     * Repeats a pattern of control points through space N-times given vertical offset.
     * @param pattern The pattern to repeat
     * @param offset The Y amount to move the pattern each repeat
     * @param repeats The number of repeats
     * @return The sequence of control points.
     */
    public static Vector2[] repeatControlPointsVertically(
        Vector2[] pattern,
        float offset,
        int repeats) {
        int totalControlPoints = pattern.length + (pattern.length * repeats);
        Vector2[] controlPoints = new Vector2[totalControlPoints];
        for (int ii = 0; ii < pattern.length; ii++) {
            controlPoints[ii] = new Vector2(pattern[ii]);
        }
        for (int rr = 1; rr <= repeats; rr++) {
            for (int ii = 0; ii < pattern.length; ii++) {
                int currIdx = ii + (pattern.length * rr);
                Vector2 prevCorrespondingPoint = controlPoints[currIdx - (pattern.length - 1)];
                controlPoints[currIdx] = new Vector2(
                    prevCorrespondingPoint.x,
                    prevCorrespondingPoint.y + offset);
            }
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
