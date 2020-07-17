package fr.fresnel.fourPolar.io.image.vector.svg.batik;

import java.io.File;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
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

    @Override
    public void write(File root, String imageName, VectorImage vectorImage) throws VectorImageIOIssues {
        if (vectorImage.metadata().axisOrder() == AxisOrder.NoOrder) {
            throw new VectorImageIOIssues("Can't write a vector image with no axis-order.");
        }
        // TODO Auto-generated method stub

    }

    @Override
    public void close() throws VectorImageIOIssues {
        // TODO Auto-generated method stub

    }

}