package at.sporty.team1.application.controller.ejb;

import at.sporty.team1.application.controller.real.InitializationController;
import at.sporty.team1.application.controller.real.api.IController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Created by sereGkaluv on 14-Dec-15.
 */
@Local
@Startup
@Singleton
public class InitializationControllerEJBAdapter implements IController {
    private static final long serialVersionUID = 1L;
    private InitializationController _initializationController;

    @PostConstruct
    public void initialize() {
        _initializationController = InitializationController.initialize();
    }

    @PreDestroy
    public void terminate() {
        _initializationController.shutdown();
    }
}
