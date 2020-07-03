package fr.fresnel.fourPolar.ui;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.IMetadataReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata.SCIFIOMetadataReader;

/**
 * Given this choice, Sophie (AKA boss) can perform the following set of actions
 * on the raw acquired images:
 * 
 * 1- Define an axis order: To define an axis order, boss has to make sure that
 * the axis order has the same number of axis as the image dimension (for
 * example XY for a 2*2 image, XYZ for a 3*2*3 image).
 * 
 */
public class SohpiesAcquisitionChoice {
    static String path;

    public static void main(String[] args) {
        // -------------------------------------------------------------------
        // YOU DON'T NEED TO TOUCH ANYTHING FROM HERE ON!
        // -------------------------------------------------------------------
        AxisOrder axisOrder = _getAxisOrder();

    }

    private static void _printAxisOrders() {
        System.out.println("Define an axis order for the image.");
        System.out.println("The possible choices of axis order are:");
        EnumSet<AxisOrder> set = EnumSet.allOf(AxisOrder.class);
        set.remove(AxisOrder.NoOrder);
        System.out.println(set);
    }

    private static void _printImageDimension() throws IOException, MetadataParseError {
        IMetadataReader metadataReader = new SCIFIOMetadataReader();
        IMetadata metadata = metadataReader.read(new File(path));

        System.out.println("The dimension of the image is" + Stream.of(metadata.getDim()).collect(Collectors.toSet()));
        metadataReader.close();
    }

    private static AxisOrder _getAxisOrder() {
        Scanner scanner = new Scanner(System.in);
        AxisOrder newAxisOrder = AxisOrder.NoOrder;
        while (newAxisOrder == AxisOrder.NoOrder) {
            System.out.println("Write down an axis order: ");

            try {
                newAxisOrder = AxisOrder.fromString(scanner.nextLine());
            } catch (Exception e) {
            }

            if (newAxisOrder == AxisOrder.NoOrder) {
                System.out.println("Axis order is not from the list of choices.");
            }
        }

        scanner.close();

        return newAxisOrder;
    }
}