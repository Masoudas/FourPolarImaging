package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import io.scif.FormatException;
import io.scif.Writer;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat;
import io.scif.img.ImgSaver;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converters;
import net.imglib2.img.Img;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Util;

public class RGB16ImgLib2TiffImageWriter implements ImageWriter<RGB16> {
    final private ImgSaver _saver;
    final private SCIFIOConfig _config;
    private Writer _writer;

    final UnsignedByteType _readType = new UnsignedByteType();
    final ARGBType _argbType = new ARGBType();

    RGB16ImgLib2TiffImageWriter() {
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

    @Override
    public void write(File path, Image<RGB16> image) throws IOException {
        Objects.requireNonNull(path, "Destination path cannot be null");
        Objects.requireNonNull(image, "Image cannot be null");

        if (path.exists()) {
            path.delete();
        }

        try {
            Img<ARGBType> imgLib2Image = ImageToImgLib2Converter.getImg(image, new RGB16());

            Img<UnsignedByteType> convertedImg = _convertRGBToChannelImage(imgLib2Image);
            this._saver.saveImg(path.getAbsolutePath(), convertedImg, this._config);

        } catch (ConverterToImgLib2NotFound e) {
            // This exception is never caught, because this class cannot be directly instantiated,
            // and the factory ensures that converter exists.
        }

    }

    private Img<UnsignedByteType> _convertRGBToChannelImage(Img<ARGBType> imgLib2Image) {
        assert imgLib2Image != null : "Image input should not be null";

        RandomAccessibleInterval<UnsignedByteType> rai = Converters.argbChannels(imgLib2Image, 1, 2, 3);

        Img<UnsignedByteType> img = Util.getSuitableImgFactory(rai, new UnsignedByteType()).create(rai);
        LoopBuilder.setImages(img, rai).forEachPixel(UnsignedByteType::set);
        return img;
    }

    @Override
    public void write(File path, IMetadata metadata, Image<RGB16> image) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() throws IOException {
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

    
}