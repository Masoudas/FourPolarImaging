package fr.fresnel.fourPolar.io.fourPolar.propagationDb;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.fourPolar.PropagationChannelNotInDatabase;
import fr.fresnel.fourPolar.core.physics.channel.IPropagationChannel;
import fr.fresnel.fourPolar.core.physics.channel.PropagationChannel;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;

public class XMLOpticalPropagationDBTest {

    @Test
    public void search_DatabaseWithTwoPropagations_ReturnsThePropagation() throws PropagationChannelNotInDatabase {
        PropagationChannel channel1 = new PropagationChannel(1e-9, 1, 2, 3, 4);
        IOpticalPropagation propagation1 = createOpticalPropagation(channel1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                14, 15, 16);

        PropagationChannel channel2 = new PropagationChannel(10e-9, 5, 6, 7, 8);
        IOpticalPropagation propagation2 = createOpticalPropagation(channel2, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8,
                0.9, 0.10, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16);

        XMLOpticalPropagationDB db = new XMLOpticalPropagationDB();
        db.add(channel1, propagation1);
        db.add(channel2, propagation2);

        IOpticalPropagation propDb = db.search(channel1);

        // Checking one factor is sufficient
        assertTrue(propDb.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0) == propagation1
                .getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0));

    }

    @Test
    public void search_NonExistentPropagation_ThrowsException() throws PropagationChannelNotInDatabase {
        PropagationChannel channel1 = new PropagationChannel(1e-9, 1, 2, 3, 4);
        IOpticalPropagation propagation1 = createOpticalPropagation(channel1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                14, 15, 16);

        PropagationChannel channel2 = new PropagationChannel(10e-9, 5, 6, 7, 8);
        IOpticalPropagation propagation2 = createOpticalPropagation(channel2, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8,
                0.9, 0.10, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16);

        XMLOpticalPropagationDB db = new XMLOpticalPropagationDB();
        db.add(channel1, propagation1);
        db.add(channel2, propagation2);

        PropagationChannel nonExistentChannel = new PropagationChannel(100e-9, 5, 6, 7, 8);

        assertThrows(PropagationChannelNotInDatabase.class, () -> {
            db.search(nonExistentChannel);
        });

    }

    private IOpticalPropagation createOpticalPropagation(IPropagationChannel channel, double xx_0, double xx_45,
            double xx_90, double xx_135, double yy_0, double yy_45, double yy_90, double yy_135, double zz_0,
            double zz_45, double zz_90, double zz_135, double xy_0, double xy_45, double xy_90, double xy_135) {
        IOpticalPropagation opticalPropagation = new OpticalPropagation();

        opticalPropagation.setPropagationChannel(channel);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, xx_0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, xx_45);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, xx_90);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, xx_135);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, yy_0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, yy_45);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, yy_90);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, yy_135);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, zz_0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, zz_45);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, zz_90);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, zz_135);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, xy_0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, xy_45);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, xy_90);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, xy_135);

        return opticalPropagation;
    }
}