package com.bank.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bank.beans.FarmerMember;
import com.bank.dao.IFarmerMemberDao;
import com.common.dao.impl.GenericMyBatisDAOSupport;

@Repository("farmerMemberDao")
public class FarmerMemberDaoImpl extends GenericMyBatisDAOSupport<FarmerMember, Long>
	implements IFarmerMemberDao {

	@Override
	public List<FarmerMember> getMembersByFarmerId(Long farmerId) {
		List<FarmerMember> members = this.getSqlSession().selectList("farmermember.findByFarmerId",farmerId);
		return members;
	}

	@Override
	public void deleteMembers(List<Long> memberIds) {
		// TODO Auto-generated method stub
		this.getSqlSession().delete("farmermember.delete", memberIds);
	}

	@Override
	public int findTotalNumberByFarmerId(Long farmerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<FarmerMember> findPagingByFarmerId(int pageIndex, int pageSize,
			String sortField, String sortOrder, Long farmerId) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
