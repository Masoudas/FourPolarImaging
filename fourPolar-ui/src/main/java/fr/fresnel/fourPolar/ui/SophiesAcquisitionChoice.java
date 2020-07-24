package fr.fresnel.fourPolar.ui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Scanner;
import java.util.stream.Collectors;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.util.image.metadata.axis.AxisReassigner;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;
import fr.fresnel.fourPolar.io.image.generic.metadata.IMetadataReader;
import fr.fresnel.fourPolar.io.image.generic.metadata.scifio.SCIFIOMetadataReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOUINT16TiffReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOUINT16TiffWriter;

/**
 * Given this choice, Sophie (AKA boss) can perform the following set of actions
 * on the raw acquired images:
 * 
 * 1- Define an axis order: To define an axis order, boss has to make sure that:
 * 1-1- The image is 16 bit tif. 1-2 The proposed axis order has same number of
 * dimension as image.
 * 
 */
public class SophiesAcquisitionChoice {
    static String rootFolder = "D:\\4PolarBackendTest\\Masoud";
    static String fileName = "AF488_3D_noSAF_2-wf.tif";

    public static void main(String[] args) throws IOException, MetadataIOIssues {
        // -------------------------------------------------------------------
        // YOU DON'T NEED TO TOUCH ANYTHING FROM HERE ON!
        // -------------------------------------------------------------------
        _defineAxisOrderForAcquiredImage();
    }

    private static void _defineAxisOrderForAcquiredImage() throws IOException, MetadataIOIssues {
        long[] imageDim = _printImageDimension();
        _printAxisOrders();

        AxisOrder axisOrder = _getAxisOrder(imageDim);

        Image<UINT16> image = _readImage();
        Image<UINT16> image_withAxisOrder = _createNewImageWithAxisOrder(image, axisOrder);
        _writeImage(image_withAxisOrder);

    }

    private static void _printAxisOrders() {
        System.out.println("Define an axis order for the image.");
        System.out.println("The possible choices of axis order are:");
        EnumSet<AxisOrder> set = EnumSet.allOf(AxisOrder.class);
        set.remove(AxisOrder.NoOrder);
        System.out.println(set);
    }

    private static long[] _printImageDimension() throws IOException, MetadataIOIssues {
        IMetadataReader metadataReader = new SCIFIOMetadataReader();
        IMetadata metadata = metadataReader.read(new File(rootFolder, fileName));

        if (metadata.axisOrder() != AxisOrder.NoOrder) {
            throw new IllegalArgumentException("The given image already have an axis order, which is "
                    + metadata.axisOrder() + ". There's no need to define axis-order.");
        }

        System.out.println("\n \n \n");
        System.out.println("The dimension of the image is "
                + Arrays.stream(metadata.getDim()).boxed().collect(Collectors.toList()));
        metadataReader.close();

        return metadata.getDim();
    }

    private static AxisOrder _getAxisOrder(long[] imageDim) {
        Scanner scanner = new Scanner(System.in);
        AxisOrder newAxisOrder = AxisOrder.NoOrder;

        boolean axisOrderIsProperlySet = false;
        while (!axisOrderIsProperlySet) {
            System.out.println("Write down an axis order: ");

            try {
                newAxisOrder = AxisOrder.fromString(scanner.nextLine());
            } catch (Exception e) {
            }

            if (newAxisOrder == AxisOrder.NoOrder) {
                System.out.println("Axis order is not from the list of choices.");
            } else if (!MetadataUtil.numAxisEqualsDimension(newAxisOrder, imageDim)) {
                System.out.println("Number of axis does not equal image dimension.");
                newAxisOrder = AxisOrder.NoOrder;
            } else {
                axisOrderIsProperlySet = true;
            }
        }

        scanner.close();

        return newAxisOrder;
    }

    private static Image<UINT16> _readImage() throws IOException {
        SCIFIOUINT16TiffReader reader = new SCIFIOUINT16TiffReader(new ImgLib2ImageFactory());
        Image<UINT16> image = reader.read(new File(rootFolder, fileName));
        reader.close();

        return image;
    }

    private static Image<UINT16> _createNewImageWithAxisOrder(Image<UINT16> noAxisImage, AxisOrder axisOrder) {
        return AxisReassigner.defineAxis(noAxisImage, axisOrder);
    }

    private static void _writeImage(Image<UINT16> image) throws IOException {
        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
        AxisOrder axisOrder = image.getMetadata().axisOrder();

        SCIFIOUINT16TiffWriter writer = new SCIFIOUINT16TiffWriter();
        writer.write(new File(rootFolder, fileNameWithoutExtension + "_" + axisOrder + ".tif"), image);
        writer.close();
    }
}