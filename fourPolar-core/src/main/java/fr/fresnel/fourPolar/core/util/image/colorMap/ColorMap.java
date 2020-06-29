package fr.fresnel.fourPolar.core.util.image.colorMap;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;

/**
 * An interface for modelling a color map. A color map returns a proper color
 * (modelled using {@link ARGB8}) for a given index value (normally double).
 */
public interface ColorMap {
    /**
     * Returns a color from the color map with the given val, as val / (max - min) ratio.
     * 
     */
    public ARGB8 getColor(double min, double max, double val);

    /**
     * Returns the name of the underlying color map.
     */
    public String getColorMap();


}