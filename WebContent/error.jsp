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

		<div id="inhalt">
			<center>
				<b> error ${javax.servlet.error.status_code}
			</b></center>
		</div>
					
		<c:if test="${requestScope['javax.servlet.error.status_code']=='404'}" >
			<%-- shorten url 短網址用測試碼 --%>
			<c:set var="validRedirectKey" value="false"/>
			<c:if test="${validRedirectKey==false}">
				<jsp:forward page="error404.html"/>
			</c:if>
			 <jsp:forward page="explain.jsp">
			 	<jsp:param name="id" value='<%=request.getAttribute("javax.servlet.forward.request_uri")%>'/>
			 	<jsp:param name="idx" value='<%=request.getAttribute("javax.servlet.error.request_uri")%>'/>
			 	<jsp:param name="id3" value='<%=request.getAttribute("javax.servlet.error.servlet_name")%>'/>
			 	<jsp:param name="tid" value="111"/>
			 </jsp:forward>
		</c:if>		

		<!-- 
		
		<ul>
			<li>Exception: <c:out value="${requestScope['javax.servlet.error.exception']}" /></li>
			<li>Exception type: <c:out value="${requestScope['javax.servlet.error.exception_type']}" /></li>
			<li>Exception message: <c:out value="${requestScope['javax.servlet.error.message']}" /></li>
			<li>Request URI: <c:out value="${requestScope['javax.servlet.error.request_uri']}" /></li>
			<li>Servlet name: <c:out value="${requestScope['javax.servlet.error.servlet_name']}" /></li>
			<li>Status code: <c:out value="${requestScope['javax.servlet.error.status_code']}" /></li>
		</ul>
		<ul>
			<li>Timestamp: <fmt:formatDate value="${date}" type="both" dateStyle="long" timeStyle="long" /></li>
			<li>User agent: <c:out value="${header['user-agent']}" /></li>
		</ul>
		<div>
			printStackTrace=
			<%--
			<%if (exception != null) {
					exception.printStackTrace(new java.io.PrintWriter(out));
			}%>	
			--%>
		</div>
		
		-->

		<%--
		<%@ include file="include/footer.jsp"%><%@ include file="include/copyright.jsp"%>
		 --%>
	</div>

<script type="text/javascript" src='r/ga.js' ></script></body></html>