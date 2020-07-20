package fr.fresnel.fourPolar.io.image.generic.metadata.json;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.exceptions.image.generic.axis.UnsupportedAxisOrder;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;

/**
 * This class works as an adaptor from/to JSON. Note that
 * {@link IMetadata#bitPerPixel()} is not adapted with this class, as this
 * parameter is a property of the image this metadata is associated with.
 */
@JsonPropertyOrder({})
public class IMetadataJSONAdaptor {
    @JsonProperty("Axis-Order")
    private String _axisOrder = null;

    @JsonProperty("Axis-Order")
    private long[] _dimension = null;

    /**
     * Adapts the given metadata to JSON.
     * 
     * @param metadata
     */
    public void toJSON(IMetadata metadata) {
        _setAxisOrder(metadata.axisOrder());
        _setDimension(metadata.getDim());
    }

    /**
     * Creates a concrete metadata instance form the json properties.
     * 
     * @return a metadata instance from JSON information.
     * @throws IOException in case at least one parameter can't be read from
     *                     JSON.
     */
    public IMetadata fromJSON() throws IOException {
        AxisOrder axisOrder = _getAxisOrder();
        long[] dimension = _getDimension();

        if (!MetadataUtil.numAxisEqualsDimension(axisOrder, dimension)) {
            throw new IOException("Metadata and dimension don't match");
        }

        return new Metadata.MetadataBuilder(dimension).axisOrder(axisOrder).build();
    }

    private void _setAxisOrder(AxisOrder axisOrder) {
        this._axisOrder = axisOrder.toString();
    }

    private void _setDimension(long[] dimension) {
        this._dimension = dimension;
    }

    private AxisOrder _getAxisOrder() throws IOException {
        if (_axisOrder == null) {
            throw new IOException("No axis order is found.");
        }

        try {
            return AxisOrder.fromString(_axisOrder);
        } catch (UnsupportedAxisOrder e) {
            throw new IOException("Axis order can't be read from JSON.");
        }

    }

    private long[] _getDimension() throws IOException {
        if (_dimension == null) {
            throw new IOException("No dimension is found.");
        }

        return _dimension;
    }
}