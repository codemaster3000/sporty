package at.sporty.team1.communication.facades.api;

import at.sporty.team1.shared.exceptions.RemoteCommunicationException;

/**
 * Created by sereGkaluv on 12-Dec-15.
 */
public interface ICommunicationFacade {

    /**
     * Returns communication type neutral instance of IMemberControllerUniversal.
     *
     * @return instance of IMemberControllerUniversal.
     * @throws RemoteCommunicationException
     */
    IMemberControllerUniversal lookupForMemberController()
    throws RemoteCommunicationException;

    /**
     * Returns communication type neutral instance of ITeamControllerUniversal.
     *
     * @return instance of ITeamControllerUniversal.
     * @throws RemoteCommunicationException
     */
    ITeamControllerUniversal lookupForTeamController()
    throws RemoteCommunicationException;

    /**
     * Returns communication type neutral instance of IDepartmentControllerUniversal.
     *
     * @return instance of IDepartmentControllerUniversal.
     * @throws RemoteCommunicationException
     */
    IDepartmentControllerUniversal lookupForDepartmentController()
    throws RemoteCommunicationException;

    /**
     * Returns communication type neutral instance of ILoginControllerUniversal.
     *
     * @return instance of ILoginControllerUniversal.
     * @throws RemoteCommunicationException
     */
    ILoginControllerUniversal lookupForLoginController()
    throws RemoteCommunicationException;

    /**
     * Returns communication type neutral instance of ITournamentControllerUniversal.
     *
     * @return instance of ITournamentControllerUniversal.
     * @throws RemoteCommunicationException
     */
    ITournamentControllerUniversal lookupForTournamentController()
    throws RemoteCommunicationException;

    /**
     * Returns communication type neutral instance of INotificationControllerUniversal.
     *
     * @return instance of INotificationControllerUniversal.
     * @throws RemoteCommunicationException
     */
    INotificationControllerUniversal lookupForNotificationController()
    throws RemoteCommunicationException;
}
