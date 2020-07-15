package fr.fresnel.fourPolar.core.util.image.metadata;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

/**
 * A set of utility methods on metadata information.
 */
public class MetadataUtil {
	private MetadataUtil() {
		throw new AssertionError();
	}

	/**
	 * Returns true if image is planer (has at most one z point, and time point, and
	 * channel or a combination of them).
	 */
	public static boolean isImagePlanar(IMetadata metadata) {
		long[] dim = metadata.getDim();

		long nChannels = metadata.axisOrder().c_axis > 0 ? dim[metadata.axisOrder().c_axis] : 0;
		long nZpoints = metadata.axisOrder().z_axis > 0 ? dim[metadata.axisOrder().z_axis] : 0;
		long nTimepoints = metadata.axisOrder().t_axis > 0 ? dim[metadata.axisOrder().t_axis] : 0;

		return nChannels <= 1 && nZpoints <= 1 && nTimepoints <= 1;
	}

	/**
	 * Returns true if an image is quasi planar. An image is quasi planar if either
	 * it's planar as defined by {@link MetadataUtil#isImagePlanar(IMetadata)}, or
	 * if it has higher dimensions, they're all one.
	 * 
	 */
	public static boolean isImageQuasiPlanar(IMetadata metadata) {
		long[] dim = metadata.getDim();

		long nChannels = metadata.axisOrder().c_axis > 0 ? dim[metadata.axisOrder().c_axis] : 0;
		long nZpoints = metadata.axisOrder().z_axis > 0 ? dim[metadata.axisOrder().z_axis] : 0;
		long nTimepoints = metadata.axisOrder().t_axis > 0 ? dim[metadata.axisOrder().t_axis] : 0;

		return nChannels <= 1 && nZpoints <= 1 && nTimepoints <= 1;
	}

	/**
	 * Returns how many xy planes are present in the image.
	 */
	public static int getNPlanes(IMetadata metadata) {
		Objects.requireNonNull(metadata, "metadata cannot be null.");
		long[] dims = metadata.getDim();

		int nPlanes = 1;
		for (int dim = 2; dim < dims.length; dim++) {
			nPlanes *= dims[dim];
		}

		return nPlanes;
	}

	public static IPointShape getPlaneDim(IMetadata metadata) {
		Objects.requireNonNull(metadata, "metadata cannot be null.");

		long[] dims = metadata.getDim();
		return new ShapeFactory().point(new long[] { dims[0], dims[1] }, AxisOrder.XY);
	}

	/**
	 * Returns the plane size of the metadata.
	 * 
	 * @return
	 */
	public static long getPlaneSize(IMetadata metadata) {
		long[] planeDim = getPlaneDim(metadata).point();
		return planeDim[0] * planeDim[1];
	}

	public static boolean isDimensionEqual(IMetadata metadata1, IMetadata metadata2) {
		return Arrays.equals(metadata1.getDim(), metadata2.getDim());
	}

	public static boolean isAxisOrderEqual(IMetadata metadata1, IMetadata metadata2) {
		return metadata1.axisOrder() == metadata2.axisOrder();

	}

	public static long[] getImageLastPixel(IMetadata metadata) {
		return Arrays.stream(metadata.getDim()).map((t) -> t - 1).toArray();
	}

	/**
	 * @return true if number of axis in the given {@link AxisOrder} equals the
	 *         given dimension vector, otherwise return false.
	 */
	public static boolean isNumAxisEqualDimension(AxisOrder axisOrder, long[] dimension) {
		return axisOrder.numAxis == dimension.length;
	}

	/**
	 * Returns an array equal in size to image dimension, indicating how many planes
	 * are present per dimension. For example, if dim = [2, 2, 3, 4], and given the
	 * first two axis indicate the plane, 3 planes are present for the 3rd and 12
	 * for the fourth. So the method returns [0, 0, 3, 12]. Note that the first two
	 * elements are always set to zero for convenience. Hence if the image is two
	 * dimensional, the method returns [0, 0], and for 1D images it returns [0].
	 * 
	 * @return an array indicating number of planes per dimension.
	 */
	public static long[] numPlanesPerDimension(IMetadata metadata) {
		long[] imageDim = metadata.getDim();
		if (imageDim.length <= 2) {
			return new long[imageDim.length];
		}

		long[] nPlanesPerDim = new long[imageDim.length];
		nPlanesPerDim[2] = imageDim[2];
		for (int dim = 3; dim < nPlanesPerDim.length; dim++) {
			nPlanesPerDim[dim] = nPlanesPerDim[dim - 1] * imageDim[dim];
		}

		return nPlanesPerDim;

	}

}