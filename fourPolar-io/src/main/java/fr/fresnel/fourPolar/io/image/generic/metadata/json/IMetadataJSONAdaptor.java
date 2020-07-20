package fr.fresnel.fourPolar.io.image.generic.metadata.json;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.exceptions.image.generic.axis.UnsupportedAxisOrder;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;

/**
 * This class works as an adaptor from/to JSON. Note that
 * {@link IMetadata#bitPerPixel()} is not adapted with this class, as this
 * parameter is a property of the image this metadata is associated with.
 */
@JsonPropertyOrder({"Axis-Order", "Dimension"})
public class IMetadataJSONAdaptor {
    @JsonProperty("Axis-Order")
    private String _axisOrder = null;

    @JsonProperty("Dimension")
    private String _dimension = null;

    /**
     * Adapts the given metadata to JSON.
     * 
     * @param metadata is the metadata instance to be adapted to JSON.
     * @throws MetadataIOIssues in case the metadata is incomplete.
     */
    public void toJSON(IMetadata metadata) throws MetadataIOIssues {
        _setAxisOrder(metadata.axisOrder());
        _setDimension(metadata.getDim());
    }

    /**
     * Creates a concrete metadata instance form the json properties.
     * 
     * @return a metadata instance from JSON information.
     * @throws MetadataIOIssues in case at least one parameter can't be read from
     *                     JSON.
     */
    public IMetadata fromJSON() throws MetadataIOIssues {
        AxisOrder axisOrder = _getAxisOrder();
        long[] dimension = _getDimension();

        if (!MetadataUtil.numAxisEqualsDimension(axisOrder, dimension)) {
            throw new MetadataIOIssues("Metadata and dimension don't match");
        }

        return new Metadata.MetadataBuilder(dimension).axisOrder(axisOrder).build();
    }

    private void _setAxisOrder(AxisOrder axisOrder) throws MetadataIOIssues {
        if (axisOrder == null){
            throw new MetadataIOIssues(MetadataIOIssues.INCOMPLETE_METADATA);
        }

        this._axisOrder = axisOrder.toString();
    }

    private void _setDimension(long[] dimension) throws MetadataIOIssues{
        if (dimension == null){
            throw new MetadataIOIssues(MetadataIOIssues.INCOMPLETE_METADATA);
        }

        this._dimension = Arrays.toString(dimension);
        
    }

    private AxisOrder _getAxisOrder() throws MetadataIOIssues {
        if (_axisOrder == null) {
            throw new MetadataIOIssues("No axis order is found from JSON.");
        }

        try {
            return AxisOrder.fromString(_axisOrder);
        } catch (UnsupportedAxisOrder e) {
            throw new MetadataIOIssues("Axis order can't be read from JSON.");
        }

    }

    private long[] _getDimension() throws MetadataIOIssues {
        if (_dimension == null) {
            throw new MetadataIOIssues("No dimension is found.");
        }

        String[] dimsAsString = _dimension.substring(1, _dimension.length() - 1).split(",");
        return Arrays.stream(dimsAsString).mapToLong(Long::parseLong).toArray();
    }
}