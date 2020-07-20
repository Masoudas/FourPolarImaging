package fr.fresnel.fourPolar.io.image.vector.svg.batik;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;
import fr.fresnel.fourPolar.io.exceptions.image.vector.VectorImageIOIssues;
import fr.fresnel.fourPolar.io.image.generic.metadata.json.IMetadataFromYAML;
import fr.fresnel.fourPolar.io.image.vector.VectorImageReader;

/**
 * 
 */
public class BatikSVGVectorImageReader implements VectorImageReader {
    private final IMetadataFromYAML _metadataReader;
    private final BatikImageFactory _batikFactory;

    public BatikSVGVectorImageReader() {
        _metadataReader = new IMetadataFromYAML();
        _batikFactory = new BatikImageFactory();
    }

    @Override
    public VectorImage read(File root, String imageName) throws VectorImageIOIssues {
        Objects.requireNonNull(root, "root can't be null");
        Objects.requireNonNull(imageName, "imageName can't be null");

        if (root.exists()) {
            throw new VectorImageIOIssues("The root folder does not exist");
        }

        IMetadata metadata = _readMetadata(root, imageName);
        _readVectorImagePlanes(metadata, root, imageName);

        return _batikFactory.;
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

    private IMetadata _readMetadata(File root, String imageName) throws VectorImageIOIssues {
        try {
            return _metadataReader.read(new File(root, imageName));
        } catch (MetadataIOIssues e) {
            throw new VectorImageIOIssues("Can't vector read image because metadata can't be read.");
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