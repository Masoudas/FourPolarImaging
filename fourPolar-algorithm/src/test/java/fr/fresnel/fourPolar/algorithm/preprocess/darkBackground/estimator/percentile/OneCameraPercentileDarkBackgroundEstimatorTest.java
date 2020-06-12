package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

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

        IPolarizationImageSet imageSet = new PolarizationImageSetBuilder(1).channel(1).fileSet(new ODummyFileSet())
                .pol0(pol0).pol45(pol45).pol90(pol90).pol135(pol135).build();

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

class ODummyFileSet implements ICapturedImageFileSet {

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

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        return null;
    }

    @Override
    public int[] getChannels() {
        return null;
    }

}