package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.fourPolar.IIntensityVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

public class FourCameraPercentileDarkBackgroundEstimatorTest {
    @Test
    public void estimate_FourPolsWithOnePixel_ReturnsSameDarkBackgroundForAll() throws CannotFormPolarizationImageSet {
        long[] dim1 = { 4, 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        this._setPixel(pol0, 1, 2, 3, 4);
        this._setPixel(pol45, 5, 6, 7, 8);
        this._setPixel(pol90, 9, 10, 11, 12);
        this._setPixel(pol135, 13, 14, 15, 16);

        FCDummyPolImgSet polSet = new FCDummyPolImgSet(pol0, pol45, pol90, pol135);

        FourCameraPercentileDarkBackgroundEstimator estimator = new FourCameraPercentileDarkBackgroundEstimator(25);

        IChannelDarkBackground background = estimator.estimate(polSet);

        // Regardless of the method chosen for estimation of percentile, it should be
        // between 1 and 2 for pol0 and 5 and 6 for pol45 ;
        assertTrue(background.getBackgroundLevel(Polarization.pol0) < 2
                && background.getBackgroundLevel(Polarization.pol0) > 1);

        assertTrue(background.getBackgroundLevel(Polarization.pol45) < 6
                && background.getBackgroundLevel(Polarization.pol45) > 5);

        assertTrue(background.getBackgroundLevel(Polarization.pol90) < 10
                && background.getBackgroundLevel(Polarization.pol90) > 9);

        assertTrue(background.getBackgroundLevel(Polarization.pol135) < 14
                && background.getBackgroundLevel(Polarization.pol135) > 13);

    }

    private void _setPixel(Image<UINT16> pol0, int value1, int value2, int value3, int value4) {
        IPixelCursor<UINT16> cursor = pol0.getCursor();

        IPixel<UINT16> pixel1 = cursor.next();
        pixel1.value().set(value1);
        cursor.setPixel(pixel1);

        IPixel<UINT16> pixel2 = cursor.next();
        pixel2.value().set(value2);
        cursor.setPixel(pixel2);

        IPixel<UINT16> pixel3 = cursor.next();
        pixel3.value().set(value3);
        cursor.setPixel(pixel3);

        IPixel<UINT16> pixel4 = cursor.next();
        pixel4.value().set(value4);
        cursor.setPixel(pixel4);

    }
}

class FCDummyPolImgSet implements IPolarizationImageSet {
    Image<UINT16> pol0;
    Image<UINT16> pol45;
    Image<UINT16> pol90;
    Image<UINT16> pol135;

    FCDummyPolImgSet(Image<UINT16> pol0, Image<UINT16> pol45, Image<UINT16> pol90, Image<UINT16> pol135) {
        this.pol0 = pol0;
        this.pol45 = pol45;
        this.pol90 = pol90;
        this.pol135 = pol135;
    }

    @Override
    public IPolarizationImage getPolarizationImage(Polarization pol) {
        switch (pol) {
            case pol0:
                return new FCPImg(pol0);

            case pol45:
                return new FCPImg(pol45);

            case pol90:
                return new FCPImg(pol90);

            case pol135:
                return new FCPImg(pol135);

            default:
                return null;
        }
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return null;
    }

    @Override
    public IIntensityVectorIterator getIterator() {
        return null;
    }

    @Override
    public int channel() {
        return 1;
    }

}

class FCPImg implements IPolarizationImage {
    Image<UINT16> image;

    FCPImg(Image<UINT16> image) {
        this.image = image;
    }

    @Override
    public Polarization getPolarization() {
        return null;
    }

    @Override
    public Image<UINT16> getImage() {
        return image;
    }
};