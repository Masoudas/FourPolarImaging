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

public class OneCameraPercentileDarkBackgroundEstimatorTest {
    @Test
    public void estimate_FourPolsWithOnePixel_ReturnsSameDarkBackgroundForAll() throws CannotFormPolarizationImageSet {
        long[] dim1 = { 1, 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        this._setPixel(pol0, 1);
        this._setPixel(pol45, 2);
        this._setPixel(pol90, 3);
        this._setPixel(pol135, 4);

        OCDummyPolImgSet imageSet = new OCDummyPolImgSet(pol0, pol45, pol90, pol135);
        
        OneCameraPercentileDarkBackgroundEstimator estimator = new OneCameraPercentileDarkBackgroundEstimator(25);

        IChannelDarkBackground background = estimator.estimate(imageSet);

        // Regardless of the method chosen for estimation of percentile, it should be
        // between 1 and 2;
        assertTrue(background.getBackgroundLevel(Polarization.pol0) < 2
                && background.getBackgroundLevel(Polarization.pol0) > 1);

        assertTrue(background.getBackgroundLevel(Polarization.pol0) == background.getBackgroundLevel(Polarization.pol45)
                && background.getBackgroundLevel(Polarization.pol45) == background
                        .getBackgroundLevel(Polarization.pol90)
                && background.getBackgroundLevel(Polarization.pol45) == background
                        .getBackgroundLevel(Polarization.pol135));

    }

    private void _setPixel(Image<UINT16> pol0, int value) {
        IPixelCursor<UINT16> cursor = pol0.getCursor();

        IPixel<UINT16> pixel = cursor.next();
        pixel.value().set(value);
        cursor.setPixel(pixel);

    }
}

class OCDummyPolImgSet implements IPolarizationImageSet {
    Image<UINT16> pol0;
    Image<UINT16> pol45;
    Image<UINT16> pol90;
    Image<UINT16> pol135;

    OCDummyPolImgSet(Image<UINT16> pol0, Image<UINT16> pol45, Image<UINT16> pol90, Image<UINT16> pol135) {
        this.pol0 = pol0;
        this.pol45 = pol45;
        this.pol90 = pol90;
        this.pol135 = pol135;
    }

    @Override
    public IPolarizationImage getPolarizationImage(Polarization pol) {
        switch (pol) {
            case pol0:
                return new OCPImg(pol0);

            case pol45:
                return new OCPImg(pol45);

            case pol90:
                return new OCPImg(pol90);

            case pol135:
                return new OCPImg(pol135);

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

class OCPImg implements IPolarizationImage {
    Image<UINT16> image;

    OCPImg(Image<UINT16> image) {
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