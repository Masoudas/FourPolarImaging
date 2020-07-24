package fr.fresnel.fourPolar.io.imagingSetup;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.Channel;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.shape.IBoxShape;
import fr.fresnel.fourPolar.core.shape.ShapeFactory;

/**
 * Note that in all the test, the files are created in the 4POLAR subfolder of
 * the root.
 */
public class FourPolarImagingSetupFromYamlTest {

    @Test
    public void write_WriteOneCameraOneChannel_FileGeneratedinResourceFolder()
            throws JsonGenerationException, JsonMappingException, IOException {
        IFourPolarImagingSetup imagingSetup = new DummyFPSetup();

        imagingSetup.setCameras(Cameras.One);

        IBoxShape rect0 = ShapeFactory.closedBox(new long[] { 0, 0 }, new long[] { 127, 127 }, AxisOrder.XY);
        IBoxShape rect45 = ShapeFactory.closedBox(new long[] { 127, 0 }, new long[] { 255, 127 }, AxisOrder.XY);
        IBoxShape rect90 = ShapeFactory.closedBox(new long[] { 0, 127 }, new long[] { 127, 255 }, AxisOrder.XY);
        IBoxShape rect135 = ShapeFactory.closedBox(new long[] { 127, 127 }, new long[] { 255, 255 },
                AxisOrder.XY);

        FieldOfView fov = new FieldOfView(rect0, rect45, rect90, rect135);
        imagingSetup.setFieldOfView(fov);

        INumericalAperture na = new NumericalAperture(1.45, 5.65, 3.4342, 1.3434);
        imagingSetup.setNumericalAperture(na);

        Channel prop = new Channel(1e-9, 1.45, 1.54, 1.34, 3.11);
        imagingSetup.setChannel(1, prop);

        File rootFolder = new File(FourPolarImagingSetupToYamlTest.class.getResource("").getPath(), "FromYaml");
        rootFolder.mkdir();
        FourPolarImagingSetupToYaml writer = new FourPolarImagingSetupToYaml(imagingSetup, rootFolder);
        writer.write();

        /**
         * Reading from disk.
         */
        IFourPolarImagingSetup diskImagingSetup = new DummyFPSetup();
        FourPolarImagingSetupFromYaml reader = new FourPolarImagingSetupFromYaml(rootFolder);
        reader.read(diskImagingSetup);

        IBoxShape diskRect0 = diskImagingSetup.getFieldOfView().getFoV(Polarization.pol0);
        IBoxShape diskRect45 = diskImagingSetup.getFieldOfView().getFoV(Polarization.pol45);
        IBoxShape diskRect90 = diskImagingSetup.getFieldOfView().getFoV(Polarization.pol90);
        IBoxShape diskRect135 = diskImagingSetup.getFieldOfView().getFoV(Polarization.pol135);
        INumericalAperture diskNA = diskImagingSetup.getNumericalAperture();
        IChannel channel = diskImagingSetup.getChannel(1);

        assertTrue(checkRectangle(diskRect0, rect0) && checkRectangle(diskRect45, rect45)
                && checkRectangle(diskRect90, rect90) && checkRectangle(diskRect135, rect135) && checkNA(diskNA, na)
                && checkChannel(channel, prop));

    }

    private boolean checkRectangle(IBoxShape rect1, IBoxShape rect2) {
        return Arrays.equals(rect1.min(), rect2.min()) && Arrays.equals(rect1.max(), rect2.max());
    }

    private boolean checkNA(INumericalAperture na1, INumericalAperture na2) {
        return na1.getNA(Polarization.pol0) == na2.getNA(Polarization.pol0)
                && na1.getNA(Polarization.pol45) == na2.getNA(Polarization.pol45)
                && na1.getNA(Polarization.pol90) == na2.getNA(Polarization.pol90)
                && na1.getNA(Polarization.pol135) == na2.getNA(Polarization.pol135);
    }

    private boolean checkChannel(IChannel channel1, IChannel channel2) {
        return channel1.getWavelength() == channel2.getWavelength()
                && channel1.getCalibrationFactor(Polarization.pol0) == channel2.getCalibrationFactor(Polarization.pol0)
                && channel1.getCalibrationFactor(Polarization.pol45) == channel2
                        .getCalibrationFactor(Polarization.pol45)
                && channel1.getCalibrationFactor(Polarization.pol90) == channel2
                        .getCalibrationFactor(Polarization.pol90)
                && channel1.getCalibrationFactor(Polarization.pol135) == channel2
                        .getCalibrationFactor(Polarization.pol135);

    }
}

class DummyFPSetup implements IFourPolarImagingSetup {
    private Cameras cameras;
    private Hashtable<Integer, IChannel> channels = new Hashtable<>();
    private INumericalAperture na;
    private IFieldOfView fov;

    @Override
    public Cameras getCameras() {
        return cameras;
    }

    @Override
    public void setCameras(Cameras cameras) throws IllegalArgumentException {
        this.cameras = cameras;
    }

    @Override
    public IChannel getChannel(int channel) throws IllegalArgumentException {
        return channels.get(channel);
    }

    @Override
    public void setChannel(int channel, IChannel propagationChannel) throws IllegalArgumentException {
        channels.put(channel, propagationChannel);
    }

    @Override
    public int getNumChannel() {
        return this.channels.size();
    }

    @Override
    public INumericalAperture getNumericalAperture() {
        return na;
    }

    @Override
    public void setNumericalAperture(INumericalAperture na) {
        this.na = na;
    }

    @Override
    public IFieldOfView getFieldOfView() {
        return this.fov;
    }

    @Override
    public void setFieldOfView(IFieldOfView fov) throws IllegalArgumentException {
        this.fov = fov;
    }

}