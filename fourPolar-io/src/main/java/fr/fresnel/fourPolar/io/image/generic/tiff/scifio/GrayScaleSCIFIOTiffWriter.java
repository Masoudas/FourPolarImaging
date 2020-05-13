package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import io.scif.Writer;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat;
import io.scif.img.ImgSaver;
import io.scif.util.FormatTools;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imagej.axis.DefaultAxisType;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import io.scif.FormatException;
import io.scif.ImageMetadata;
import io.scif.SCIFIO;

/**
 * An abstract class for writing grayscale tiff images using the SCIFIO library.
 * 
 * @param <T> extends Pixel type.
 */
public abstract class GrayScaleSCIFIOTiffWriter<T extends PixelType> implements ImageWriter<T> {
    final protected ImgSaver _saver;
    final protected SCIFIOConfig _config;
    protected Writer _writer;

    /**
     * An abstract class for writing grayscale tiff images using the SCIFIO library.
     * This constructor creates the save once, which can be used as many times as
     * desired for saving different images of the same time.
     */
    GrayScaleSCIFIOTiffWriter() {
        this._saver = new ImgSaver();
        this._config = this._setSCFIOConfig();

        final TIFFFormat tiffFormat = new TIFFFormat();
        tiffFormat.setContext(this._saver.getContext());
        try {
            _writer = tiffFormat.createWriter();

        } catch (FormatException e) {
            // Will never be caught.
        }
    }

    /**
     * Close all resources when done reading all the files.
     */
    public void close() {
        this._saver.context().dispose();
    }

    /**
     * Sets the configuration for how the image is opened.
     * 
     * @return
     */
    private SCIFIOConfig _setSCFIOConfig() {
        // For the time being, we use the very basic config.
        SCIFIOConfig config = new SCIFIOConfig();
        return config;
    }

    @Override
    public abstract void write(File path, Image<T> image) throws IOException;

    @Override
    public abstract void write(File path, IMetadata metadata, Image<T> tiff) throws IOException;

    public static void main(String[] args) throws FormatException, IOException {
        long[] dim = {200, 200, 3, 2, 3};
        Img<UnsignedByteType> img = new ArrayImgFactory<>(new UnsignedByteType()).create(dim);

        io.scif.formats.TIFFFormat.Metadata metadata = new io.scif.formats.TIFFFormat.Metadata();
        metadata.createImageMetadata(1);

        ImageMetadata imageMetadata = metadata.get(0);
        imageMetadata.setBitsPerPixel(16);
        imageMetadata.setFalseColor(true);
        imageMetadata.setPixelType(FormatTools.UINT16);
        imageMetadata.setPlanarAxisCount(2);
        imageMetadata.setLittleEndian(false);
        imageMetadata.setIndexed(false);
        imageMetadata.setInterleavedAxisCount(0);
        imageMetadata.setThumbnail(false);
        imageMetadata.setOrderCertain(true);

        imageMetadata.addAxis(Axes.X, dim[0]);
        imageMetadata.addAxis(Axes.Y, dim[1]);
        imageMetadata.addAxis(Axes.TIME, dim[2]);
        imageMetadata.addAxis(Axes.Z, dim[3]);
        imageMetadata.addAxis(Axes.CHANNEL, dim[4]);

        SCIFIO scifio = new SCIFIO();
        io.scif.formats.TIFFFormat.Writer<io.scif.formats.TIFFFormat.Metadata> writer = new io.scif.formats.TIFFFormat.Writer<>();
        writer.setContext(scifio.context());
        writer.setMetadata(metadata);
        writer.setDest("/home/masoud/Documents/SampleImages/UnknownAxis.tif", new SCIFIOConfig());

        new ImgSaver().saveImg(writer, img);

    }

    
}