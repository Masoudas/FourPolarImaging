package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;

public class RGB16ImgLib2TiffImageReader implements ImageReader<RGB16> {
    public RGB16ImgLib2TiffImageReader(ImgLib2ImageFactory factory) {

    }

    @Override
    public Image<RGB16> read(File path) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub

    }
    
}