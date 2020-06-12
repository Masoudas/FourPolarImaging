package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;

import net.imglib2.realtransform.AffineGet;

class ImgLib2ShapeUtils {

	public static long[] applyAffineToLong(AffineGet affineTransform, long[] point) {
		double[] pointAsDouble = Arrays.stream(point).mapToDouble((t) -> t).toArray();
		double[] resultAsDouble = new double[pointAsDouble.length];

		affineTransform.apply(pointAsDouble, resultAsDouble);
		return Arrays.stream(resultAsDouble).mapToLong((t) -> (long) t).toArray();
	}

}