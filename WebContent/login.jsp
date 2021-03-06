<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><c:set var="contextPath" value="${pageContext.request.contextPath}"/><c:set var="basePath" value="${pageContext.request.scheme}${'://'}${pageContext.request.serverName}${':'}${pageContext.request.serverPort}${pageContext.request.contextPath}"/><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%!org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(getClass());%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="language" content="en-US" />
<meta name="description" content="" />
<meta name="keywords" content=",,," />
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="r/reset.css" media="screen" />
<link rel="stylesheet" type="text/css" href="r/base.css" media="screen" />

<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js?ver=1.6.1'></script>
<script type="text/javascript" src="r/base.js"></script>
<title>login</title>
</head>
<body>
<%@ include file="include/navbar.jsp"%>
<div>
	<span class="infoMsg" align="left"> Please use sysadmin/asdfzxcv as username/password to test it. </span><br/>
	<c:if test="${param['action']=='login' &&empty param['username']  }"><span span class="warningMsg" align="left">please input username</span><br/></c:if>
	<c:if test="${param['action']=='login' &&empty param['password'] }"><span span class="warningMsg" align="left">please input password</span><br/></c:if>
	<c:if test="${param['action']=='login' && (not empty param['username'] && not empty param['password']) }"><span class="errorMsg" align="left">login fail!</span><br/></c:if>
	<c:if test="${warningMsg!=null}"><span class="warningMsg" align="left"> ${warningMsg} </span><br/></c:if>
	<c:if test="${errorMsg!=null}"><span class="errorMsg" align="left"> ${errorMsg} </span><br/></c:if>
</div>
<form method="post" action="loginControl"><input type="hidden" name="action" value="login"/>
	<table border="0" cellpadding="3">
		<thead>
			<tr>
				<th colspan="2" align='left'><h1>Login Here</h1></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td align='right'>Username</td>
				<td><input type="text" name="username" value="" autofocus/></td>
			</tr>
			<tr>
				<td align='right'>Password</td>
				<td><input type="password" name="password" value="" /></td>
			</tr>
			<tr>
				<td align='center' colspan="2">
					<input type="submit" value="Login" /> <input type="reset" value="Reset" />
				</td>
			</tr>
		</tbody>
	</table>
	
</form>


<script type="text/javascript" src='r/ga.js' ></script></body></html>