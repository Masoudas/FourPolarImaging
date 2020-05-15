package fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import io.scif.ImageMetadata;
import net.imagej.axis.Axes;
import net.imagej.axis.DefaultAxisType;

public class SCIFIOTiffMetadataConverterTest {
    @Test
    public void convertImageMetadata_fiveAxisMetadata_ReturnsCorrectMetadata() throws MetadataParseError {
        io.scif.formats.TIFFFormat.Metadata tiffMetadata = new io.scif.formats.TIFFFormat.Metadata();
        tiffMetadata.createImageMetadata(1);
        ImageMetadata imageMetadata = tiffMetadata.get(0);

        long[] dim = { 1, 2, 3, 4, 5 };
        imageMetadata.addAxis(Axes.X, dim[0]);
        imageMetadata.addAxis(Axes.Y, dim[1]);
        imageMetadata.addAxis(Axes.Z, dim[2]);
        imageMetadata.addAxis(Axes.CHANNEL, dim[3]);
        imageMetadata.addAxis(Axes.TIME, dim[4]);

        imageMetadata.setBitsPerPixel(16);

        IMetadata metadata = SCIFIOTiffMetadataConverter.convertFrom(imageMetadata);

        assertTrue(metadata.axisOrder().name().equals("XYZCT") && Arrays.equals(dim, metadata.getDim()));

    }

    @Test
    public void convertImageMetadata_UnSupportedAxisOrder_ThrowsUnsupportedAxisOrder() {
        io.scif.formats.TIFFFormat.Metadata tiffMetadata = new io.scif.formats.TIFFFormat.Metadata();
        tiffMetadata.createImageMetadata(1);
        ImageMetadata imageMetadata = tiffMetadata.get(0);

        long[] dim = { 1, 2, 3 };
        imageMetadata.addAxis(Axes.Z, dim[0]);
        imageMetadata.addAxis(Axes.Z, dim[1]);
        imageMetadata.addAxis(Axes.Z, dim[2]);

        MetadataParseError exception = assertThrows(MetadataParseError.class, () -> {
            SCIFIOTiffMetadataConverter.convertFrom(imageMetadata);
        });

        assertTrue(exception.getMessage().equals(MetadataParseError.UNDEFINED_AXIS_ORDER));
    }

    @Test
    public void convertImageMetadata_UndefinedAxis_ThrowsUnsupportedAxisOrder() {
        io.scif.formats.TIFFFormat.Metadata tiffMetadata = new io.scif.formats.TIFFFormat.Metadata();
        tiffMetadata.createImageMetadata(1);
        ImageMetadata imageMetadata = tiffMetadata.get(0);

        long[] dim = { 1, 2, 3 };
        imageMetadata.addAxis(Axes.Z, dim[0]);
        imageMetadata.addAxis(new DefaultAxisType(Axes.UNKNOWN_LABEL), dim[1]);
        imageMetadata.addAxis(Axes.Z, dim[2]);

        MetadataParseError exception = assertThrows(MetadataParseError.class, () -> {
            SCIFIOTiffMetadataConverter.convertFrom(imageMetadata);
        });

        assertTrue(exception.getMessage().equals(MetadataParseError.UNDEFINED_AXIS));
    }

    @Test
    public void convertMetadata_FiveAxisMetadata_ReturnsCorrectImageMetadata() {
        long[] dim = new long[] { 1, 2, 3, 4, 5 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT).bitPerPixel(16).build();

        io.scif.formats.TIFFFormat.Metadata tiffMetadata = new io.scif.formats.TIFFFormat.Metadata();
        tiffMetadata.createImageMetadata(1);
        ImageMetadata imageMetadata = tiffMetadata.get(0);

        SCIFIOTiffMetadataConverter.convertTo(metadata, imageMetadata);

        assertTrue(imageMetadata.getAxis(0).type().getLabel().equals("X")
                && imageMetadata.getAxis(1).type().getLabel().equals("Y")
                && imageMetadata.getAxis(2).type().getLabel().equals("Channel")
                && imageMetadata.getAxis(3).type().getLabel().equals("Z")
                && imageMetadata.getAxis(4).type().getLabel().equals("Time")
                && Arrays.equals(imageMetadata.getAxesLengths(), dim));
    }
}