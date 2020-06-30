package fr.fresnel.fourPolar.core.image.generic.pixel.types.color;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;

/**
 * Blends colors using screen method, as described in
 * {@linkplain https://en.wikipedia.org/wiki/Blend_modes}.
 * The alpha value of base is not changed with this method.
 */
public class MultiplyColorBlender implements ColorBlender {

    @Override
    public void blend(ARGB8 base, ARGB8 blendColor) {
        int blendedR = _multiplyColors(base.getR(), blendColor.getR());
        int blendedG = _multiplyColors(base.getG(), blendColor.getG());
        int blendedB = _multiplyColors(base.getB(), blendColor.getB());

        base.set(blendedR, blendedG, blendedB, base.getAlpha());
    }

    private int _multiplyColors(int baseColor, int blendColor){
        double baseAsDouble = ((double)baseColor) / 255d;
        double blendAsDouble = ((double)blendColor) / 255d;
        return  (int)(baseAsDouble * blendAsDouble * 255);

    }
    
}