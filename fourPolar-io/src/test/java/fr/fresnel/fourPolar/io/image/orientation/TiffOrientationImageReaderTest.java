package fr.fresnel.fourPolar.io.image.orientation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImageRandomAccess;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOFloat32TiffWriter;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageFileSet;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageInDegreeFileSet;
import io.scif.img.ImgIOException;
import io.scif.img.ImgSaver;
import net.imglib2.exception.IncompatibleTypeException;

public class TiffOrientationImageReaderTest {
    final private static File _root = new File(TiffOrientationImageReaderTest.class.getResource("").getPath(),
            "Reader");

    @BeforeAll
    public static void createPath() {
        _root.mkdirs();
    }

    /**
     * The premise of the test is write and then read an orientation image.
     * 
     * @throws ConverterToImgLib2NotFound
     * @throws IncompatibleTypeException
     * @throws ImgIOException
     * 
     */
    @Test
    public void read_PolarizationImageSetFromDisk_ReadImagesHaveSameDimensionAsDisk() throws IOException,
            CannotFormOrientationImage, ImgIOException, IncompatibleTypeException, ConverterToImgLib2NotFound {
        // Create an alias for a captured set.
        String setName = "TestCapturedSet";
        int channel = 1;
        ReaderDummyCapturedImageFileSet fileSet = new ReaderDummyCapturedImageFileSet(setName);
        TiffOrientationImageFileSet fSet = new TiffOrientationImageFileSet(_root, fileSet, channel);

        // Create orientation image to be written.
        long[] dim = { 2, 1, 1, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        long[] pos0 = new long[] { 0, 0, 0, 0, 0 };
        long[] pos1 = new long[] { 1, 0, 0, 0, 0 };

        _setPixel(rho, pos0, 0);
        _setPixel(rho, pos1, (float) IOrientationVector.MAX_Rho);

        _setPixel(delta, pos0, (float) Math.PI / 4);
        _setPixel(delta, pos1, (float) (3 * Math.PI / 4));

        _setPixel(eta, pos0, 0);
        _setPixel(eta, pos1, (float) IOrientationVector.MAX_Eta);

        // Write the orientation images manually.
        ImgSaver saver = new ImgSaver();
        saver.saveImg(fSet.getFile(OrientationAngle.rho).getAbsolutePath(),
                ImageToImgLib2Converter.getImg(rho, Float32.zero()));
        saver.saveImg(fSet.getFile(OrientationAngle.delta).getAbsolutePath(),
                ImageToImgLib2Converter.getImg(delta, Float32.zero()));
        saver.saveImg(fSet.getFile(OrientationAngle.eta).getAbsolutePath(),
                ImageToImgLib2Converter.getImg(eta, Float32.zero()));

        ImageFactory factory = new ImgLib2ImageFactory();
        IOrientationImageReader reader = new TiffOrientationImageReader(factory, channel);
        IOrientationImage imageSet = reader.read(_root, fileSet, channel);

        assertTrue(Arrays.equals(imageSet.getAngleImage(OrientationAngle.rho).getImage().getMetadata().getDim(), dim)
                && Arrays.equals(imageSet.getAngleImage(OrientationAngle.delta).getImage().getMetadata().getDim(), dim)
                && Arrays.equals(imageSet.getAngleImage(OrientationAngle.eta).getImage().getMetadata().getDim(), dim));

        assertTrue(_isOrientationVectorEqualTo(imageSet, pos0, new OrientationVector(0, Math.PI / 4, 0)));
        assertTrue(_isOrientationVectorEqualTo(imageSet, pos1,
                new OrientationVector(IOrientationVector.MAX_Rho, 3 * Math.PI / 4, IOrientationVector.MAX_Eta)));

    }

    @Test
    public void readFromDegrees_ManuallyMadeAngleImagesInDegrees_ReadImagesHaveSameDimensionAsDisk()
            throws ImgIOException, IncompatibleTypeException, ConverterToImgLib2NotFound, IOException,
            CannotFormOrientationImage {
        String setName = "TestCapturedSet";
        int channel = 1;
        ReaderDummyCapturedImageFileSet fileSet = new ReaderDummyCapturedImageFileSet(setName);
        TiffOrientationImageInDegreeFileSet fSet = new TiffOrientationImageInDegreeFileSet(_root, fileSet, channel);

        // Create orientation image to be written.
        long[] dim = { 2, 2, 1, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        long[] pos0 = new long[] { 0, 0, 0, 0, 0 };
        long[] pos1 = new long[] { 1, 0, 0, 0, 0 };

        _setPixel(rho, pos0, 0);
        _setPixel(rho, pos1, 180);

        _setPixel(delta, pos0, 45);
        _setPixel(delta, pos1, 135);

        _setPixel(eta, pos0, 0);
        _setPixel(eta, pos1, 90);

        ImgSaver saver = new ImgSaver();
        saver.saveImg(fSet.getFile(OrientationAngle.rho).getAbsolutePath(),
                ImageToImgLib2Converter.getImg(rho, Float32.zero()));
        saver.saveImg(fSet.getFile(OrientationAngle.delta).getAbsolutePath(),
                ImageToImgLib2Converter.getImg(delta, Float32.zero()));
        saver.saveImg(fSet.getFile(OrientationAngle.eta).getAbsolutePath(),
                ImageToImgLib2Converter.getImg(eta, Float32.zero()));

        TiffOrientationImageReader reader = new TiffOrientationImageReader(new ImgLib2ImageFactory(), 1);

        IOrientationImage orientationImage = reader.readFromDegrees(_root, fileSet, 1);

        assertTrue(_isOrientationVectorEqualTo(orientationImage, pos0, new OrientationVector(0, Math.PI / 4, 0)));
        assertTrue(_isOrientationVectorEqualTo(orientationImage, pos1,
                new OrientationVector(IOrientationVector.MAX_Rho, 3 * Math.PI / 4, IOrientationVector.MAX_Eta)));

    }

    private void _setPixel(Image<Float32> image, long[] position, float value) {
        IPixelRandomAccess<Float32> ra = image.getRandomAccess();

        ra.setPosition(position);
        IPixel<Float32> pixel = ra.getPixel();

        pixel.value().set(value);
        ra.setPixel(pixel);
    }

    private boolean _isOrientationVectorEqualTo(IOrientationImage image, long[] position, IOrientationVector vector) {
        IOrientationImageRandomAccess ra = image.getRandomAccess();
        ra.setPosition(position);

        IOrientationVector diskVector = ra.getOrientation();

        // Check the value is close enough
        return Math.abs(diskVector.getAngle(OrientationAngle.rho) - vector.getAngle(OrientationAngle.rho)) < 1e-6
                && Math.abs(
                        diskVector.getAngle(OrientationAngle.delta) - vector.getAngle(OrientationAngle.delta)) < 1e-6
                && Math.abs(diskVector.getAngle(OrientationAngle.eta) - vector.getAngle(OrientationAngle.eta)) < 1e-6;
    }

}

class ReaderDummyCapturedImageFileSet implements ICapturedImageFileSet {
    String setName;

    public ReaderDummyCapturedImageFileSet(String setName) {
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
