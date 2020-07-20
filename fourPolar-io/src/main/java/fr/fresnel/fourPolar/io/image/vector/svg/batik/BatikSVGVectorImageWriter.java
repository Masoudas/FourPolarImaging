package fr.fresnel.fourPolar.io.image.vector.svg.batik;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.ImagePlaneAccessor;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.util.vectorImage.VectorImageUtil;
import fr.fresnel.fourPolar.io.exceptions.image.vector.VectorImageIOIssues;
import fr.fresnel.fourPolar.io.image.vector.VectorImageWriter;

/**
 * Using this class, we can write a {@link VectorImage} to the disk as svg.
 * <p>
 * The writer writes each plane of the image as a separate file inside the root.
 * The naming convention is imageName_{axisNamexxx}.svg. For example, if the
 * axis order is xy, there would only be one file named imageName.svg. If the
 * axis order is xyz and there are three planes, we have imageName_z000.svg,
 * imageName_z001.svg and imageName_z002.svg.
 * <p>
 * In addition to the vector images, this saver writes an extra metadata.yaml
 * file, which would be used as a helper for reading the images from the disk.
 */
public class BatikSVGVectorImageWriter implements VectorImageWriter {
    private final SVGTranscoder _transcoder;

    public BatikSVGVectorImageWriter() {
        _transcoder = new SVGTranscoder();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(File root, String imageName, VectorImage vectorImage) throws VectorImageIOIssues {
        if (!VectorImageUtil.hasBackEndPlanes(vectorImage, SVGDocument.class)) {
            throw new IllegalArgumentException("The given vector image does not have batik implementation.");
        }

        if (vectorImage.metadata().axisOrder() == AxisOrder.NoOrder) {
            throw new VectorImageIOIssues("Can't write a vector image with no axis-order.");
        }

        BatikSVGVectorImagePathCreator pathCreator = new BatikSVGVectorImagePathCreator(vectorImage.metadata(), root,
                imageName);
        ImagePlaneAccessor<SVGDocument> planeAccesser = (ImagePlaneAccessor<SVGDocument>) vectorImage;
        for (int planeIndex = 0; planeIndex < planeAccesser.numPlanes(); planeIndex++) {
            File imagePath = pathCreator.createPlaneImageFile(planeIndex);
            _writeBatikSVG(planeAccesser.getImagePlane(planeIndex).getPlane(), imagePath);
        }

    }

    /**
     * Write the given batik svg document to the specified path.
     */
    private void _writeBatikSVG(SVGDocument svgDocument, File path) throws VectorImageIOIssues {
        try (FileWriter writer = new FileWriter(path)) {
            PrintWriter printWriter = new PrintWriter(writer);

            TranscoderInput input = new TranscoderInput(svgDocument);
            TranscoderOutput output = new TranscoderOutput(printWriter);

            _transcoder.transcode(input, output);
        } catch (IOException | TranscoderException e) {
            throw new VectorImageIOIssues("Can't write svg document to disk.");
        }
    }

    private void _deleteAndCreateImageNameFolder(File ) {
        
    }

    @Override
    public void close() throws VectorImageIOIssues {

    }

}