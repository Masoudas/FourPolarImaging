package fr.fresnel.fourPolar.core.image.generic.metadata;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;

/**
 * Implements the metadata. Note that all current and future metadata info are
 * set to default values.
 */
public class Metadata implements IMetadata {
    private AxisOrder _axisOrder;
    private int _numChannels;

    /**
     * Build metadata.
     */
    public static class MetadataBuilder {
        private AxisOrder _axisOrder = AxisOrder.NoOrder;
        private int _numChannels = -1; 
        /**
         * Build metadata from scratch.
         */
        public MetadataBuilder() {

        }

        /**
         * Build metadata, starting from another metadata, but optionally change other
         * parameters.
         * 
         * @param metadata
         */
        public MetadataBuilder(IMetadata metadata) {
            this.axisOrder(metadata.axisOrder());
        }

        public MetadataBuilder axisOrder(AxisOrder axisOrder) {
            this._axisOrder = Objects.requireNonNull(axisOrder, "axisOrder must not be null");
            return this;
        }

        public MetadataBuilder numChannels(int n) {
            this._numChannels = n;
            return this;
        }

        public IMetadata build() {
            return new Metadata(this);
        }
    }

    private Metadata(MetadataBuilder builder) {
        this._axisOrder = builder._axisOrder;
        this._numChannels = builder._numChannels;
    }

    @Override
    public AxisOrder axisOrder() {
        return this._axisOrder;
    }

   @Override
   public int numChannels() {
       if (this._axisOrder == AxisOrder.NoOrder){
           return -1;
       }

       return this._numChannels;
   }

}