package at.sporty.team1.presentation.controllers.core;

import at.sporty.team1.rmi.api.IDTO;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Created by sereGkaluv on 28-Nov-15.
 */
public abstract class EditViewController<T extends IDTO> extends ConsumerViewController<T> {

    public final SimpleBooleanProperty IN_WORK_PROPERTY = new SimpleBooleanProperty(true);

    /**
     * Returns header text for stage or dialog.
     *
     * @return String text header.
     */
    public abstract String getHeaderText();

    /**
     * Saves DTO changes.
     *
     * @return DTO Saved(updated) dto object.
     */
    public abstract T saveDTO();
}
