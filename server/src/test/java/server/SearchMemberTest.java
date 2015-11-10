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
		List<MemberDTO> members = testSearchMember(searchString,false, false);

		Assert.assertNotNull(members);
		Assert.assertEquals(1, members.size());

	}
	
	/**
	 * One Member found with lName Field
	 */
	@Test
	public void searchMemberTest_2() {

		String searchString = "Field";
		List<MemberDTO> members = testSearchMember(searchString, false, false);

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
			members = mem.searchMembersByDateOfBirth(searchString, false, false);
		} catch (RemoteException | ValidationException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(members);
		Assert.assertTrue(members.size() > 0);

	}
	
	/**
	 * Search for all Members with 'a'
	 */
	@Test
	public void searchMemberTest_4() {

		String searchString = "a";
		List<MemberDTO> members = null;

		try {
			MemberController mem = new MemberController();
			members = mem.searchMembersByNameString(searchString, false, false);
		} catch (RemoteException | ValidationException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(members);
		Assert.assertTrue(members.size() > 0);
	}
	
	/**
	 * Search for all Members with 'Fred' and fee paid
	 */
	@Test
	public void searchMemberTest_5() {

		String searchString = "f";
		List<MemberDTO> members = null;

		try {
			MemberController mem = new MemberController();
			members = mem.searchMembersByNameString(searchString, false, true);
		} catch (RemoteException | ValidationException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(members);
		Assert.assertTrue(members.size() > 5);
	}

	
	
	
	
	private List<MemberDTO> testSearchMember(String searchString, boolean notPaidFee, boolean paidFee){

		try {
			MemberController mem = new MemberController();
			return mem.searchMembersByNameString(searchString, notPaidFee, paidFee);
		} catch (RemoteException | ValidationException e) {
			e.printStackTrace();
			return null;
		}

	}
}
