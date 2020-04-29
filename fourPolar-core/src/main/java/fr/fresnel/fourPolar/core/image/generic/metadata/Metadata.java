package fr.fresnel.fourPolar.core.image.generic.metadata;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;

/**
 * Implements the metadata. Note that all current and future metadata
 * info are set to default values.
 */
public class Metadata implements IMetadata {
    private AxisOrder _axisOrder;

    /**
     * Build metadata.
     */
    public class MetadataBuilder {
        private AxisOrder _axisOrder = AxisOrder.NoOrder;

        public MetadataBuilder() {

        }

        public MetadataBuilder axisOrder(AxisOrder axisOrder){
            this._axisOrder = Objects.requireNonNull(axisOrder, "axisOrder must not be null");
            return this;
        }

        public IMetadata build() {
            return new Metadata(this);
        }
    }

    private Metadata(MetadataBuilder builder) {
        this._axisOrder = builder._axisOrder;
    }

    @Override
    public AxisOrder axisOrder() {
        return this._axisOrder;
    }

}