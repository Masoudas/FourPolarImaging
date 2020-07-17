package fr.fresnel.fourPolar.io.image.generic.metadata.scifio;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;
import fr.fresnel.fourPolar.io.image.generic.IMetadataReader;
import io.scif.SCIFIO;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat.Metadata;
import io.scif.formats.TIFFFormat.Reader;

/**
 * Reads metadata of a tiff image using SCIFIO library.
 */
public class SCIFIOMetadataReader implements IMetadataReader {
    final protected SCIFIOConfig _config;
    final protected Reader<Metadata> _reader;

    public SCIFIOMetadataReader() {
        this._config = _setSCFIOConfig();
        this._reader = new Reader<>();
        this._reader.setContext(new SCIFIO().context());
    }

    @Override
    public IMetadata read(File imageFile) throws MetadataIOIssues {
        try {
            this._reader.setSource(imageFile.getAbsolutePath(), this._config);
        } catch (IOException e) {
            throw new MetadataIOIssues(MetadataIOIssues.READ_ISSUES);
        }

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