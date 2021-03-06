package com.bank.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bank.beans.Privilege;
import com.bank.beans.UserRole;
import com.bank.dao.IPrivilegeDao;
import com.bank.service.IPrivilegeService;
import com.common.dao.GenericDAO;
import com.common.service.impl.GenericServiceImpl;

@Service("privilegeService")
public class PrivilegeServiceImpl extends GenericServiceImpl<Privilege, String> implements
		IPrivilegeService {
	private static Logger log = LoggerFactory.getLogger(PrivilegeServiceImpl.class);
	
	@Resource
	private IPrivilegeDao privilegeDao;
	
	

	public GenericDAO<Privilege, String> getGenericDAO() {
		return privilegeDao;
	}



	@Override
	public void save(HashMap row) {
		privilegeDao.save(row);
		
	}



	@Override
	public void update(HashMap row) throws Exception {
		privilegeDao.update(row);
		
	}
}
