package fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata;

import java.util.Iterator;
import java.util.List;

import fr.fresnel.fourPolar.core.exceptions.image.generic.axis.UnsupportedAxisOrder;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import io.scif.ImageMetadata;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imagej.axis.CalibratedAxis;
import net.imagej.axis.DefaultAxisType;

/**
 * Using this class, we can convert the SCIFIO Tiff metadata to
 * {@link IMetadata}.
 */
public class SCIFIOTiffMetadataConverter {
    /**
     * Convert from SCIFIO metadata
     * 
     * @throws UnsupportedAxisOrder in case the underlying image has an undefined axis
     *                            order.
     */
    public static IMetadata convertFrom(ImageMetadata SCIFIOMetadata) throws UnsupportedAxisOrder {
        long[] dim = SCIFIOMetadata.getAxesLengths();
        int bitDepth = SCIFIOMetadata.getBitsPerPixel();
        AxisOrder axisOrder = _getAxisOrder(SCIFIOMetadata.getAxes());

        return new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).bitPerPixel(bitDepth).build();
    }

    /**
     * Creates a blank {@link ImageMetadata}, and sets the Axis, axis length and
     * bitPerPixel. Then it returns this metadata. Note that PixelType is not set 
     * by this method.
     * 
     */
    public static ImageMetadata convertTo(IMetadata metadata) {
        io.scif.formats.TIFFFormat.Metadata tiffMetadata = new io.scif.formats.TIFFFormat.Metadata();
        tiffMetadata.createImageMetadata(1);

        ImageMetadata imageMetadata = tiffMetadata.get(0);
        _setImageMetadataAxis(metadata.axisOrder(), metadata.getDim(), imageMetadata);
        imageMetadata.setBitsPerPixel(metadata.bitPerPixel());

        return imageMetadata;
    }

    /**
     * Returns {@link AxisOrder} from the given axisList.
     */
    private static AxisOrder _getAxisOrder(List<CalibratedAxis> axisList) throws UnsupportedAxisOrder {
        String axisOrder = "";

        boolean undefAxis = true;
        for (Iterator<CalibratedAxis> itr = axisList.iterator(); itr.hasNext() && undefAxis;) {
            String axisName = itr.next().type().getLabel();
            if (axisName.equals(Axes.CHANNEL.getLabel())) {
                axisOrder += "C";
            } else if (axisName.equals(Axes.TIME.getLabel())) {
                axisOrder += "T";
            } else if (axisName.equals(Axes.UNKNOWN_LABEL)) {
                undefAxis = false;
                axisOrder = "NoOrder";
            } else {
                axisOrder += axisName;
            }
        }
        return AxisOrder.fromString(axisOrder);
    }

    /**
     * Set {@link ImageMetadata} axis based on {@link IMetadata} axis.
     */
    private static void _setImageMetadataAxis(AxisOrder axisOrder, long[] dim, ImageMetadata imageMetadata) {
        assert axisOrder != null : "axisOrder is null";

        String orderAsString = axisOrder.name();
        for (int n = 0; n < dim.length; n++) {
            AxisType axis;
            if (axisOrder == AxisOrder.NoOrder) {
                axis = new DefaultAxisType(Axes.UNKNOWN_LABEL);
            } else if (orderAsString.charAt(n) == 'C') {
                axis = Axes.CHANNEL;
            } else if (orderAsString.charAt(n) == 'T') {
                axis = Axes.TIME;
            } else {
                axis = new DefaultAxisType(String.valueOf(orderAsString.charAt(n)));
            }

            imageMetadata.addAxis(axis, dim[n]);
        }
    }

    private static long _getNumChannels(long[] dim, AxisOrder axisOrder) {
        int channelAxis = AxisOrder.getChannelAxis(axisOrder);
        if (channelAxis < 0) {
            return 0;
        } else {
            return dim[channelAxis];
        }

    }
}