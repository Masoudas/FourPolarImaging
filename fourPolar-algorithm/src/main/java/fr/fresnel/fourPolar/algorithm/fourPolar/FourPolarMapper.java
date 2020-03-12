package fr.fresnel.fourPolar.algorithm.fourPolar;

import java.util.NoSuchElementException;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.IteratorMissMatch;
import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.converters.ImpossibleOrientationVector;
import fr.fresnel.fourPolar.algorithm.fourPolar.converters.IIntensityToOrientationConverter;
import fr.fresnel.fourPolar.core.fourPolar.IOrientationVectorIterator;
import fr.fresnel.fourPolar.core.fourPolar.IPolarizationsIntensityIterator;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;

/**
 * An implementation of the 4Polar algorithm, which can be used to convert an
 * intensity iterator to an orientation iterator.
 * 
 * Note that essentialy this implementation can be used to convert all
 * intensities associated with one channel.
 * 
 */
public class FourPolarMapper {
    final private IIntensityToOrientationConverter _converter;

    /**
     * @param converter is the intensity converter for one class.
     */
    public FourPolarMapper(IIntensityToOrientationConverter converter) {
        this._converter = converter;
    }

    /**
     * Iterates over the given set of intensities and puts the calculated
     * orientation vector into the orientation set via its iterator.
     * 
     * @param intensityIterator   is the intensity set iterator.
     * @param orientationIterator is the orientation vector set iterator.
     * @throws ImpossibleOrientationVector
     */
    public void map(
        IPolarizationsIntensityIterator intensityIterator, IOrientationVectorIterator orientationIterator)
        throws IteratorMissMatch, ImpossibleOrientationVector {
        try {
            while (intensityIterator.hasNext()) {
                IPolarizationsIntensity intensity = intensityIterator.next();

                IOrientationVector orientationVector = _converter.convert(intensity);

                orientationIterator.next();
                orientationIterator.set(orientationVector);
            }

        } catch (NoSuchElementException e) {
            throw new IteratorMissMatch("Polarization iterator has more elements than orientation iterator.");
        }
        
        if (orientationIterator.hasNext()) {
            throw new IteratorMissMatch("Orientation iterator has more elements than polarization iterator.");
        }
    }

}