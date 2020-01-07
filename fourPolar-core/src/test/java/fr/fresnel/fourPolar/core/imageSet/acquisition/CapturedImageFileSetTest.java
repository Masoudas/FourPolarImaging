package fr.fresnel.fourPolar.core.imageSet.acquisition;

import static org.junit.Assert.assertTrue;


import java.io.File;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class CapturedImageFileSetTest
{
    File root = new File("/root");
  
    @Test
    public void getSetName_SameFileSet_ReturnsEqualSetName()
    {
        File pol0 = new File(root, "pol0.tiff");
        File pol45 = new File(root, "pol45.tiff");
        File pol90 = new File(root, "pol90.tiff");
        File pol135 = new File(root, "pol135.tiff");
    
        CapturedImageFileSet fileSet = new CapturedImageFileSet(pol0, pol45, pol90, pol135);

        File pol0_1 = new File(root, "pol0.tiff");
        File pol45_1 = new File(root, "pol45.tiff");
        File pol90_1 = new File(root, "pol90.tiff");
        File pol135_1 = new File(root, "pol135.tiff");
    
        CapturedImageFileSet fileSet_1 = new CapturedImageFileSet(pol0_1, pol45_1, pol90_1, pol135_1);
        
        assertTrue( fileSet.getSetName().equals(fileSet_1.getSetName()) );
    }

    @Test
    public void getSetName_OneByOne_ReturnsFileName()
    {
        File pol = new File(root, "pol.tiff");
    
        CapturedImageFileSet fileSet = new CapturedImageFileSet(pol);
        
        assertTrue( fileSet.getSetName().equals("pol") );
    }

    @Test
    public void equal_SameSetName_ReturnsObjectsEqual()
    {

        File pol1 = new File(root, "pol.tiff");
        
        File root2 = new File("/root2");
        File pol2 = new File(root2, "pol.tiff");
    
        CapturedImageFileSet fileSet1 = new CapturedImageFileSet(pol1);
    
        CapturedImageFileSet fileSet2 = new CapturedImageFileSet(pol2);
        
        assertTrue( fileSet1.equals(fileSet2) );
    }

}
