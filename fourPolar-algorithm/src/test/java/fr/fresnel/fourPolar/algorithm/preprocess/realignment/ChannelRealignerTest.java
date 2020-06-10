package fr.fresnel.fourPolar.algorithm.preprocess.realignment;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.fourPolar.IIntensityVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.transform.Affine2D;

public class ChannelRealignerTest {
    @Test
    public void realign_Transform2DImage1ToRightAnd1ToBottom_ReturnsCorrectImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        _setPixel(pol0, new long[] { 0, 0 }, 1);
        _setPixel(pol45, new long[] { 0, 0 }, 1);
        _setPixel(pol90, new long[] { 0, 0 }, 1);
        _setPixel(pol135, new long[] { 0, 0 }, 1);

        Affine2D transform2d = new Affine2D();
        transform2d.set(0, 2, 1);
        transform2d.set(1, 2, 1);

        IChannelRealigner realigner = ChannelRealigner.create(new DummyChannelRegistrationResult(transform2d));

        DummyPolImgSet polSet = new DummyPolImgSet(pol0, pol45, pol90, pol135);
        realigner.realign(polSet);

        // Doesn't make a difference which image we get here, all are the same.
        assertTrue(
                _getPixel(polSet.getPolarizationImage(Polarization.pol45).getImage(), new long[] { 1, 1 }).get() == 1);
        assertTrue(
                _getPixel(polSet.getPolarizationImage(Polarization.pol90).getImage(), new long[] { 1, 1 }).get() == 1);
        assertTrue(
                _getPixel(polSet.getPolarizationImage(Polarization.pol135).getImage(), new long[] { 1, 1 }).get() == 1);

    }

    @Test
    public void realign_Transform5D1ToRightAnd1ToBottom_ReturnsCorrectImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 1, 1, 1 }).axisOrder(AxisOrder.XYCZT)
                .build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        _setPixel(pol0, new long[] { 0, 0, 0, 0, 0 }, 1);
        _setPixel(pol45, new long[] { 0, 0, 0, 0, 0 }, 1);
        _setPixel(pol90, new long[] { 0, 0, 0, 0, 0 }, 1);
        _setPixel(pol135, new long[] { 0, 0, 0, 0, 0 }, 1);

        Affine2D transform2d = new Affine2D();
        transform2d.set(0, 2, 1);
        transform2d.set(1, 2, 1);

        IChannelRealigner realigner = ChannelRealigner.create(new DummyChannelRegistrationResult(transform2d));

        DummyPolImgSet polSet = new DummyPolImgSet(pol0, pol45, pol90, pol135);
        realigner.realign(polSet);

        // Doesn't make a difference which image we get here, all are the same.
        assertTrue(_getPixel(polSet.getPolarizationImage(Polarization.pol45).getImage(), new long[] { 1, 1, 0, 0, 0 })
                .get() == 1);
        assertTrue(_getPixel(polSet.getPolarizationImage(Polarization.pol90).getImage(), new long[] { 1, 1, 0, 0, 0 })
                .get() == 1);
        assertTrue(_getPixel(polSet.getPolarizationImage(Polarization.pol135).getImage(), new long[] { 1, 1, 0, 0, 0 })
                .get() == 1);

    }

    private void _setPixel(Image<UINT16> image, long[] position, int value) {
        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        ra.setPosition(position);
        ra.getPixel();

        Pixel<UINT16> pixel = new Pixel<>(new UINT16(value));
        ra.setPixel(pixel);
    }

    private UINT16 _getPixel(Image<UINT16> image, long[] position) {
        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        ra.setPosition(position);

        return ra.getPixel().value();
    }

}

class DummyChannelRegistrationResult implements IChannelRegistrationResult {
    private Affine2D transform2d;

    public DummyChannelRegistrationResult(Affine2D transform2d) {
        this.transform2d = transform2d;
    }

    @Override
    public boolean registrationSuccessful(RegistrationRule rule) {
        return false;
    }

    @Override
    public Affine2D getAffineTransform(RegistrationRule rule) {
        return this.transform2d;
    }

    @Override
    public double error(RegistrationRule rule) {
        return 0;
    }

    @Override
    public String getDescription(RegistrationRule rule) {
        return null;
    }

    @Override
    public int channel() {
        return 0;
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