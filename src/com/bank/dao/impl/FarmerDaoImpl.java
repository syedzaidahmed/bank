package com.bank.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.bank.beans.Apply;
import com.bank.beans.Farmer;
import com.bank.beans.FarmerExample;
import com.bank.dao.IFarmerDao;
import com.common.dao.impl.GenericMyBatisDAOSupport;

@Repository("farmerDao")
public class FarmerDaoImpl extends GenericMyBatisDAOSupport<Farmer, Long>
	implements IFarmerDao {

	@Override
	public void updateBySelective(Farmer farmer) {
		// TODO Auto-generated method stub
		this.getSqlSession().update("farmer.updateBySelective",farmer);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int findTotalNumber(Map paramMap) {
		// TODO Auto-generated method stub
		int totalNumber = this.getSqlSession().selectOne("farmer.findTotalNumber",paramMap);
		return totalNumber;
	}

	@Override
	public List<Farmer> findByFarmerIds(List<Long> farmerIds) {
		if(farmerIds.size() >0 ){
			List<Farmer> farmers = this.getSqlSession().selectList("farmer.findByPKM",farmerIds);
			return farmers;
		}else{
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Farmer> findPagingByPK(int pageIndex, int pageSize,
			String sortField, String sortOrder, List<Long> farmerIds) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		int start = pageIndex * pageSize;
		int end = start + pageSize;
		map.put("farmerIds",farmerIds);
		map.put("start",start);
		map.put("end",end);
		map.put("sortOrder",sortOrder);
		List<Farmer> farmers = this.getSqlSession().selectList("farmer.findPagingByPKs",map);
		return farmers;
	}

	@Override
	public Farmer findByID(String farmerIdNum,String organId) {
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("farmeridnum", farmerIdNum);
		map.put("sourcecode", organId);
		Farmer farmer = this.getSqlSession().selectOne("farmer.findByID",map);
		return farmer;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Farmer> findFarmer(Map map) {
		
		List<Farmer> farmers = this.getSqlSession().selectList("farmer.findByMultiCondition",map);
		return farmers;
	}

	@Override
	public List<Farmer> findByIDAndName(String farmerIdNum, String farmerName) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("farmerIdNum", farmerIdNum);
		map.put("farmerName", farmerName);
		List<Farmer> farmers = this.getSqlSession().selectList("farmer.findByIDAndName",map);
		return farmers;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Farmer> findPagingByIDAndName(String pageIndex, String pageSize,
			String sortField, String sortOrder, String farmerIdNum,
			String farmerName, String organId) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		int start =Integer.valueOf(pageIndex) * Integer.valueOf(pageSize);
		int end = start + Integer.valueOf(pageSize);
		map.put("farmerIdNum",farmerIdNum);
		map.put("farmerName", farmerName);
		map.put("organId", organId);
		map.put("start",start);
		map.put("end",end);
		map.put("sortOrder",sortOrder);
		List<Farmer> farmers = this.getSqlSession().selectList("farmer.findPagingByIDAndName",map);
		return farmers;
	}

	@Override
	public List<Farmer> findByNames(List<String> farmerNames) {
		List<Farmer> farmers = this.getSqlSession().selectList("farmer.findByNames",farmerNames);
		return farmers;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void saveApply(Apply apply) {
		this.getSqlSession().insert("farmer.saveApply",apply);
	}

	@Override
	public List<Apply> findApplyByUser(String userId) {
		List<Apply> applys = this.getSqlSession().selectList("farmer.findApplyByUser",userId);
		return applys;
	}

	@Override
	public Farmer findSignalByWhereClause(@Param("example")Map param) {
		// TODO Auto-generated method stub
		Farmer farmer = this.getSqlSession().selectOne("farmer.findByWhereClause",param);
		return farmer;
	}

	@Override
	public List<Farmer> findMultiByWhereClause(@Param("example")Map param) {
		// TODO Auto-generated method stub
		List<Farmer> farmers = this.getSqlSession().selectList("farmer.findByWhereClause", param);
		return farmers;
	}

	@Override
	public List<Farmer> selectByExample(FarmerExample example) {
		// TODO Auto-generated method stub
		List<Farmer> farmers = this.getSqlSession().selectList("farmer.selectByExample",example);
		return farmers;
	}

	@Override
	public List<Farmer> selectSignalByExample(FarmerExample example) {
		// TODO Auto-generated method stub
		List<Farmer> farmers = this.getSqlSession().selectList("farmer.selectSignalByExample",example);
		return farmers;
	}
	
}
