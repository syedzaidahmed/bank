package com.bank.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bank.beans.FarmerHouse;
import com.bank.dao.IFarmerHouseDao;
import com.common.dao.impl.GenericMyBatisDAOSupport;

@Repository("farmerHouseDao")
public class FarmerHouseDaoImpl extends GenericMyBatisDAOSupport<FarmerHouse, Long>
	implements IFarmerHouseDao {

	@Override
	public List<FarmerHouse> getHousesByFarmerId(Long farmerId) {
		List<FarmerHouse> houses =this.getSqlSession().selectList("farmerhouse.findByFarmerId",farmerId);
		return houses;
	}
	

}
