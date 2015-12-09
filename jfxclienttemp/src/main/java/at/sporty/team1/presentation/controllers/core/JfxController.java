package at.sporty.team1.presentation.controllers.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

/**
 * Created by sereGkaluv on 30-Oct-15.
 */
public abstract class JfxController implements IJfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private Consumer<IJfxController> _disposeFunction;

    @Override
    public void setDisposeFunction(Consumer<IJfxController> disposeFunction) {
        _disposeFunction = disposeFunction;
    }

    @Override
    public void dispose() {
        if (_disposeFunction != null) _disposeFunction.accept(this);
    }
}
