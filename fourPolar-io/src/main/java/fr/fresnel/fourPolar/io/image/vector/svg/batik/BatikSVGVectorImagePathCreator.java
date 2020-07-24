package fr.fresnel.fourPolar.io.image.vector.svg.batik;

import java.io.File;
import java.text.DecimalFormat;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.image.generic.metadata.MetadataUtil;

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
    private final File _root;
    private final String _imageName;

    /**
     * Initialize by passing metadata of the image, together with desired root and
     * image name.
     * 
     * @param metadata  is the metadata of the image.
     * @param root      is the root of where the images are stored.
     * @param imageName is the common name of all image planes.
     */
    public BatikSVGVectorImagePathCreator(IMetadata metadata, File root, String imageName) {
        _axisOrderAsString = AxisOrder.mapAxisOrdersToLowerCaseChars().get(metadata.axisOrder());
        _metadata = metadata;
        _root = root;
        _imageName = imageName;
    }

    /**
     * Create path for the given plane index.
     * 
     * @param planeIndex is the index of the plane.
     * @return the path to the image file for this coordinate.
     * 
     * @throws IndexOutOfBounds if the given metadata does not have the requested
     *                          plane.
     */
    public File createPlaneImageFile(int planeIndex) {
        long[] plane_coords = _getPlaneCoodinates(_metadata, planeIndex);

        String name_with_coordinates = _imageName;
        for (int dim = _XY_AXIS_OFFSET; dim < plane_coords.length; dim++) {
            name_with_coordinates += "_" + _axisOrderAsString[dim] + _DECIMAL_FORMATTER.format(plane_coords[dim]);
        }

        return new File(_root, name_with_coordinates + ".svg");
    }

    /**
     * @return the coordinates of this index, starting from the x coordinate.
     */
    private long[] _getPlaneCoodinates(IMetadata metadata, int planeIndex) {
        return MetadataUtil.getPlaneCoordinates(metadata, planeIndex)[1];
    }

}