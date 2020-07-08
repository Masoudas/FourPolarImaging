package fr.fresnel.fourPolar.core.image.vector;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import fr.fresnel.fourPolar.core.image.vector.filter.Filter;

/**
 * A concrete implementation of {@link CompositeFilter}.
 */
class FiltersComposite implements FilterComposite {
    private final String _id;

    private final String _xStart;
    private final String _yStart;
    private final String _widthPercent;
    private final String _heightPercent;

    private final List<Filter> _filters;

    public FiltersComposite(IFilterCompositeBuilder builder) {
        _id = builder.id();

        _xStart = _createStartCoordinateString(builder.xStart());
        _yStart = _createStartCoordinateString(builder.yStart());

        _widthPercent = _createPercentageString(builder.widthPercent());
        _heightPercent = _createPercentageString(builder.heightPercent());

        _filters = builder.filters();
    }

    private String _createStartCoordinateString(int start) {
        if (start < 0) {
            return null;
        }
        return String.valueOf(start);
    }

    private String _createPercentageString(int percent) {
        if (percent < 0) {
            return null;
        }
        return String.valueOf(percent) + "%";
    }

    @Override
    public String id() {
        return _id;
    }

    @Override
    public Optional<String> xStart() {
        return Optional.ofNullable(_xStart);
    }

    @Override
    public Optional<String> yStart() {
        return Optional.ofNullable(_yStart);
    }

    @Override
    public Optional<String> widthPercent() {
        return Optional.ofNullable(_widthPercent);
    }

    @Override
    public Optional<String> heightPercent() {
        return Optional.ofNullable(_heightPercent);
    }

    @Override
    public Iterator<Filter> filters() {
        return _filters.iterator();
    }
}