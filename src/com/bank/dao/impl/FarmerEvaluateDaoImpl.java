package com.bank.dao.impl;

import org.springframework.stereotype.Repository;

import com.bank.beans.FarmerEvaluate;
import com.bank.dao.IFarmerEvaluateDao;
import com.common.dao.impl.GenericMyBatisDAOSupport;

@Repository("farmerEvaluateDao")
public class FarmerEvaluateDaoImpl extends GenericMyBatisDAOSupport<FarmerEvaluate, Long>
	implements IFarmerEvaluateDao {

	@Override
	public FarmerEvaluate getEvaluateByFarmerId(Long farmerId) {
		FarmerEvaluate evaluate =this.getSqlSession().selectOne("farmerevaluate.findByFarmerId",farmerId);
		return evaluate;
	}
	

}
