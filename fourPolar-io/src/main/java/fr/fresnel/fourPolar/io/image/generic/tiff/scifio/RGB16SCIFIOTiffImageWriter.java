package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata.MetadataBuilder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata.SCIFIOTiffMetadataConverter;
import io.scif.FormatException;
import io.scif.ImageMetadata;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat.Metadata;
import io.scif.formats.TIFFFormat.Writer;
import io.scif.img.ImgSaver;
import io.scif.util.FormatTools;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converters;
import net.imglib2.img.Img;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Util;

public class RGB16SCIFIOTiffImageWriter implements ImageWriter<RGB16> {
    final private ImgSaver _saver;
    final private SCIFIOConfig _config;
    final private Writer<Metadata> _writer;

    final UnsignedByteType _readType = new UnsignedByteType();
    final ARGBType _argbType = new ARGBType();

    public RGB16SCIFIOTiffImageWriter() {
        this._saver = new ImgSaver();
        this._config = this._setSCFIOConfig();
        this._writer = new Writer<>();
        this._writer.setContext(this._saver.context());

    }

    @Override
    public void write(File path, Image<RGB16> image) throws IOException {
        Objects.requireNonNull(path, "path cannot be null");
        Objects.requireNonNull(image, "Image cannot be null");

        SCIFIOUtils.checkExtension(path.getName());

        if (path.exists()) {
            path.delete();
        }

        IMetadata diskMetadata = this._createMetadata(image.getMetadata());
        io.scif.formats.TIFFFormat.Metadata scifioMetadata = this._createSCIFIOMetadata();

        SCIFIOTiffMetadataConverter.convertTo(diskMetadata, scifioMetadata.get(0));
        try {
            this._writer.setDest(path.getAbsolutePath());
            this._writer.setMetadata(scifioMetadata);
            Img<ARGBType> imgLib2Image = ImageToImgLib2Converter.getImg(image, RGB16.zero());

            Img<UnsignedByteType> convertedImg = _convertRGBToChannelImage(imgLib2Image);
            this._saver.saveImg(this._writer, convertedImg, this._config);

        } catch (ConverterToImgLib2NotFound | FormatException e) {
            // This exception is never caught, because this class cannot be directly
            // instantiated,
            // and the factory ensures that converter exists.
            // Format exception is also not caught because extension is checked.
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

    /**
     * Create {@link IMetadata}, given that {@link Image} of RGB16 type has no
     * channel and to save to image, we append three channels to the end of current
     * image.
     */
    public IMetadata _createMetadata(IMetadata originalMetadata) {
        long[] originalDim = originalMetadata.getDim();
        AxisOrder originalAxisOrder = originalMetadata.axisOrder();

        AxisOrder axisOrder = AxisOrder.appendChannelToEnd(originalAxisOrder);
        long[] dim = new long[originalDim.length + 1];

        for (int i = 0; i < dim.length - 1; i++) {
            dim[i] = originalDim[i];
        }
        dim[dim.length - 1] = 3;

        return new MetadataBuilder(dim).axisOrder(axisOrder).bitPerPixel(PixelTypes.RGB_16).build();
    }

    private io.scif.formats.TIFFFormat.Metadata _createSCIFIOMetadata() {
        io.scif.formats.TIFFFormat.Metadata metadata = new io.scif.formats.TIFFFormat.Metadata();
        metadata.createImageMetadata(1);

        ImageMetadata imageMetadata = metadata.get(0);
        imageMetadata.setFalseColor(true);
        imageMetadata.setPixelType(FormatTools.UINT8);
        imageMetadata.setPlanarAxisCount(2);
        imageMetadata.setLittleEndian(false);
        imageMetadata.setIndexed(false);
        imageMetadata.setInterleavedAxisCount(0);
        imageMetadata.setThumbnail(false);
        imageMetadata.setOrderCertain(true);

        return metadata;
    }
}