package fr.fresnel.fourPolar.io.image.orientation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageFileSet;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageInDegreeFileSet;
import io.scif.img.ImgOpener;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.FloatType;

public class TiffOrientationImageWriterTest {
    final private static File _root = new File(TiffOrientationImageWriterTest.class.getResource("").getPath(),
            "TiffOrientationImageSetWriter");

    @Test
    public void write_ImgLib2ORientationImage_WritesIntoTheTargetFolder()
            throws CannotFormOrientationImage, IOException {
        int channel = 1;
        String setNameAlias = "testSet";

        WriterDummyCapturedImageFileSet fileSet = new WriterDummyCapturedImageFileSet(setNameAlias);

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

        IOrientationImage imageSet = OrientationImageFactory.create(fileSet, channel, rho, delta, eta);

        IOrientationImageWriter writer = new TiffOrientationImageWriter();
        writer.write(_root, imageSet);

        TiffOrientationImageFileSet fSet = new TiffOrientationImageFileSet(_root, fileSet, channel);
        assertTrue(fSet.getFile(OrientationAngle.rho).exists() && fSet.getFile(OrientationAngle.delta).exists()
                && fSet.getFile(OrientationAngle.eta).exists());

        ImgOpener opener = new ImgOpener();
        Img<FloatType> diskRho = opener.openImgs(fSet.getFile(OrientationAngle.rho).getAbsolutePath(),
                new ArrayImgFactory<FloatType>(new FloatType())).get(0);
        Img<FloatType> diskDelta = opener.openImgs(fSet.getFile(OrientationAngle.delta).getAbsolutePath(),
                new ArrayImgFactory<FloatType>(new FloatType())).get(0);
        Img<FloatType> diskEta = opener.openImgs(fSet.getFile(OrientationAngle.eta).getAbsolutePath(),
                new ArrayImgFactory<FloatType>(new FloatType())).get(0);

        assertTrue(_isOrientationVectorEqualTo(diskRho, diskDelta, diskEta, pos0, 0, (float) Math.PI / 4, 0));
        assertTrue(_isOrientationVectorEqualTo(diskRho, diskDelta, diskEta, pos1, (float) IOrientationVector.MAX_Rho,
                (float) (3 * Math.PI / 4), (float) IOrientationVector.MAX_Eta));
    }

    @Test
    public void writeInDegrees_ManuallyMadeAngleImagesInDegrees_ReadImagesHaveSameDimensionAsDisk()
            throws CannotFormOrientationImage, IOException {
        int channel = 1;
        String setNameAlias = "testSet";

        WriterDummyCapturedImageFileSet fileSet = new WriterDummyCapturedImageFileSet(setNameAlias);

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

        IOrientationImage imageSet = OrientationImageFactory.create(fileSet, channel, rho, delta, eta);

        IOrientationImageWriter writer = new TiffOrientationImageWriter();
        writer.writeInDegrees(_root, imageSet);

        TiffOrientationImageInDegreeFileSet fSet = new TiffOrientationImageInDegreeFileSet(_root, fileSet, channel);
        assertTrue(fSet.getFile(OrientationAngle.rho).exists() && fSet.getFile(OrientationAngle.delta).exists()
                && fSet.getFile(OrientationAngle.eta).exists());

        ImgOpener opener = new ImgOpener();
        Img<FloatType> diskRho = opener.openImgs(fSet.getFile(OrientationAngle.rho).getAbsolutePath(),
                new ArrayImgFactory<FloatType>(new FloatType())).get(0);
        Img<FloatType> diskDelta = opener.openImgs(fSet.getFile(OrientationAngle.delta).getAbsolutePath(),
                new ArrayImgFactory<FloatType>(new FloatType())).get(0);
        Img<FloatType> diskEta = opener.openImgs(fSet.getFile(OrientationAngle.eta).getAbsolutePath(),
                new ArrayImgFactory<FloatType>(new FloatType())).get(0);

        assertTrue(_isOrientationVectorEqualTo(diskRho, diskDelta, diskEta, pos0, 0, 45, 0));
        assertTrue(_isOrientationVectorEqualTo(diskRho, diskDelta, diskEta, pos1, 180, 135, 90));
    }

    private void _setPixel(Image<Float32> image, long[] position, float value) {
        IPixelRandomAccess<Float32> ra = image.getRandomAccess();

        ra.setPosition(position);
        IPixel<Float32> pixel = ra.getPixel();

        pixel.value().set(value);
        ra.setPixel(pixel);
    }

    private boolean _isOrientationVectorEqualTo(Img<FloatType> diskRho, Img<FloatType> diskDelta,
            Img<FloatType> diskEta, long[] position, float rho, float delta, float eta) {
        RandomAccess<FloatType> ra_Rho = diskRho.randomAccess();
        RandomAccess<FloatType> ra_Delta = diskDelta.randomAccess();
        RandomAccess<FloatType> ra_Eta = diskEta.randomAccess();

        ra_Rho.setPosition(position);
        ra_Delta.setPosition(position);
        ra_Eta.setPosition(position);

        // Check the value is close enough
        return Math.abs(ra_Rho.get().get() - rho) < 1e-6 && Math.abs(ra_Delta.get().get() - delta) < 1e-6
                && Math.abs(ra_Eta.get().get() - eta) < 1e-6;
    }

}

class WriterDummyCapturedImageFileSet implements ICapturedImageFileSet {
    String setName;

    public WriterDummyCapturedImageFileSet(String setName) {
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
