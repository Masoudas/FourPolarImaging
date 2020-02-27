package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;

public class RGB16ImgLib2TiffImageWriter implements ImageWriter<RGB16> {

    @Override
    public void write(File path, Image<RGB16> image) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void write(File path, IMetadata metadata, Image<RGB16> image) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub

    }

    
}