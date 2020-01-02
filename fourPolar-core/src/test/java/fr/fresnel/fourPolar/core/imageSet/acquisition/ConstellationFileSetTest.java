package fr.fresnel.fourPolar.core.imageSet.acquisition;

import static org.junit.Assert.assertTrue;


import java.io.File;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ConstellationFileSetTest
{
    File root = new File("/root");
        
    @Test
    public void nameExtract_TwoByTwo_Returnspol0FileName()
    {
        File pol0 = new File(root, "pol0.tiff");
        File pol45 = new File(root, "pol45.tiff");
        File pol90 = new File(root, "pol90.tiff");
        File pol135 = new File(root, "pol135.tiff");
    
        ConstellationFileSet fileSet = new ConstellationFileSet(pol0, pol45, pol90, pol135, 1);
        
        assertTrue( fileSet.getNameExtract().equals("pol0") );
    }

    @Test
    public void nameExtract_OneByOne_ReturnsFileName()
    {
        File pol = new File(root, "pol.tiff");
    
        ConstellationFileSet fileSet = new ConstellationFileSet(pol, 1);
        
        assertTrue( fileSet.getNameExtract().equals("pol") );
    }

}
