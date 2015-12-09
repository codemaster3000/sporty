package at.sporty.team1.presentation.controllers.core;

import at.sporty.team1.rmi.api.IDTO;

/**
 * Created by sereGkaluv on 28-Nov-15.
 */
public abstract class ConsumerViewController<T extends IDTO> extends JfxController {

    /**
     * Loads DTO of type <T extends IDTO> content in view as specified.
     * @param dto <T extends IDTO> object to be displayed;
     */
    public abstract void loadDTO(T dto);
}
