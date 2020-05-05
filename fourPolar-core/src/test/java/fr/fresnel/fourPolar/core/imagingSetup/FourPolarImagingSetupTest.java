package fr.fresnel.fourPolar.core.imagingSetup;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.channel.Channel;

public class FourPolarImagingSetupTest {
    @Test
    public void instance_setCompleteSet_ReturnsSameParamsForAllInstances() {
        Channel c1 = new Channel(1, 1, 1, 1, 1);
        Channel c2 = new Channel(2, 2, 2, 2, 2);

        FourPolarImagingSetup setup = FourPolarImagingSetup.instance();

        setup.setCameras(Cameras.One);
        setup.setChannel(1, c1);
        setup.setChannel(2, c2);

        FourPolarImagingSetup sameSetup = FourPolarImagingSetup.instance();

        assertTrue(sameSetup.getCameras() == Cameras.One && sameSetup.getChannel(1).equals(c1)
                && sameSetup.getChannel(2).equals(c2) && sameSetup.getNumChannel() == 2);
    }

    @Test
    public void instance_DuplicateChannel_ThrowsIllegalArgumentException() {
        Channel c1 = new Channel(1, 1, 1, 1, 1);

        FourPolarImagingSetup setup = FourPolarImagingSetup.instance();

        // This is because test reources are shared!
        // Gets confused with other tests!
        assertThrows(IllegalArgumentException.class, () -> {
            setup.setChannel(3, c1);});
    }
}