package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;

public class ImgLib2Metadata implements IMetadata {
    private AxisOrder _axisOrder;

    public class ImgLib2MetadataBuilder {
        private AxisOrder _axisOrder;

        public ImgLib2MetadataBuilder(AxisOrder axisOrder){
            this._axisOrder = Objects.requireNonNull(axisOrder, "axisOrder must not be null")
        }

        public ImgLib2Metadata build(){
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