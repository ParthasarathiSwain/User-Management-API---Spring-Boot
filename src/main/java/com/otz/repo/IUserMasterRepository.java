package com.otz.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otz.entity.UserMaster;

public interface IUserMasterRepository extends JpaRepository<UserMaster, Integer> {
	public UserMaster findByNameAndEmail(String name, String email);
}
