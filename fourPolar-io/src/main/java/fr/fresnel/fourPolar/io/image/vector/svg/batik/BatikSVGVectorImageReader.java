package fr.fresnel.fourPolar.io.image.vector.svg.batik;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.batikModel.BatikVectorImageFactory;
import fr.fresnel.fourPolar.core.util.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;
import fr.fresnel.fourPolar.io.exceptions.image.vector.VectorImageIOIssues;
import fr.fresnel.fourPolar.io.image.generic.metadata.IMetadataReader;
import fr.fresnel.fourPolar.io.image.generic.metadata.json.IMetadataFromYAML;
import fr.fresnel.fourPolar.io.image.vector.VectorImageReader;

/**
 * Reads a {@link VectorImage} as written by {@link BatikSVGVectorImageWriter}
 * from the disk.
 */
public class BatikSVGVectorImageReader implements VectorImageReader {
    private final IMetadataReader _metadataReader;
    private final BatikVectorImageFactory _batikFactory;

    /**
     * Instantiate the reader, setting its metadata reader to
     * {@link IMetadataToYaml}.
     */
    public BatikSVGVectorImageReader() {
        _metadataReader = new IMetadataFromYAML();
        _batikFactory = new BatikVectorImageFactory();
    }

    /**
     * Instantiate class by providing a metadata reader.
     * 
     * @param metadataReader is the metadata reader interface.
     */
    public BatikSVGVectorImageReader(IMetadataReader metadataReader) {
        _metadataReader = metadataReader;
        _batikFactory = new BatikVectorImageFactory();
    }

    @Override
    public VectorImage read(File root, String imageName) throws VectorImageIOIssues {
        Objects.requireNonNull(root, "root can't be null");
        Objects.requireNonNull(imageName, "imageName can't be null");

        if (!root.exists()) {
            throw new VectorImageIOIssues("The root folder does not exist");
        }

        IMetadata metadata = _readMetadata(root, imageName);
        SVGDocument[] planes = _readVectorImagePlanes(metadata, root, imageName);

        try {
            return _batikFactory.createFromPlanes(metadata, planes);
        } catch (IllegalArgumentException e) {
            throw new VectorImageIOIssues("Can't read vector image due to inconsistency between metadata and planes.");
        }

    }

    private SVGDocument[] _readVectorImagePlanes(IMetadata metadata, File root, String imageName)
            throws VectorImageIOIssues {
        int numPlanes = _getNumPlanesFromMetadata(metadata);
        SVGDocument[] planes = new SVGDocument[numPlanes];

        BatikSVGVectorImagePathCreator pathCreator = new BatikSVGVectorImagePathCreator(metadata, root, imageName);
        for (int planeIndex = 1; planeIndex <= numPlanes; planeIndex++) {
            planes[planeIndex - 1] = _readBatikImage(pathCreator.createPlaneImageFile(planeIndex));
        }

        return planes;
    }

    private SVGDocument _readBatikImage(File path) throws VectorImageIOIssues {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);

        try {
            return (SVGDocument) factory.createDocument(path.toURI().toURL().toString());
        } catch (IOException e) {
            throw new VectorImageIOIssues("Can't read " + path.getName() + " plane of the vector image");
        }
    }

    private IMetadata _readMetadata(File root, String name) throws VectorImageIOIssues {
        try {
            return _metadataReader.read(root, name);
        } catch (MetadataIOIssues e) {
            throw new VectorImageIOIssues("Can't read vector image because metadata can't be read.");
        }
    }

    private int _getNumPlanesFromMetadata(IMetadata metadata) {
        return MetadataUtil.getNPlanes(metadata);
    }

    @Override
    public void close() throws VectorImageIOIssues {
        try {
            _metadataReader.close();
        } catch (MetadataIOIssues e) {
            throw new VectorImageIOIssues("Can't close reader resources.");
        }
    }

}