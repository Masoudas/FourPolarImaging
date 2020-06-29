package fr.fresnel.fourPolar.io.image.generic.tiff.ImageJ1;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageJ1Model.ImageToImageJ1Conveter;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import ij.ImagePlus;
import ij.io.FileSaver;

public class ImageJ1RGB16TiffWriter implements ImageWriter<ARGB8> {

    @Override
    public void write(File path, Image<ARGB8> image) throws IOException {
        Utils.checkExtension(path.getAbsolutePath());
        Utils.deleteFileIfExists(path);

        ImagePlus imagePlus = ImageToImageJ1Conveter.convertToImgPlus(image, ARGB8.zero());
        FileSaver writer = new FileSaver(imagePlus);

        writer.saveAsTiff(path.getAbsolutePath());
    }

    @Override
    public void close() throws IOException {

    }

}