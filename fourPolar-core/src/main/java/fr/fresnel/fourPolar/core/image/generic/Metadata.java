package fr.fresnel.fourPolar.core.image.generic;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;

/**
 * Implements the metadata info for the ImgLib2 image model. Note that if 
 * additional metadata fields are added, they should have default values. Otherwise,
 * unexpected results may happen when there's dependency on metadata.
 */
class ImgLib2Metadata implements IMetadata {
    private AxisOrder _axisOrder;

    public class ImgLib2MetadataBuilder {
        private AxisOrder _axisOrder = AxisOrder.NoOrder;

        public ImgLib2MetadataBuilder() {

        }

        public void axisOrder(AxisOrder axisOrder){
            this._axisOrder = Objects.requireNonNull(axisOrder, "axisOrder must not be null")
        }

        public ImgLib2Metadata build() {
            return new ImgLib2Metadata(this);
        }
    }

    private ImgLib2Metadata(ImgLib2MetadataBuilder builder) {
        this._axisOrder = builder._axisOrder;
    }

    @Override
    public AxisOrder axisOrder() {
        return this._axisOrder;
    }

}