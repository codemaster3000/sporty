package server;

import at.sporty.team1.application.controller.real.MemberController;
import at.sporty.team1.domain.Member;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;
import org.dozer.DozerBeanMapper;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.rmi.RemoteException;
import java.util.List;


public class NewMemberTest {
	
	@Rule 
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * all Parameters in the right format
	 */
	@Test
	public void newMemberTest_1() {
        Integer id = 41;
		String fName = "Anne";
		String lName = "Tester";
		String bday = "2001-01-01";
		String email = "test001@gmx.at";
		String gender = "F";
		String address = "Street 1";
		String sport = "Soccer";
        Boolean feePaid = true;
        String team = "Soccer Damen 1";

		executeTest(fName, lName, bday, email, gender, address, sport, feePaid);
	}

	/**
	 * Bday in false format
	 * @throws Exception
	 */
	@Test
	public void newMemberTest_2() {
		String fName = "Fred";
		String lName = "Tester";
		String bday = "01.01.1900";
		String email = "test002@gmx.at";
		String gender = "F";
		String address = "Street 1";
		String sport = "Soccer";
		String team = "Incredible Kickers";
        Boolean feePaid = true;

        MemberDTO activeMemberDTO = new MemberDTO();
        activeMemberDTO.setFirstName(fName);
        activeMemberDTO.setLastName(lName);
        activeMemberDTO.setDateOfBirth(bday);
        activeMemberDTO.setEmail(email);
        activeMemberDTO.setGender(gender);
        activeMemberDTO.setAddress(address);
//        _activeMemberDTO.setDepartmentId(sport);
        activeMemberDTO.setIsFeePaid(feePaid);
        

        try {
            createNewMemberTest(activeMemberDTO);
        } catch (RemoteException | ValidationException | NotAuthorisedException e) {
            //dumb check to be sure that anything is ok
            Assert.assertNotNull(e);
        }
    }

	/**
	 * Bday in right format
	 * @throws Exception
	 */
	@Test
	public void newMemberTest_3() {
		String fName = "Nyan";
		String lName = "Cat";
		String bday = "1900-12-12";
		String email = "naynNyanNyanNyanNyanNyan@nyan.cat";
		String gender = "F";
		String address = "Street 1";
		String sport = "Soccer";
        Boolean feePaid = true;

		executeTest(fName, lName, bday, email, gender, address, sport, feePaid);
	}

	private void executeTest(
		String fName,
		String lName,
		String bday,
		String email,
		String gender,
		String address,
		String sport,
        Boolean feePaid
	) {

		MemberDTO activeMemberDTO = new MemberDTO();
		activeMemberDTO.setFirstName(fName);
        activeMemberDTO.setLastName(lName);
        activeMemberDTO.setDateOfBirth(bday);
        activeMemberDTO.setEmail(email);
        activeMemberDTO.setGender(gender);
        activeMemberDTO.setAddress(address);
//        _activeMemberDTO.setDepartmentId(sport);
        activeMemberDTO.setIsFeePaid(feePaid);

        try {
            createNewMemberTest(activeMemberDTO);
        } catch (RemoteException | ValidationException | NotAuthorisedException e) {
            e.printStackTrace();
            Assert.fail("ERROR WHILE CREATING NEW USER");
        }

		Member receivedMember = findMemberByEmail(activeMemberDTO);

		//check if member was created or saved
		Assert.assertNotNull(receivedMember);

		MemberDTO receivedMemberDTO = new DozerBeanMapper().map(receivedMember, MemberDTO.class);

		//checking data
		Assert.assertNotNull(receivedMemberDTO);
		Assert.assertTrue(fName.equals(receivedMemberDTO.getFirstName()));
		Assert.assertTrue(lName.equals(receivedMemberDTO.getLastName()));
		Assert.assertTrue(bday.equals(receivedMemberDTO.getDateOfBirth()));
		Assert.assertTrue(email.equals(receivedMemberDTO.getEmail()));
		Assert.assertTrue(gender.equals(receivedMemberDTO.getGender()));
		Assert.assertTrue(address.equals(receivedMemberDTO.getAddress()));
        Assert.assertTrue(feePaid.equals(receivedMemberDTO.getIsFeePaid()));

		//removing and checking if removed
        try {
            removeFromDB(receivedMemberDTO.getMemberId());
        } catch (RemoteException | UnknownEntityException e) {
			e.printStackTrace();
			Assert.fail("NOT REMOVED FROM DB");
		}
		Assert.assertNull(findMemberByEmail(receivedMemberDTO));
	}

	private void createNewMemberTest(MemberDTO memberDTO) throws RemoteException, ValidationException, NotAuthorisedException {
        new MemberController().createOrSaveMember(memberDTO, null);
	}

	private Member findMemberByEmail(MemberDTO memberDTO) {
        List<Member> members = PersistenceFacade.getNewMemberDAO().findByEmail(memberDTO.getEmail());

        return members != null && members.size() == 1 ? members.get(0) : null;
	}

	private void removeFromDB(Integer memberId) throws RemoteException, UnknownEntityException {
	}
}
