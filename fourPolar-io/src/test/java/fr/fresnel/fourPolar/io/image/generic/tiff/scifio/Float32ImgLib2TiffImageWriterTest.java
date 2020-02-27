package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import io.scif.FormatException;
import io.scif.Metadata;
import io.scif.Reader;
import io.scif.SCIFIO;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

public class Float32ImgLib2TiffImageWriterTest {
    private static long[] _dim = {10, 10, 1};
    private static File _root;

    @BeforeAll
    private static void setRoot() {
        _root = new File(Float32ImgLib2TiffImageWriterTest.class.getResource("Writers").getPath());
    }

    @Test
    public void write_Float32Image_DiskImageHasSameDimensions() throws IOException {
        File destination = new File(_root, "UINT32Image.tif");

        Image<Float32> image = new ImgLib2ImageFactory().create(_dim, new Float32());
        Float32ImgLib2TiffImageWriter writer = new Float32ImgLib2TiffImageWriter();
        writer.write(destination, image);
        writer.close();

        Img<UnsignedShortType> diskImage = new TiffGrayScaleReader<>(new UnsignedShortType()).read(destination);

        assertTrue(diskImage.dimension(0) == _row && diskImage.dimension(1) == _column
                && diskImage.dimension(2) == _channel);
    }

    @Test
    public void write_WriteFloat_DiskImageHasSameDimensions() throws IOException {
        File destination = new File(_root, "Float.tif");
        Img<FloatType> img = new ArrayImgFactory<>(new FloatType()).create(_row, _column, _channel);

        TiffGrayScaleWriter<FloatType> writer = new TiffGrayScaleWriter<FloatType>();
        writer.write(destination, img);
        writer.close();

        Img<UnsignedShortType> diskImage = new TiffGrayScaleReader<>(new UnsignedShortType()).read(destination);

        assertTrue(diskImage.dimension(0) == _row && diskImage.dimension(1) == _column
                && diskImage.dimension(2) == _channel);
    }

    @Test
    public void write_WriteMultipleFiles_DiskImageHasSameDimensions() throws IOException {
        Img<FloatType> img = new ArrayImgFactory<>(new FloatType()).create(_row, _column, _channel);
        TiffGrayScaleWriter<FloatType> writer = new TiffGrayScaleWriter<FloatType>();

        for (int i = 0; i < 10000; i++) {
            File destination = new File(_root, "UShort.tif");
            writer.write(destination, img);
        }

        writer.close();

        TiffGrayScaleReader<UnsignedShortType> reader = new TiffGrayScaleReader<>(new UnsignedShortType());
        Img<UnsignedShortType> diskImage = reader.read(new File(_root, "UShort.tif"));
        reader.close();

        assertTrue(diskImage.dimension(0) == _row && diskImage.dimension(1) == _column
                && diskImage.dimension(2) == _channel);

    }

    @Test
    public void write_WriteUShortImageWithMetadata_DiskImageHasSameDimensions() throws IOException, FormatException {
        File source = new File(_root, "UShortImage.tif");
        final SCIFIO scifio = new SCIFIO();
        final Reader reader = scifio.initializer().initializeReader(source.getAbsolutePath());
        final Metadata meta = reader.getMetadata();

        TiffGrayScaleReader<UnsignedShortType> reader1 = new TiffGrayScaleReader<>(new UnsignedShortType());
        Img<UnsignedShortType> diskImage = reader1.read(source);
        reader.close();

        File destination = new File(_root, "UShortImage_saved.tif");
        TiffGrayScaleWriter<UnsignedShortType> writer = new TiffGrayScaleWriter<UnsignedShortType>();
        
        assertDoesNotThrow(() -> {writer.write(destination, meta, diskImage);});
        writer.close();

    }



}