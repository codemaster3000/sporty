package at.sporty.team1.application.controller;

import at.sporty.team1.application.controller.ejb.EJBControllerFacade;
import at.sporty.team1.application.controller.rmi.RMIControllerFacade;

/**
 * Created by sereGkaluv on 10-Dec-15.
 *
 * This is a utility class. No instantiating is available.
 */
public class FacadeManager {

    private FacadeManager(){
    }

    /**
     * Returns new instance of the RMIControllerFacade, that provides access
     * to server functionality based on active AccessPolicy in RMI mode.
     *
     * @return new instance of RMIControllerFacade
     */
    public RMIControllerFacade getRMIControllerFacade() {
        return new RMIControllerFacade();
    }

    /**
     * Returns new instance of the EJBControllerFacade, that provides access
     * to server functionality based on active AccessPolicy in EJB mode.
     *
     * @return new instance of EJBControllerFacade
     */
    public EJBControllerFacade getEJBControllerFacade() {
        return new EJBControllerFacade();
    }
}
