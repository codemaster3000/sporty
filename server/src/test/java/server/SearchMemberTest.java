package server;

import at.sporty.team1.rmi.exceptions.ValidationException;
import org.junit.Assert;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import at.sporty.team1.application.controller.MemberController;
import at.sporty.team1.rmi.dtos.MemberDTO;


public class SearchMemberTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	/**
	 * One Member found with fName Claudia
	 */
	@Test
	public void searchMemberTest_1() {

		String searchString = "Claudia";
		List<MemberDTO> members = testSearchMember(searchString);

		Assert.assertNotNull(members);
		Assert.assertEquals(1, members.size());

	}
	
	/**
	 * One Member found with lName Field
	 */
	@Test
	public void searchMemberTest_2() {

		String searchString = "Field";
		List<MemberDTO> members = testSearchMember(searchString);

		Assert.assertNotNull(members);
		Assert.assertEquals(1, members.size());

	}
	
	/**
	 * One Member found with bDay 2001-01-01
	 */
	@Test
	public void searchMemberTest_3() {

		String searchString = "1950-05-10";
		List<MemberDTO> members = null;

		try {
			MemberController mem = new MemberController();
			members = mem.searchMembersByDateOfBirth(searchString);
		} catch (RemoteException | ValidationException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(members);
		Assert.assertTrue(members.size() > 0);

	}

	private List<MemberDTO> testSearchMember(String searchString){

		try {
			MemberController mem = new MemberController();
			return mem.searchMembersByNameString(searchString);
		} catch (RemoteException | ValidationException e) {
			e.printStackTrace();
			return null;
		}

	}
}
