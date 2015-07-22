<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><c:set var="contextPath" value="${pageContext.request.contextPath}"/><c:set var="basePath" value="${pageContext.request.scheme}${'://'}${pageContext.request.serverName}${':'}${pageContext.request.serverPort}${pageContext.request.contextPath}"/><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%!org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(getClass());%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="language" content="en_US" />
<meta name="description" content="" />
<meta name="keywords" content=",,," />
<link rel="icon" favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" favicon.ico" type="image/x-icon" />
<link rel="alternate" type="application/rss+xml" title="xxxxxxx RSS Feed" href="${basePath}/rss" />
<link rel="stylesheet" type="text/css" href="r/reset.css" media="screen" />
<link rel="stylesheet" type="text/css" href="https://current.bootstrapcdn.com/bootstrap-v204/css/bootstrap-combined.min.css" />
<link rel="stylesheet" type="text/css" href="r/base.css" media="screen" />

<style type="text/css">
<!--
body {
	background-color: black;
	color: gray;
	font-size: 44px;
}

#inhalt {
	position: absolute;
	height: 400px;
	width: 600px;
	margin: -200px 0px 0px -300px;
	top: 50%;
	left: 50%;
	text-align: center;
	font-weight: bold;
}
-->
</style>
</head>
<body onunload="" id="body">
	<div class="container">
		explain ${requestScope["javax.servlet.forward.request_uri"]}
		
		<%-- shorten url 短網址用測試碼 --%>
		<c:set var="validRedirectKey" value="true"/>
		<c:if test="${validRedirectKey==false}">
			<jsp:forward page="error404.html"/>
		</c:if>
		<jsp:forward page="index.jsp">
			<jsp:param name="id2" value='<%=request.getAttribute("javax.servlet.forward.request_uri")%>'/>
			<jsp:param name="tid" value="11132"/> 
		</jsp:forward>
		
	</div>

<script type="text/javascript" src='r/ga.js' ></script></body></html>