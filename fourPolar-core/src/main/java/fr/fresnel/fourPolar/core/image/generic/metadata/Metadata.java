package fr.fresnel.fourPolar.core.image.generic.metadata;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;

/**
 * Implements the metadata. Note that all current and future metadata info are
 * set to default values.
 */
public class Metadata implements IMetadata {
    private final AxisOrder _axisOrder;
    private final int _numChannels;
    private final PixelTypes _pixelType;
    private final long[] _dim;

    /**
     * Build metadata.
     */
    public static class MetadataBuilder {
        private AxisOrder _axisOrder = AxisOrder.NoOrder;
        private int _numChannels = 0;
        private final PixelTypes _pixelType;
        private final long[] _dim;

        /**
         * Build metadata from scratch.
         * 
         * @param pixelType is the desired pixel type.
         * @param dim is the dimension of the underlying image.
         */
        public MetadataBuilder(PixelTypes pixelType, long[] dim) {
            this._pixelType = Objects.requireNonNull(pixelType, "pixelType can't be null.");
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
            this._pixelType = metadata.getPixelType();
            this.axisOrder(metadata.axisOrder());
            this.numChannels(metadata.numChannels());
        }

        public MetadataBuilder axisOrder(AxisOrder axisOrder) {
            if (AxisOrder.getNumDefinedAxis(axisOrder) != this._dim.length){
                throw new IllegalArgumentException("Number of axis does not equal image dimension.");
            }

            this._axisOrder = Objects.requireNonNull(axisOrder, "axisOrder must not be null");
            return this;
        }

        public MetadataBuilder numChannels(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Number of channels must be greater than equal zero.");
            }
            this._numChannels = n;
            return this;
        }

        public IMetadata build() {
            if (this._axisOrder != AxisOrder.NoOrder
                    && (this._numChannels > 0 && AxisOrder.getChannelAxis(this._axisOrder) < 0)) {
                throw new IllegalArgumentException("No channel in AxisOrder for channels > 1");
            }
            return new Metadata(this);
        }
    }

    private Metadata(MetadataBuilder builder) {
        this._axisOrder = builder._axisOrder;
        this._numChannels = builder._numChannels;
        this._dim = builder._dim;
        this._pixelType = builder._pixelType;
    }

    @Override
    public AxisOrder axisOrder() {
        return this._axisOrder;
    }

    @Override
    public int numChannels() {
        if (this._axisOrder == AxisOrder.NoOrder) {
            return -1;
        }

        return this._numChannels;
    }

    @Override
    public PixelTypes getPixelType() {
        return this._pixelType;
    }

    @Override
    public long[] getDim() {
        return this._dim;
    }

}