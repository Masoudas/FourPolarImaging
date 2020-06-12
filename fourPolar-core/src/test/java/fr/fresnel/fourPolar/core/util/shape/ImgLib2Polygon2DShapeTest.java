package fr.fresnel.fourPolar.core.util.shape;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class ImgLib2Polygon2DShapeTest {
    @Test
    public void init_SingleOrTwoPointPolygon_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ImgLib2Polygon2DShape.create(new long[] { 0 }, new long[] { 1 });
        });

        assertThrows(IllegalArgumentException.class, () -> {
            ImgLib2Polygon2DShape.create(new long[] { 0, 0 }, new long[] { 1, 1 });
        });

        assertThrows(IllegalArgumentException.class, () -> {
            ImgLib2Polygon2DShape.create(new long[] { 0, 0 }, new long[] { 1 });
        });

        assertThrows(IllegalArgumentException.class, () -> {
            ImgLib2Polygon2DShape.create(new long[] { 0 }, new long[] { 1, 1 });
        });

    }

    @Test
    public void getIterator_ARightTriangle_ReturnsCorrectTiangleVertices() {
        IShapeIterator iterator = ImgLib2Polygon2DShape.create(new long[] { 0, 1, 0 }, new long[] { 0, 0, 1 })
                .getIterator();

        HashSet<long[]> set = new HashSet<>();
        for (; iterator.hasNext();) {
            set.add(iterator.next());
        }

        assertTrue(set.size() == 3);

        assertTrue(_setContains(set, new long[] { 0, 0 }));
        assertTrue(_setContains(set, new long[] { 0, 1 }));
        assertTrue(_setContains(set, new long[] { 1, 0 }));

    }

    private boolean _setContains(Set<long[]> set, long[] arr) {
        for (Iterator<long[]> iterator = set.iterator(); iterator.hasNext();) {
            if (Arrays.equals(iterator.next(), arr))
                return true;
        }
        return false;
    }

}