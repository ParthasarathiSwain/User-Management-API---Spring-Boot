package com.otz.service;

import java.util.List;

import com.otz.binding.ActivateUser;
import com.otz.binding.LoginCredentials;
import com.otz.binding.RecoveryPassword;
import com.otz.binding.UserAccount;

public interface IUserMasterMgmtService {
	public  String regiserUser(UserAccount user) throws Exception;
	public  String activateUserAccount(ActivateUser user);
	public	String login(LoginCredentials credentials);
	public	List<UserAccount> listUsers();
	public	UserAccount showUserByUserld(Integer id);
	public	UserAccount showUserByEmailAndName(String email,String name);
	public	String updateUser(UserAccount user);
	public	String deleteUserByld(Integer id);
	public	String changeUserStatus(Integer id,String status);
	public	String recoverPassword(RecoveryPassword recover) throws Exception;
}
