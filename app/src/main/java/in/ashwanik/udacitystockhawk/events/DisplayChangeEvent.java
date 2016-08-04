package in.ashwanik.udacitystockhawk.events;

/**
 * Created by AshwaniK on 2/28/2016.
 */
public class DisplayChangeEvent {

    public boolean isShowChange() {
        return isShowChange;
    }

    private boolean isShowChange;

    public DisplayChangeEvent(boolean isShowChange) {
        this.isShowChange = isShowChange;
    }
}
