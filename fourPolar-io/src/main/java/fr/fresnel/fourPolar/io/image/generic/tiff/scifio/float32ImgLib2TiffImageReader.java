package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imglib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.float32;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * This class reads a 16 bit unsigned image using SCIFIO library.
 */
public class float32ImgLib2TiffImageReader extends GrayScaleImgLib2TiffReader<float32> {
    final private UnsignedShortType imgLib2Type = new UnsignedShortType();

    public float32ImgLib2TiffImageReader(ImgLib2ImageFactory<float32> factory) {
        super(factory);
    }

    @Override
    public Image<float32> read(File path) throws IOException {
        this._reader.setSource(path.getAbsolutePath(), this._config);
        final Img<UnsignedShortType> img = this._imgOpener.openImgs(_reader, imgLib2Type, this._config).get(0);

        Image<float32> image = null;
        try {
            this._imgFactory.create(img, imgLib2Type);
        } catch (ConverterNotFound e) {
            // Exception never caught, because type aware.
        }

        return image;
    }

    @Override
    public void close() throws IOException {
        this._reader.close();

    }

}