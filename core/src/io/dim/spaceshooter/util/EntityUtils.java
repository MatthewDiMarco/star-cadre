package io.dim.spaceshooter.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class EntityUtils {

    public static Random random = new Random();

    /**
     * Given the dimensions for a space, the method returns the distances in all
     * 4 directions between the provided bounding box and the encompassing boundaries.
     * @param boundingBox The box
     * @param width The width of the space
     * @param height The height of the space
     * @return Array with the distances in a clockwise order: [up, right, down, left]
     */
    public static float[] calcBoundaryDistances(
        Rectangle boundingBox,
        float width,
        float height) {
        return new float[] {
            height - boundingBox.y - boundingBox.height, // up
            width - boundingBox.x - boundingBox.width, // right
            -boundingBox.y, // down
            -boundingBox.x // left
        };
    }

    public static void randomizePoint(
        Vector2 point,
        int xx, int yy,
        int width, int height) {
        point.x = random
            .nextInt(width - xx) + xx;
        point.y = random
            .nextInt(height - yy) + yy;
    }

}
