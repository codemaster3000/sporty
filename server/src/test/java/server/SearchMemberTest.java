package server;

import org.junit.Assert;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

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
		MemberController mem = null;
		List<MemberDTO> members = new ArrayList<>();
		
		try {
			mem = new MemberController();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			members = mem.searchForMembers(searchString);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertEquals(1, members.size());
	}
	
	/**
	 * One Member found with fName Claudia
	 */
	@Test
	public void searchMemberTest_2() {
		
		String searchString = "Claudia";
		MemberController mem = null;
		List<MemberDTO> members = new ArrayList<>();
		
		try {
			mem = new MemberController();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			members = mem.searchForMembers(searchString);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertEquals(1, members.size());
	}

}
