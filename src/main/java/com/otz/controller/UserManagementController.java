package com.otz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otz.binding.ActivateUser;
import com.otz.binding.LoginCredentials;
import com.otz.binding.RecoveryPassword;
import com.otz.binding.UserAccount;
import com.otz.service.IUserMasterMgmtService;

@RestController
@RequestMapping("/user-api")
public class UserManagementController {
	@Autowired
	private IUserMasterMgmtService userService;

	@PostMapping("/save")
	public ResponseEntity<String> saveUser( @RequestBody UserAccount account){
		//user service
		try{
			String resultMsg=userService.regiserUser(account);
			return new ResponseEntity<String>(resultMsg,HttpStatus.CREATED);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/acivate")
	public ResponseEntity<String> acivateUser( @RequestBody ActivateUser user){
		//user service
		try{
			String resultMsg=userService.activateUserAccount(user);
			return new ResponseEntity<String>(resultMsg,HttpStatus.CREATED);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/login")
	public ResponseEntity<String> performingLogin( @RequestBody LoginCredentials credentials){
		//user service
		try{
			String resultMsg=userService.login(credentials);
			return new ResponseEntity<String>(resultMsg,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/allUsers")
	public ResponseEntity<?> showUsers(){
		//user service
		try{
			List<UserAccount> list=userService.listUsers();
			return new ResponseEntity<List<UserAccount>>(list,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/find/{id}")
	public ResponseEntity<?> showUserById(@PathVariable Integer id){
		//user service
		try{
			UserAccount account=userService.showUserByUserld(id);
			return new ResponseEntity<UserAccount>(account,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/find/{email}/{name}")
	public ResponseEntity<?> showUserByEmailAndName(@PathVariable String email,@PathVariable String name){
		//user service
		try{
			UserAccount account=userService.showUserByEmailAndName(email,name);
			return new ResponseEntity<UserAccount>(account,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PutMapping("/update")
	public ResponseEntity<String> updateUserAccount( @RequestBody UserAccount userAccount){
		//user service
		try{
			String resultMsg=userService.updateUser(userAccount);
			return new ResponseEntity<String>(resultMsg,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteByUserId( @PathVariable Integer	 id){
		//user service
		try{
			String resultMsg=userService.deleteUserByld(id);
			return new ResponseEntity<String>(resultMsg,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PatchMapping("/changestatus/{id}/{status}")
	public ResponseEntity<String> changeUserStatusById( @PathVariable Integer id, @PathVariable String status){
		//user service
		try{
			String resultMsg=userService.changeUserStatus(id,status);
			return new ResponseEntity<String>(resultMsg,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/recoverPassword")
	public ResponseEntity<String> recoverPassword( @RequestBody RecoveryPassword recover){
		//user service
		try{
			String resultMsg=userService.recoverPassword(recover);
			return new ResponseEntity<String>(resultMsg,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
