package fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata;

import java.util.Iterator;
import java.util.List;

import fr.fresnel.fourPolar.core.exceptions.image.generic.axis.UnsupportedAxisOrder;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
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
     * @throws UnsupportedAxisOrder in case the underlying image has an undefined
     *                              axis order.
     */
    public static IMetadata convertFrom(ImageMetadata SCIFIOMetadata) throws MetadataParseError {
        long[] dim = SCIFIOMetadata.getAxesLengths();
        int bitDepth = SCIFIOMetadata.getBitsPerPixel();
        AxisOrder axisOrder = _getAxisOrder(SCIFIOMetadata.getAxes());

        return new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).bitPerPixel(bitDepth).build();
    }

    /**
     * Fills the axis type, axis length and bitPerPixel of the given
     * {@link ImageMetadata} from the given {@link IMetadata}.
     * 
     * @param metadata       is a metadata interface
     * @param scifioMetadata is a SCIFIO tiff metadata instance. This convert method
     *                       fills the axis type, axis length and bitPerPixel of
     *                       this metadata.
     */
    public static void convertTo(IMetadata metadata, io.scif.formats.TIFFFormat.Metadata scifioMetadata) {
        _setImageMetadataAxis(metadata.axisOrder(), metadata.getDim(), scifioMetadata.get(0));
        scifioMetadata.get(0).setBitsPerPixel(metadata.bitPerPixel());
    }

    /**
     * Returns {@link AxisOrder} from the given axisList.
     */
    private static AxisOrder _getAxisOrder(List<CalibratedAxis> axisList) throws MetadataParseError {
        String axisOrderAsString = "";

        for (Iterator<CalibratedAxis> itr = axisList.iterator(); itr.hasNext();) {
            String axisName = itr.next().type().getLabel();
            if (axisName.equals(Axes.CHANNEL.getLabel())) {
                axisOrderAsString += "C";
            } else if (axisName.equals(Axes.TIME.getLabel())) {
                axisOrderAsString += "T";
            } else if (axisName.equals(Axes.Z.getLabel()) || axisName.equals(Axes.X.getLabel())
                    || axisName.equals(Axes.Y.getLabel())) {
                axisOrderAsString += axisName;
            } else {
                throw new MetadataParseError(MetadataParseError.UNDEFINED_AXIS);
            }
        }

        try {
            return AxisOrder.fromString(axisOrderAsString);
        } catch (UnsupportedAxisOrder e) {
            throw new MetadataParseError(MetadataParseError.UNDEFINED_AXIS_ORDER);
        }

    }

    /**
     * Set {@link ImageMetadata} axis based on {@link IMetadata} axis.
     */
    private static void _setImageMetadataAxis(AxisOrder axisOrder, long[] dim, ImageMetadata imageMetadata) {
        assert axisOrder != null : "axisOrder is null";

        String orderAsString = axisOrder.name();
        for (int n = 0; n < dim.length; n++) {
            AxisType axis = null;
            if (axisOrder == AxisOrder.NoOrder) {
                axis = new DefaultAxisType(Axes.UNKNOWN_LABEL);
            } else if (orderAsString.charAt(n) == 'C') {
                axis = Axes.CHANNEL;
            } else if (orderAsString.charAt(n) == 'T') {
                axis = Axes.TIME;
            } else if (orderAsString.charAt(n) == 'X') {
                axis = Axes.X;
            } else if (orderAsString.charAt(n) == 'Y') {
                axis = Axes.Y;
            } else if (orderAsString.charAt(n) == 'Z') {
                axis = Axes.Z;
            }

            imageMetadata.addAxis(axis, dim[n]);
        }
    }

}