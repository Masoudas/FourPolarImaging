package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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

public class CameraPercentileBackgroundEstimatorTest {
    @Test
    public void estimate_SinglePolCamera_Returns50PercentileOfFirstPlane() {
        long[] dim1 = { 4, 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        this._setPixel(pol0, 1, 2, 3, 4);

        DummyPolImgSet polSet = new DummyPolImgSet(pol0, pol45, pol90, pol135);

        assertTrue((int) CameraPercentileBackgroundEstimator.estimate(polSet, new Polarization[] { Polarization.pol0 },
                50) == 2);

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

class DummyPolImgSet implements IPolarizationImageSet {
    Image<UINT16> pol0;
    Image<UINT16> pol45;
    Image<UINT16> pol90;
    Image<UINT16> pol135;

    DummyPolImgSet(Image<UINT16> pol0, Image<UINT16> pol45, Image<UINT16> pol90, Image<UINT16> pol135) {
        this.pol0 = pol0;
        this.pol45 = pol45;
        this.pol90 = pol90;
        this.pol135 = pol135;
    }

    @Override
    public IPolarizationImage getPolarizationImage(Polarization pol) {
        switch (pol) {
            case pol0:
                return new PImg(pol0);

            case pol45:
                return new PImg(pol45);

            case pol90:
                return new PImg(pol90);

            case pol135:
                return new PImg(pol135);

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

class PImg implements IPolarizationImage {
    Image<UINT16> image;

    PImg(Image<UINT16> image) {
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