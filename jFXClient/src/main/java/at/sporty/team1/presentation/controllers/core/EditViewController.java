package at.sporty.team1.presentation.controllers.core;

import at.sporty.team1.rmi.api.IDTO;

/**
 * Created by sereGkaluv on 28-Nov-15.
 */
public abstract class EditViewController<T extends IDTO> extends ConsumerViewController<T> {

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
