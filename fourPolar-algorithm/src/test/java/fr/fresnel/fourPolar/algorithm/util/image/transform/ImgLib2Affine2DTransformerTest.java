package fr.fresnel.fourPolar.algorithm.util.image.transform;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.util.transform.Affine2D;
import javassist.tools.reflect.CannotCreateException;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class ImgLib2Affine2DTransformerTest {
    @Test
    public void realign_Transform2DImage1ToRightAnd1ToBottom_ReturnsCorrectImage() throws CannotCreateException {
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(new UnsignedShortType()).create(2, 2);

        _setPixel(img, new long[] { 0, 0 }, 1);

        Affine2D transform2d = new Affine2D();
        transform2d.set(0, 2, 1);
        transform2d.set(1, 2, 1);

        ImgLib2Affine2DTransformer.applyfromWithNearestNeighbor(img, transform2d);

        assertTrue(_getPixel(img, new long[] { 0, 0 }) == 0);
        assertTrue(_getPixel(img, new long[] { 0, 1 }) == 0);
        assertTrue(_getPixel(img, new long[] { 1, 0 }) == 0);
        assertTrue(_getPixel(img, new long[] { 1, 1 }) == 1);

    }

    @Test
    public void realign_Transform3D1ToRightAnd1ToBottom_TranslatesEveryplane() throws CannotCreateException {
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(new UnsignedShortType()).create(2, 2, 2);

        _setPixel(img, new long[] { 0, 0, 0 }, 1);
        _setPixel(img, new long[] { 0, 0, 1 }, 1);

        Affine2D transform2d = new Affine2D();
        transform2d.set(0, 2, 1);
        transform2d.set(1, 2, 1);

        ImgLib2Affine2DTransformer.applyfromWithNearestNeighbor(img, transform2d);

        assertTrue(_getPixel(img, new long[] { 0, 0, 0 }) == 0);
        assertTrue(_getPixel(img, new long[] { 0, 1, 0 }) == 0);
        assertTrue(_getPixel(img, new long[] { 1, 0, 0 }) == 0);
        assertTrue(_getPixel(img, new long[] { 1, 1, 0 }) == 1);

        assertTrue(_getPixel(img, new long[] { 0, 0, 1 }) == 0);
        assertTrue(_getPixel(img, new long[] { 0, 1, 1 }) == 0);
        assertTrue(_getPixel(img, new long[] { 1, 0, 1 }) == 0);
        assertTrue(_getPixel(img, new long[] { 1, 1, 1 }) == 1);

    }

    @Test
    public void realign_2DImageRotate90Degrees_ReturnsCorrectImage() throws CannotCreateException {
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(new UnsignedShortType()).create(2, 2, 1);

        _setPixel(img, new long[] { 1, 0, 0 }, 1);

        Affine2D transform2d = new Affine2D();
        transform2d.set(0, 1, -1);
        transform2d.set(1, 0, 1);
        transform2d.set(1, 1, 0);
        transform2d.set(0, 0, 0);

        ImgLib2Affine2DTransformer.applyfromWithNearestNeighbor(img, transform2d);

        assertTrue(_getPixel(img, new long[] { 0, 0, 0 }) == 0);
        assertTrue(_getPixel(img, new long[] { 0, 1, 0 }) == 1);
        assertTrue(_getPixel(img, new long[] { 1, 0, 0 }) == 0);
        assertTrue(_getPixel(img, new long[] { 1, 1, 0 }) == 0);

    }

    private <T extends RealType<T>> void _setPixel(Img<T> image, long[] position, int value) {
        RandomAccess<T> ra = image.randomAccess();
        ra.setPosition(position);
        ra.get().setReal(value);

    }

    private <T extends RealType<T>> double _getPixel(Img<T> image, long[] position) {
        RandomAccess<T> ra = image.randomAccess();
        ra.setPosition(position);

        return ra.get().getRealDouble();
    }

}
