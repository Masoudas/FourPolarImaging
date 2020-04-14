package fr.fresnel.fourPolar.algorithm.util.image.stats;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RealType;
import fr.fresnel.fourPolar.core.util.DPoint;

public class ImageStatistics {

    /**
     * Returns how many xy planes are present in the image.
     */
    public static <T extends PixelType> int getNPlanes(Image<T> image) {
        Objects.requireNonNull(image, "image cannot be null.");
        long[] dims = image.getDimensions();

        int nPlanes = 1;
        for (int dim = 2; dim < dims.length; dim++) {
            nPlanes *= dims[dim];
        }

        return nPlanes;
    }

    /**
     * Returns the minimum and maximum value of each plane of the image.
     * 
     * @return an array, where first row contains the minimums, and second the
     *         maximums of each plane of the image.
     */
    public static <T extends RealType> double[][] getPlaneMinMax(Image<T> image) {
        int nPlanes = getNPlanes(image);
        double[][] minMax = new double[2][nPlanes];

        DPoint planeDim = getPlaneDim(image);
        long planeSize = planeDim.x * planeDim.y;

        IPixelCursor<T> cursor = image.getCursor();
        for (int plane = 0; plane < nPlanes; plane++) {
            int planeCounter = 1;

            double planeMin = Double.POSITIVE_INFINITY;
            double planeMax = 0;
            while (cursor.hasNext() && planeCounter++ <= planeSize) {
                double pixel = cursor.next().value().getRealValue();

                planeMin = pixel < planeMin ? pixel : planeMin;
                planeMax = pixel > planeMax ? pixel : planeMax;
            }
            planeCounter = 1;
            minMax[0][plane] = planeMin;
            minMax[1][plane] = planeMax;

        }

        return minMax;

    }

    public static <T extends PixelType> DPoint getPlaneDim(Image<T> image) {
        long[] dims = image.getDimensions();
        return new DPoint((int) dims[0], (int) dims[1]);
    }

}