package fr.fresnel.fourPolar.algorithm.visualization.figures.polarization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.core.fourPolar.IIntensityVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
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
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;

public class PolarizationImageSetCompositesCreatorTest {
    /**
     * Note that the second pixel has to be put so that min an max of each plane is
     * not equal.
     */
    @Test
    public void create_TwoPixelPolarizationImagesRGComposite_ReturnsCorrectColorForComposite() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 2, 1, 1, 1 }).axisOrder(AxisOrder.XYCZT)
                .build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        long[] position0 = { 0, 0, 0, 0, 0 };
        _setPixel(pol0, position0, UINT16.MAX_VAL);
        _setPixel(pol45, position0, 0);
        _setPixel(pol90, position0, UINT16.MAX_VAL);
        _setPixel(pol135, position0, UINT16.MAX_VAL);

        DummyPolImgSet polImageSet = new DummyPolImgSet(pol0, pol45, pol90, pol135);

        PolarizationImageSetCompositesCreator creator = new PolarizationImageSetCompositesCreator(1, Color.Red,
                Color.Green);
        IPolarizationImageSetComposites composites = creator.create(polImageSet);

        Image<RGB16> pol45_to_0 = composites.getCompositeImage(RegistrationRule.Pol45_to_Pol0).getImage();
        Image<RGB16> pol90_to_0 = composites.getCompositeImage(RegistrationRule.Pol90_to_Pol0).getImage();
        Image<RGB16> pol135_to_0 = composites.getCompositeImage(RegistrationRule.Pol135_to_Pol0).getImage();

        RGB16 pol45_to_0_pixel = this._getPixel(pol45_to_0.getRandomAccess(), position0);
        RGB16 pol90_to_0_pixel = this._getPixel(pol90_to_0.getRandomAccess(), position0);
        RGB16 pol135_to_0_pixel = this._getPixel(pol135_to_0.getRandomAccess(), position0);

        assertTrue(pol45_to_0_pixel.getR() == 255 && pol45_to_0_pixel.getG() == 0 && pol45_to_0_pixel.getB() == 0);
        assertTrue(pol90_to_0_pixel.getR() == 255 && pol90_to_0_pixel.getG() == 255 && pol90_to_0_pixel.getB() == 0);
        assertTrue(pol135_to_0_pixel.getR() == 255 && pol135_to_0_pixel.getG() == 255 && pol135_to_0_pixel.getB() == 0);

    }

    @Test
    public void create_TwoPixelPolarizationImagesRBComposite_ReturnsCorrectColorForComposite() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 2, 1, 1, 1 }).axisOrder(AxisOrder.XYCZT)
                .build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        long[] position0 = { 0, 0, 0, 0, 0 };
        _setPixel(pol0, position0, UINT16.MAX_VAL);
        _setPixel(pol45, position0, 0);
        _setPixel(pol90, position0, UINT16.MAX_VAL);
        _setPixel(pol135, position0, UINT16.MAX_VAL);

        DummyPolImgSet polImageSet = new DummyPolImgSet(pol0, pol45, pol90, pol135);

        PolarizationImageSetCompositesCreator creator = new PolarizationImageSetCompositesCreator(1, Color.Red,
                Color.Blue);
        IPolarizationImageSetComposites composites = creator.create(polImageSet);

        Image<RGB16> pol45_to_0 = composites.getCompositeImage(RegistrationRule.Pol45_to_Pol0).getImage();
        Image<RGB16> pol90_to_0 = composites.getCompositeImage(RegistrationRule.Pol90_to_Pol0).getImage();
        Image<RGB16> pol135_to_0 = composites.getCompositeImage(RegistrationRule.Pol135_to_Pol0).getImage();

        RGB16 pol45_to_0_pixel = this._getPixel(pol45_to_0.getRandomAccess(), position0);
        RGB16 pol90_to_0_pixel = this._getPixel(pol90_to_0.getRandomAccess(), position0);
        RGB16 pol135_to_0_pixel = this._getPixel(pol135_to_0.getRandomAccess(), position0);

        assertTrue(pol45_to_0_pixel.getR() == 255 && pol45_to_0_pixel.getG() == 0 && pol45_to_0_pixel.getB() == 0);
        assertTrue(pol90_to_0_pixel.getR() == 255 && pol90_to_0_pixel.getG() == 0 && pol90_to_0_pixel.getB() == 255);
        assertTrue(pol135_to_0_pixel.getR() == 255 && pol135_to_0_pixel.getG() == 0 && pol135_to_0_pixel.getB() == 255);

    }

    private void _setPixel(Image<UINT16> image, long[] position, int value) {
        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        ra.setPosition(position);
        ra.getPixel();

        Pixel<UINT16> pixel = new Pixel<>(new UINT16(value));
        ra.setPixel(pixel);
    }

    private RGB16 _getPixel(IPixelRandomAccess<RGB16> ra, long[] position) {
        ra.setPosition(position);

        return ra.getPixel().value();
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
        return new ICapturedImageFileSet() {

            @Override
            public boolean hasLabel(String label) {
                return false;
            }

            @Override
            public Cameras getnCameras() {
                return null;
            }

            @Override
            public String getSetName() {
                return null;
            }

            @Override
            public Iterator<ICapturedImageFile> getIterator() {
                return null;
            }

            @Override
            public ICapturedImageFile[] getFile(String label) {
                return null;
            }

            @Override
            public int[] getChannels() {
                return null;
            }

            @Override
            public boolean deepEquals(ICapturedImageFileSet fileset) {
                return false;
            }
        };
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