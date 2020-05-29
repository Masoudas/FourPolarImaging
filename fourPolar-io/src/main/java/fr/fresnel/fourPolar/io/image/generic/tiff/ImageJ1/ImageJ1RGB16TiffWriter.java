package fr.fresnel.fourPolar.io.image.generic.tiff.ImageJ1;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageJ1Model.ImageToImageJ1Conveter;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import ij.ImagePlus;
import ij.io.FileSaver;

public class ImageJ1RGB16TiffWriter implements ImageWriter<RGB16> {

    @Override
    public void write(File path, Image<RGB16> image) throws IOException {
        Utils.checkExtension(path.getAbsolutePath());
        Utils.deleteFileIfExists(path);

        ImagePlus imagePlus = ImageToImageJ1Conveter.convertToImgPlus(image, RGB16.zero());
        FileSaver writer = new FileSaver(imagePlus);

        writer.saveAsTiff(path.getAbsolutePath());
    }

    @Override
    public void close() throws IOException {

    }

}