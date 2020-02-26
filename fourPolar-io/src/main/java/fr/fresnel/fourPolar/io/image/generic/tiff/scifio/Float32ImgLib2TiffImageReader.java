package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

/**
 * This class reads a 16 bit unsigned image using SCIFIO library.
 */
public class Float32ImgLib2TiffImageReader extends GrayScaleImgLib2TiffReader<Float32> {
    final private FloatType imgLib2Type = new FloatType();

    public Float32ImgLib2TiffImageReader(ImgLib2ImageFactory factory) {
        super(factory);
    }

    @Override
    public Image<Float32> read(File path) throws IOException {
        this._reader.setSource(path.getAbsolutePath(), this._config);
        final Img<FloatType> img = this._imgOpener.openImgs(_reader, imgLib2Type, this._config).get(0);

        return this._imgFactory.create(img, imgLib2Type);
    }

    @Override
    public void close() throws IOException {
        this._reader.close();

    }

}