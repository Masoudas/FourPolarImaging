package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import io.scif.FormatException;
import io.scif.Reader;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat;
import io.scif.img.ImgOpener;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.ColorChannelOrder;
import net.imglib2.converter.Converters;
import net.imglib2.img.Img;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Util;

public class RGB16ImgLib2TiffImageReader implements ImageReader<RGB16> {
    final private SCIFIOConfig _config;
    private Reader _reader;
    final private ImgOpener _imgOpener;
    final private ImgLib2ImageFactory _imgFactory;

    final UnsignedByteType _readType = new UnsignedByteType();
    final ARGBType _argbType = new ARGBType();

    RGB16ImgLib2TiffImageReader(ImgLib2ImageFactory factory) {
        this._config = _setSCFIOConfig();
        this._imgOpener = new ImgOpener();
        this._imgFactory = factory;

        final TIFFFormat format = new TIFFFormat();
        format.setContext(_imgOpener.getContext());

        try {
            this._reader = format.createReader();
        } catch (FormatException e) {
            // Will never be caught.
        }
    }

    @Override
    public Image<RGB16> read(File path) throws IOException {
        Objects.requireNonNull(path, "Read path should not be null");

        if (!path.exists()) {
            throw new IOException("The given Tiff file does not exist.");
        }

        this._reader.setSource(path, this._config);

        // Read the raw image, which holds R, G and B as channels.
        Img<UnsignedByteType> rawImage = this._imgOpener.openImgs(this._reader, this._readType, this._config).get(0);

        // Merger RGB channes to form ARGB image.
        Img<ARGBType> img = createARGBImage(rawImage);

        return this._imgFactory.create(img, this._argbType);
    }

    /**
     * Creates an {@link Img} interface from a given
     * {@link RandomAccessibleInterval}.
     */
    private Img<ARGBType> createARGBImage(Img<UnsignedByteType> rawImage) {
        assert rawImage != null : "Image cannot be null";
        RandomAccessibleInterval<ARGBType> rai = Converters.mergeARGB(rawImage, ColorChannelOrder.RGB);

        Img<ARGBType> img = Util.getSuitableImgFactory(rai, new ARGBType()).create(rai);
        LoopBuilder.setImages(img, rai).forEachPixel(ARGBType::set);

        return img;
    }

    @Override
    public void close() throws IOException {
        this._reader.close();

    }

    /**
     * Sets the configuration for how the image is opened.
     * 
     * @return
     */
    private SCIFIOConfig _setSCFIOConfig() {
        // For the time being, we use the very basic config.
        SCIFIOConfig config = new SCIFIOConfig();
        config.imgOpenerSetImgModes(SCIFIOConfig.ImgMode.AUTO);
        return config;
    }

}