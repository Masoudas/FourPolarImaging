package fr.fresnel.fourPolar.core.image.generic.pixel.types.color;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;

/**
 * Blends colors using screen method, as described in
 * {@linkplain https://en.wikipedia.org/wiki/Blend_modes}.
 */
public class ScreenColorBlender implements ColorBlender {

    @Override
    public void blend(ARGB8 base, ARGB8 blendColor) {
        int blendedR = _screenBlend(base.getR(), blendColor.getR());
        int blendedG = _screenBlend(base.getG(), blendColor.getG());
        int blendedB = _screenBlend(base.getB(), blendColor.getB());

        base.set(blendedR, blendedG, blendedB, base.getAlpha());
    }

    private int _screenBlend(int baseColor, int blendColor){
        double baseAsDouble = ((double)baseColor) / 255d;
        double blendAsDouble = ((double)blendColor) / 255d;

        double screenBlendFactor = 1 - (1 - baseAsDouble) * (1 - blendAsDouble);
        return (int)(screenBlendFactor * 255);
    }

}