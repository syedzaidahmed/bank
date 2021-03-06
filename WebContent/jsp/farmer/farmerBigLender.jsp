<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${pageContext.request.contextPath}/miniui/boot.js" type="text/javascript"></script>
<title>Insert title here</title>
<style type="text/css">
table{
color:#333333;
border-width: 1px;
border-color: black;
border-collapse: collapse;
}
</style>

</head>
<body>
<form action="/bank/farmer/queryBigLender.do" method="POST">
	<table width="90%" border="0" cellspacing="0" cellpadding="0" style="margin:auto auto">
		<tr>
			<td>
				<select name="type">
					<c:choose>
						<c:when test="${type == '1'}">
						<option value="1" selected="selected">按贷款余额</option>
						</c:when>
						<c:otherwise>
						<option value="1">按贷款余额</option>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${type == '2'}">
						<option value="2" selected="selected">贷款累计发放金额</option>
						</c:when>
						<c:otherwise>
						<option value="2">贷款累计发放金额</option>
						</c:otherwise>
					</c:choose>
				</select>
			</td>
			<td>
				<input type="submit" value="查询"/>
			</td>
		</tr>
	</table>
</form>
	<div style="margin:auto auto;text-align:center">
		<h1>农户贷款大户统计</h1>
		<div style="color:red">
			可按地区（某个乡镇），按贷款余额或贷款累计发放额两种情况统计（排名前50农户）。
		</div>
	</div>
	<table width="90%"border="1" cellspacing="0" cellpadding="0" style="margin:auto auto">

	<tr>
		<th>
			编号
			</th>
			<th>
			姓名
			</th>
			<th>
			身份证号码
			</th>
			<th>
			金额(元)
			</th>
	</tr>
	<c:forEach items="${lenders}" var="temp" varStatus="status">
		<tr>
		 <td align="center">
				${status.index+1}
		</td>
			<td align="center">
			<c:forEach var="lender" items="${temp}"> 
				<c:if test="${lender.key =='FARMERNAME'}">
				${lender.value}
			</c:if>
			</c:forEach>
			</td>
			<td align="center">
			<c:forEach var="lender" items="${temp}"> 
				<c:if test="${lender.key =='FARMERIDNUM'}">
				${lender.value}
			</c:if>
			</c:forEach>
			</td>
			<td align="center">
			<c:forEach var="lender" items="${temp}"> 
				<c:if test="${lender.key =='TOTAL'}">
				${lender.value}
			</c:if>
			</c:forEach>
			</td>
			</tr>
		
	</c:forEach>
	</table>

</body>
</html>