package fr.fresnel.fourPolar.io.image.tiff.grayscale;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.scijava.io.location.FileLocation;

import io.scif.FormatException;
import io.scif.Metadata;
import io.scif.Reader;
import io.scif.SCIFIO;
import io.scif.img.ImgSaver;
import io.scif.img.SCIFIOImgPlus;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

public class TiffGrayScaleWriterTest {
    private static int _row;
    private static int _column;
    private static int _channel;
    private static File _root;

    @BeforeAll
    private static void setRoot() {
        _row = 10;
        _column = 10;
        _channel = 1;
        _root = new File(TiffGrayScaleWriterTest.class.getResource("TiffGrayScaleWriter").getPath());
    }

    @Test
    public void write_WriteUShortImage_DiskImageHasSameDimensions() throws IOException {
        File destination = new File(_root, "UShort.tif");
        Img<UnsignedShortType> img = new ArrayImgFactory<>(new UnsignedShortType()).create(_row, _column, _channel);

        TiffGrayScaleWriter<UnsignedShortType> writer = new TiffGrayScaleWriter<UnsignedShortType>();
        writer.write(destination, img);
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