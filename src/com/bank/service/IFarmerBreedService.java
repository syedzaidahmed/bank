package com.bank.service;

import java.util.List;

import com.bank.beans.FarmerBreed;
import com.common.service.GenericService;

public interface IFarmerBreedService extends
		GenericService<FarmerBreed, Long> {
	public int findTotalNumberByFarmerId(Long farmerId);
	public List<FarmerBreed> findPagingByFarmerId(int pageIndex,int pageSize,String sortField,
			String sortOrder,Long farmerId);
	public List<FarmerBreed> findBreedByFarmer(Long farmerId);

}
