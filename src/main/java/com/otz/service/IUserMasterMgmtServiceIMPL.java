package com.otz.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.otz.binding.ActivateUser;
import com.otz.binding.LoginCredentials;
import com.otz.binding.RecoveryPassword;
import com.otz.binding.UserAccount;
import com.otz.entity.UserMaster;
import com.otz.repo.IUserMasterRepository;
import com.otz.util.EmailUtils;



@Service
public class IUserMasterMgmtServiceIMPL implements IUserMasterMgmtService {
	@Autowired
	private IUserMasterRepository userMasterRepo;

	@Autowired
	private EmailUtils emailUtils;
	@Autowired
	private Environment env;
	private  String getRandGeneratedStr(int length) {
		// a list of characters to choose from in form of a string
		String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
		// creating a StringBuffer size of AlphaNumericStr
		StringBuilder s = new StringBuilder(length);
		int i;
		for ( i=0; i<length; i++) {
			//generating a random number using math.random()
			int ch = (int)(AlphaNumericStr.length() * Math.random());
			//adding Random character one by one at the end of s
			s.append(AlphaNumericStr.charAt(ch));
		}
		return s.toString();
	}
	@Override
	public String regiserUser(UserAccount user) throws Exception {
		//convert user account obj data to User Master 	obj Entity
		UserMaster master=new UserMaster();
		BeanUtils.copyProperties(user, master);
		//set randaom password for user
		String tempPwd=getRandGeneratedStr(6);
		master.setPassword(tempPwd);
		master.setActive_sw("Inactive");
		//save the object
		UserMaster savedMaster=userMasterRepo.save(master);
		//todo  :: send mail
		String subject="User Registration Success";
		String body=readEmailMEssageBody(env.getProperty("mailbody.registeruser.location"), user.getName(),tempPwd);
		emailUtils.sendEmailMessage(user.getEmail(), subject, body);
		return savedMaster!=null?"User is registered with id value "+savedMaster.getUserId():"Problem in user Registration";
	}

	@Override
	public String activateUserAccount(ActivateUser user) {
		// Convert Activate User obj to UserMaster obj
		UserMaster master=new UserMaster();
		master.setEmail(user.getEmail());
		master.setPassword(user.getTempPassword());
		Example<UserMaster> example=Example.of(master);
		List<UserMaster> list=userMasterRepo.findAll(example);
		//if valid email and tempPass given the set enduser real Password
		if (list!=null) {
			//get Entity class object
			UserMaster entity=list.get(0);
			entity.setPassword(user.getConfirmPassword());
			//change the user account status to active
			entity.setActive_sw("Active");
			UserMaster updatedMaster=userMasterRepo.save(entity);
			return "User is Activated";
		}
		return "User is not Activated";
	}

	@Override
	public String login(LoginCredentials credentials) {
		// LoginCredentials obj to UserMaster
		UserMaster master=new UserMaster();
		BeanUtils.copyProperties(credentials, master);
		//prepare example
		Example<UserMaster> example=Example.of(master);
		List<UserMaster> listEntities=userMasterRepo.findAll(example);
		if(listEntities.size()==0) {
			return "Invalid Credentials!";
		}else {
			UserMaster um=listEntities.get(0);
			if (um.getActive_sw().contentEquals("Active")) {
				return  "Valid Credentials and Login Successfull!";
			} else {
				return  "User Account is Not Active!";
			}
		}

	}

	@Override
	public List<UserAccount> listUsers() {
		// Load all Entity

		List<UserAccount> listUser=userMasterRepo.findAll().stream().map(entity->{
			UserAccount user=new UserAccount();
			BeanUtils.copyProperties(entity, user);
			return user;
		}).toList();
		return listUser;
		//		List<UserMaster> list=userMasterRepo.findAll();
		//		List<UserAccount> listUser=new ArrayList<>();
		//		list.forEach(entity->{
		//			UserAccount user=new UserAccount();
		//			BeanUtils.copyProperties(entity, user);
		//			listUser.add(user);
		//		});
		//		return listUser;
	}

	@Override
	public UserAccount showUserByUserld(Integer id) {
		//Load by userId
		Optional<UserMaster> opt=userMasterRepo.findById(id);
		UserAccount userAccount=null;
		if (opt.isPresent()) {
			userAccount=new UserAccount();
			BeanUtils.copyProperties(opt.get(),userAccount);
		}
		return userAccount;
	}

	@Override
	public UserAccount showUserByEmailAndName(String email, String name) {
		// use the custome FindBy method
		UserMaster master=userMasterRepo.findByNameAndEmail(name, email);
		UserAccount account=null; 
		if(master!=null) {
			account=new UserAccount();
			BeanUtils.copyProperties(master, account);
		}
		return account;
	}

	@Override
	public String updateUser(UserAccount user) {
		//use the custom findBy(-) method
		Optional<UserMaster> opt=userMasterRepo.findById(user.getUserId());
		if(opt!=null) {
			UserMaster master=opt.get();
			BeanUtils.copyProperties(user, master);
			userMasterRepo.save(master);
			return "User Details are updated";
		}else {
			return "User not found for updation";
		}
	}

	@Override
	public String deleteUserByld(Integer id) {
		//Load the obj
		Optional<UserMaster> opt=userMasterRepo.findById(id);
		if(opt.isPresent()) {
			userMasterRepo.deleteById(id);
			return "User is deleted";
		}
		return "user is not found for deletion";
	}

	@Override
	public String changeUserStatus(Integer id, String status) {
		//Load the obj
		Optional<UserMaster> opt=userMasterRepo.findById(id);
		if(opt.isPresent()) {
			UserMaster master=opt.get();
			master.setActive_sw(status);
			userMasterRepo.save(master);
			return "User status changed";
		}
		return "user not found for changing the status";
	}

	@Override
	public String recoverPassword(RecoveryPassword recover)  throws Exception{
		//get UserMaster Entity oj y name, email
		UserMaster master=userMasterRepo.findByNameAndEmail(recover.getName(), recover.getEmail());
		if(master!=null) {
			String pwd=master.getPassword();
			// TODO sent the recovered to email account
			String subject="Mail for password Recover";
			String body=readEmailMEssageBody(env.getProperty("mailbody.recoverpwd.location"), recover.getName(),pwd);
			emailUtils.sendEmailMessage(recover.getEmail(), subject, body);
			return pwd+" Mail is sent having recovered Password";
		}
		return "User and email is not found";
	}
	private String readEmailMEssageBody(String fileName,String fullName,String pwd)throws  Exception {
		String mailBody=null;
		String url="http://localhost:8089/user-api/acivate";
		try( FileReader reader=new FileReader(fileName);
				BufferedReader br=new BufferedReader(reader)){
			//read file content to StringBuffer object line by line
			StringBuffer buffer=new StringBuffer();
			String line=null;
			do {
				line=br.readLine();
				if(line!=null)
					buffer.append(line);
			}while(line!=null);
			mailBody=buffer.toString();
			mailBody=mailBody.replace("{FULL-NAME}", fullName);
			mailBody=mailBody.replace("{PWD}", pwd);
			mailBody=mailBody.replace("{URL}", url);
		}//try
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mailBody;
	}
}
