package fr.fresnel.fourPolar.io.image.vector.svg.batik;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;

class BatikSVGVectorImageUtil {
    private static final Map<AxisOrder, String[]> _axisOrdersAsString = AxisOrder.mapAxisOrdersToLowerCaseChars();
    private static final DecimalFormat _decimalFormat = new DecimalFormat("000");

    private static final int XY_AXIS_OFFSET = 2;
    /**
     * Creates the path file to a coordinate of the image. If image is 2D, the file
     * name would be equal to image name. Otherwise, the axis order starting from
     * first axis after xy together with coordinate is added (e.g.
     * imageName_z000_t000.svg for xyzt axis order).
     * 
     * @param root       is the root of images.
     * @param imageName  is the image name for this svg set.
     * @param metadata   is the metadata of the image.
     * @param coordinate is the coordinate of this plane.
     * @return the path to the image file for this coordinate.
     * 
     * @throws IndexOutOfBounds if the given metadata does not have the requested plane.
     */
    static File createPlaneImageFile(File root, String imageName, IMetadata metadata, int planeIndex) {
        String[] axisOrderAsString = _axisOrdersAsString.get(metadata.axisOrder());
        long[] plane_coords = _getPlaneCoodinates(metadata, planeIndex);

        String name_with_coordinates = imageName;
        for (int dim = XY_AXIS_OFFSET; dim < plane_coords.length; dim++) {
            name_with_coordinates += "_" + axisOrderAsString[dim] + _decimalFormat.format(plane_coords[dim]);
        }

        return new File(root, name_with_coordinates + ".svg");
    }

    /**
     * @return the coordinates of this index, starting from the x coordinate.
     */
    private static long[] _getPlaneCoodinates(IMetadata metadata, int planeIndex) {
        return MetadataUtil.getPlaneCoordinates(metadata, planeIndex)[1];
    }

}