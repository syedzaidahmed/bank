<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../common/CurrentTime.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>农户当前未结清信贷情况</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<script src="${pageContext.request.contextPath}/miniui/boot.js" type="text/javascript"></script>
</head>
<body>
<div class="mini-toolbar" style="padding-top:5px;border-bottom:0;">
	<table style="width:100%;">
    	<tr>
        	<td style="white-space:nowrap;">
	        	<a class="mini-button" iconCls="icon-save" plain="true" onclick="submitForm()">保存</a>
	           	<span class="separator"></span>
	            <a class="mini-button" iconCls="icon-upgrade" plain="true" onclick="back()">返回</a>
                <span class="separator"></span>
            </td>
         </tr>
      </table>
</div>
<div id="form1" style="width:90%;margin:auto auto">
	<form action="/bank/company/save.do" method="POST">
	<input type="hidden" name="deviceId"/>
	<input name="recorder" class="mini-hidden" value="管理员"/>
	<input name="recordTime" class="mini-hidden" value="${currentTime}"/>
	<table border="0" cellpadding="1" cellspacing="15" width="100%" >
	<tr>
	<td colspan="4" style="width:100%">
	<fieldset id="fd2" style="width:100%;margin:auto auto">
	<legend><label>农户当前未结清信贷基本情况</label></legend>
	<div class="fieldset-body">
	<table width="100%">
	<tr>
	<td style="width:10%"><label for="textbox1$text">户主姓名:</label></td>
	<td style="width:40%">
	<input id="textbox1"  name="farmerName" class="mini-textbox" required="true" 
		requiredErrorText="户主姓名不能为空" style="width:90%"/>
	</td>
	<td style="width:10%"><label for="textbox2$text">户主身份证号:</label></td>
	<td style="width:40%" >
	<input id="textbox2"  name="farmerIdnum" class="mini-textbox" required="true" 
		requiredErrorText="户主身份证号不能为空"  style="width:90%"/>
	</td>
	</tr>
	<tr>
	<td style="width:10%"><label for="textbox1$text">贷款金融机构:</label></td>
	<td style="width:40%">
	<input id="textbox1"  name="bank" class="mini-textbox" required="true" 
		requiredErrorText="贷款金融机构不能为空"style="width:90%"/>
	</td>
	<td style="width:10%"><label for="textbox2$text">业务类别:</label></td>
	<td style="width:40%" >
	<input id="textbox2"  name="businessType" class="mini-textbox" required="true" 
		requiredErrorText="业务类别不能为空" style="width:90%"/>
	</td>
	</tr>
	<tr>
	<td style="width:10%"><label for="textbox1$text">贷款用途:</label></td>
	<td style="width:40%">
	<input id="textbox1"  name="loanUse" class="mini-textbox" required="true" 
		requiredErrorText="贷款用途不能为空" style="width:90%"/>
	</td>
	<td style="width:10%"><label for="textbox1$text">贷款方式:</label></td>
	<td style="width:40%">
	<input id="textbox1"  name="loanWay" class="mini-combobox" required="true" 
		requiredErrorText="贷款方式不能为空" style="width:90%"/>
	</td>
	</tr>
	<tr>
	<td style="width:10%"><label for="textbox1$text">贷款余额(万元):</label></td>
	<td style="width:40%">
	<input id="textbox1"  name="loanBalance" class="mini-textbox" required="true" 
		requiredErrorText="当前评估价格不能为空" style="width:90%"/>
	</td>
	<td style="width:10%"><label for="textbox1$text">贷款发放时间:</label></td>
	<td style="width:40%">
	<input id="textbox1"  name="provideTime" class="mini-datepicker" required="true" 
		requiredErrorText="贷款发放时间不能为空" style="width:90%"/>
	</td>
	</tr>
	<tr>
	<td style="width:10%"><label for="textbox1$text">贷款到期时间:</label></td>
	<td style="width:40%">
	<input id="textbox1"  name="limitTime" class="mini-datepicker" required="true" 
		requiredErrorText="贷款到期时间不能为空" style="width:90%"/>
	</td>
	</tr>
	</table>
	</div>
	</fieldset>
	</td>
	</tr>
	</table>
	</form>
</div>
<script type="text/javascript">

	mini.parse();
	var form = new mini.Form("#form1");
	
	$(document).ready(function(){
		$.ajax({
		    url: "${pageContext.request.contextPath}/farmer/loadUnbalanced.do",
		    type: "post",
		    data:{id:"${param.id}"},
		    success: function (text) {
		        var data = mini.decode(text);   //反序列化成对象
		        form.setData(data);             //设置多个控件数据
		    }
		});
		
	});
	function back(){
		history.go(-1);
	}
	function submitForm() {
		//提交表单数据
	    var formData = form.getData();      //获取表单多个控件的数据
	    var json = mini.encode(formData);   //序列化成JSON
	    $.ajax({
	        url: "${pageContext.request.contextPath}/farmer/saveUnbalanced.do",
	        type: "post",
	        data: { formData: json},
	        contentType: "application/x-www-form-urlencoded; charset=utf-8",
	        success: function (text) {
	        	var data = mini.decode(text);   //反序列化成对象
		        if(data.id!=null){
		       	 	form.setData(data);  
		        }
	        	 mini.alert('保存成功！');
	        },
	        error: function (jqXHR, textStatus, errorThrown) {
	            mini.alert('系统异常！');
	        }
	});
	function onDrawCell(e) {
            value = e.value;
            //组织HTML设置给cellHtml
            e.cellHtml = '<span style="color:red;">'+value+'</span>';   
	}
}
</script>
</body>
</html>