package fr.fresnel.fourPolar.core.util.colorMap;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;

/**
 * An interface for modelling a color map. A color map returns a proper color
 * (modelled using {@link RGB16}) for a given index value (normally double).
 */
public interface ColorMap {
    /**
     * Returns a color from the color map with the given val, as val / (max - min) ratio.
     * 
     */
    public RGB16 getColor(double min, double max, double val);

    /**
     * Returns the name of the underlying color map.
     */
    public String getColorMap();


}