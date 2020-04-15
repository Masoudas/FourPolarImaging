package fr.fresnel.fourPolar.core.util.shape;

import org.junit.jupiter.api.Test;

public class ShapeFactoryTest {

    @Test
    public void close3DBox_zRotation90AtOrigin_ReturnsCorrectCoordinates() {
        long[] center = {0,0,0};

        IShape shape = new ShapeFactory().close3DBox(center, 1, 1, 1, Math.PI/2, 0);

        IShapeIteraror iterator = shape.getIterator();
        while (iterator.hasNext()) {
            long[] coords = iterator.next();

            System.out.println(coords[0] + " " + coords[1] + " " + coords[2]);
            
        }
    }

}