package server;

import java.rmi.RemoteException;

import org.junit.Test;
import org.junit.Assert;

import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.application.controller.*;


public class NewMemberTest {

	@Test
	public void newMemberTest_1() {
		
		
		MemberDTO _activeMemberDTO = null;
		
		String fName = "";
        String lName = "";
        String bday = "";
        String email = "";
        String phone = "";
        String gender = null;
        String address = "";
        String sport = "";
        
        MemberController memberCon = null;
        
        boolean error = false;
        
		try {
			memberCon = new MemberController();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        try {
			error = memberCon.createNewMember(_activeMemberDTO);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Assert.assertTrue(error);
   
	}

	@Test
	public void newMemberTest_2() {
		
		
		MemberDTO _activeMemberDTO = new MemberDTO();
		boolean error = false;
		String fName = "Anne";
        String lName = "Tester";
        String bday = "2001-01-01";
        String email = "test@gmx.at";
        String phone = "0043 235923847";
        String gender = "F";
        String address = "Street 1";
        String sport = "Soccer";
        
        MemberController memberCon = null;
        
        _activeMemberDTO.setFirstName(fName);
        _activeMemberDTO.setLastName(lName);
        _activeMemberDTO.setDateOfBirth(bday);
        _activeMemberDTO.setEmail(email);
        //_activeMemberDTO.setPhone(phone);
        _activeMemberDTO.setGender(gender);
        _activeMemberDTO.setAddress(address);
        _activeMemberDTO.setDepartment(sport);
        
		try {
			memberCon = new MemberController();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        try {
			error = memberCon.createNewMember(_activeMemberDTO);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Assert.assertFalse(error);
   
	}

}
