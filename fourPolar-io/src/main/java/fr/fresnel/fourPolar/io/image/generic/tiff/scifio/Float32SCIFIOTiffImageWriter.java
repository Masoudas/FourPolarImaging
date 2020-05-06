package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;

/**
 * Class for writing grayscale tiffs to disk.
 * 
 */
public class Float32SCIFIOTiffImageWriter extends GrayScaleSCIFIOTiffWriter<Float32> {
    final private Float32 _pixelType = Float32.zero();

    @Override
    public void write(File path, Image<Float32> image) throws IOException {
        if (path.exists()) {
            path.delete();
        }

        try {
            this._saver.saveImg(path.getAbsolutePath(), ImageToImgLib2Converter.getImg(image, this._pixelType),
                    this._config);
        } catch (ConverterToImgLib2NotFound e) {
            // This exception potentially should not be caught, if we use the same Image 
            // type, and especially if converter has been implemented.
        }

    }
 
    @Override
    public void write(File path, IMetadata metadata, Image<Float32> tiff) throws IOException {
        /**
         * We are probably going to need to define a Dataplane object for each plane of
         * the image to be written, then populate it with img object plane, and finally
         * write every plane with the writer object. It appears that if I want to write
         * omero metadata, I need to manually iterate over the image.
         * 
         * It will be interesting to see how the saveImg method of ImgSaver populates
         * the metadata too.
         * 
         * If it comes down to simply adding comments, this link is helpful
         * https://github.com/ome/bioformats/blob/develop/components/formats-gpl/utils/EditTiffComment.java,
         * where the TiffParser gets us the comments and TiffSaver has a method for
         * overriding comments.
         * 
         * This link is also interesting:
         * https://github.com/ome/bioformats/blob/develop/components/formats-gpl/utils/EditImageName.java
         */
        if (path.exists()) {
            path.delete();
        }

        // try {
        //     // _writer.setMetadata(metadata);
        //     // _writer.setDest(destination.getAbsolutePath(), this._config);

        //     // this._saver.saveImg(_writer, img, _config);
        // } catch (FormatException e) {

        // }


    }
}