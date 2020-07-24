package fr.fresnel.fourPolar.core.image.generic.metadata;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.util.image.generic.metadata.MetadataUtil;

/**
 * Implements the metadata. Note that all current and future metadata info are
 * set to default values.
 */
public class Metadata implements IMetadata {
    private final AxisOrder _axisOrder;
    private final int _numChannels;
    private final long[] _dim;
    private final int _bitPerPixel;

    /**
     * Build metadata.
     */
    public static class MetadataBuilder {
        private final long[] _dim;
        private AxisOrder _axisOrder = AxisOrder.NoOrder;
        private int _bitPerPixel;

        /**
         * Build metadata from scratch.
         * 
         * @param pixelType is the desired pixel type.
         * @param dim       is the dimension of the underlying image.
         */
        public MetadataBuilder(long[] dim) {
            this._dim = Objects.requireNonNull(dim, "dim can't be null.");
        }

        /**
         * Build metadata, starting from another metadata, but optionally change other
         * parameters.
         * 
         * @param metadata
         */
        public MetadataBuilder(IMetadata metadata) {
            this._dim = metadata.getDim();
            this.axisOrder(metadata.axisOrder());
        }

        public MetadataBuilder axisOrder(AxisOrder axisOrder) {
            if (axisOrder != AxisOrder.NoOrder && !MetadataUtil.numAxisEqualsDimension(axisOrder, this._dim)) {
                throw new IllegalArgumentException("Number of axis does not equal image dimension.");
            }

            this._axisOrder = Objects.requireNonNull(axisOrder, "axisOrder must not be null");
            return this;
        }

        /**
         * Set bit per pixel directly. There's no guarantee that the underlying image
         * would have the same bit-depth as specified. Use
         * {@link #bitPerPixel(PixelTypes)} for known types.
         */
        public MetadataBuilder bitPerPixel(int n) {
            this._bitPerPixel = n;

            return this;
        }

        /**
         * Set bit per pixel from the {@link PixelTypes}.
         */
        public MetadataBuilder bitPerPixel(PixelTypes pixelType) {
            if (pixelType == PixelTypes.UINT_16) {
                this._bitPerPixel = 16;
            } else if (pixelType == PixelTypes.FLOAT_32) {
                this._bitPerPixel = 32;
            } else if (pixelType == PixelTypes.ARGB_8) {
                this._bitPerPixel = 8;
            }

            return this;
        }

        public IMetadata build() {
            return new Metadata(this);
        }
    }

    private Metadata(MetadataBuilder builder) {
        this._axisOrder = builder._axisOrder;
        this._dim = builder._dim;
        this._bitPerPixel = builder._bitPerPixel;

        if (this._axisOrder.c_axis > 0) {
            this._numChannels = (int) this._dim[this._axisOrder.c_axis];
        } else {
            this._numChannels = 0;
        }
    }

    @Override
    public AxisOrder axisOrder() {
        return this._axisOrder;
    }

    @Override
    public int numChannels() {
        return this._numChannels;
    }

    @Override
    public long[] getDim() {
        return this._dim.clone();
    }

    @Override
    public int bitPerPixel() {
        return this._bitPerPixel;
    }

}