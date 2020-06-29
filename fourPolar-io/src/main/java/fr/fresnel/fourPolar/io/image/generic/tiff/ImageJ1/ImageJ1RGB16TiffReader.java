package fr.fresnel.fourPolar.io.image.generic.tiff.ImageJ1;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import ij.ImagePlus;
import ij.io.FileInfo;
import ij.io.FileOpener;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.ARGBType;

public class ImageJ1RGB16TiffReader implements ImageReader<ARGB8> {

    @Override
    public Image<ARGB8> read(File path) throws IOException, MetadataParseError {
        Utils.checkExtension(path.getAbsolutePath());
        Utils.checkFileExists(path);

        FileInfo fileInfo = _createFileInfo(path);

        FileOpener opener = new FileOpener(null);

        ImagePlus imagePlus = opener.openImage();

        // TODO properply write a converter in ImageLib2 model to convert this.
        // TODO it's not clear to me what this wrapper does to imagePlus. For example,
        // if dimension is 4, would it return 4?
        Img<ARGBType> imgLib2Image = (Img<ARGBType>) ImageJFunctions.wrap(imagePlus);

        return null;
    }

    private FileInfo _createFileInfo(File path) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.fileName = path.getAbsolutePath();

        fileInfo.fileFormat = FileInfo.RGB;
        fileInfo.fileType = FileInfo.TIFF;

        return fileInfo;
    }

    private IMetadata _createMetadata(ImagePlus imagePlus) {
        // TODO How one creates AxisOrder based on these numbers?
        int numChannels = imagePlus.getNChannels();
        int numZ = imagePlus.getNSlices();
        int numTime = imagePlus.getNFrames();

        long[] dimAsLong = Arrays.stream(imagePlus.getDimensions()).mapToLong((t) -> t).toArray();
        return new Metadata.MetadataBuilder(dimAsLong).build();
    }

    @Override
    public void close() throws IOException {

    }

}