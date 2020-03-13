package fr.fresnel.fourPolar.core.image.polarization;

import fr.fresnel.fourPolar.core.fourPolar.IIntensityVectorIterator;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;

class IntensityVectorIterator implements IIntensityVectorIterator {
    final private IPixelCursor<UINT16> _pol0Cursor;
    final private IPixelCursor<UINT16> _pol45Cursor;
    final private IPixelCursor<UINT16> _pol90Cursor;
    final private IPixelCursor<UINT16> _pol135Cursor;

    public IntensityVectorIterator(IPixelCursor<UINT16> pol0Cursor, IPixelCursor<UINT16> pol45Cursor,
            IPixelCursor<UINT16> pol90Cursor, IPixelCursor<UINT16> pol135Cursor) {
        this._pol0Cursor = pol0Cursor;
        this._pol45Cursor = pol45Cursor;
        this._pol90Cursor = pol90Cursor;
        this._pol135Cursor = pol135Cursor;

        this._pol0Cursor.reset();
    }

    @Override
    public boolean hasNext() {
        return _pol0Cursor.hasNext();
    }

    @Override
    public IntensityVector next() {
        double intensityPol0 = this._pol0Cursor.next().value().get();
        double intensityPol45 = this._pol45Cursor.next().value().get();
        double intensityPol90 = this._pol90Cursor.next().value().get();
        double intensityPol135 = this._pol135Cursor.next().value().get();

        IntensityVector polIntensity = new IntensityVector(intensityPol0, intensityPol45, intensityPol90,
                intensityPol135);
        return polIntensity;
    }

    @Override
    public long size() {
        return _pol0Cursor.size();
    }

}