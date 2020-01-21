package fr.fresnel.fourPolar.io.imagingSetup;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.Rectangle;
import fr.fresnel.fourPolar.core.physics.channel.PropagationChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;

/**
 * Note that in all the test, the files are created in the 4POLAR subfolder of
 * the root.
 */
public class FourPolarImagingSetupToYamlTest {

    @Test
    public void write_WriteOneCameraOneChannel_FileGeneratedinResourceFolder()
            throws JsonGenerationException, JsonMappingException, IOException {
        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.One);

        Rectangle rect0 = new Rectangle(1, 1, 128, 128);
        Rectangle rect45 = new Rectangle(128, 1, 128, 128);
        Rectangle rect90 = new Rectangle(1, 128, 128, 128);
        Rectangle rect135 = new Rectangle(128, 128, 128, 128);

        FieldOfView fov = new FieldOfView(rect0, rect45, rect90, rect135);
        imagingSetup.setFieldOfView(fov);

        INumericalAperture na = new NumericalAperture(1.45, 5.65, 3.4342, 1.3434);
        imagingSetup.setNumericalAperture(na);

        PropagationChannel prop = new PropagationChannel(1, 1.45, 1.54, 1.34, 3.11);
        imagingSetup.setPropagationChannel(1, prop);

        File rootFolder = new File(FourPolarImagingSetupToYamlTest.class.getResource("").getPath());
        FourPolarImagingSetupToYaml writer = new FourPolarImagingSetupToYaml(imagingSetup, rootFolder);
        writer.write();

    }
    
}