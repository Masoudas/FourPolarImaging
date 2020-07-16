package fr.fresnel.fourPolar.core.util.image.metadata;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;

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

	/**
	 * Returns dimension of plane (first two coordinates).
	 * 
	 * @param metadata is the metadata.
	 * @return an array containing plane dimension.
	 */
	public static long[] getPlaneDim(IMetadata metadata) {
		Objects.requireNonNull(metadata, "metadata cannot be null.");

		long[] dims = metadata.getDim();
		return new long[] { dims[0], dims[1] };
	}

	/**
	 * Returns the plane size of the metadata.
	 * 
	 * @return
	 */
	public static long getPlaneSize(IMetadata metadata) {
		long[] planeDim = getPlaneDim(metadata);
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
	 * @throws IllegalArgumentException if image is not at least 2D.
	 */
	public static long[] numPlanesPerDimension(IMetadata metadata) {
		long[] imageDim = metadata.getDim();

		if (imageDim.length <= 1) {
			throw new IllegalArgumentException("Image must be at least 2D to have a plane.");
		}

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

	/**
	 * Returns the coordinates of particular images as a matrix, where the first row
	 * indicates the starting coordinates and the second indicates the end
	 * coordinate.
	 * <p>
	 * Example: Imaging image is [2, 2, 2]. Then the method returns [[0, 0, 1], [1,
	 * 1, 1]] for the second plane of the image.
	 * 
	 * @param metadata   is the metadata of the image.
	 * @param planeIndex is the desired plane index, starting from one.
	 * @return the coordinates of the plane as a 2*n matrix.
	 * 
	 * @throws IndexOutOfBoundsException if the plane index is less than 1, or
	 *                                   exceeds the number of planes, or if the
	 *                                   image is less than 2D.
	 */
	public static long[][] getPlaneCoordinates(IMetadata metadata, long planeIndex) {
		if (planeIndex < 1 || planeIndex > getNPlanes(metadata)) {
			throw new IndexOutOfBoundsException("Image plane does not exist.");
		}

		long[] imgDim = metadata.getDim();
		if (imgDim.length == 2) {
			return new long[][] { { 0, 0 }, { imgDim[0] - 1, imgDim[1] - 1 } };
		}

		planeIndex = planeIndex - 1; // To compensate for index starting from 1.
		long[] planesPerDim = numPlanesPerDimension(metadata);
		long[] start = new long[planesPerDim.length];
		start[planesPerDim.length - 1] = planesPerDim[planesPerDim.length - 2] > 0
				? planeIndex / planesPerDim[planesPerDim.length - 2]
				: planeIndex;

		long planeIndexRemainder = planeIndex - start[planesPerDim.length - 1] * planesPerDim[planesPerDim.length - 2];
		for (int dim = planesPerDim.length - 2; dim >= 3; dim--) {
			start[dim] = planeIndexRemainder / planesPerDim[dim - 1];
			planeIndexRemainder = planeIndexRemainder - start[dim] * planesPerDim[dim - 1];
		}
		start[2] = planeIndexRemainder;

		long[] end = start.clone();
		end[0] = imgDim[0] - 1;
		end[1] = imgDim[1] - 1;

		return new long[][] { start, end };
	}

}