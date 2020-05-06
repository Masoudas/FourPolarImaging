package fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.exceptions.image.generic.axis.UnsupportedAxisOrder;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.io.image.generic.IMetadataReader;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat.Metadata;
import io.scif.formats.TIFFFormat.Reader;
import io.scif.formats.TIFFFormat.Writer;

/**
 * Reads metadata of a tiff image using SCIFIO library.
 */
public class SCIFIOMetadataReader implements IMetadataReader {
    final protected SCIFIOConfig _config;
    final protected Reader<Metadata> _reader;

    public SCIFIOMetadataReader() {
        this._config = _setSCFIOConfig();
        this._reader = new Reader<>();
    }

    @Override
    public IMetadata read(File imageFile) throws IOException, UnsupportedAxisOrder {
        this._reader.setSource(imageFile, this._config);
        return SCIFIOTiffMetadataConverter.convertFrom(this._reader.getMetadata().get(0));
    }

    @Override
    public void close() throws IOException {
        this._reader.close();
    }

    /**
     * Sets the configuration for how the image is opened.
     * 
     * @return
     */
    private SCIFIOConfig _setSCFIOConfig() {
        // For the time being, we use the very basic config.
        SCIFIOConfig config = new SCIFIOConfig();
        config.imgOpenerSetImgModes(SCIFIOConfig.ImgMode.AUTO);
        return config;
    }

}