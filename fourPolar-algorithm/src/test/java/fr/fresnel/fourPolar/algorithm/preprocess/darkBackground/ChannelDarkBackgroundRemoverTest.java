package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground;

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

/**
 * Removes the dark background from the given polarization set.
 */
public class ChannelDarkBackgroundRemoverTest {
    /**
     * Intentially, to test if pixels that have intensity below dark background are
     * set to zero, pol0 image has noise greater than all pixels, to ensure that
     * it's set to zero after background removal.
     * 
     */
    @Test
    public void remove_SinglePixelPolImagesDifferentNoise_RemovesBackground() throws CannotFormPolarizationImageSet {
        long[] dim1 = { 1, 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        this._setPixel(pol0, 2);
        this._setPixel(pol45, 3);
        this._setPixel(pol90, 4);
        this._setPixel(pol135, 5);

        DummyChannelBackground darkBackground = new DummyChannelBackground(1, 3, 2, 3, 4);

        IPolarizationImageSet imageSet = new PolarizationImageSetBuilder(1).channel(1).fileSet(new CDummyFileSet())
                .pol0(pol0).pol45(pol45).pol90(pol90).pol135(pol135).build();

        IChannelDarkBackgroundRemover remover = ChannelDarkBackgroundRemover.create(darkBackground);
        remover.remove(imageSet);

        assertTrue(this._checkPixel(pol0, 0));
        assertTrue(this._checkPixel(pol45, 1));
        assertTrue(this._checkPixel(pol90, 1));
        assertTrue(this._checkPixel(pol135, 1));

    }

    private void _setPixel(Image<UINT16> pol, int value) {
        for (IPixelCursor<UINT16> cursor = pol.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();
            pixel.value().set(value);
            cursor.setPixel(pixel);
        }

    }

    private boolean _checkPixel(Image<UINT16> pol, int value) {
        if (!pol.getCursor().hasNext()) {
            return false;
        }
        boolean equals = true;
        for (IPixelCursor<UINT16> cursor = pol.getCursor(); cursor.hasNext() && equals;) {
            equals = cursor.next().value().get() == value;
        }

        return equals;
    }

}

class DummyChannelBackground implements IChannelDarkBackground {
    private final double _pol0;
    private final double _pol45;
    private final double _pol90;
    private final double _pol135;

    private final int _channel;

    public DummyChannelBackground(int channel, double pol0, double pol45, double pol90, double pol135) {
        this._channel = channel;

        _pol0 = pol0;
        _pol45 = pol45;
        _pol90 = pol90;
        _pol135 = pol135;
    }

    @Override
    public double getBackgroundLevel(Polarization polarization) {
        switch (polarization) {
            case pol0:
                return _pol0;

            case pol45:
                return _pol45;

            case pol90:
                return _pol90;

            case pol135:
                return _pol135;

            default:
                return -1;
        }
    }

    @Override
    public int channel() {
        return this._channel;
    }

    @Override
    public String estimationMethod() {
        return null;
    }

}

class CDummyFileSet implements ICapturedImageFileSet {

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