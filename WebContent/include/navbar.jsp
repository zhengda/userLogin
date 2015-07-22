<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><c:set var="contextPath" value="${pageContext.request.contextPath}"/><c:set var="basePath" value="${pageContext.request.scheme}${'://'}${pageContext.request.serverName}${':'}${pageContext.request.serverPort}${pageContext.request.contextPath}"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<c:set var="uri" value="${contextPath}/login.jsp" />  
<c:choose>
<c:when test="${!fn:startsWith(pageContext.request.requestURI,uri)}">
<c:set var="browseMode" value="${basePath}/" />
<div id="navbar" class="bg037 fg030" >
	<a href="${basePath}/">Home</a> 
	<a href="${basePath}/userControl?action=list">list</a>
	<a href="${basePath}/userControl?action=create">create</a>
	<a href="${basePath}/logout.jsp">logout</a>
</div>
</c:when>
</c:choose>