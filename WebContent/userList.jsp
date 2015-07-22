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
<title>list user</title>
</head>
<body>
<%@ include file="include/navbar.jsp"%>
<div>
	<c:if test="${warningMsg!=null}"><span class="warningMsg" align="left"> ${warningMsg} </span><br/></c:if>
	<c:if test="${errorMsg!=null}"><span class="errorMsg" align="left"> ${errorMsg} </span><br/></c:if>
</div>
<h1>User list</h1>
<ul>
<c:forEach items="${users}" var="user">
	<li><a href="userControl?action=edit&username=${user.username}">edit</a> <a href="userControl?action=delete&username=${user.username}">delete</a> ${user.username}, ${user.description}</li>
</c:forEach>
</ul>


<script type="text/javascript" src='r/ga.js' ></script></body></html>