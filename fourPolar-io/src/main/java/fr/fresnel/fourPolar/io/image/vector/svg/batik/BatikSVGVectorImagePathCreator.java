package fr.fresnel.fourPolar.io.image.vector.svg.batik;

import java.io.File;
import java.text.DecimalFormat;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;

/**
 * Creates the path file to a coordinate of the image. If image is 2D, the file
 * name would be equal to image name. Otherwise, the axis order starting from
 * first axis after xy together with coordinate is added (e.g.
 * imageName_z000_t000.svg for xyzt axis order).
 */
class BatikSVGVectorImagePathCreator {
    private final static DecimalFormat _DECIMAL_FORMATTER = new DecimalFormat("000");
    private final static int _XY_AXIS_OFFSET = 2;

    private final String[] _axisOrderAsString;
    private final IMetadata _metadata;

    /**
     * Initialize by passing metadata of the image.
     * 
     * @param metadata is the metadata of the image.
     */
    public BatikSVGVectorImagePathCreator(IMetadata metadata) {
        _axisOrderAsString = AxisOrder.mapAxisOrdersToLowerCaseChars().get(metadata.axisOrder());
        _metadata = metadata;
    }

    /**
     * Create path for the given plane index.
     * 
     * @param root       is the root of images.
     * @param imageName  is the image name for this svg set.
     * @param metadata   is the metadata of the image.
     * @param coordinate is the coordinate of this plane.
     * @return the path to the image file for this coordinate.
     * 
     * @throws IndexOutOfBounds if the given metadata does not have the requested
     *                          plane.
     */
    public File createPlaneImageFile(File root, String imageName, int planeIndex) {
        long[] plane_coords = _getPlaneCoodinates(_metadata, planeIndex);

        String name_with_coordinates = imageName;
        for (int dim = _XY_AXIS_OFFSET; dim < plane_coords.length; dim++) {
            name_with_coordinates += "_" + _axisOrderAsString[dim] + _DECIMAL_FORMATTER.format(plane_coords[dim]);
        }

        return new File(root, name_with_coordinates + ".svg");
    }

    /**
     * @return the coordinates of this index, starting from the x coordinate.
     */
    private long[] _getPlaneCoodinates(IMetadata metadata, int planeIndex) {
        return MetadataUtil.getPlaneCoordinates(metadata, planeIndex)[1];
    }

}