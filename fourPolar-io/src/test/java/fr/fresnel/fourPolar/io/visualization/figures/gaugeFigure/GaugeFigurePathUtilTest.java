package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;

public class GaugeFigurePathUtilTest {

}

class GFPUTDummyGaugeFigure implements IGaugeFigure {
    AngleGaugeType _type;
    ICapturedImageFileSet _fileSet;
    GaugeFigureLocalization _localization;
    int _channel;

    public GFPUTDummyGaugeFigure(AngleGaugeType _type, ICapturedImageFileSet _fileSet,
            GaugeFigureLocalization _localization, int _channel) {
        this._type = _type;
        this._fileSet = _fileSet;
        this._localization = _localization;
        this._channel = _channel;
    }

    @Override
    public AngleGaugeType getGaugeType() {
        return _type;
    }

    @Override
    public GaugeFigureLocalization getLocalization() {
        return _l;
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getChannel() {
        // TODO Auto-generated method stub
        return 0;
    }
    
}