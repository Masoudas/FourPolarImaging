package fr.fresnel.fourPolar.algorithm.util.image.stats;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RealType;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;

public class ImageStatistics {

    /**
     * Returns the minimum and maximum value of each plane of the image.
     * 
     * @return an array, where first row contains the minimums, and second the
     *         maximums of each plane of the image.
     */
    public static <T extends RealType> double[][] getPlaneMinMax(Image<T> image) {
        int nPlanes = MetadataUtil.getNPlanes(image);
        double[][] minMax = new double[2][nPlanes];

        IPointShape planeDim = MetadataUtil.getPlaneDim(image);
        long planeSize = planeDim.point()[0] * planeDim.point()[1];

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

}