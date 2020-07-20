package fr.fresnel.fourPolar.io.image.vector.svg.batik;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;

public class BatikSVGVectorImageUtilTest {
    @Test
    public void createPlaneImageFile_XYImage_ImageFileOnlyHasImageName(){
        IMetadata metadata = new Metadata.MetadataBuilder(new long[]{1, 2}).axisOrder(AxisOrder.XY).build();

        File root = new File("root");
        String imageName = "XYImage";
        File expectedFile = new File(root, imageName + ".svg");

        File image_path = BatikSVGVectorImageUtil.createPlaneImageFile(root, imageName, metadata, 1);
        
        assertTrue(image_path.equals(expectedFile));
    }

    @Test
    public void createPlaneImageFile_XYZImageThridPlane_FileHasCorrectCoordinate(){
        IMetadata metadata = new Metadata.MetadataBuilder(new long[]{1, 2, 3}).axisOrder(AxisOrder.XYZ).build();

        File root = new File("root");
        String imageName = "XYZImage";
        File expectedFile = new File(root, imageName + "_z002" + ".svg");

        File image_path = BatikSVGVectorImageUtil.createPlaneImageFile(root, imageName, metadata, 3);
        assertTrue(image_path.equals(expectedFile));
    }

    @Test
    public void createPlaneImageFile_XYZTImageFourthPlane_FileHasCorrectCoordinate(){
        IMetadata metadata = new Metadata.MetadataBuilder(new long[]{1, 2, 3, 3}).axisOrder(AxisOrder.XYZT).build();

        File root = new File("root");
        String imageName = "XYZTImage";
        File expectedFile = new File(root, imageName + "_z000" + "_t001" + ".svg");

        File image_path = BatikSVGVectorImageUtil.createPlaneImageFile(root, imageName, metadata, 4);
        assertTrue(image_path.equals(expectedFile));
    }

    @Test
    public void createPlaneImageFile_XYCZTImageThirteenthPlane_FileHasCorrectCoordinate(){
        IMetadata metadata = new Metadata.MetadataBuilder(new long[]{1, 2, 4, 3, 3}).axisOrder(AxisOrder.XYCZT).build();

        File root = new File("root");
        String imageName = "XYCZTImage";
        File expectedFile = new File(root, imageName + "_c000" + "_z000" + "_t001" + ".svg");

        File image_path = BatikSVGVectorImageUtil.createPlaneImageFile(root, imageName, metadata, 13);
        assertTrue(image_path.equals(expectedFile));
    }
}