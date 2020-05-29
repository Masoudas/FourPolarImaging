package fr.fresnel.fourPolar.io.image.polarization;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.algorithm.util.image.axis.AxisReassigner;
import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSetBuilder;
import fr.fresnel.fourPolar.io.image.polarization.file.IPolarizationImageFileSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import fr.fresnel.fourPolar.io.image.polarization.file.TiffPolarizationImageFileSet;

/**
 * A concrete implementation of {@link IOrientationImageReader} to read tiff
 * polarization images.
 */
public class TiffPolarizationImageSetReader implements IPolarizationImageSetReader {
    final private ImageReader<UINT16> _reader;
    final private int _numChannels;

    /**
     * Initialize the reader for the provided {@link Image} implementation. The same
     * class can read several polarization images from the disk.
     * 
     * @param factory
     * @throws NoReaderFoundForImage
     */
    public TiffPolarizationImageSetReader(ImageFactory factory, int numChannels) throws NoReaderFoundForImage {
        this._reader = TiffImageReaderFactory.getReader(factory, UINT16.zero());
        this._numChannels = numChannels;
    }

    @Override
    public IPolarizationImageSet read(File root4PProject, ICapturedImageFileSet fileSet, int channel)
            throws IOException, CannotFormPolarizationImageSet {
        IPolarizationImageFileSet polFileSet = new TiffPolarizationImageFileSet(root4PProject, fileSet, channel);

        IPolarizationImageSet imageSet = null;
        try {
            Image<UINT16> pol0 = _readPolarizationImage(Polarization.pol0, polFileSet);
            this._reassignPolarizationImageToXYCZT(pol0);

            Image<UINT16> pol45 = _readPolarizationImage(Polarization.pol45, polFileSet);
            this._reassignPolarizationImageToXYCZT(pol45);

            Image<UINT16> pol90 = _readPolarizationImage(Polarization.pol90, polFileSet);
            this._reassignPolarizationImageToXYCZT(pol90);

            Image<UINT16> pol135 = _readPolarizationImage(Polarization.pol90, polFileSet);
            this._reassignPolarizationImageToXYCZT(pol135);

            imageSet = new PolarizationImageSetBuilder(this._numChannels).channel(1).fileSet(fileSet).pol0(pol0)
                    .pol45(pol45).pol90(pol90).pol135(pol135).build();

        } catch (IOException | MetadataParseError e) {
            throw new IOException("Polarization images don't exist or are corrupted");
        }

        return imageSet;
    }

    private Image<UINT16> _readPolarizationImage(Polarization pol, IPolarizationImageFileSet fileSet)
            throws IOException, MetadataParseError {
        File imageFile = fileSet.getFile(pol);
        return this._reader.read(imageFile);
    }

    /**
     * It may happen that if the t dimension of the image is 1 (or z for that
     * matter), when we read it from disk, it would appear as xy. Hence, we need to
     * reassign if necessary.
     */
    private Image<UINT16> _reassignPolarizationImageToXYCZT(Image<UINT16> angleImage) {
        // TODO we need to get rid of this method, by making sure that when image is
        // written,
        // z and t with dimension 1 are properly written to the disk.
        if (angleImage.getMetadata().axisOrder() == IPolarizationImage.AXIS_ORDER) {
            return angleImage;
        } else {
            return AxisReassigner.reassignToXYCZT(angleImage, UINT16.zero());
        }

    }

    @Override
    public void close() throws IOException {
        this._reader.close();
    }

}