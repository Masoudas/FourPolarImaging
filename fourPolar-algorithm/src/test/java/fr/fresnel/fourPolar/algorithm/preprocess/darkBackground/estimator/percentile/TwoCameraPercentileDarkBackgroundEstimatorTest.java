package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSetBuilder;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

public class TwoCameraPercentileDarkBackgroundEstimatorTest {
    @Test
    public void estimate_FourPolsWithOnePixel_ReturnsSameDarkBackgroundForAll() throws CannotFormPolarizationImageSet {
        long[] dim1 = { 2, 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        this._setPixel(pol0, 1, 2);
        this._setPixel(pol45, 5, 6);
        this._setPixel(pol90, 3, 4);
        this._setPixel(pol135, 7, 8);

        IPolarizationImageSet imageSet = new PolarizationImageSetBuilder(1).channel(1).fileSet(new DummyFileSet())
                .pol0(pol0).pol45(pol45).pol90(pol90).pol135(pol135).build();

        TwoCameraPercentileDarkBackgroundEstimator estimator = new TwoCameraPercentileDarkBackgroundEstimator(25);

        IChannelDarkBackground background = estimator.estimate(imageSet);

        // Regardless of the method chosen for estimation of percentile, it should be
        // between 1 and 2 for pol0 and 5 and 6 for pol45 ;
        assertTrue(background.getBackgroundLevel(Polarization.pol0) < 2
                && background.getBackgroundLevel(Polarization.pol0) > 1);

        assertTrue(background.getBackgroundLevel(Polarization.pol45) < 6
                && background.getBackgroundLevel(Polarization.pol45) > 5);

        assertTrue(background.getBackgroundLevel(Polarization.pol0) == background.getBackgroundLevel(Polarization.pol90)
                && background.getBackgroundLevel(Polarization.pol45) == background
                        .getBackgroundLevel(Polarization.pol135)
                && background.getBackgroundLevel(Polarization.pol45) != background
                        .getBackgroundLevel(Polarization.pol90));

    }

    private void _setPixel(Image<UINT16> pol0, int value1, int value2) {
        IPixelCursor<UINT16> cursor = pol0.getCursor();

        IPixel<UINT16> pixel1 = cursor.next();
        pixel1.value().set(value1);
        cursor.setPixel(pixel1);

        IPixel<UINT16> pixel2 = cursor.next();
        pixel2.value().set(value2);
        cursor.setPixel(pixel2);
    }
}

class DummyFileSet implements ICapturedImageFileSet {

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return null;
    }

    @Override
    public String getSetName() {
        return null;
    }

    @Override
    public Cameras getnCameras() {
        return null;
    }

    @Override
    public boolean hasLabel(String label) {
        return false;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return false;
    }

}