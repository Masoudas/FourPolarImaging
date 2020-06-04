package fr.fresnel.fourPolar.io.image.captured.tiff;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.CapturedImageSetBuilder;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;

/**
 * Used for reading a (16 bit tiff) captured image set. An instance of this
 * object is enough to read several images.
 */
public class TiffCapturedImageSetReader implements ICapturedImageSetReader {
    final private ImageReader<UINT16> _reader;

    /**
     * Used for reading a (16 bit tiff) captured images. An instance of this object
     * is enough to read several images.
     * 
     */
    public TiffCapturedImageSetReader(ImageFactory factory) {
        _reader = TiffImageReaderFactory.getReader(factory, UINT16.zero());
    }

    @Override
    public ICapturedImageSet read(final ICapturedImageFileSet fileSet) throws IOException {
        CapturedImageSetBuilder builder = new CapturedImageSetBuilder(fileSet.getnCameras());
        builder.setFileSet(fileSet);

        try {
            String[] labels = Cameras.getLabels(fileSet.getnCameras());
            for (String label : labels) {
                ICapturedImageFile[] labelCapturedFiles = fileSet.getFile(label);
                for (ICapturedImageFile capturedImageFile : labelCapturedFiles) {
                    Image<UINT16> img = _reader.read(capturedImageFile.file());
                    builder.setCapturedImage(label, capturedImageFile, img);
                }

            }
        } catch (IOException | MetadataParseError e) {
            throw new IOException("Captured images don't exist or are corrupted");
        }

        return builder.build();
    }

    /**
     * Close the resources of the underlying reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        _reader.close();
    }

}