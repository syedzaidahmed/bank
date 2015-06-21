package com.bank.controller.farmer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bank.Constants;
import com.bank.beans.Farmer;
import com.bank.beans.Loan;
import com.bank.beans.Organ;
import com.bank.beans.User;
import com.bank.beans.enums.DicExplain;
import com.bank.beans.enums.FarmerNSEnum;
import com.bank.beans.enums.FarmerNYEnum;
import com.bank.beans.enums.FarmerPoorEnum;
import com.bank.beans.enums.LoanNSEnum;
import com.bank.beans.enums.LoanNYEnum;
import com.bank.common.util.JsonUtil;
import com.bank.service.IFarmerService;
import com.bank.service.ILoanService;
import com.bank.utils.ParseDataUtils;
import com.common.exception.CreateException;
import com.common.exception.DAOException;
import com.common.exception.DataNotFoundException;
import com.common.exception.UpdateException;

@Controller
@RequestMapping(value = "/loan")
public class FarmerLoanController {
	
@Resource
private IFarmerService farmerService;

@Resource
private ILoanService loanService;

@RequestMapping(value="/saveLoan",method=RequestMethod.POST)
public ModelAndView saveLoan(HttpServletRequest request, 
		HttpServletResponse response) throws Exception{
	String loanData = request.getParameter("loan");
	Object loanJsonData = JsonUtil.Decode(loanData);
	loanData = JSON.toJSONStringWithDateFormat(loanJsonData, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
	JSONObject loanJson = JSONObject.parseObject(loanData);
	Loan loan = (Loan) JSON.toJavaObject(loanJson, Loan.class);
	if(loan.getId()==null){
		loanService.save(loan);
	}else{
		loanService.update(loan);
	}

	response.setContentType("text/html;charset=UTF-8");
	String json = JSON.toJSONString(loan);
    response.getWriter().write(json);
	return null;
}
@RequestMapping(value="/saveLoan1",method=RequestMethod.POST)
public ModelAndView saveLoan1(@ModelAttribute(value="loan") Loan loan,
		HttpServletRequest request,HttpServletResponse response) throws Exception{
	
	Organ organ = (Organ)request.getSession().getAttribute(Constants.SESSION_CURRENT_UNIT);
	String organId = organ.getOrganId();
	String organName = organ.getOrganName();
	loan.setRunitId(organId);
	loan.setRunitName(organName);
	if(loan.getId()==null){
		loanService.save(loan);
	}else{
		loanService.update(loan);
	}
	ModelAndView view = new ModelAndView("/farmer/farmerLoanView1");
	Farmer farmer = null;
	try {
		 farmer = farmerService.findByPK(loan.getClientId());
	} catch (DAOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DataNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	view.addObject("farmer",farmer);
	return view;
}
@RequestMapping(value="/deleteLoan",method=RequestMethod.GET)
public ModelAndView deleteLoan(HttpServletRequest request,HttpServletResponse response){
	
	String id = request.getParameter("id");
	String fid = request.getParameter("fid");
	Long loanId = Long.valueOf(id);
	Long farmerId = Long.valueOf(fid);
	try {
		loanService.delete(loanId);
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	Farmer farmer = null;
	try {
		 farmer = farmerService.findByPK(farmerId);
	} catch (DAOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DataNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	ModelAndView view = new ModelAndView("/farmer/farmerLoanView1");
	view.addObject("farmer",farmer);
	return view;
}
@RequestMapping(value="/editLoan",method=RequestMethod.GET)
public ModelAndView editLoan(@RequestParam(value="id") String id,@RequestParam(value="fid") String fid,
		HttpServletRequest request,HttpServletResponse response){
	
	Long houseId = Long.valueOf(id);
	Long farmerId = Long.valueOf(fid);
	Farmer farmer = null;
	Loan loan = null;
	try {
		farmer = farmerService.findByPK(farmerId);
		loan = loanService.findByPK(houseId);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	ModelAndView view = new ModelAndView("/farmer/farmerLoanForm1");
	view.addObject("farmer",farmer);
	view.addObject("loan",loan);
	return view;
}
@RequestMapping(value="/queryLoan",method=RequestMethod.POST)
public ModelAndView queryLoan(@RequestParam(value="farmerName") String farmerName, 
		@RequestParam(value="farmerIdNum") String farmerIdNum, 
		HttpServletResponse response) throws Exception{

	if(StringUtils.isEmpty(farmerName) && StringUtils.isEmpty(farmerIdNum)){
		ModelAndView view = new ModelAndView("/farmer/farmerLoanView");
		return view;
	}
	List<Farmer> farmers = farmerService.findByIDAndName(farmerIdNum, farmerName);
	//不存在客户信息需要先录入客户信息
	if(farmers.size() == 0){
		ModelAndView  view = new ModelAndView("/farmer/farmerLoanView");
		view.addObject("farmerName",farmerName);
		view.addObject("farmerIdNum", farmerIdNum);
		view.addObject("msg","未找到匹配的农户信息!您可以到【农户】-【数据采集】-【基本信息】模块中录入农户信息后再录入农户的资产信息!");
		return view;
	}else{
		List<Long> farmerIds = new ArrayList<Long>();
		for(Iterator<Farmer> it = farmers.iterator();it.hasNext();){
			Farmer farmer = it.next();
			farmerIds.add(farmer.getId());
		}
		List<Loan> aloans = loanService.findByFarmers(farmerIds);
		if(aloans.size()==0){
			ModelAndView view = new ModelAndView("/farmer/farmerLoanView");
			view.addObject("farmerName",farmerName);
			view.addObject("farmerIdNum", farmerIdNum);
			view.addObject("msg","未找到符合条件的贷款信息!");
			return view;
		}else{
			ModelAndView view = new ModelAndView("/farmer/farmerLoanView");
			view.addObject("farmerName",farmerName);
			view.addObject("farmerIdNum", farmerIdNum);
			view.addObject("loans",aloans);
			return view;
		}
	}
}
@RequestMapping(value="/queryLoan1",method=RequestMethod.GET)
public ModelAndView queryLoan1(@RequestParam(value="fid") String fid, 
		HttpServletRequest request,HttpServletResponse response){
	
	Long farmerId = Long.valueOf(fid);
	ModelAndView view = new ModelAndView("/farmer/farmerLoanView1");
	Farmer farmer = null;
	try {
		 farmer = farmerService.findByPK(farmerId);
	} catch (DAOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DataNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	view.addObject("farmer",farmer);
	return view;
}
@RequestMapping(value="/loadLoan",method=RequestMethod.GET)
public ModelAndView loadLoan(@RequestParam(value="id") String id,
		HttpServletResponse response) throws DAOException, DataNotFoundException{
	if(StringUtils.isEmpty(id)){
		ModelAndView view = new ModelAndView("/farmer/farmerLoanView");
		return view;
	}
	Long loanId = Long.valueOf(id);
	Loan loan = loanService.findByPK(loanId);
	Long farmerId = loan.getClientId();
	Farmer farmer = farmerService.findByPK(farmerId);
	ModelAndView view = new ModelAndView("/farmer/farmerLoanForm");
	view.addObject("farmer", farmer);
	view.addObject("loan",loan);
	return view;
}
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping(value="/loadLoan1",method=RequestMethod.POST)
public ModelAndView loadLoan1(@RequestParam(value="fid") Long fid, 
		@RequestParam(value="pageIndex") int pageIndex,
		@RequestParam(value="pageSize") int pageSize,
		@RequestParam(value="sortField") String sortField,
		@RequestParam(value="sortOrder") String sortOrder,
		HttpServletRequest request,HttpServletResponse response){
	int totalNumber = loanService.findTotalNumberByFarmerId(fid);
	List<Loan> loans =loanService.findByPaging(pageIndex, pageSize, sortField, sortOrder, fid);
    Map map = new HashMap();
    map.put("total", totalNumber);
    map.put("data", loans);
	String json = JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter writer = null;
    try {
		writer = response.getWriter();
		writer.write(json);
		writer.flush();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}
@RequestMapping(value="/typeInLoan",method=RequestMethod.POST)
public ModelAndView typeinLoan(@RequestParam(value="farmerName") String farmerName, 
		@RequestParam(value="farmerIdNum") String farmerIdNum, HttpServletRequest request,
		HttpServletResponse response){
	
	if(StringUtils.isEmpty(farmerIdNum)){
		ModelAndView view = new ModelAndView("/farmer/farmerLoanView");
		return view;
	}
	Organ organ =(Organ)request.getSession().getAttribute(Constants.SESSION_CURRENT_UNIT);
	String organId = organ.getOrganId();
	Farmer farmer = farmerService.findById(farmerIdNum,organId);
	//不存在客户信息需要先录入客户信息
	if(farmer == null){
		ModelAndView  view = new ModelAndView("/farmer/farmerLoanForm");
		view.addObject("error","未找到匹配的农户信息!您可以到【农户】-【数据采集】-【基本信息】模块中录入农户信息后再录入农户的资产信息!");
		return view;
	}else{
		ModelAndView view = new ModelAndView("/farmer/farmerLoanForm");
		view.addObject("farmer",farmer);
		return view;
	}
}
@RequestMapping(value="/typeInLoan1",method=RequestMethod.GET)
public ModelAndView typeinLoan1(@RequestParam(value="fid") String fid, 
		HttpServletRequest request,HttpServletResponse response){
	
	Long farmerId = Long.valueOf(fid);
	Farmer farmer = null;
	try {
		farmer = farmerService.findByPK(farmerId);
	} catch (DAOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DataNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	ModelAndView view = new ModelAndView("/farmer/farmerLoanForm1");
	view.addObject("farmer",farmer);
	return view;
}
@RequestMapping(value="/loadFile",method=RequestMethod.POST)
public ModelAndView loadFile(MultipartFile myfile,HttpServletRequest request, 
		HttpServletResponse response) throws DAOException, CreateException, UpdateException, DataNotFoundException{
		String[][] data = null;
		Organ organ = (Organ)request.getSession().getAttribute(Constants.SESSION_CURRENT_UNIT);
		User user = (User)request.getSession().getAttribute(Constants.SESSION_AUTH_USER);
		String organId = organ.getOrganId();
		String organName = organ.getOrganName();
		String recorder = user.getUserId();
		Date recordTime = new Date();
		List<Map<String,String>> msgs = new ArrayList<Map<String,String>>();
		ModelAndView view = new ModelAndView("/farmer/farmerImportFile");
		//异常输入流及不可解析的文件格式
		try{
			 InputStream in = myfile.getInputStream();
			 data=ParseDataUtils.getExeclData(in,0);
		}catch(IOException e){
			Map<String,String> msg = new HashMap<String,String>();
			msg.put("row", String.valueOf(1));
			msg.put("tip", "error");
			msg.put("msg", "不支持的");
			view.addObject("msgs",msgs);
			return view;
		}
		
		if(data.length>0 && data[0].length>0 && data[0][0].indexOf("个人客户信息")>0){
			msgs = importFarmerNS(organId,organName,recorder,recordTime,data);
		}else if("贷款日期".equals(data[0][5])){
			msgs = importLoanNS(organId,organName,recorder,recordTime,data);
		}else if(data[0][0].contains("个人客户基本信息查询")){
			msgs = importFarmerNY(organId,organName,recorder,recordTime,data);
		}else if(data[0][0].contains("个人借款合同查询")){
			msgs = importLoanNY(organId,organName,recorder,recordTime,data);
		}else if("主要致贫原因".equals(data[0][5])){
			msgs = importFarmerPoor(organId,organName,recorder,recordTime,data);
		}else{
			//文件格式可能存在问题，请及时联系管理员
			Map<String,String> msg = new HashMap<String,String>();
			msg.put("row", "0");
			msg.put("tip", "error");
			msg.put("msg", "文件格式可能存在问题，请及时联系管理员!");
			msgs.add(msg);
		}
		view.addObject("msgs",msgs);
		return view;
}
@RequestMapping(value="/loadFile1",method=RequestMethod.POST)
public ModelAndView loadFile1(MultipartFile myfile,HttpServletRequest request, 
		HttpServletResponse response) throws DAOException, CreateException, UpdateException, DataNotFoundException{
		String[][] data = null;
		Organ organ = (Organ)request.getSession().getAttribute("unit");
		String organId = organ.getOrganId();
		String organName = organ.getOrganName();
		List<Map<String,String>> msgs = new ArrayList<Map<String,String>>();
		ModelAndView view = new ModelAndView("/farmer/farmerImportFile1");
		//异常输入流及不可解析的文件格式
		try{
			 InputStream in = myfile.getInputStream();
			 data=ParseDataUtils.getExeclData(in,0);
		}catch(IOException e){
			Map<String,String> msg = new HashMap<String,String>();
			msg.put("row", String.valueOf(1));
			msg.put("tip", "error");
			msg.put("msg", "不支持的");
			view.addObject("msgs",msgs);
			return view;
		}
		
		if(data[0][0].contains("个人客户基本信息查询")){
			for(int row =3;row<data.length;row++){
				Map<String,String> msg = new HashMap<String,String>();
				Farmer farmer = new Farmer();
				String farmerName = data[row][FarmerNYEnum.FARMERNAME.getIndex()];
				if(StringUtils.isEmpty(farmerName)){
					msg.put("row", String.valueOf(row));
					msg.put("tip", "info");
					msg.put("msg", "姓名不能为空。");
					continue;
				}
				farmer.setFarmername(farmerName);
				String farmerIdNum = data[row][FarmerNYEnum.IDNUM.getIndex()];
				if(StringUtils.isEmpty(farmerIdNum)){
					msg.put("row", String.valueOf(row));
					msg.put("tip", "info");
					msg.put("msg", "证件号码不能为空。");
					continue;
				}
				Farmer dbFarmer = farmerService.findById(farmerIdNum,organId);
				if(dbFarmer != null){
					msg.put("row", String.valueOf(row));
					msg.put("tip", "info");
					msg.put("msg", "该用户已经存在、不再导入。");
					continue;
				}
				farmer.setFarmeridnum(farmerIdNum);
				farmer.setRunitid(organId);
				farmer.setRunitname(organName);
				String sex = DicExplain.explain(DicExplain.$SEX,data[row][FarmerNYEnum.SEX.getIndex()]);
				farmer.setSex(Integer.valueOf(sex));
				String education = DicExplain.explain(DicExplain.$EDUCATION,data[row][FarmerNYEnum.EDUCATION.getIndex()]);
				farmer.setEducation(Integer.valueOf(education));
				String marryStatus = DicExplain.explain(DicExplain.$MARRYSTATUS,data[row][FarmerNYEnum.MARRYSTATUS.getIndex()]);
				farmer.setMarrystatus(Integer.valueOf(marryStatus));
				farmer.setAddress(data[row][FarmerNYEnum.ADDRESS.getIndex()]);
				farmerService.save(farmer);
				//关联农户信贷信息
				List<Loan> loans = loanService.findByID("1","0",farmer.getFarmeridnum());
				if(loans == null || loans.size()==0){
				//未匹配信贷信息提示未匹配
					msg.put("row", String.valueOf(row));
					msg.put("tip", "error");
					msg.put("msg", "["+data[row][FarmerNYEnum.FARMERNAME.getIndex()]+"]"
							+ "找到"+loans.size()+"条匹配的信贷信息");
					msgs.add(msg);
				}else{
				//匹配信贷信息提示匹配数量
					for(Iterator<Loan> it = loans.iterator();it.hasNext();){
						Loan loan = it.next();
						loan.setClientId(farmer.getId());
						loanService.update(loan);
					}
					msg.put("row", String.valueOf(row));
					msg.put("tip", "info");
					msg.put("msg", "["+data[row][FarmerNYEnum.FARMERNAME.getIndex()]+"]"
							+ "找到"+loans.size()+"条匹配的信贷信息");
					msgs.add(msg);
				}	
			}
			
		}else if(data[0][0].contains("个人借款合同查询")){
			for(int row=3;row<data.length;row++){
				Map<String,String> msg = new HashMap<String,String>();
				Loan loan = new Loan();
				loan.setClientNum(data[row][LoanNYEnum.CLIENTNUM.getIndex()]);
				String noteNum = data[row][LoanNYEnum.NOTENUM.getIndex()];
				if(StringUtils.isEmpty(noteNum)){
					msg.put("row", String.valueOf(row));
					msg.put("tip", "error");
					msg.put("msg", "申请书编号不能为空！");
					msgs.add(msg);
					continue;
				}
				loan.setNoteNum(noteNum);
				Loan dbloan = loanService.findByNoteNum(noteNum);
				if(dbloan != null){
					msg.put("row", String.valueOf(row));
					msg.put("tip", "info");
					msg.put("msg", "该信贷信息已经存在、不再导入！");
					msgs.add(msg);
					continue;
				}
				String compactNum =data[row][LoanNYEnum.COMPACTNUM.getIndex()];
				if(StringUtils.isEmpty(compactNum)){
					//如果合同号为空提示合同号不能为空
					msg.put("row", String.valueOf(row));
					msg.put("tip", "error");
					msg.put("msg", "信贷信息的合同号不能为空！");
					msgs.add(msg);
					continue;
				}
				loan.setCompactNum(compactNum);
				loan.setLoanDate(data[row][LoanNYEnum.LOANDATE.getIndex()]);
				loan.setLimitDate(data[row][LoanNYEnum.LIMITDATE.getIndex()]);
				String currency =DicExplain.explain(DicExplain.$CURRENCY, data[row][LoanNYEnum.CURRENCY.getIndex()]);
				loan.setCurrency(currency);
				loan.setAmount(data[row][LoanNYEnum.AMOUNT.getIndex()]);
				loan.setBalance(data[row][LoanNYEnum.BALANCE.getIndex()]);
				loan.setClientType("1");
				loan.setClientName(data[row][LoanNYEnum.CLIENTNAME.getIndex()]);
				String idType = DicExplain.explain(DicExplain.$IDTYPE,data[row][LoanNYEnum.IDTYPE.getIndex()]);
				loan.setIdType(idType);
				loan.setIdNum(data[row][LoanNYEnum.IDNUM.getIndex()]);
				loan.setGuaranteeType(data[row][LoanNYEnum.GUARANTEETYPE.getIndex()]);
				loan.setOrganCode(data[row][LoanNYEnum.ORGANCODE.getIndex()]);
				loan.setGuaranteeType1(data[row][LoanNYEnum.GUARANTEETYPE1.getIndex()]);
				loan.setRunitId(organId);
				loan.setRunitName(organName);
				Farmer farmer = farmerService.findById(loan.getIdNum(),organId);
				if(farmer == null ){
				//未匹配农户信息提示未匹配
					loanService.findByNoteNum(noteNum);
					try{
						loanService.save(loan);
					}catch(Exception e){
						System.err.println(row);
						System.err.println(loan.getNoteNum());
					}
					msg.put("row", String.valueOf(row));
					msg.put("tip", "success");
					msg.put("msg", "导入成功但未匹配农户信息，请稍后导入农户信息！");
					msgs.add(msg);
				}else{
				//匹配农户信息提示匹配数量
					msg.put("row", String.valueOf(row));
					loan.setClientId(farmer.getId());
					loanService.save(loan);
					msg.put("tip", "success");	
					msg.put("msg", "导入成功且匹配到相应的农户信息！");
					msgs.add(msg);
				}
			}
		}else{
			//文件格式可能存在问题，请及时联系管理员
			Map<String,String> msg = new HashMap<String,String>();
			msg.put("row", "0");
			msg.put("tip", "error");
			msg.put("msg", "文件格式可能存在问题，请及时联系管理员!");
			msgs.add(msg);
		}
		view.addObject("msgs",msgs);
		return view;
}
@RequestMapping(value="/importLoan",method=RequestMethod.POST)
public ModelAndView loadLoan(MultipartFile myfile,HttpServletRequest request, 
		HttpServletResponse response) throws DAOException, CreateException{
	
		Organ organ = (Organ)request.getSession().getAttribute(Constants.SESSION_CURRENT_UNIT);
		String organId = organ.getOrganId();
		String organName = organ.getOrganName();
		String[][] data = null;
		List<Map<String,String>> msgs = new ArrayList<Map<String,String>>();
		ModelAndView view = new ModelAndView("/farmer/farmerImportFile");
		//异常输入流及不可解析的文件格式
		try{
			 InputStream in = myfile.getInputStream();
			 data=ParseDataUtils.getExeclData(in,0);
		}catch(IOException e){
			Map<String,String> msg = new HashMap<String,String>();
			msg.put("row", String.valueOf(1));
			msg.put("tip", "error");
			msg.put("msg", "不支持的");
			view.addObject("msgs",msgs);
			return view;
		}
		
		 if(!"贷款日期".equals(data[0][5])){
			 return view;
		 }
		for(int row=1;row<data.length;row++){
				Map<String,String> msg = new HashMap<String,String>();
				Loan loan = new Loan();
				loan.setClientNum(data[row][LoanNSEnum.CLIENTNUM.getIndex()]);
				String noteNum = data[row][LoanNSEnum.NOTENUM.getIndex()];
				loan.setNoteNum(noteNum);
				Loan dbloan = loanService.findByNoteNum(noteNum);
				if(dbloan != null){
					msg.put("row", String.valueOf(row));
					msg.put("tip", "info");
					msg.put("msg", "该信贷信息已经存在、不再导入！");
					msgs.add(msg);
					continue;
				}
				String compactNum =data[row][LoanNSEnum.COMPACTNUM.getIndex()];
				if(StringUtils.isEmpty(compactNum)){
					//如果合同号为空提示合同号不能为空
					msg.put("row", String.valueOf(row));
					msg.put("tip", "error");
					msg.put("msg", "信贷信息的合同号不能为空！");
					msgs.add(msg);
					continue;
				}
				loan.setCompactNum(compactNum);
				loan.setLoanDate(data[row][LoanNSEnum.LOANDATE.getIndex()]);
				loan.setLimitDate(data[row][LoanNSEnum.LIMITDATE.getIndex()]);
				loan.setLimitDate1(data[row][LoanNSEnum.LIMITDATE1.getIndex()]);
				loan.setClientName(data[row][LoanNSEnum.CLIENTNAME.getIndex()]);
				String currency =DicExplain.explain(DicExplain.$CURRENCY, data[row][LoanNSEnum.CURRENCY.getIndex()]);
				loan.setCurrency(currency);
				loan.setAmount(data[row][LoanNSEnum.AMOUNT.getIndex()]);
				loan.setBalance(data[row][LoanNSEnum.BALANCE.getIndex()]);
				loan.setOweInterest(data[row][LoanNSEnum.OWEINTEREST.getIndex()]);
				loan.setCurrentRate(data[row][LoanNSEnum.CURRENTRATE.getIndex()]);
				loan.setRateType(data[row][LoanNSEnum.RATETYPE.getIndex()]);
				String clientType = DicExplain.explain(DicExplain.$CLIENTTYPE,data[row][LoanNSEnum.CLIENTTYPE.getIndex()]);
				loan.setClientType(clientType);
				String idType = DicExplain.explain(DicExplain.$IDTYPE,data[row][LoanNSEnum.IDTYPE.getIndex()]);
				loan.setIdType(idType);
				loan.setIdNum(data[row][LoanNSEnum.IDNUM.getIndex()]);
				loan.setPhone(data[row][LoanNSEnum.PHONE.getIndex()]);
				loan.setAddress(data[row][LoanNSEnum.ADDRESS.getIndex()]);
				loan.setLoanUse(data[row][LoanNSEnum.LOANUSE.getIndex()]);
				loan.setGuaranteeType(data[row][LoanNSEnum.GUARANTEETYPE.getIndex()]);
				loan.setGrade(data[row][LoanNSEnum.GRADE.getIndex()]);
				loan.setGrade1(data[row][LoanNSEnum.GRADE1.getIndex()]);
				loan.setSettleType(data[row][LoanNSEnum.SETTLETYPE.getIndex()]);
				loan.setChargePerson(data[row][LoanNSEnum.CHARGEPERSON.getIndex()]);
				loan.setOrganCode(data[row][LoanNSEnum.ORGANCODE.getIndex()]);
				loan.setOrganName(data[row][LoanNSEnum.ORGANNAME.getIndex()]);
				loan.setBusinessType(data[row][LoanNSEnum.BUSINESSTYPE.getIndex()]);
				loan.setBusinessBody(data[row][LoanNSEnum.BUSINESSBODY.getIndex()]);
				loan.setProvideType(data[row][LoanNSEnum.PROVIDETYPE.getIndex()]);
				loan.setInvest(data[row][LoanNSEnum.INVEST.getIndex()]);
				loan.setTermType(data[row][LoanNSEnum.TERMTYPE.getIndex()]);
				loan.setGuaranteeType1(data[row][LoanNSEnum.GUARANTEETYPE1.getIndex()]);
				loan.setFloatScope(data[row][LoanNSEnum.FLOATSCOPE.getIndex()]);
				loan.setAdjustType(data[row][LoanNSEnum.ADJUSTTYPE.getIndex()]);
				loan.setRunType(data[row][LoanNSEnum.RUNTYPE.getIndex()]);
				loan.setFrequeency(data[row][LoanNSEnum.FREQUEENCY.getIndex()]);
				loan.setRunitId(organId);
				loan.setRunitName(organName);
				Farmer farmer = farmerService.findById(loan.getIdNum(),organId);
				if(farmer == null ){
				//未匹配农户信息提示未匹配
					loanService.findByNoteNum(noteNum);
					loanService.save(loan);
					msg.put("row", String.valueOf(row));
					msg.put("tip", "success");
					msg.put("msg", "导入成功但未匹配农户信息，请稍后导入农户信息！");
					msgs.add(msg);
				}else{
				//匹配农户信息提示匹配数量
					msg.put("row", String.valueOf(row));
					loan.setClientId(farmer.getId());
					loanService.save(loan);
					msg.put("tip", "success");	
					msg.put("msg", "导入成功且匹配到相应的农户信息！");
					msgs.add(msg);
				}
		}
		view.addObject("msgs",msgs);
		return view;
}
public List<Map<String,String>> importFarmerNS(String organId,String organName,
		String recorder,Date recordTime,String[][] data){
	
	List<Map<String,String>> msgs = new ArrayList<Map<String,String>>();
	
	for(int row =2;row<data.length;row++){
		Map<String,String> msg = new HashMap<String,String>();
		Farmer farmer = new Farmer();
		farmer.setSourcecode("C3140436000017");
		farmer.setSourcename("新干县农村信用合作联社");
		farmer.setRunitid(organId);
		farmer.setRunitname(organName);
		farmer.setRecorder(recorder);
		farmer.setRecordtime(recordTime);
		farmer.setFarmertype(0);
		String farmerName = data[row][FarmerNSEnum.FARMERNAME.getIndex()];
		if(StringUtils.isEmpty(farmerName)){
			msg.put("row", String.valueOf(row));
			msg.put("tip", "info");
			msg.put("msg", "姓名不能为空。");
			continue;
		}
		farmer.setFarmername(farmerName);
		String farmerIdNum = data[row][FarmerNSEnum.IDNUM.getIndex()];
		if(StringUtils.isEmpty(farmerIdNum)){
			msg.put("row", String.valueOf(row));
			msg.put("tip", "info");
			msg.put("msg", "证件号码不能为空。");
			continue;
		}
		farmer.setFarmeridnum(farmerIdNum);
		farmer.setRunitid(organId);
		farmer.setRunitname(organName);
		String sex = DicExplain.explain(DicExplain.$SEX,data[row][FarmerNSEnum.SEX.getIndex()]);
		farmer.setSex(Integer.valueOf(sex));
		String education = DicExplain.explain(DicExplain.$EDUCATION,data[row][FarmerNSEnum.EDUCATION.getIndex()]);
		farmer.setEducation(Integer.valueOf(education));
		String marryStatus = DicExplain.explain(DicExplain.$MARRYSTATUS,data[row][FarmerNSEnum.MARRYSTATUS.getIndex()]);
		farmer.setMarrystatus(Integer.valueOf(marryStatus));
		farmer.setOccupation(data[row][FarmerNSEnum.OCCUPATION.getIndex()]);
		farmer.setPhone(data[row][FarmerNSEnum.PHONE.getIndex()]);
		farmer.setAddress(data[row][FarmerNSEnum.ADDRESS.getIndex()]);
		Farmer dbFarmer = farmerService.findById(farmerIdNum,"C3140436000017");
		try {
			if(dbFarmer == null){
				farmerService.save(farmer);
			}else{
				farmer.setId(dbFarmer.getId());
				farmerService.updateBySelective(farmer);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//关联农户信贷信息
		List<Loan> loans = loanService.findByID("1","0",farmer.getFarmeridnum());
		if(loans == null || loans.size()==0){
		//未匹配信贷信息提示未匹配
			msg.put("row", String.valueOf(row));
			msg.put("tip", "error");
			msg.put("msg", "["+data[row][FarmerNSEnum.FARMERNAME.getIndex()]+"]"
					+ "找到"+loans.size()+"条匹配的信贷信息");
			msgs.add(msg);
		}else{
		//匹配信贷信息提示匹配数量
			for(Iterator<Loan> it = loans.iterator();it.hasNext();){
				Loan loan = it.next();
				loan.setClientId(farmer.getId());
				try {
					loanService.update(loan);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			msg.put("row", String.valueOf(row));
			msg.put("tip", "info");
			msg.put("msg", "["+data[row][FarmerNSEnum.FARMERNAME.getIndex()]+"]"
					+ "找到"+loans.size()+"条匹配的信贷信息");
			msgs.add(msg);
		}	
	}
	return msgs;
}
public List<Map<String,String>> importFarmerPoor(String organId,String organName,
			String recorder,Date recordTime,String[][] data){
	
	List<Map<String,String>> msgs = new ArrayList<Map<String,String>>();
	
	for(int row =1;row<data.length;row++){
		
		Map<String,String> msg = new HashMap<String,String>();
		
		Farmer farmer = new Farmer();
		farmer.setSourcecode("JinZhunHuPin");
		farmer.setSourcename("新干县人民政府扶贫办");
		farmer.setRunitid(organId);
		farmer.setRunitname(organName);
		farmer.setRecorder(recorder);
		farmer.setRecordtime(recordTime);
		farmer.setFarmertype(1);
		String farmerName = data[row][FarmerPoorEnum.FARMERNAME.getIndex()];
		if(StringUtils.isEmpty(farmerName)){
			msg.put("row", String.valueOf(row));
			msg.put("tip", "info");
			msg.put("msg", "姓名不能为空。");
			continue;
		}
		farmer.setFarmername(farmerName);
		String farmerIdNum = data[row][FarmerPoorEnum.IDNUM.getIndex()];
		if(StringUtils.isEmpty(farmerIdNum)){
			msg.put("row", String.valueOf(row));
			msg.put("tip", "info");
			msg.put("msg", "证件号码不能为空。");
			continue;
		}
		farmer.setFarmeridnum(farmerIdNum);
		String familyNumStr = data[row][FarmerPoorEnum.FAMILYNUM.getIndex()];
		int familyNum = StringUtils.isEmpty(familyNumStr)?0:Integer.valueOf(familyNumStr);
		farmer.setFamilynum(familyNum);
		String averageStr = data[row][FarmerPoorEnum.AVERAGEINCOME.getIndex()];
		double average = StringUtils.isEmpty(averageStr)?0:Double.valueOf(averageStr);
		double netIncome = familyNum*average;
		farmer.setNetincome(netIncome);
		String phone = data[row][FarmerPoorEnum.PHONE.getIndex()];
		farmer.setPhone(phone);
		String bank = data[row][FarmerPoorEnum.BANK.getIndex()];
		farmer.setBank(bank);
		String account = data[row][FarmerPoorEnum.ACCOUNT.getIndex()];
		farmer.setAccount(account);
		String province = data[row][FarmerPoorEnum.PROVINCE.getIndex()];
		String city = data[row][FarmerPoorEnum.CITY.getIndex()];
		String country = data[row][FarmerPoorEnum.COUNTRY.getIndex()];
		String town= data[row][FarmerPoorEnum.TOWN.getIndex()];
		String group = data[row][FarmerPoorEnum.GROUP.getIndex()];
		String address = province+city+country+town+group;
		farmer.setAddress(address);
		Farmer dbFarmer = farmerService.findById(farmer.getFarmeridnum(),"JinZhunHuPin");
		try {
			if(dbFarmer == null){
				farmerService.save(farmer);
			}else{
				farmer.setId(dbFarmer.getId());
				farmerService.updateBySelective(farmer);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	return msgs;
	
}
public List<Map<String,String>> importFarmerNY(String organId,String organName,
		String recorder,Date recordTime,String[][] data){
	
	List<Map<String,String>> msgs = new ArrayList<Map<String,String>>();
	for(int row =3;row<data.length;row++){
		Map<String,String> msg = new HashMap<String,String>();
		Farmer farmer = new Farmer();
		farmer.setSourcecode("C1010336005158");
		farmer.setSourcename("中国农业银行新干县支行");
		farmer.setRunitid(organId);
		farmer.setRunitname(organName);
		farmer.setRecorder(recorder);
		farmer.setRecordtime(recordTime);
		farmer.setFarmertype(0);
		String farmerName = data[row][FarmerNYEnum.FARMERNAME.getIndex()];
		if(StringUtils.isEmpty(farmerName)){
			msg.put("row", String.valueOf(row));
			msg.put("tip", "info");
			msg.put("msg", "姓名不能为空。");
			continue;
		}
		farmer.setFarmername(farmerName);
		String farmerIdNum = data[row][FarmerNYEnum.IDNUM.getIndex()];
		if(StringUtils.isEmpty(farmerIdNum)){
			msg.put("row", String.valueOf(row));
			msg.put("tip", "info");
			msg.put("msg", "证件号码不能为空。");
			continue;
		}
		farmer.setFarmeridnum(farmerIdNum);
		String sex = DicExplain.explain(DicExplain.$SEX,data[row][FarmerNYEnum.SEX.getIndex()]);
		farmer.setSex(Integer.valueOf(sex));
		String education = DicExplain.explain(DicExplain.$EDUCATION,data[row][FarmerNYEnum.EDUCATION.getIndex()]);
		farmer.setEducation(Integer.valueOf(education));
		String marryStatus = DicExplain.explain(DicExplain.$MARRYSTATUS,data[row][FarmerNYEnum.MARRYSTATUS.getIndex()]);
		farmer.setMarrystatus(Integer.valueOf(marryStatus));
		farmer.setAddress(data[row][FarmerNYEnum.ADDRESS.getIndex()]);
		String postCodeStr = data[row][FarmerNYEnum.POSTCODE.getIndex()];
		Integer postCode = null;
		try{
			postCode = StringUtils.isEmpty(postCodeStr)?null:Integer.valueOf(postCodeStr);
		}catch(NumberFormatException e){
		}
		farmer.setPostcode(postCode);
		Farmer dbFarmer = farmerService.findById(farmer.getFarmeridnum(),"C1010336005158");
		try {
			if(dbFarmer == null){
				farmerService.save(farmer);
			}else{
				farmer.setId(dbFarmer.getId());
				farmerService.updateBySelective(farmer);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//关联农户信贷信息
		List<Loan> loans = loanService.findByID("1","0",farmer.getFarmeridnum());
		if(loans == null || loans.size()==0){
		//未匹配信贷信息提示未匹配
			msg.put("row", String.valueOf(row));
			msg.put("tip", "error");
			msg.put("msg", "["+data[row][FarmerNYEnum.FARMERNAME.getIndex()]+"]"
					+ "找到"+loans.size()+"条匹配的信贷信息");
			msgs.add(msg);
		}else{
		//匹配信贷信息提示匹配数量
			for(Iterator<Loan> it = loans.iterator();it.hasNext();){
				Loan loan = it.next();
				loan.setClientId(farmer.getId());
				try {
					loanService.update(loan);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			msg.put("row", String.valueOf(row));
			msg.put("tip", "info");
			msg.put("msg", "["+data[row][FarmerNYEnum.FARMERNAME.getIndex()]+"]"
					+ "找到"+loans.size()+"条匹配的信贷信息");
			msgs.add(msg);
		}	
	}
	return msgs;
		
}
	public List<Map<String,String>> importLoanNS(String organId,String organName,
			String recorder,Date recordTime,String[][] data){
		
		List<Map<String,String>> msgs = new ArrayList<Map<String,String>>();
		for(int row=1;row<data.length;row++){
			Map<String,String> msg = new HashMap<String,String>();
			Loan loan = new Loan();
			loan.setClientNum(data[row][LoanNSEnum.CLIENTNUM.getIndex()]);
			String noteNum = data[row][LoanNSEnum.NOTENUM.getIndex()];
			loan.setNoteNum(noteNum);
			Loan dbloan = loanService.findByNoteNum(noteNum);
			if(dbloan != null){
				msg.put("row", String.valueOf(row));
				msg.put("tip", "info");
				msg.put("msg", "该信贷信息已经存在、不再导入！");
				msgs.add(msg);
				continue;
			}
			String compactNum =data[row][LoanNSEnum.COMPACTNUM.getIndex()];
			if(StringUtils.isEmpty(compactNum)){
				//如果合同号为空提示合同号不能为空
				msg.put("row", String.valueOf(row));
				msg.put("tip", "error");
				msg.put("msg", "信贷信息的合同号不能为空！");
				msgs.add(msg);
				continue;
			}
			loan.setCompactNum(compactNum);
			loan.setLoanDate(data[row][LoanNSEnum.LOANDATE.getIndex()]);
			loan.setLimitDate(data[row][LoanNSEnum.LIMITDATE.getIndex()]);
			loan.setLimitDate1(data[row][LoanNSEnum.LIMITDATE1.getIndex()]);
			loan.setClientName(data[row][LoanNSEnum.CLIENTNAME.getIndex()]);
			String currency =DicExplain.explain(DicExplain.$CURRENCY, data[row][LoanNSEnum.CURRENCY.getIndex()]);
			loan.setCurrency(currency);
			loan.setAmount(data[row][LoanNSEnum.AMOUNT.getIndex()]);
			loan.setBalance(data[row][LoanNSEnum.BALANCE.getIndex()]);
			loan.setOweInterest(data[row][LoanNSEnum.OWEINTEREST.getIndex()]);
			loan.setCurrentRate(data[row][LoanNSEnum.CURRENTRATE.getIndex()]);
			loan.setRateType(data[row][LoanNSEnum.RATETYPE.getIndex()]);
			String clientType = DicExplain.explain(DicExplain.$CLIENTTYPE,data[row][LoanNSEnum.CLIENTTYPE.getIndex()]);
			loan.setClientType(clientType);
			String idType = DicExplain.explain(DicExplain.$IDTYPE,data[row][LoanNSEnum.IDTYPE.getIndex()]);
			loan.setIdType(idType);
			loan.setIdNum(data[row][LoanNSEnum.IDNUM.getIndex()]);
			loan.setPhone(data[row][LoanNSEnum.PHONE.getIndex()]);
			loan.setAddress(data[row][LoanNSEnum.ADDRESS.getIndex()]);
			loan.setLoanUse(data[row][LoanNSEnum.LOANUSE.getIndex()]);
			loan.setGuaranteeType(data[row][LoanNSEnum.GUARANTEETYPE.getIndex()]);
			loan.setGrade(data[row][LoanNSEnum.GRADE.getIndex()]);
			loan.setGrade1(data[row][LoanNSEnum.GRADE1.getIndex()]);
			loan.setSettleType(data[row][LoanNSEnum.SETTLETYPE.getIndex()]);
			loan.setChargePerson(data[row][LoanNSEnum.CHARGEPERSON.getIndex()]);
			loan.setOrganCode(data[row][LoanNSEnum.ORGANCODE.getIndex()]);
			loan.setOrganName(data[row][LoanNSEnum.ORGANNAME.getIndex()]);
			loan.setBusinessType(data[row][LoanNSEnum.BUSINESSTYPE.getIndex()]);
			loan.setBusinessBody(data[row][LoanNSEnum.BUSINESSBODY.getIndex()]);
			loan.setProvideType(data[row][LoanNSEnum.PROVIDETYPE.getIndex()]);
			loan.setInvest(data[row][LoanNSEnum.INVEST.getIndex()]);
			loan.setTermType(data[row][LoanNSEnum.TERMTYPE.getIndex()]);
			loan.setGuaranteeType1(data[row][LoanNSEnum.GUARANTEETYPE1.getIndex()]);
			loan.setFloatScope(data[row][LoanNSEnum.FLOATSCOPE.getIndex()]);
			loan.setAdjustType(data[row][LoanNSEnum.ADJUSTTYPE.getIndex()]);
			loan.setRunType(data[row][LoanNSEnum.RUNTYPE.getIndex()]);
			loan.setFrequeency(data[row][LoanNSEnum.FREQUEENCY.getIndex()]);
			loan.setRunitId(organId);
			loan.setRunitName(organName);
			Farmer farmer = farmerService.findById(loan.getIdNum(),organId);
			if(farmer == null ){
			//未匹配农户信息提示未匹配
				loanService.findByNoteNum(noteNum);
				try {
					loanService.save(loan);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msg.put("row", String.valueOf(row));
				msg.put("tip", "success");
				msg.put("msg", "导入成功但未匹配农户信息，请稍后导入农户信息！");
				msgs.add(msg);
			}else{
			//匹配农户信息提示匹配数量
				msg.put("row", String.valueOf(row));
				loan.setClientId(farmer.getId());
				try {
					loanService.save(loan);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msg.put("tip", "success");	
				msg.put("msg", "导入成功且匹配到相应的农户信息！");
				msgs.add(msg);
			}
		}
		return msgs;
	}
	public List<Map<String,String>> importLoanNY(String organId,String organName,
			String recorder,Date recordTime,String[][] data){
		
		List<Map<String,String>> msgs = new ArrayList<Map<String,String>>();
		for(int row=3;row<data.length;row++){
			Map<String,String> msg = new HashMap<String,String>();
			Loan loan = new Loan();
			loan.setClientNum(data[row][LoanNYEnum.CLIENTNUM.getIndex()]);
			String noteNum = data[row][LoanNYEnum.NOTENUM.getIndex()];
			if(StringUtils.isEmpty(noteNum)){
				msg.put("row", String.valueOf(row));
				msg.put("tip", "error");
				msg.put("msg", "申请书编号不能为空！");
				msgs.add(msg);
				continue;
			}
			loan.setNoteNum(noteNum);
			Loan dbloan = loanService.findByNoteNum(noteNum);
			if(dbloan != null){
				msg.put("row", String.valueOf(row));
				msg.put("tip", "info");
				msg.put("msg", "该信贷信息已经存在、不再导入！");
				msgs.add(msg);
				continue;
			}
			String compactNum =data[row][LoanNYEnum.COMPACTNUM.getIndex()];
			if(StringUtils.isEmpty(compactNum)){
				//如果合同号为空提示合同号不能为空
				msg.put("row", String.valueOf(row));
				msg.put("tip", "error");
				msg.put("msg", "信贷信息的合同号不能为空！");
				msgs.add(msg);
				continue;
			}
			loan.setCompactNum(compactNum);
			loan.setLoanDate(data[row][LoanNYEnum.LOANDATE.getIndex()]);
			loan.setLimitDate(data[row][LoanNYEnum.LIMITDATE.getIndex()]);
			String currency =DicExplain.explain(DicExplain.$CURRENCY, data[row][LoanNYEnum.CURRENCY.getIndex()]);
			loan.setCurrency(currency);
			loan.setAmount(data[row][LoanNYEnum.AMOUNT.getIndex()]);
			loan.setBalance(data[row][LoanNYEnum.BALANCE.getIndex()]);
			loan.setClientType("1");
			loan.setClientName(data[row][LoanNYEnum.CLIENTNAME.getIndex()]);
			String idType = DicExplain.explain(DicExplain.$IDTYPE,data[row][LoanNYEnum.IDTYPE.getIndex()]);
			loan.setIdType(idType);
			loan.setIdNum(data[row][LoanNYEnum.IDNUM.getIndex()]);
			loan.setGuaranteeType(data[row][LoanNYEnum.GUARANTEETYPE.getIndex()]);
			loan.setOrganCode(data[row][LoanNYEnum.ORGANCODE.getIndex()]);
			loan.setGuaranteeType1(data[row][LoanNYEnum.GUARANTEETYPE1.getIndex()]);
			loan.setRunitId(organId);
			loan.setRunitName(organName);
			Farmer farmer = farmerService.findById(loan.getIdNum(),organId);
			if(farmer == null ){
			//未匹配农户信息提示未匹配
				loanService.findByNoteNum(noteNum);
				try{
					loanService.save(loan);
				}catch(Exception e){
					System.err.println(row);
					System.err.println(loan.getNoteNum());
				}
				msg.put("row", String.valueOf(row));
				msg.put("tip", "success");
				msg.put("msg", "导入成功但未匹配农户信息，请稍后导入农户信息！");
				msgs.add(msg);
			}else{
			//匹配农户信息提示匹配数量
				msg.put("row", String.valueOf(row));
				loan.setClientId(farmer.getId());
				try {
					loanService.save(loan);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msg.put("tip", "success");	
				msg.put("msg", "导入成功且匹配到相应的农户信息！");
				msgs.add(msg);
			}
		}
	
		return msgs;
	}
}
