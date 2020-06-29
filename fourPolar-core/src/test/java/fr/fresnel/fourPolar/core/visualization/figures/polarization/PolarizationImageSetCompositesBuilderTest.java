package fr.fresnel.fourPolar.core.visualization.figures.polarization;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

public class PolarizationImageSetCompositesBuilderTest {
    @Test
    public void build_SinglePixelCompositeImages_CompositeImageAreOnePixel() {
        int channel = 1;
        long[] dim = { 1, 1, 1, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();

        String setName_alias = "TestSet";
        BuilderDummyCapturedImageFileSet fSet = new BuilderDummyCapturedImageFileSet(setName_alias);

        PolarizationImageSetCompositesBuilder builder = new PolarizationImageSetCompositesBuilder(channel);
        builder.fileSet(fSet);

        for (RegistrationRule rule : RegistrationRule.values()) {
            Image<ARGB8> ruleImage = new ImgLib2ImageFactory().create(metadata, ARGB8.zero());
            builder.channel(1).compositeImage(rule, ruleImage);

        }

        IPolarizationImageSetComposites composites = builder.build();
        for (RegistrationRule rule : RegistrationRule.values()) {
            assertArrayEquals(composites.getCompositeImage(rule).getImage().getMetadata().getDim(), dim);
        }
        assertTrue(composites.channel() == channel);
        assertTrue(composites.getFileSet().get() == fSet);
    }

    @Test
    public void build_RepetitiveBuild_CreatesSeparateClasses() {
        int channel = 1;
        long[] dim = { 1, 1, 1, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();

        PolarizationImageSetCompositesBuilder builder = new PolarizationImageSetCompositesBuilder(channel);

        int iterations = 2;
        IPolarizationImageSetComposites[] composites = new IPolarizationImageSetComposites[iterations];
        for (int iteration = 0; iteration < iterations; iteration++) {
            String setName_alias = "TestSet";
            BuilderDummyCapturedImageFileSet fSet = new BuilderDummyCapturedImageFileSet(setName_alias);

            builder.fileSet(fSet);

            for (RegistrationRule rule : RegistrationRule.values()) {
                Image<ARGB8> ruleImage = new ImgLib2ImageFactory().create(metadata, ARGB8.zero());
                builder.channel(1).compositeImage(rule, ruleImage);

            }

            composites[iteration] = builder.build();

        }

        assertTrue(composites[0] != composites[1]);
    }
}

class BuilderDummyCapturedImageFileSet implements ICapturedImageFileSet {
    String setName;

    public BuilderDummyCapturedImageFileSet(String setName) {
        this.setName = setName;
    }

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return null;
    }

    @Override
    public String getSetName() {
        return this.setName;
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