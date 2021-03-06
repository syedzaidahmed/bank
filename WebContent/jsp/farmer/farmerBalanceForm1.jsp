<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="dt" uri="http://gov.jian.bank/dateformat" %>    
<%@ include file="../common/CurrentTime.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>农户收支信息</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link href="${pageContext.request.contextPath}/jsp/farmer/form.css"  rel="stylesheet" type="text/css"/>
<script src="${pageContext.request.contextPath}/miniui/boot.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/jsp/farmer/farmer.js" type="text/javascript"></script>
</head>
<body>
<form  action="/bank/farmer/saveBalance1.do" id="farmerPay" method="POST">
<input name="farmerid" class="mini-hidden" value="${farmer.id}" />
<table width="100%" cellpadding="0" cellspacing="0"  height="60px">
    <tr>
    	<td class="labelName" width="30%" align="right">姓名</td>
    	<td class="labelValue" width="10%">:${farmer.farmername }</td>
    	<td class="labelName" width="10%" align="right">证件号码</td>
    	<td class="labelValue" width ="10%">:${farmer.farmeridnum }</td>
    	<td width="10%" align="center">
       		<input type="button" class="bank-btn" onclick="submitForm()" value="保存"/>
        </td>
        <td width="30%" align="left" >
         	<input type="button" class="bank-btn" onclick="back()" value="返回"/>
        </td>
    </tr>
</table>
<div id="form1" style="width:90%;margin:auto auto">
<input name="deleteIncome" id="deleteIncome" class="mini-hidden"/>
<input name="id" class="mini-hidden" value="${balance.id}" />
<input name="farmeridnum" class="mini-hidden"  value="${farmer.farmeridnum}" />
<input name="sourcecode" class="mini-hidden"  value="${balance.sourcecode}" />
<input name="sourcename" class="mini-hidden"  value="${balance.sourcename}" />
<input name="runitid" class="mini-hidden"  value="${balance.runitid}" />
<input name="runitname" class="mini-hidden"  value="${balance.runitname}" />
<input name="recorder" class="mini-hidden" value="${recorder}"/>
<input name="recordtime" class="mini-hidden" value="${currentTime}"/>
<fieldset id="fd2" style="width:100%;margin:auto auto">
<legend><label>农户年度收支基本情况</label></legend>
<div class="fieldset-body">
<table width="100%">
<tr>
	<td class="required_text" width="2%" >*</td>
	<td style="width:18%">年份:</td>
	<td width="30%">
		<input  name="year" class="mini-datepicker" value="${dt:format(balance.year,'yyyy-MM-dd')}" format="yyyy" style="width:90%"
			errorMode="none" required="true" requiredErrorText="年份不能为空" onvalidation="onValidation"/>
	</td>
	<td class="required_text" width="2%" ></td>
	<td style="width:18%">主要农业年度净收入合计(元):</td>
    <td width="30%">
		<input  name="farmingincome" class="mini-textbox" value="${balance.farmingincome}" style="width:90%"
	   	 errorMode="none" vtype="float"  onvalidation="onValidation"/>
    </td>
</tr>
<tr>
	<td></td>
	<td id="year_error" class="errorText" colspan="2"></td>
	<td></td>
	<td id="farmingIncome_error" class="errorText" colspan="2"></td>
</tr>
<tr>
	<td class="required_text"></td>
    <td >林、牧、副、渔业年度净收入合计(元):</td>
    <td >
	    <input name="avocationincome" class="mini-textbox" value ="${balance.avocationincome}" style="width:90%" 
	 	errorMode="none" vtype="float"  onvalidation="onValidation"/>
    </td>
    <td class="required_text"></td>
    <td>家庭工商业年度净收入合计(元):</td>
    <td >
    	<input name="businessincome" class="mini-textbox" value ="${balance.businessincome}" style="width:90%"
        errorMode="none" vtype="float"  onvalidation="onValidation"/>
    </td>  
</tr>
<tr>
	<td ></td>
	<td id="avocationincome_error" class="errorText" colspan="2"></td>
	<td ></td>
	<td id="businessincome_error" class="errorText" colspan="2"></td>
	</tr>
<tr>
	<td class="required_text"></td>
	<td> 外出务工年度净收入合计(元):</td>
    <td>
    	<input name="workincome" class="mini-textbox" value ="${balance.workincome}" style="width:90%"
        errorMode="none" vtype="float"  onvalidation="onValidation"/>
    </td>  
    <td class="required_text"  ></td>                              
    <td>其他收入年度净收入合计(元):</td>
    <td>
    	<input name="otherincome" class="mini-textbox" value ="${balance.otherincome}" style="width:90%"
        errorMode="none" vtype="float"  onvalidation="onValidation"/>
    </td>
</tr>
<tr>
	<td ></td>
	<td id="workincome_error" class="errorText" colspan="2"></td>
	<td ></td>
	<td id="otherincome_error" class="errorText" colspan="2"></td>
	</tr>
<tr>
<td class="required_text"></td>
	<td>生产支出(元):</td>
    <td>
    	<input  name="productpay" class="mini-textbox" value ="${balance.productpay}" style="width:90%"
        errorMode="none" vtype="float"  onvalidation="onValidation"/>
    </td>
    <td class="required_text"  ></td>
    <td>生活支出(元):</td>
    <td>
    	<input name="livingpay" class="mini-textbox" value ="${balance.livingpay}" style="width:90%"
        errorMode="none" vtype="float"  onvalidation="onValidation"/>
    </td>
</tr>
<tr>
	<td ></td>
	<td id="productpay_error" class="errorText" colspan="2"></td>
	<td ></td>
	<td id="livingpay_error" class="errorText" colspan="2"></td>
</tr>
<tr>
<td class="required_text"  ></td>
	<td> 医疗支出(元):</td>
    <td>
    	<input  name="medicalpay" class="mini-textbox" value ="${balance.medicalpay}"  style="width:90%"
        errorMode="none" vtype="float"  onvalidation="onValidation"/>
    </td>
    <td class="required_text"  ></td>
    <td>教育支出(元):</td>
    <td>
    	<input  name="educatepay" class="mini-textbox" value ="${balance.educatepay}" style="width:90%"
        errorMode="none" vtype="float"  onvalidation="onValidation"/>
    </td>
</tr>
<tr>
		<td ></td>
		<td id="medicalpay_error" class="errorText" colspan="2"></td>
		<td ></td>
		<td id="educatepay_error" class="errorText" colspan="2"></td>
	</tr>
<tr>
<td class="required_text"  ></td>
	<td style="width:18%">参保支出(元):</td>
    <td style="width:27%">
    	<input  name="insuredpay" class="mini-textbox" value ="${balance.insuredpay}" style="width:90%"
        errorMode="none" vtype="float"  onvalidation="onValidation"/>
    </td>
    <td class="required_text"  ></td>
    <td style="width:22%">其他支出(元):</td>
    <td style="width:27%">
    	<input  name="otherpay" class="mini-textbox" value ="${balance.otherpay}" style="width:90%"
        errorMode="none" vtype="float"  onvalidation="onValidation"/>
    </td>
</tr>
<tr>
	<td ></td>
	<td id="insuredpay_error" class="errorText" colspan="2"></td>
	<td ></td>
	<td id="otherpay_error" class="errorText" colspan="2"></td>
	</tr>
<tr>
<td class="required_text"  >*</td>
	<td>家庭年度总收入合计(元):</td>
    <td>
    	<input name="totalincome" class="mini-textbox" value ="${balance.totalincome}" style="width:90%"
        	errorMode="none" vtype="float" required="true" requiredErrorText="家庭年度总收入合计不能为空" onvalidation="onValidation"	/>
    </td>
    <td class="required_text"  >*</td>
	<td>家庭年度支出总合计(元):</td>
	<td>
    	<input  name="totalpay" class="mini-textbox" value ="${balance.totalpay}" style="width:90%"
        errorMode="none" vtype="float" required="true" requiredErrorText="家庭年度支出总合计不能为空" onvalidation="onValidation"		/>
    </td>
</tr>
<tr>
	<td ></td>
	<td id="totalincome_error" class="errorText" colspan="2"></td>
	<td ></td>
	<td id="totalpay_error" class="errorText" colspan="2"></td>
</tr>
</table>
</div>
</fieldset>
</div>
<fieldset style="width:90%;margin:auto auto">
<legend><label>农户小微收入基本情况</label></legend>
<div class="fieldset-body">
<table width="100%"><tr><td>
<c:forEach items="${incomes}" var="income" varStatus="status">
<div id="farmerIncome${status.index}" class="farmerIncome">
<input name="incomes[${status.index}].id" class="mini-hidden" value="${income.id}"/>
<table width="100%" border="0" cellpadding="1" cellspacing="15" >
<tr><td>
<table width="100%" >
<tr>
    <c:choose>
	<c:when test="${status.index ==0}">
		<td rowspan="2" style="width:2%"></td>
	</c:when>
	<c:otherwise>
		<td rowspan="2" style="width:2%"><input type="button"  class="small_delbtn" value="" onclick="delIncome(${status.index},${farmer.id})"/></td>
	</c:otherwise>
	</c:choose>
	<td style="width:12%">家庭收入来源:</td>
	<td style="width:38%">
		<input name="incomes[${status.index}].incometype" class="mini-combobox" value="${income.incometype}" style="width:90%"
			required="true" requiredErrorText="家庭收入来源不能为空" 
            url="/bank/dic/IncomeType.txt" emptyText="请选择..."/>
    </td>
    <td style="width:12%">农作物或项目名称:</td>
    <td style="width:38%" >
     	<input type="text" name="incomes[${status.index}].incomename" value="${income.incomename}" style="width:90%"
     		required="true" requiredErrorText="名称不能为空"  />
    </td>
</tr>
 <tr>
	<td style="width:12%">净收入(元):</td>
    <td style="width:38%">
    	<input type="text" name="incomes[${status.index}].netincome" value="${income.netincome}"  style="width:90%"/>
    </td>
</tr>
</table>
</td></tr>
</table>
</div>
</c:forEach>
</td></tr>
<tr>
	<td colspan="2" align="center">
		<input type="button" class="bank-btn" onClick="addIncome(${farmer.id})" value="新增"/>
	</td>
</tr>
</table>
</div>
</fieldset>
</form>
<script type="text/javascript">
mini.parse();
var deleteIncome = new Array();
function addIncome(fid){
 $(".farmerIncome").last().after(FarmerIncome($(".farmerIncome").length,fid));
	  mini.parse();
}
function back(){
	window.history.go(-1);
}
function delIncome(index,fid){
	var incomeId = $("input[name^='incomes["+index+"].id'").val();
	$("#farmerIncome"+index).remove();
	deleteIncome.push(incomeId);
	$("#deleteIncome"). val(deleteIncome.join(","));
	var next = index+1;
	$(".farmerIncome").each(function(){
		var name = $(this).attr("id");
		var n = name.substr(12,name.length);
		if(n>index){
			$("input[name^='incomes["+next+"]']").each(function(){
				var oldName =$(this).attr("name");
				var newName = "incomes["+index+"]"+oldName.substr(oldName.indexOf("."),oldName.length);
				$(this).attr("name",newName);
			});
			$("#delIncome"+next).attr("onclick","delIncome("+index+","+fid+")");
			$(this).attr("id","farmerIncome"+index);
		};
		
	});
}
function submitForm() {           
	var form = new mini.Form("#form1");
    form.validate();
	if (form.isValid() == false) return;
	$("form").submit();
}
function updateError(e) {
	var id = e.sender.name + "_error";
    var el = document.getElementById(id);
    if (el) {
        el.innerHTML = e.errorText;
    }
}
function onValidation(e) {                  
    updateError(e);
}
</script>
</body>
</html>