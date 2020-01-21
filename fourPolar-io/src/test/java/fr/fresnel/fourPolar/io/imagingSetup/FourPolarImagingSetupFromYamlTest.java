package fr.fresnel.fourPolar.io.imagingSetup;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.Rectangle;
import fr.fresnel.fourPolar.core.physics.channel.IPropagationChannel;
import fr.fresnel.fourPolar.core.physics.channel.PropagationChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * Note that in all the test, the files are created in the 4POLAR subfolder of
 * the root.
 */
public class FourPolarImagingSetupFromYamlTest {

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

        FourPolarImagingSetupFromYaml reader = new FourPolarImagingSetupFromYaml(rootFolder);
        FourPolarImagingSetup diskImagingSetup = reader.read();

        Rectangle diskRect0 = diskImagingSetup.getFieldOfView().getFoV(Polarizations.pol0);        
        Rectangle diskRect45 = diskImagingSetup.getFieldOfView().getFoV(Polarizations.pol45);        
        Rectangle diskRect90 = diskImagingSetup.getFieldOfView().getFoV(Polarizations.pol90);        
        Rectangle diskRect135 = diskImagingSetup.getFieldOfView().getFoV(Polarizations.pol135);        
        INumericalAperture diskNA = diskImagingSetup.getNumericalAperture();
        IPropagationChannel diskProp = diskImagingSetup.getPropagationChannel(1);

        assertTrue( checkRectangle(diskRect0, rect0) && checkRectangle(diskRect45, rect45) &&
            checkRectangle(diskRect90, rect90) && checkRectangle(diskRect135, rect135) &&
            checkNA(diskNA, na) && checkChannel(diskProp, prop) );

    }

    private boolean checkRectangle(Rectangle rect1, Rectangle rect2) {
        return rect1.getHeight() == rect2.getHeight() && rect1.getWidth() == rect2.getWidth() &&
        rect1.getxTop() == rect2.getxTop() && rect1.getyTop() == rect2.getyTop();
    }

    private boolean checkNA(INumericalAperture na1, INumericalAperture na2) {
        return na1.getNA(Polarizations.pol0) == na2.getNA(Polarizations.pol0) &&
            na1.getNA(Polarizations.pol45) == na2.getNA(Polarizations.pol45) &&
            na1.getNA(Polarizations.pol90) == na2.getNA(Polarizations.pol90) &&
            na1.getNA(Polarizations.pol135) == na2.getNA(Polarizations.pol135);
    }

    private boolean checkChannel(IPropagationChannel channel1, IPropagationChannel channel2) {
        return channel1.getWavelength() == channel2.getWavelength() &&
            channel1.getCalibrationFactor(Polarizations.pol0) == channel2.getCalibrationFactor(Polarizations.pol0) &&
            channel1.getCalibrationFactor(Polarizations.pol45) == channel2.getCalibrationFactor(Polarizations.pol45) &&
            channel1.getCalibrationFactor(Polarizations.pol90) == channel2.getCalibrationFactor(Polarizations.pol90) &&
            channel1.getCalibrationFactor(Polarizations.pol135) == channel2.getCalibrationFactor(Polarizations.pol135);
        
    }
}