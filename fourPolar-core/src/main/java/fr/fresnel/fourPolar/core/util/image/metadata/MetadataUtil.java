package fr.fresnel.fourPolar.core.util.image.metadata;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;

/**
 * A set of utility methods on metadata information.
 */
public class MetadataUtil {
	private MetadataUtil() {
		throw new AssertionError();
	}

	/**
	 * Returns true if image is planer (i.e, it has only two dimensions).
	 */
	public static boolean isImagePlanar(IMetadata metadata) {
		long[] dim = metadata.getDim();
		return dim.length == 2;
	}

	/**
	 * Returns true if an image is quasi planar. An image is quasi planar if either
	 * it's planar as defined by {@link MetadataUtil#isImagePlanar(IMetadata)}, or
	 * if it has higher dimensions, they're all one.
	 * 
	 */
	public static boolean isImageQuasiPlanar(IMetadata metadata) {
		long[] dim = metadata.getDim();
		if (dim.length < 2) {
			return false;
		}

		return isImagePlanar(metadata) || IntStream.range(2, dim.length).allMatch(i -> dim[i] == 1);
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
	 * 
	 * @throws IllegalArgumentException if the image is not at least 2D.
	 */
	public static long[] getPlaneDim(IMetadata metadata) {
		Objects.requireNonNull(metadata, "metadata cannot be null.");

		long[] dims = metadata.getDim();
		if (dims.length < 2) {
			throw new IllegalArgumentException("Image must be at least 2D to have a plane.");
		}

		return new long[] { dims[0], dims[1] };
	}

	/**
	 * 
	 * @param metadata is the metadata.
	 * @return the number of pixels in a plane.
	 * @throws IllegalArgumentException if the image is not at least 2D.
	 */
	public static long getPlaneSize(IMetadata metadata) {
		Objects.requireNonNull(metadata, "metadata cannot be null.");

		long[] dims = metadata.getDim();
		if (dims.length < 2) {
			throw new IllegalArgumentException("Image must be at least 2D to have a plane.");
		}

		long[] planeDim = getPlaneDim(metadata);
		return planeDim[0] * planeDim[1];
	}

	public static boolean isDimensionEqual(IMetadata metadata1, IMetadata metadata2) {
		Objects.requireNonNull(metadata1, "metadata1 cannot be null.");
		Objects.requireNonNull(metadata2, "metadata2 cannot be null.");

		return Arrays.equals(metadata1.getDim(), metadata2.getDim());
	}

	public static boolean isAxisOrderEqual(IMetadata metadata1, IMetadata metadata2) {
		Objects.requireNonNull(metadata1, "metadata1 cannot be null.");
		Objects.requireNonNull(metadata2, "metadata2 cannot be null.");

		return metadata1.axisOrder() == metadata2.axisOrder();

	}

	/**
	 * Returns the coordinates of the last pixel of the image, which is dim - 1 for
	 * each dimension.
	 * 
	 * @param metadata is the metadata of the image.
	 * @return an array containing the last position.
	 */
	public static long[] getImageLastPixel(IMetadata metadata) {
		Objects.requireNonNull(metadata, "metadata cannot be null.");
		return Arrays.stream(metadata.getDim()).map((t) -> t - 1).toArray();
	}

	/**
	 * @return true if number of axis in the given {@link AxisOrder} equals the
	 *         given dimension vector, otherwise return false.
	 */
	public static boolean numAxisEqualsDimension(AxisOrder axisOrder, long[] dimension) {
		Objects.requireNonNull(axisOrder, "axisOrder cannot be null.");
		Objects.requireNonNull(dimension, "dimension cannot be null.");
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
		Objects.requireNonNull(metadata, "metadata cannot be null.");

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
		Objects.requireNonNull(metadata, "metadata cannot be null.");

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

	/**
	 * Create a metadata instance, which replicates the source metadata, but would
	 * correspond to only a plane of this image. For example, if AxisOrder is XYZ
	 * and dimension is [2, 3, 3], the new metadata would have AxisOrder XY and
	 * dimension [2, 3].
	 * 
	 * @param srcMetadata is the source metadata whose plane metadata we wish to
	 *                    extract.
	 * @throws IllegalArgumentException if the source metadata does is not at least
	 *                                  2D.
	 */
	public static IMetadata createPlaneMetadata(IMetadata srcMetadata) {
		Objects.requireNonNull(srcMetadata, "srcMetadata cannot be null.");

		long[] planeDim = MetadataUtil.getPlaneDim(srcMetadata);
		AxisOrder planeAxisOrder = AxisOrder.planeAxisOrder(srcMetadata.axisOrder());

		return new Metadata.MetadataBuilder(planeDim).axisOrder(planeAxisOrder).bitPerPixel(srcMetadata.bitPerPixel())
				.build();
	}

}