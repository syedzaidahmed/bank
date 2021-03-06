<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.bank.controller.economy.CooperationProfitController" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/jsp/farmer/form.css"  rel="stylesheet" type="text/css"/>
<script src="${pageContext.request.contextPath}/miniui/boot.js" type="text/javascript"></script>
<title>Insert title here</title>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

body {
	font-size: 14px;
}

legend {
	font-weight: bold;
	color: seagreen;
	font-family: "圆幼", "宋体";
}

table {
	margin-top: 10px;
	margin-bottom: 10px;
	width: 100%;
}

li {
	margin-top: 5px;
	margin-left: 30px;
}

input {
	vertical-align: middle;
	margin: 0;
	padding: 0
}

.txt {
	height: 22px;
	border: 1px solid #cdcdcd;
	width: 180px;
}

.btn {
	background-color: #FFF;
	border: 1px solid #CDCDCD;
	height: 24px;
	width: 70px;
	display: inline-block;
	cursor: hand;
}

.file {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 120px;
	height: 24px;
	opacity: 0;
	filter: alpha(opacity = 0);
	cursor: hand;
}



#downLoadModel {
	width: 100px;
	height: 25px;
	border: 0;
	background: url(/bank/images/download.png) no-repeat;
}



.success {
	color: green;
	font-weight: bold;
	height: 25px;
}

.info {
	color: blue;
	font-weight: bold;
	height: 25px;
}
</style>
</head>
<body>

<div class="queryPane">
<form action="#" method="post" enctype="multipart/form-data" onsubmit="return validateSubmit();">
<table width="100%" height ="30px">
	<tr>
		<td class="labelName" width="15%" align="right">请选择数据来源：</td>
		<td  width="15%">
			<input id="sourceForm" class="mini-buttonedit txt" onbuttonclick="onButtonEdit"/>
		</td>
 		<td class="labelName" width="15%" align="right">请选择文件路径：</td>
    		<td width="15%" >
        		<input type='text' name='textfield' id='textfield' class='bank-text' value="" />
        	</td>
        	<td width="15%" >
        	<div style="position:relative">
	          <input id="scanFile" type='button'  class="bank-btn" value="浏览" />
	        	<input type="file" name="mufile" class="file" id="mufile" size="28" onchange="judgeFile(this)" />
       	</div>
        </td>
        <td align="left" >
            <input id="uploadFile" type="submit" name="submit" class='bank-btn' value="上传"/>
        </td>
        
     </tr>
 </table>
</form>
</div>
<fieldset class="bank-view-fieldset" style="width:90%;margin:auto auto">
<legend style="width:310px;height:74px;background:url(/bank/images/filetips.png) no-repeat"></legend>
<table width="100%">
    <tr>
        <td>
            <div >
                <ol>
                	<li>
                        	请先<a href="/bank/jsp/cooperation/model/ProfitInfoModel.xlsx"><font color="red">下载模板</font></a>后,在模板内进行操作
                    </li>
                    <li>
                        	支持EXCEL格式导入[Excel2003,Excel2007 等],本模板为07,需要其余版本请在Excel另存为...
                    </li>
                    <li>
                        	导入文件数据比较大时、可能需要等待一段时间
                    </li>
                </ol>

            </div>
        </td>
    </tr>
</table>
</fieldset>
<c:if test="${!empty msgs}">
<div style="scrollbar-face-color:#6DC8E3;width:90%;height:45px;verflow-y:auto;margin:auto">
<table  id="tips" border="1" cellspacing=0 cellpadding=0 >
<c:forEach items="${msgs}" var="msg">
<tr >
	<c:choose>
		<c:when test="${msg.tip == 'success'}">
			<td class ="success" width="10%" align="center">
				${msg.row}
			</td>
		</c:when>
		<c:when test="${msg.tip == 'error'}">
			<td class="error" width="10%" align="center">
				${msg.row}
			</td>
		</c:when>
		<c:otherwise>
			<td class="info" width="10%" align="center">
			       ${msg.row}
			</td>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${msg.tip == 'success'}">
			<td class ="success" width="10%" align="center">
				成功
			</td>
		</c:when>
		<c:when test="${msg.tip == 'error'}">
			<td class="error" width="10%" align="center">
				失败
			</td>
		</c:when>
		<c:otherwise>
			<td class="info" width="10%" align="center">
			       提示
			</td>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${msg.tip == 'success'}">
			<td class ="success">
				&nbsp;&nbsp;&nbsp;
				${msg.msg}
			</td>
		</c:when>
		<c:when test="${msg.tip == 'error'}">
			<td class="error" >
				&nbsp;&nbsp;&nbsp;
				${msg.msg}
			</td>
		</c:when>
		<c:otherwise>
			<td class="info">
			      &nbsp;&nbsp;&nbsp;
				${msg.msg}
			</td>
		</c:otherwise>
	</c:choose>
	</tr>
</c:forEach>
</table>
</div>
</c:if>
<%
com.bank.beans.User user = (com.bank.beans.User) request.getSession().getAttribute("user");
java.util.List<java.util.Map<String,String>> list = CooperationProfitController.uMap.get(user.getOrganId()+"$"+user.getUserId());
%>
<c:if test="<%=((list!=null && list.size()>0) || request.getAttribute(\"importError\") != null) %>">
<div align="center">
<iframe src="/bank/common/viewView.do?dest=cooperation/profitInfo/profitImportResult" scrolling="no" 
   			width="95%" title="错误记录及原因" frameborder="0" align="center" style="margin-top:-10px">
   </iframe>
</div>
</c:if>
 </div>
</body>
<script type="text/javascript">
function judgeFile(htmlEl){
	if(htmlEl){
		var v = htmlEl.value;
		var str = v.substr(v.lastIndexOf(".")+1).toUpperCase();
		if(str != "XLS" && str != "XLSX"){
			alert("文件格式不正确,请选择Excel文件");
			document.getElementById('textfield').value = '';
		}else{
			document.getElementById('textfield').value=v;
		}
	}
}

function downLoadExcel(){
	window.location.href = '/bank/jsp/cooperation/model/ProfitInfoModel.xlsx';
}
function validateSubmit(){
	var ls = mini.get("sourceForm").getValue();
	if(ls =='' || ls==null){
		mini.alert("请选择数据来源");
		return false;
	}
	var s = document.getElementById('textfield').value;
	if(s == ''  || s == null){
		mini.alert('请选择文件后操作');
		return false;
	}
	document.forms[0].action="/bank/economy/profit/loadFile.do?t="+ls;
	return true;;
}
mini.parse();
function onButtonEdit(e) {
   var btnEdit = this;
    mini.open({
        url: "/bank/common/viewView.do?dest=cooperation/mapt/mapt",
        showMaxButton: false,
        title: "选择树",
        width: 450,
        height: 353,
        ondestroy: function (action) {                    
            if (action == "ok") {
                var iframe = this.getIFrameEl();
                var data = iframe.contentWindow.GetData();
                data = mini.clone(data);
                if (data) {
                    btnEdit.setValue(data.id);
                    btnEdit.setText(data.text);
                }
            }
        }
    });            
     
}
</script>
</html>