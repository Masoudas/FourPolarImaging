package fr.fresnel.fourPolar.core.image.generic.pixel.types;

public enum PixelTypes {
    FLOAT_32, RGB_16, UINT_16;

    /**
     * Returns an appropriate zero value instance of the {@link PixelTypes}
     * 
     * @return
     */
    public static PixelType create(PixelTypes type) {
        switch (type) {
            case FLOAT_32:
                return new Float32(Float32.MIN_VAL);

            case UINT_16:
                return new UINT16(UINT16.MIN_VAL);

            case RGB_16:
                return new ARGB8(ARGB8.MIN_COLOR, ARGB8.MIN_COLOR, ARGB8.MIN_COLOR, ARGB8.MIN_ALPHA);

            default:
                return null;
        }
    }
}