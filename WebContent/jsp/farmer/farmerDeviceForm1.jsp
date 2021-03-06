<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../common/CurrentTime.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备信息</title>
<link href="${pageContext.request.contextPath}/jsp/farmer/form.css"  rel="stylesheet" type="text/css"/>
<script src="${pageContext.request.contextPath}/miniui/boot.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/jsp/farmer/farmer.js" type="text/javascript"></script>
</head>
<body>
<div id="form1">
<form  action="/bank/farmer/saveDevice1.do"  class="farmerDevice" method="POST">
<div class="queryPane">
<table cellpadding="0" cellspacing="0"  height="30px">
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
</div>
<fieldset style="width:90%;margin:auto auto">
<legend><label>主要设备基本情况</label></legend>
<div class="fieldset-body">
<table width="100%"><tr><td>
<input name="id" class="mini-hidden" value="${device.id}"/>
<input name="farmerid" class="mini-hidden" value="${farmer.id}" />
<input name="farmeridnum" class="mini-hidden"  value="${farmer.farmeridnum}" />
<input name="sourcecode" class="mini-hidden"  value="${device.sourcecode}" />
<input name="sourcename" class="mini-hidden"  value="${device.sourcename}" />
<input name="runitid" class="mini-hidden"  value="${device.runitid}" />
<input name="runitname" class="mini-hidden"  value="${device.runitname}" />
<input name="recorder" class="mini-hidden" value="${recorder}"/>
<input name="recordtime" class="mini-hidden" value="${currentTime}"/>
<table border="0" cellpadding="1" cellspacing="10" width="100%" >
<tr><td>
<table width="100%">
<tr>
	<td class="required_text" width="2%">*</td>
	<td style="width:15%">设备名称:</td>
    <td style="width:33%">
	    <input name="name" class="mini-textbox"  value="${device.name}" style="width:90%"
	    errorMode="none" required="true" requiredErrorText="设备名称不能为空!" onvalidation="onValidation" />
    </td>
    <td class="required_text" width="2%">*</td>
    <td style="width:15%">品牌型号:</td>
    <td style="width:33%" >
        <input name="brand" class="mini-textbox" value="${device.brand}" style="width:90%" />
    </td>
</tr>
<tr>
	<td></td>
	<td colspan="2" id="name_error" class="errorText"></td>
	<td></td>
	<td colspan="2" id="brand_error" class="errorText"></td>
</tr>
<tr>
	<td class="required_text">*</td>
    <td >构建价格(元):</td>
    <td >
    	<input name="buyingprice" class="mini-textbox" value="${device.buyingprice}" style="width:90%" 
    	errorMode="none" vtype="float" required="true" requiredErrorText="构建价格不能为空!" onvalidation="onValidation"  />
    </td>
    <td class="required_text">*</td>
    <td >购进年份:</td>
    <td >
    	<input name="buyingdate" class="mini-textbox" value="${device.buyingdate}" style="width:90%"
    	errorMode="none" vtype="float" required="true" requiredErrorText="购进年份不能为空!" onvalidation="onValidation" />
    </td>
</tr>
<tr>
	<td></td>
	<td colspan="2" id="buyingprice_error" class="errorText"></td>
	<td></td>
	<td colspan="2" id="buyingdate_error" class="errorText"></td>
</tr>
<tr>
	<td class="required_text"></td>
	<td >当前评估价格(元):</td>
    <td >
   		<input name="assessprice" class="mini-textbox" value="${device.assessprice}" style="width:90%"
   		errorMode="none" vtype="float" onvalidation="onValidation"/>
    </td>
</tr>
<tr>
	<td></td>
	<td colspan="2" id="assessprice_error" class="errorText"></td>
</tr>
</table>
</td></tr></table>
</table>
</div>
</fieldset>
</form>
</div>
<script type="text/javascript">
	function back(){
		history.go(-1);
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
</form>
</body>
</html>